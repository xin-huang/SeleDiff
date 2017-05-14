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
import static xin.bio.popgen.estimators.Model.quickSelect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringJoiner;

import xin.bio.popgen.infos.IndInfo;

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
    private final float[][] popPairVars;
    
    /**
     * Constructor of class {@code PopVarMedianEstimator}.
     *
     * @param sampleInfo a SampleInfo instance containing sample information
     */
    public PopVarMedianEstimator(IndInfo sampleInfo, int snpNum) {
    	super(sampleInfo, snpNum);
        popPairVarMedians = new float[popPairNum];
        popPairVars = new float[popPairNum][snpNum];
    }
    
    @Override
	public void analyze(BufferedReader br) {
    	long readStart = System.currentTimeMillis();
		readFile(br);
		long readEnd = System.currentTimeMillis();
		System.out.println("Used time for reading file: " + ((readEnd-readStart)/1000));
		long findStart = System.currentTimeMillis();
		findMedians();
		long findEnd = System.currentTimeMillis();
		System.out.println("Used time for finding medians: " + ((findEnd-findStart)/1000));
	}
    
	protected void parseLine(char[] cbuf) {
		int[][] alleleCounts = countAlleles(cbuf);
        for (int m = 0; m < alleleCounts.length; m++) {
			for (int n = m + 1; n < alleleCounts.length; n++) {
				int popPairIndex = sampleInfo.getPopPairIndex(m,n);
                /*if ((alleleCounts[m][0] + alleleCounts[m][1] == 0) 
                		|| (alleleCounts[n][0] + alleleCounts[n][1] == 0))
                    continue;*/
				// Assume no missing data
                popPairVars[popPairIndex][snpIndex] = (float) calDriftVar(alleleCounts[m][0],
                		alleleCounts[m][1], alleleCounts[n][0],alleleCounts[n][1]);
			}
		}
        snpIndex++;
	}

    @Override
    protected void writeHeader(BufferedWriter bw) throws IOException {}

    @Override
    protected void writeLine(BufferedWriter bw) throws IOException {
        for (int i = 0; i < popPairNum; i++) {
            StringJoiner sj = new StringJoiner("\t");
            sj.add(popPairIds[i][0])
            	.add(popPairIds[i][1])
            	.add(String.valueOf(popPairVarMedians[i]));
            bw.write(sj.toString());
            bw.newLine();
        }
    }

    /**
     * Helper function for finding medians of variances of drift between populations.
     * 
     * @param popPairVars a DoubleArrayList containing variances of drift between populations
     */
    private void findMedians() {
        for (int i = 0; i < popPairNum; i++) {
        	if (snpNum % 2 != 0) {
        		popPairVarMedians[i] = quickSelect(popPairVars[i], snpNum/2);
        	}
        	else {
        		float left = quickSelect(popPairVars[i], snpNum/2-1);
        		float right = quickSelect(popPairVars[i], snpNum/2);
        		popPairVarMedians[i] = (left + right) / 2;
        	}
        }
    }

}



