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
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import xin.bio.popgen.infos.IndInfo;

/**
 * Class {@code PopVarMedianEstimator} extends {@code Estimator} to
 * estimate variances of drift between populations.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class ConcurrentPopVarMedianEstimator extends Estimator {

    // a DoubleArrayList stores variances of drift between populations
    private final float[][] popPairVars;
    
    // a List of Future instances stores results of medians of variances of drift between populations
    private List<Future<String>> futures;
    
    private final int snpNum;

    // an integer stores the number of threads to be used
    private final int thread;

    /**
     * Constructor of class {@code PopVarMedianEstimator}.
     *
     * @param sampleInfo a SampleInfo instance containing sample information
     */
    public ConcurrentPopVarMedianEstimator(IndInfo sampleInfo, int thread, int snpNum) {
    	super(sampleInfo);
    	this.snpNum = snpNum;
    	this.thread = thread;
        popPairVars = new float[popPairNum][snpNum];
        futures = new ArrayList<>();
    }

	@Override
	public void analyze(BufferedReader br) {
		readFile(br);
		findMedians();
	}
    
    @Override
    void writeLine(BufferedWriter bw) throws IOException {
    	for (Future<String> f:futures) {
    		try {
				bw.write(f.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
    		bw.newLine();
    	}
    }

    @Override
    void writeHeader(BufferedWriter bw) throws IOException {}
    
    /**
     * Helper function for reading file.
     * 
     * @param fileName the name of a file
     */
    private void readFile(BufferedReader br) {
    	int batchSize = snpNum / thread;
    	int remainder = snpNum % batchSize;
    	ExecutorService executor = Executors.newFixedThreadPool(thread);
    	CountDownLatch doneSignal = new CountDownLatch(thread+1);
    	try {
			int startIndex = 0;
	    	for (int i = 0; i < thread; i++) {
	    		String[] lines = new String[batchSize];
	    		for (int j = 0; j < batchSize; j++) {
	    			lines[j] = br.readLine();
	    		}
	    		executor.submit(new Counter(lines, startIndex, doneSignal));
	    		startIndex += batchSize;
	    	}
	    	if (remainder != 0) {
	    		String[] lines = new String[remainder];
	    		for (int j = 0; j < remainder; j++) {
	    			lines[j] = br.readLine();
	    		}
	    		executor.submit(new Counter(lines, startIndex, doneSignal));
	    	}
	    	else doneSignal.countDown();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
     * Helper function for finding medians of variances of drift between populations.
     * 
     * @param popPairVars a DoubleArrayList containing variances of drift between populations
     */
    private void findMedians() {
    	ExecutorService executor = Executors.newFixedThreadPool(thread);
    	CountDownLatch doneSignal = new CountDownLatch(popPairVars.length);
        for (int i = 0; i < popPairVars.length; i++) {
        	futures.add(executor.submit(new Worker(popPairIds[i][0], popPairIds[i][1], 
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
    
    private class Counter implements Runnable {
    	
    	private final String[] lines;
    	private final CountDownLatch doneSignal;
    	private int snpIndex;
    	
    	Counter(String[] lines, int snpIndex, CountDownLatch doneSignal) {
    		this.lines = lines;
    		this.doneSignal = doneSignal;
    		this.snpIndex = snpIndex;
    	}

		@Override
		public void run() {
			for (String line:lines) {
				int[][] alleleCounts = countAlleles(line);
		        for (int m = 0; m < alleleCounts.length; m++) {
					for (int n = m + 1; n < alleleCounts.length; n++) {
						int popPairIndex = sampleInfo.getPopPairIndex(m,n);
		                if ((alleleCounts[m][0] + alleleCounts[m][1] == 0) 
		                		|| (alleleCounts[n][0] + alleleCounts[n][1] == 0))
		                    continue;
		                popPairVars[popPairIndex][snpIndex] = (float) calDriftVar(alleleCounts[m][0],
		                		alleleCounts[m][1], alleleCounts[n][0],alleleCounts[n][1]);
					}
				}
		        snpIndex++;
			}
			doneSignal.countDown();
		}
    	
    }
    
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

}
