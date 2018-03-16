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
package com.xin.popgen.estimators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import com.xin.popgen.infos.InfoReader;
import com.xin.popgen.infos.PopVarInfo;
import com.xin.popgen.infos.SnpInfo;
import com.xin.popgen.infos.TimeInfo;

/**
 * Class {@code SeleDiffEstimator} extends {@code Estimator} to
 * estimate selection differences between populations.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public class SeleDiffEstimator extends Estimator {
	
    // a PopVarInfo instance stores variances of drift between populations
    private final PopVarInfo popVarInfo;
    
    // a TimeInfo instance stores divergence times of population pairs
    private final TimeInfo timeInfo;
    
    private final Pattern pattern = Pattern.compile("\\s+");
    // a SnpInfo instance stores information of SNPs
    //private final SnpInfo snpInfo;

    // a double array stores log-Odds ratios between populations
   // private final double[][] logOdds;
    
    // a double array stores variances of log-Odds ratios between populations
    //private final double[][] varLogOdds;
    
    // a ChiSquareTable stores p-value of chi-square statistics
    private final ChiSquareTable chisq = new ChiSquareTable();
    
    private BufferedReader genoReader = null;
    private BufferedReader snpReader = null;
    
    /**
     * Constructor of class {@code SeleDiffEstimator}.
     *
     * @param indFileName the name of an EIGENSTRAT IND file
     * @param snpFileName the name of an EIGENSTRAT SNP file
     * @param popVarFileName the name of a file stores population variances
     * @param timeFileName the name of a file stores divergence time between populations
     */
    public SeleDiffEstimator(String indFileName, String snpFileName, 
    		String popVarFileName, String timeFileName) {
    	super(indFileName, snpFileName);
        this.popVarInfo = new PopVarInfo(popVarFileName, sampleInfo);
        this.timeInfo = new TimeInfo(timeFileName, sampleInfo);
		this.snpReader = new InfoReader(snpFileName).getBufferedReader();
        //this.snpInfo = new SnpInfo(snpFileName, snpNum);
        
        //logOdds = new double[popPairNum][snpNum];
        //varLogOdds = new double[popPairNum][snpNum];
    }
    
	@Override
	public void analyze(List<String> genoFileNames) {
		/*long start = System.currentTimeMillis();
		readFile(new InfoReader(genoFileNames.get(0)).getBufferedReader());
		long end = System.currentTimeMillis();
		System.out.println("Used Time for Reading: " + ((end-start)/1000) + " seconds");*/
		this.genoReader = new InfoReader(genoFileNames.get(0)).getBufferedReader();
	}
	
	@Override
	protected void parseLine(char[] cbuf) {
		/*int[][] alleleCounts = countAlleles(cbuf);
    	for (int m = 0; m < alleleCounts.length; m++) {
			for (int n = m + 1; n < alleleCounts.length; n++) {
				int popPairIndex = sampleInfo.getPopPairIndex(m,n);
				logOdds[popPairIndex][snpIndex] = Model.calLogOdds(alleleCounts[m][0], 
						alleleCounts[m][1], alleleCounts[n][0], alleleCounts[n][1]);
				varLogOdds[popPairIndex][snpIndex] = Model.calVarLogOdds(alleleCounts[m][0], 
						alleleCounts[m][1], alleleCounts[n][0], alleleCounts[n][1]);
			}
		}
    	snpIndex++;*/
	}

    @Override
    protected void writeLine(BufferedWriter bw) throws IOException {
    	for (int i = 0; i < snpNum; i++) {
    		char[] cbuf = new char[indNum];
			genoReader.read(cbuf);
			int[][] alleleCounts = countAlleles(cbuf);
			String[] snpInfo = pattern.split(snpReader.readLine().trim());
			for (int m = 0; m < alleleCounts.length; m++) {
				for (int n = m + 1; n < alleleCounts.length; n++) {
					int popPairIndex = sampleInfo.getPopPairIndex(m, n);
					double popVar = popVarInfo.getPopVar(popPairIndex);
					double time = timeInfo.getTime(popPairIndex);
					double logOdds = Model.calLogOdds(alleleCounts[m][0], 
						alleleCounts[m][1], alleleCounts[n][0], alleleCounts[n][1]);
					double varLogOdds = Model.calVarLogOdds(alleleCounts[m][0], 
						alleleCounts[m][1], alleleCounts[n][0], alleleCounts[n][1]);
					double diff = logOdds / time;
	    			double std = Math.sqrt(varLogOdds + popVar)
	    					/ time;
	    			String delta = format((logOdds * logOdds / (varLogOdds + popVar)), 3);
	    			//String pvalue = chisq.getPvalue(delta);
	    			double[] vals = new double[]{diff, std, diff-1.96*std, diff+1.96*std};
	    			
	    			//bw.write(snpInfo.getSnp(i));
	    			bw.write(snpInfo[0]);
	    			bw.write("\t");
	    			bw.write(snpInfo[4]);
	    			bw.write("\t");
	    			bw.write(snpInfo[5]);
	    			bw.write(format(vals, 6));
	    			bw.write(delta);
	    			bw.write("\t");
	    			bw.write(chisq.getPvalue(delta));
	    			bw.newLine();
				}
			}
			genoReader.read();
    		/*for (int j = 0; j < popPairNum; j++) {
    			double popVar = popVarInfo.getPopVar(j);
    			double time = timeInfo.getTime(j);
    			double logOdd = logOdds[j][i];
    			double varLogOdd = varLogOdds[j][i];
    			double diff = logOdd / time;
    			double std = Math.sqrt(varLogOdd + popVar)
    					/ time;
    			String delta = format((logOdd * logOdd / (varLogOdd + popVar)),3);
    			String pvalue = chisq.getPvalue(delta);
    			double[] vals = new double[]{diff, std, diff-1.96*std, diff+1.96*std};
    			
    			bw.write(snpInfo.getSnp(i));
    			bw.write(format(vals, 6));
    			bw.write(delta);
    			bw.write("\t");
    			bw.write(pvalue);
    		}*/
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
                .add("Delta")
                .add("p-value")
                .add("\n");
        bw.write(sj.toString());
    }

}
