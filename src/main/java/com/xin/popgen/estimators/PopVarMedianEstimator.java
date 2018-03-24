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



