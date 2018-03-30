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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringJoiner;

/**
 * Class {@code PopVarMedianEstimator} extends {@code Estimator} to
 * estimate variances of drift between populations.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public abstract class PopVarMedianEstimator extends Estimator {

    // a double array stores medians of variances of drift
    final double[] popPairVarMedians;
    
    /**
     * Constructor of class {@code PopVarMedianEstimator}.
     *
     * @param indFileName an EIGENSTRAT .ind file name
     * @param snpFileName an EIGENSTRAT .snp file name
     */
    PopVarMedianEstimator(String genoFileName, String indFileName, String snpFileName, String outputFileName, char format) {
    	super(genoFileName, indFileName, snpFileName, outputFileName, format);

        popPairVarMedians = new double[popPairNum];
    }
    
    @Override
	public void analyze() {
		findMedians();
		writeResults();
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



