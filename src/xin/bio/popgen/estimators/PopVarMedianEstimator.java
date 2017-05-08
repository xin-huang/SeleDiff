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

import static xin.bio.popgen.estimators.Model.calDriftVar;
import xin.bio.popgen.infos.SampleInfo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringJoiner;

import it.unimi.dsi.fastutil.floats.FloatArrayList;

/**
 * Class {@code PopVarMedianEstimator} extends {@code Estimator} to
 * estimate variances of drift between populations.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class PopVarMedianEstimator extends Estimator {

    // a double array stores medians of variances of drift
    private final float[] popPairVarMedians;
    
    // a DoubleArrayList stores variances of drift between populations
    private final FloatArrayList[] popPairVars;

    /**
     * Constructor of class {@code PopVarMedianEstimator}.
     *
     * @param sampleInfo a SampleInfo instance containing sample information
     */
    public PopVarMedianEstimator(SampleInfo sampleInfo) {
    	super(sampleInfo, null);
        popPairVarMedians = new float[popPairNum];
        popPairVars = new FloatArrayList[popPairNum];
        for (int i = 0; i < popPairNum; i++) {
        	popPairVars[i] = new FloatArrayList();
        }
    }

    @Override
    public void estimate() { findMedians(popPairVars); }
    
	@Override
	public void parseSnpInfo(String line) {
		// Read SNP information
		int start = 0, end = 0;
		for (int i = 0; i < 9; i++) {
			end = line.indexOf("\t", start);
			start = end + 1;
		}
        // Read allele counts of individuals
		int[][] alleleCounts = countAlleles(line, start, sampleIndNum);
        for (int m = 0; m < alleleCounts.length; m++) {
			for (int n = m + 1; n < alleleCounts.length; n++) {
				int popPairIndex = sampleInfo.getPopPairIndex(m,n);
                if ((alleleCounts[m][0] + alleleCounts[m][1] == 0) 
                		|| (alleleCounts[n][0] + alleleCounts[n][1] == 0))
                    continue;
                popPairVars[popPairIndex].add((float) calDriftVar(alleleCounts[m][0],alleleCounts[m][1],
                		alleleCounts[n][0],alleleCounts[n][1]));
			}
		}
	}

    @Override
    void writeLine(BufferedWriter bw) throws IOException {
        for (int i = 0; i < popPairNum; i++) {
            StringJoiner sj = new StringJoiner("\t");
            sj.add(popPairIds[i][0])
            	.add(popPairIds[i][1])
            	.add(String.valueOf(popPairVarMedians[i]));
            bw.write(sj.toString());
            bw.newLine();
        }
    }

    @Override
    void writeHeader(BufferedWriter bw) throws IOException {}

    /**
     * Helper function for finding medians of variances of drift between populations.
     * @param popPairVars a DoubleArrayList containing variances of drift between populations
     */
    private void findMedians(FloatArrayList[] popPairVars) {
        for (int i = 0; i < popPairVars.length; i++) {
        	int length = popPairVars[i].size();
        	if (length % 2 != 0) {
        		popPairVarMedians[i] = quickSelect(popPairVars[i], length/2);
        	}
        	else {
        		float left = quickSelect(popPairVars[i], length/2-1);
        		float right = quickSelect(popPairVars[i], length/2);
        		popPairVarMedians[i] = (left + right) / 2;
        	}
        }
    }
    
    /**
     * Finds median with quick select algorithm O(n).
     * 
     * @param arrList a FloatArrayList
     * @param k the k-th element to be selected
     * @return the median of the array
     */
    private float quickSelect(FloatArrayList arrList, int k) {
        int from = 0;
        int to = arrList.size() - 1;
        while (from < to) {
        	int r = from;
        	int w = to;
        	float mid = arrList.getFloat((r+w)/2);
        	while (r < w) {
        		if (arrList.getFloat(r) >= mid) {
        			float tmp = arrList.getFloat(w);
        			arrList.set(w, arrList.getFloat(r));
        			arrList.set(r, tmp);
        			w--;
        		}
        		else r++;
        	}
        	if (arrList.getFloat(r) > mid) r--;
        	if (k <= r) to = r;
        	else from = r + 1;
        }
        return arrList.getFloat(k);
    }
    
}
