/*
    Copyright 2018 Xin Huang

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.xin.popgen.estimators;

import static com.xin.popgen.estimators.Model.calVarOmega;

import com.tdunning.math.stats.ArrayDigest;
import com.tdunning.math.stats.TDigest;

/**
 * Class {@code TDigestPopVarMedianEstimator} is a class for estimating median
 * using t-digest.
 *
 * @author Xin Huang {@code xin.huang07@gmail.com}
 */
public final class TDigestPopVarMedianEstimator extends PopVarMedianEstimator {

    // an array of ArrayDigest stores empirical distributions of pairwise population variances
	private final ArrayDigest[] popPairVarDigests;

    /**
     * Constructor of {@code TDigestPopVarMedianEstimator}
     *
     * @param indFileName an EIGENSTRAT .ind file name
     * @param snpFileName an EIGENSTRAT .snp file name
     */
	TDigestPopVarMedianEstimator(String genoFileName, String indFileName, String snpFileName, String outputFileName, char format) {
		super(genoFileName, indFileName, snpFileName, outputFileName, format);
        popPairVarDigests = new ArrayDigest[popPairNum];
        for (int i = 0; i < popPairNum; i++) {
        	popPairVarDigests[i] = TDigest.createArrayDigest(100);
        }
	}
	
	@Override
    protected void findMedians() {
	    int[][] alleleCounts;
	    int snpNum = 0;
	    while ((alleleCounts = genoInfo.countAlleles()) != null) {
	        snpNum++;
            for (int m = 0; m < popNum; m++) {
                for (int n = m + 1; n < popNum; n++) {
                    // Only use SNP neither fix nor lose in any population
                    if ((alleleCounts[m][0] * alleleCounts[m][1] == 0) || (alleleCounts[n][0] * alleleCounts[n][1] == 0))
                        continue;
                    int popPairIndex = popInfo.getPopPairIndex(m, n);
                    popPairVarDigests[popPairIndex].add(calVarOmega(alleleCounts[m][0],
                                alleleCounts[m][1], alleleCounts[n][0], alleleCounts[n][1]));
                }
            }
        }
        for (int i = 0; i < popPairNum; i++) {
        	popPairVarMedians[i] = popPairVarDigests[i].quantile(0.5d);
        }
        System.out.println(snpNum + " variants are read from " + snpFileName);
        genoInfo.close();
    }
	
}
