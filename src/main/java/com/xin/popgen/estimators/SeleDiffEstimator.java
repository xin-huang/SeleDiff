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

import com.xin.popgen.infos.PopVarInfo;
import com.xin.popgen.infos.TimeInfo;

/**
 * Class {@code SeleDiffEstimator} extends {@code Estimator} to
 * estimate selection differences between populations.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public class SeleDiffEstimator extends Estimator {
	
    // a PopVarInfo instance stores variances of drift between populations
    private final PopVarInfo popVarInfo;
    
    // a TimeInfo instance stores divergence times of population pairs
    private final TimeInfo timeInfo;
    
    // a ChiSquareTable stores p-value of chi-square statistics
    private final ChiSquareTable chisq = new ChiSquareTable();
    
    /**
     * Constructor of class {@code SeleDiffEstimator}.
     *
     * @param indFileName the name of an EIGENSTRAT IND file
     * @param snpFileName the name of an EIGENSTRAT SNP file
     * @param popVarFileName the name of a file stores population variances
     * @param timeFileName the name of a file stores divergence time between populations
     */
    SeleDiffEstimator(String genoFileName, String indFileName, String snpFileName,
    		String popVarFileName, String timeFileName, String outputFileName, char format) {
    	super(genoFileName, indFileName, snpFileName, outputFileName, format);
        this.popVarInfo = new PopVarInfo(popVarFileName, sampleInfo);
        this.timeInfo = new TimeInfo(timeFileName, sampleInfo);
    }
    
	@Override
	public void analyze() {
        writeResults();
	}

    @Override
    protected void writeLine(BufferedWriter bw) throws IOException {
        genoInfo.openSnp();
        int snpNum = 0;
    	int[][] alleleCounts;
    	while ((alleleCounts = genoInfo.countAlleles()) != null) {
    	    snpNum++;
            String snp = genoInfo.getSnpInfo();
            for (int m = 0; m < alleleCounts.length; m++) {
                for (int n = m + 1; n < alleleCounts.length; n++) {
                    int popPairIndex = sampleInfo.getPopPairIndex(m, n);
                    double popVar = popVarInfo.getPopVar(popPairIndex);
                    double time = timeInfo.getTime(popPairIndex);
                    double logOdds = Model.calLogOdds(alleleCounts[m][0],
                                alleleCounts[m][1], alleleCounts[n][0], alleleCounts[n][1]);
                    double varLogOdds = Model.calVarLogOdds(alleleCounts[m][0],
                                alleleCounts[m][1], alleleCounts[n][0], alleleCounts[n][1]);
                    double diff = logOdds / time;
                    double std = Math.sqrt(varLogOdds + popVar)
                                / time;
                    String delta = format((logOdds * logOdds / (varLogOdds + popVar)), 3);
                    double[] vals = new double[]{diff, std, diff - 1.96 * std, diff + 1.96 * std};

                    bw.write(snp);
                    bw.write("\t");
                    bw.write(popPairIds[popPairIndex][0]);
                    bw.write("\t");
                    bw.write(popPairIds[popPairIndex][1]);
                    bw.write("\t");
                    bw.write(format(vals, 6));
                    bw.write("\t");
                    bw.write(delta);
                    bw.write("\t");
                    bw.write(chisq.getPvalue(delta));
                    bw.newLine();
                }
            }
        }
        System.out.println(snpNum + " variants are read from " + snpFileName);
        genoInfo.closeSnp();
    	genoInfo.close();
    }

    @Override
    protected void writeHeader(BufferedWriter bw) throws IOException {
        StringJoiner sj = new StringJoiner("\t");
        sj.add("SNP ID")
                .add("Ref")
                .add("Alt")
                .add("Population 1")
                .add("Population 2")
                .add("Selection difference (Population 1 - Population 2)")
                .add("Std")
                .add("Lower bound of 95% CI")
                .add("Upper bound of 95% CI")
                .add("Delta")
                .add("p-value")
                .add("\n");
        bw.write(sj.toString());
    }

}
