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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import com.tdunning.math.stats.ArrayDigest;
import com.tdunning.math.stats.MergingDigest;
import com.tdunning.math.stats.TDigest;

import xin.bio.popgen.infos.InfoReader;

/**
 * Class {@code ConcurrentPopVarMedianEstimator} extends {@code ConcurrentEstimator} to
 * estimate variances of drift between populations concurrently.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class ConcurrentPopVarMedianEstimator extends PopVarMedianEstimator {

	private final MergingDigest[] popPairVarDigests;
	
    private final ReentrantLock lock = new ReentrantLock();
    
    private int nThreads;
    
    /**
     * Constructor of class {@code ConcurrentPopVarMedianEstimator}.
     *
     * @param sampleInfo a SampleInfo instance containing sample information
     */
    public ConcurrentPopVarMedianEstimator(String indFileName, String snpFileName, int nThreads) {
    	super(indFileName, snpFileName);
        this.nThreads = nThreads;
        popPairVarDigests = new MergingDigest[popPairNum];
        for (int i = 0; i < popPairNum; i++) {
        	popPairVarDigests[i] = new MergingDigest(100);
        }
    }

	@Override
	public void analyze(List<String> genoFileNames) {
		if (genoFileNames.size() > 1) readMultipleFiles(genoFileNames);
		else readSingleFile(genoFileNames.get(0));
		findMedians();
	}
	
	private void readSingleFile(String genoFileName) {}
    
	private void readMultipleFiles(List<String> genoFileNames) {
		BufferedReader[] br = new BufferedReader[genoFileNames.size()];
		for (int i = 0; i < br.length; i++) {
			br[i] = new InfoReader(genoFileNames.get(i)).getBufferedReader();
		}
		nThreads = Math.min(Runtime.getRuntime().availableProcessors(), Math.min(nThreads, br.length));
		ExecutorService executor = Executors.newFixedThreadPool(nThreads);
		CountDownLatch doneSignal = new CountDownLatch(br.length);
		for (int i = 0; i < br.length; i++) {
			executor.submit(new Worker(br[i], doneSignal));
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
    protected void findMedians() {
        for (int i = 0; i < popPairNum; i++) {
        	popPairVarMedians[i] = popPairVarDigests[i].quantile(0.5d);
        }
    }

	@Override
	protected void parseLine(char[] cbuf) {
	}
	
	private class Worker implements Runnable {
		
		private final BufferedReader br;
		private final CountDownLatch doneSignal;
		
		Worker(BufferedReader br, CountDownLatch doneSignal) {
			this.br = br;
			this.doneSignal = doneSignal;
		}

		@Override
		public void run() {
			ArrayDigest[] tmpDigests = new ArrayDigest[popPairNum];
			for (int i = 0; i < popPairNum; i++) {
				tmpDigests[i] = TDigest.createArrayDigest(100);
			}
	    	try {
	    		do {
			    	char[] cbuf = new char[indNum];
		    		br.read(cbuf);
		    		int[][] alleleCounts = countAlleles(cbuf);
		    	    for (int m = 0; m < alleleCounts.length; m++) {
		    			for (int n = m + 1; n < alleleCounts.length; n++) {
		    				int popPairIndex = sampleInfo.getPopPairIndex(m,n);
		    				tmpDigests[popPairIndex].add(calDriftVar(alleleCounts[m][0],
		    	                	alleleCounts[m][1], alleleCounts[n][0],alleleCounts[n][1]));
		    			}
		    		}
	    		} while (br.read() != -1);
	    		lock.lock();
	    		for (int i = 0; i < popPairNum; i++) {
	    			popPairVarDigests[i].add(tmpDigests[i]);
	    		}
	    		lock.unlock();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				doneSignal.countDown();
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
    
}
