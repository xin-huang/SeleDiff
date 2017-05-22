package com.xin.popgen.estimators;

import static com.xin.popgen.estimators.Model.calDriftVar;

import com.tdunning.math.stats.ArrayDigest;
import com.tdunning.math.stats.TDigest;

public final class TDigestPopVarMedianEstimator extends PopVarMedianEstimator {
	
	private final ArrayDigest[] popPairVarDigests;
	
	public TDigestPopVarMedianEstimator(String indFileName, String snpFileName) {
		super(indFileName, snpFileName);
        popPairVarDigests = new ArrayDigest[popPairNum];
        for (int i = 0; i < popPairNum; i++) {
        	popPairVarDigests[i] = TDigest.createArrayDigest(100);
        }
	}
	
	@Override
	protected void parseLine(char[] cbuf) {
		int[][] alleleCounts = countAlleles(cbuf);
        for (int m = 0; m < alleleCounts.length; m++) {
			for (int n = m + 1; n < alleleCounts.length; n++) {
				int popPairIndex = sampleInfo.getPopPairIndex(m,n);
				// Assume no missing SNP in any population
				popPairVarDigests[popPairIndex].add(calDriftVar(alleleCounts[m][0],
                		alleleCounts[m][1], alleleCounts[n][0],alleleCounts[n][1]));
			}
		}
	}
	
	@Override
    protected void findMedians() {
        for (int i = 0; i < popPairNum; i++) {
        	popPairVarMedians[i] = popPairVarDigests[i].quantile(0.5d);
        }
    }
	
}