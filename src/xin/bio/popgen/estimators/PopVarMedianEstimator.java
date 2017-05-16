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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.StringJoiner;

import xin.bio.popgen.infos.IndInfo;

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
    public PopVarMedianEstimator(IndInfo sampleInfo, int snpNum) {
    	super(sampleInfo, snpNum);
        popPairVarMedians = new double[popPairNum];
    }
    
    @Override
	public void analyze(BufferedReader[] br) {
		readFile(br[0]);
		findMedians();
	}
    
    /**
     * Helper function for finding medians of variances of drift between populations.
     * 
     * @param popPairVars a DoubleArrayList containing variances of drift between populations
     */
    protected abstract void findMedians();
    
    @Override
    protected void writeHeader(Writer bw) throws IOException {}

    @Override
    protected void writeLine(Writer bw) throws IOException {
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



