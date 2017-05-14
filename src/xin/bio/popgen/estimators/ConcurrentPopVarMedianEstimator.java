/*
  Copyright (C) 2017 Xin Huang

  This file is part of SeleDiff.

  SeleDiff is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version

  SeleDiff is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package xin.bio.popgen.estimators;

import static xin.bio.popgen.estimators.Model.calDriftVar;
import static xin.bio.popgen.estimators.Model.quickSelect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import xin.bio.popgen.infos.IndInfo;

/**
 * Class {@code ConcurrentPopVarMedianEstimator} extends {@code ConcurrentEstimator} to
 * estimate variances of drift between populations concurrently.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class ConcurrentPopVarMedianEstimator extends ConcurrentEstimator {

    // a DoubleArrayList stores variances of drift between populations
    private final float[][] popPairVars;
    
    /**
     * Constructor of class {@code ConcurrentPopVarMedianEstimator}.
     *
     * @param sampleInfo a SampleInfo instance containing sample information
     */
    public ConcurrentPopVarMedianEstimator(IndInfo sampleInfo, int snpNum, int thread) {
    	super(sampleInfo, snpNum, thread);
        popPairVars = new float[popPairNum][snpNum];
    }

	@Override
	public void analyze(BufferedReader br) {
		readFile(br);
		findMedians();
	}
    
    @Override
    protected void writeLine(BufferedWriter bw) throws IOException {
    	for (Future<String> r:results) {
    		try {
				bw.write(r.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
    		bw.newLine();
    	}
    }

    @Override
    protected void writeHeader(BufferedWriter bw) throws IOException {}
    

    /**
     * Helper function for finding medians of variances of drift between populations.
     * 
     * @param popPairVars a DoubleArrayList containing variances of drift between populations
     */
    private void findMedians() {
    	ExecutorService executor = Executors.newFixedThreadPool(thread);
    	CountDownLatch doneSignal = new CountDownLatch(popPairNum);
        for (int i = 0; i < popPairNum; i++) {
        	results.add(executor.submit(new Worker(popPairIds[i][0], popPairIds[i][1], 
        			popPairVars[i], doneSignal)));
        }
        try {
			doneSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
		}
    }
    
    /**
     * Helper class for finding medians of variances of drift concurrently.
     */
    private class Worker implements Callable<String> {

        private final float[] arr;
        private final CountDownLatch doneSignal;
        private final String popi;
        private final String popj;

        Worker(String popi, String popj, 
        		float[] arr, CountDownLatch doneSignal) {
        	this.popi = popi;
        	this.popj = popj;
            this.arr = arr;
            this.doneSignal = doneSignal;
        }

        @Override
        public String call() throws Exception {
        	float median;
        	int length = arr.length;
        	if (length % 2 != 0) {
        		median = quickSelect(arr, length/2);
        	}
        	else {
        		float left = quickSelect(arr, length/2-1);
        		float right = quickSelect(arr, length/2);
        		median = (left + right) / 2;
        	}
        	StringJoiner sj = new StringJoiner("\t");
        	sj.add(popi).add(popj).add(String.valueOf(median));
            doneSignal.countDown();
            return sj.toString();
        }
    }

	@Override
	protected Runnable creatCounter(char[][] lines, int startIndex,
			Semaphore threadSemaphore, CountDownLatch doneSignal) {
		/**
	     * Helper class for counting alleles concurrently.
	     */
	    class PopVarCounter implements Runnable {

			private final char[][] lines;
			private final Semaphore threadSemaphore;
	    	private final CountDownLatch doneSignal;
	    	private int startIndex;
	    	
	    	PopVarCounter(char[][] lines, int startIndex, 
	    			Semaphore threadSemaphore, CountDownLatch doneSignal) {
	    		this.lines = lines;
	    		this.startIndex = startIndex;
	    		this.threadSemaphore = threadSemaphore;
	    		this.doneSignal = doneSignal;
			}

			@Override
			public void run() {
				try {
					threadSemaphore.acquire();
					for (char[] line:lines) {
						int[][] alleleCounts = countAlleles(line);
				        for (int m = 0; m < alleleCounts.length; m++) {
							for (int n = m + 1; n < alleleCounts.length; n++) {
								int popPairIndex = sampleInfo.getPopPairIndex(m,n);
				                popPairVars[popPairIndex][startIndex] = (float) calDriftVar(alleleCounts[m][0],
				                		alleleCounts[m][1], alleleCounts[n][0],alleleCounts[n][1]);
							}
						}
				        startIndex++;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					threadSemaphore.release();
					doneSignal.countDown();
				}
			}
	    	
	    }
		
		return new PopVarCounter(lines, startIndex, threadSemaphore, doneSignal);
	}

}
