/*
	Copyright (c) 2018 Xin Huang

	This file is part of SeleDiff.

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.
 */
package com.xin.popgen.estimators;

import static com.xin.popgen.estimators.Model.calDriftVar;

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
	TDigestPopVarMedianEstimator(String genoFileName, String indFileName, String snpFileName, String outputFileName) {
		super(genoFileName, indFileName, snpFileName, outputFileName);
        popPairVarDigests = new ArrayDigest[popPairNum];
        for (int i = 0; i < popPairNum; i++) {
        	popPairVarDigests[i] = TDigest.createArrayDigest(100);
        }
	}
	
	@Override
    protected void findMedians() {
	    for (int i = 0; i < snpNum; i++) {
            int[][] alleleCounts = genoInfo.countAlleles();
            for (int m = 0; m < popNum; m++) {
                for (int n = m + 1; n < popNum; n++) {
                    // Only use SNP neither fix nor lose in any population
                    if ((alleleCounts[m][0] * alleleCounts[m][1] == 0) || (alleleCounts[n][0] * alleleCounts[n][1] == 0))
                        continue;
                    int popPairIndex = sampleInfo.getPopPairIndex(m, n);
                    popPairVarDigests[popPairIndex].add(calDriftVar(alleleCounts[m][0],
                            alleleCounts[m][1], alleleCounts[n][0], alleleCounts[n][1]));
                }
            }
        }
        for (int i = 0; i < popPairNum; i++) {
        	popPairVarMedians[i] = popPairVarDigests[i].quantile(0.5d);
        }
        genoInfo.close();
    }
	
}
