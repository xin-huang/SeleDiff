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
        this.popVarInfo = new PopVarInfo(popVarFileName, popInfo);
        this.timeInfo = new TimeInfo(timeFileName, popInfo);
    }
    
	@Override
	public void analyze() {
        writeResults();
	}

    @Override
    protected void writeLine(BufferedWriter bw) throws IOException {
        int snpNum = 0;
    	int[][] alleleCounts;
    	while ((alleleCounts = genoInfo.countAlleles()) != null) {
    	    snpNum++;
            String snp = genoInfo.getSnpInfo();
            for (int m = 0; m < alleleCounts.length; m++) {
                for (int n = m + 1; n < alleleCounts.length; n++) {
                    int popPairIndex = popInfo.getPopPairIndex(m, n);
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
    	genoInfo.close();
    }

    @Override
    protected void writeHeader(BufferedWriter bw) throws IOException {
        StringJoiner sj = new StringJoiner("\t");
        sj.add("#CHROMO")
                .add("POS")
                .add("ID")
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
