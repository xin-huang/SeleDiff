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

import xin.bio.popgen.infos.SampleInfo;
import xin.bio.popgen.infos.VcfInfo;
import xin.bio.popgen.utils.LinkedQueue;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Class {@code EstimatePopVar} extends {@code Estimator} to
 * estimate variances of drift between populations.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class EstimatePopVar extends Estimator {

    // a double array stores medians of variances of drift
    private final double[] popPairVarMedians;

    /**
     * Constructor of class {@code EstimatePopVar}.
     *
     * @param sampleInfo a SampleInfo instance containing sample information
     */
    public EstimatePopVar(SampleInfo sampleInfo) {
        this.sampleInfo = sampleInfo;
        popPairNum = (sampleInfo.getPopNum() * (sampleInfo.getPopNum() - 1))/2;
        popPairVarMedians = new double[popPairNum];
    }

    @Override
    public void estimate(VcfInfo vcfInfo) {
        LinkedQueue[] popPairVars = new LinkedQueue[popPairNum];
        for (int i = 0; i < popPairNum; i++) {
            popPairVars[i] = new LinkedQueue();
        }
        for (String snpId:vcfInfo.getSnps()) {
            int[][] tmp = vcfInfo.getCounts(snpId);
            for (int m = 0; m < tmp.length; m++) {
                for (int n = m + 1; n < tmp.length; n++) {
                    int popPairIndex = sampleInfo.getPopPairIndex(m,n);
                    if ((tmp[m][0] + tmp[m][1] == 0) || (tmp[n][0] + tmp[n][1] == 0))
                        continue;
                    popPairVars[popPairIndex].enqueue(Model.calDriftVar(tmp[m][0],tmp[m][1],tmp[n][0],tmp[n][1]));
                }
            }
        }

        findMedians(popPairVars);
    }

    @Override
    void writeLine(BufferedWriter bw) throws IOException {
        for (int i = 0; i < popPairNum; i++) {
            String[] popPair = sampleInfo.getPopPair(i);
            StringJoiner sj = new StringJoiner("\t");
            sj.add(popPair[0]).add(popPair[1]).add(String.valueOf(round(popPairVarMedians[i])));
            bw.write(sj.toString());
            bw.newLine();
        }
    }

    @Override
    void writeHeader(BufferedWriter bw) throws IOException {}

    private void findMedians(LinkedQueue[] popPairVars) {
        for (int i = 0; i < popPairVars.length; i++) {
            int effectedSize = popPairVars[i].size();
            double[] vars = new double[effectedSize];
            int j = 0;
            for (Object d:popPairVars[i]) {
                vars[j++] = (double) d;
            }
            Arrays.sort(vars);
            popPairVarMedians[i] = vars[effectedSize/2];
        }
    }

    private double quickSelectMedian(double[] arr) {
        int k = arr.length / 2;
        return 0;
    }

}
