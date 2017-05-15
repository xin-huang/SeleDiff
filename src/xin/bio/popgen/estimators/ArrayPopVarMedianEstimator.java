package xin.bio.popgen.estimators;

import static xin.bio.popgen.estimators.Model.calDriftVar;

import org.apache.commons.math3.stat.StatUtils;

import xin.bio.popgen.infos.IndInfo;

public class ArrayPopVarMedianEstimator extends PopVarMedianEstimator{
	
    // a 2-D double array stores variances of drift between populations
    private final double[][] popPairVars;
    
    public ArrayPopVarMedianEstimator(IndInfo sampleInfo, int snpNum) {
    	super(sampleInfo, snpNum);
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
    	//System.out.println("\nStart finding medians: " + System.currentTimeMillis());
        for (int i = 0; i < popPairNum; i++) {
        	//System.out.println("Finding " + i + "-th meidans at " + System.currentTimeMillis());
        	popPairVarMedians[i] = StatUtils.percentile(popPairVars[i], 50);
        }
        //System.out.println("End finding medians: " + System.currentTimeMillis() + "\n");
    }

}
