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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import xin.bio.popgen.infos.IndInfo;
import xin.bio.popgen.infos.PopVarInfo;
import xin.bio.popgen.infos.SnpInfo;
import xin.bio.popgen.infos.TimeInfo;

/**
 * Class {@code ConcurrentSeleDiffEstimator} extends {@code ConcurrentEstimator} to
 * estimate selection differences between populations concurrently.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class ConcurrentSeleDiffEstimator extends ConcurrentEstimator {

    // a PopVarInfo instance stores variances of drift between populations
    private final PopVarInfo popVarInfo;
    
    // a TimeInfo instance stores divergence times of population pairs
    private final TimeInfo timeInfo;
    
    // a SnpInfo instance stores information of SNPs
    private final SnpInfo snpInfo;

    // a String stores the name of the file containing ancestral allele information
    private final BufferedReader ancAlleleFile;
    
    // a double array stores log-Odds ratios between populations
    private final float[][] logOdds;
    
    // a double array stores variances of log-Odds ratios between populations
    private final float[][] varLogOdds;
    
    // a ChiSquaredDistribution instance for performing chi-square tests
    private final ChiSquaredDistribution chisq;
    
    /**
     * Constructor of class {@code ConcurrentSeleDiffEstimator}.
     *
     * @param ancAlleleFileName the name of the file containing ancestral allele information
     * @param popVarInfo a PopVarInfo instance containing variances of drift between populations
     * @param sampleInfo a SampleInfo instance containing sample information
     * @param timeInfo a TimeInfo instance containing divergence times between populations
     */
    public ConcurrentSeleDiffEstimator(BufferedReader ancAlleleFile, int snpNum, int thread, 
    		PopVarInfo popVarInfo, IndInfo sampleInfo, SnpInfo snpInfo, TimeInfo timeInfo) {
    	super(sampleInfo, snpNum, thread);
        this.popVarInfo = popVarInfo;
        this.timeInfo = timeInfo;
        this.snpInfo = snpInfo;
        this.chisq = new ChiSquaredDistribution(1);
        this.ancAlleleFile = ancAlleleFile;
        
        logOdds = new float[popPairNum][snpNum];
        varLogOdds = new float[popPairNum][snpNum];
    }
    
	@Override
	public void analyze(BufferedReader[] br) {
		readFile(br[0]);
		alignAncAllele();
		getResults();
	}
	
    private void alignAncAllele() {
    	if (ancAlleleFile == null)
    		return;
    	int i = 0;
    	HashMap<String, Integer> snpIndices = new HashMap<>(snpInfo.getSnpIds().length);
    	for (String snpId:snpInfo.getSnpIds()) {
    		snpIndices.put(snpId, i++);
    	}
    	try {
			String line;
			while ((line = ancAlleleFile.readLine().trim()) != null) {
				int start = 0;
				int end = line.indexOf("\\s+");
				String snpId = line.substring(start, end);
				String ancAllele = line.substring(end+1);
				if (snpIndices.containsKey(snpId)) {
					int snpIndex = snpIndices.get(snpId);
					String refAllele = snpInfo.getRefAlleles()[snpIndex];
					if (!ancAllele.equals(snpInfo.getRefAlleles()[snpIndex])) {
						snpInfo.getAltAlleles()[snpIndex] = refAllele;
						snpInfo.getRefAlleles()[snpIndex] = ancAllele;
						for (int j = 0; j < popPairNum; j++) {
							logOdds[j][snpIndex] = -logOdds[j][snpIndex];
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ancAlleleFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
	
	private void getResults() {
		ExecutorService executor = Executors.newFixedThreadPool(thread);
		CountDownLatch doneSignal = new CountDownLatch(popPairNum);
		for (int i = 0; i < popPairNum; i++) {
			results.add(executor.submit(new Worker(logOdds[i], varLogOdds[i], doneSignal)));
		}
        try {
			doneSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
		}
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
    protected void writeHeader(BufferedWriter bw) throws IOException {
        StringJoiner sj = new StringJoiner("\t");
        sj.add("SNP ID")
                .add("Ancestral allele")
                .add("Derived allele")
                .add("Population 1")
                .add("Population 2")
                .add("Selection difference (Population 1 - Population 2)")
                .add("Std")
                .add("Lower bound of 95% CI")
                .add("Upper bound of 95% CI")
                .add("Divergence time")
                .add("Variance of drift")
                .add("Delta")
                .add("p-value");
        bw.write(sj.toString());
        bw.newLine();
    }
    
    private class Worker implements Callable<String> {
    	
    	private final float[] logOdds;
    	private final float[] varLogOdds;
    	private final CountDownLatch doneSignal;
    	
    	Worker(float[] logOdds, float[] varLogOdds, CountDownLatch doneSignal) {
    		this.logOdds = logOdds;
    		this.varLogOdds = varLogOdds;
    		this.doneSignal = doneSignal;
    	}

		@Override
		public String call() throws Exception {
			// TODO: current implementation would require large memory usage
			StringJoiner block = new StringJoiner("\n");
			int snpNum = snpInfo.getSnpIds().length;
	    	for (int i = 0; i < snpNum; i++) {
	    		String snpId = snpInfo.getSnpIds()[i];
	    		String ancAllele = snpInfo.getRefAlleles()[i];
	    		String derAllele = snpInfo.getAltAlleles()[i];
	    		for (int j = 0; j < popPairNum; j++) {
	    			float logOdd = logOdds[i];
	    			float varLogOdd = varLogOdds[i];
	    			double diff = logOdd / timeInfo.getTime(j);
	    			double std = Math.sqrt(varLogOdd + popVarInfo.getPopVar(j))
	    					/ timeInfo.getTime(j);
	    			double delta = logOdd * logOdd / (varLogOdd + popVarInfo.getPopVar(j));
	    			double pvalue = 1.0 - chisq.cumulativeProbability(delta);
	    			
	    			StringJoiner sj = new StringJoiner("\t");
	    			sj.add(snpId)
	    				.add(ancAllele)
	    				.add(derAllele)
	    				.add(popPairIds[j][0])
	    				.add(popPairIds[j][1])
	    				.add(String.valueOf(Model.round(diff)))
	    				.add(String.valueOf(Model.round(std)))
	    				.add(String.valueOf(Model.round(diff-1.96*std)))
	    				.add(String.valueOf(Model.round(diff+1.96*std)))
	    				.add(String.valueOf(timeInfo.getTime(j)))
	    				.add(String.valueOf(Model.round(popVarInfo.getPopVar(j))))
	    				.add(String.valueOf(Model.round(delta)))
	    				.add(String.valueOf(Model.round(pvalue)));
	    			block.add(sj.toString());
	    		}
	    	}
	    	doneSignal.countDown();
			return block.toString();
		}}

	@Override
	protected Runnable creatCounter(char[][] lines, int startIndex, 
			Semaphore threadSemaphore, CountDownLatch doneSignal) {
		
		class SeleDiffCounter implements Runnable {
	    	
	    	private final char[][] lines;
	    	private final Semaphore threadSemaphore;
	    	private final CountDownLatch doneSignal;
	    	private int snpIndex;
	    	
	    	SeleDiffCounter(char[][] lines, int snpIndex, 
	    			Semaphore threadSemaphore, CountDownLatch doneSignal) {
	    		this.lines = lines;
	    		this.threadSemaphore = threadSemaphore;
	    		this.doneSignal = doneSignal;
	    		this.snpIndex = snpIndex;
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
				                logOdds[popPairIndex][snpIndex] = (float) Model.calLogOdds(alleleCounts[m][0], 
										alleleCounts[m][1], alleleCounts[n][0], alleleCounts[n][1]);
								varLogOdds[popPairIndex][snpIndex] = (float) Model.calVarLogOdds(alleleCounts[m][0], 
										alleleCounts[m][1], alleleCounts[n][0], alleleCounts[n][1]);
							}
						}
				        snpIndex++;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					threadSemaphore.release();
					doneSignal.countDown();
				}
			}
	    	
	    }
		
		return new SeleDiffCounter(lines, startIndex, threadSemaphore, doneSignal);
	}

}
