/*
  Copyright (C) 2018 Xin Huang

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
