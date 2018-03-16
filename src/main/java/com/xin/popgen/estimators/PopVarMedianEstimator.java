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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

import com.xin.popgen.infos.InfoReader;

/**
 * Class {@code PopVarMedianEstimator} extends {@code Estimator} to
 * estimate variances of drift between populations.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public abstract class PopVarMedianEstimator extends Estimator {

    // a double array stores medians of variances of drift
    protected final double[] popPairVarMedians;
    
    /**
     * Constructor of class {@code PopVarMedianEstimator}.
     *
     * @param sampleInfo a SampleInfo instance containing sample information
     */
    public PopVarMedianEstimator(String indFileName, String snpFileName) {
    	super(indFileName, snpFileName);
        popPairVarMedians = new double[popPairNum];
    }
    
    @Override
	public void analyze(List<String> genoFileNames) {
		readFile(new InfoReader(genoFileNames.get(0)).getBufferedReader());
		findMedians();
	}
    
    /**
     * Helper function for finding medians of variances of drift between populations.
     */
    protected abstract void findMedians();
    
    @Override
    protected void writeHeader(BufferedWriter bw) throws IOException {}

    @Override
    protected void writeLine(BufferedWriter bw) throws IOException {
        for (int i = 0; i < popPairNum; i++) {
            StringJoiner sj = new StringJoiner("\t");
            sj.add(popPairIds[i][0])
            	.add(popPairIds[i][1])
            	.add(String.valueOf(format(popPairVarMedians[i],6)))
            	.add("\n");
            bw.write(sj.toString());
        }
    }

}



