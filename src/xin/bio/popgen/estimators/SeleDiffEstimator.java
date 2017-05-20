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

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.StringJoiner;

import xin.bio.popgen.infos.InfoReader;
import xin.bio.popgen.infos.PopVarInfo;
import xin.bio.popgen.infos.SnpInfo;
import xin.bio.popgen.infos.TimeInfo;

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
    
    // a SnpInfo instance stores information of SNPs
    private final SnpInfo snpInfo;

    // a double array stores log-Odds ratios between populations
    private final double[][] logOdds;
    
    // a double array stores variances of log-Odds ratios between populations
    private final double[][] varLogOdds;
    
    //
    private final ChiSquareTable chisq = new ChiSquareTable();
    
    /**
     * Constructor of class {@code SeleDiffEstimator}.
     *
     * @param ancAlleleFileName the name of the file containing ancestral allele information
     * @param popVarInfo a PopVarInfo instance containing variances of drift between populations
     * @param sampleInfo a SampleInfo instance containing sample information
     * @param timeInfo a TimeInfo instance containing divergence times between populations
     */
    public SeleDiffEstimator(String indFileName, String snpFileName, 
    		String popVarFileName, String timeFileName) {
    	super(indFileName, snpFileName);
        this.popVarInfo = new PopVarInfo(popVarFileName, sampleInfo);
        this.timeInfo = new TimeInfo(timeFileName, sampleInfo);
        this.snpInfo = new SnpInfo(snpFileName, snpNum);
        
        logOdds = new double[popPairNum][snpNum];
        varLogOdds = new double[popPairNum][snpNum];
    }
    
	@Override
	public void analyze(List<String> genoFileNames) {
		long start = System.currentTimeMillis();
		readFile(new InfoReader(genoFileNames.get(0)).getBufferedReader());
		long end = System.currentTimeMillis();
		System.out.println("Used Time for Reading: " + ((end-start)/1000) + " seconds");
	}
	
	@Override
	protected void parseLine(char[] cbuf) {
		int[][] alleleCounts = countAlleles(cbuf);
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

    @Override
    protected void writeLine(Writer bw) throws IOException {
    	for (int i = 0; i < snpNum; i++) {
    		for (int j = 0; j < popPairNum; j++) {
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
    		}
    	}
    }

    @Override
    protected void writeHeader(Writer bw) throws IOException {
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
