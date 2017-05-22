package com.xin.popgen.estimators;

import static com.xin.popgen.estimators.Model.calDriftVar;

import org.apache.commons.math3.stat.StatUtils;

public final class ArrayPopVarMedianEstimator extends PopVarMedianEstimator {
	
    // a 2-D double array stores variances of drift between populations
    private final double[][] popPairVars;
    
    public ArrayPopVarMedianEstimator(String indFileName, String snpFilName) {
    	super(indFileName, snpFilName);
    	popPairVars = new double[popPairNum][snpNum];
    }
    
    @Override
	protected void parseLine(char[] cbuf) {
		int[][] alleleCounts = countAlleles(cbuf);
        for (int m = 0; m < alleleCounts.length; m++) {
			for (int n = m + 1; n < alleleCounts.length; n++) {
				int popPairIndex = sampleInfo.getPopPairIndex(m,n);
				// Assume no missing SNP in any population
                popPairVars[popPairIndex][snpIndex] = calDriftVar(alleleCounts[m][0],
                		alleleCounts[m][1], alleleCounts[n][0],alleleCounts[n][1]);
			}
		}
        snpIndex++;
	}
    
    @Override
    protected void findMedians() {
        for (int i = 0; i < popPairNum; i++) {
        	popPairVarMedians[i] = StatUtils.percentile(popPairVars[i], 50);
        }
    }

}
