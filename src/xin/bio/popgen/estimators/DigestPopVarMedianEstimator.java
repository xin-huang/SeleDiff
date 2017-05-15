package xin.bio.popgen.estimators;

import static xin.bio.popgen.estimators.Model.calDriftVar;

import com.tdunning.math.stats.ArrayDigest;
import com.tdunning.math.stats.TDigest;

import xin.bio.popgen.infos.IndInfo;

public class DigestPopVarMedianEstimator extends PopVarMedianEstimator {

	private final ArrayDigest[] popPairVarDigests;
	
	public DigestPopVarMedianEstimator(IndInfo sampleInfo, int snpNum) {
		super(sampleInfo, snpNum);
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
        snpIndex++;
	}
	
	@Override
    protected void findMedians() {
    	//System.out.println("\nStart finding medians: " + System.currentTimeMillis());
        for (int i = 0; i < popPairNum; i++) {
        	//System.out.println("Finding " + i + "-th meidans at " + System.currentTimeMillis());
        	popPairVarMedians[i] = popPairVarDigests[i].quantile(0.5d);
        }
        //System.out.println("End finding medians: " + System.currentTimeMillis() + "\n");
    }
	
}
