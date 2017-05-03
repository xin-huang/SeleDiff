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

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import xin.bio.popgen.infos.PopVarInfo;
import xin.bio.popgen.infos.SampleInfo;
import xin.bio.popgen.infos.TimeInfo;
import xin.bio.popgen.infos.VCFInfo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringJoiner;

/**
 * Class {@code EstimateSeleDiff} extends {@code Estimator} to
 * estimate selection differences between populations.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class EstimateSeleDiff extends Estimator {

    // a PopVarInfo instance stores variances of drift between populations
    private final PopVarInfo popVarInfo;

    // a VcfInfo instance stores the variant information of the sample
    private VCFInfo vcfInfo;

    // a HashMap stores selection differences of each variants between populations
    // key: SNP ID
    // value: a double array containing selection differences between populations
    private final HashMap<String, double[]> diffs;

    // a HashMap stores delta statistics of each variants between populations
    // key: SNP ID
    // value: a double array containing delta statistics between populations
    private final HashMap<String, double[]> deltas;

    // a HashMap stores standard deviation of selection differences of each variants between populations
    // key: SNP ID
    // value: a double array containing standard deviation of selection differences between populations
    private final HashMap<String, double[]> stds;

    // a HashMap stores p-values delta statistics of each variants between populations
    // key: SNP ID
    // value: a double array containing p-values of delta statistics between populations
    private final HashMap<String, double[]> pvalues;

    /**
     * Constructor of class {@code EstimateSeleDiff}.
     *
     * @param popVarInfo a PopVarInfo instance containing variances of drift between populations
     * @param sampleInfo a SampleInfo instance containing sample information
     * @param timeInfo a TimeInfo instance containing divergence times between populations
     */
    public EstimateSeleDiff(PopVarInfo popVarInfo, SampleInfo sampleInfo, TimeInfo timeInfo) {
        this.popVarInfo = popVarInfo;
        this.sampleInfo = sampleInfo;
        this.timeInfo = timeInfo;
        popPairNum = (sampleInfo.getPopNum() * (sampleInfo.getPopNum() - 1))/2;
        diffs = new HashMap<>();
        deltas = new HashMap<>();
        stds = new HashMap<>();
        pvalues = new HashMap<>();
    }

    @Override
    public void estimate(VCFInfo vcfInfo) {
        ChiSquaredDistribution chisq = new ChiSquaredDistribution(1);
        this.vcfInfo = vcfInfo;
        for (String snpId:vcfInfo.getSnps()) {
            int[][] tmp = vcfInfo.getCounts(snpId);
            double[] diff = new double[popPairNum];
            double[] delta = new double[popPairNum];
            double[] std = new double[popPairNum];
            double[] pvalue = new double[popPairNum];
            for (int m = 0; m < tmp.length; m++) {
                for (int n = m + 1; n < tmp.length; n++) {
                    int popPairIndex = sampleInfo.getPopPairIndex(m,n);
                    if ((tmp[m][0] + tmp[m][1] == 0) || (tmp[n][0] + tmp[n][1] == 0))
                        continue;
                    double logOdds = Model.calLogOdds(tmp[m][0],tmp[m][1],tmp[n][0],tmp[n][1]);
                    double varLogOdds = Model.calVarLogOdds(tmp[m][0],tmp[m][1],tmp[n][0],tmp[n][1]);
                    diff[popPairIndex] = logOdds / timeInfo.getTime(popPairIndex);
                    delta[popPairIndex] = logOdds * logOdds / (varLogOdds + popVarInfo.getPopVar(popPairIndex));
                    std[popPairIndex] = Math.sqrt(varLogOdds + popVarInfo.getPopVar(popPairIndex)) / timeInfo.getTime(popPairIndex);
                    pvalue[popPairIndex] = 1.0 - chisq.cumulativeProbability(delta[popPairIndex]);
                }
            }
            diffs.put(snpId,diff);
            deltas.put(snpId,delta);
            stds.put(snpId,std);
            pvalues.put(snpId,pvalue);
        }
    }

    @Override
    void writeLine(BufferedWriter bw) throws IOException {
        for (String snpId:diffs.keySet()) {
            double[] diff = diffs.get(snpId);
            double[] delta = deltas.get(snpId);
            double[] std = stds.get(snpId);
            double[] pvalue = pvalues.get(snpId);
            for (int i = 0; i < popPairNum; i++) {
                String[] popPair = sampleInfo.getPopPair(i);
                StringJoiner sj = new StringJoiner("\t");
                sj.add(snpId)
                        .add(vcfInfo.getAncAllele(snpId))
                        .add(vcfInfo.getDerAllele(snpId))
                        .add(popPair[0])
                        .add(String.valueOf(vcfInfo.getAlleleCount(snpId, sampleInfo.getPopIndex(popPair[0]), 0)))
                        .add(String.valueOf(vcfInfo.getAlleleCount(snpId, sampleInfo.getPopIndex(popPair[0]), 1)))
                        .add(popPair[1])
                        .add(String.valueOf(vcfInfo.getAlleleCount(snpId, sampleInfo.getPopIndex(popPair[1]), 0)))
                        .add(String.valueOf(vcfInfo.getAlleleCount(snpId, sampleInfo.getPopIndex(popPair[1]), 1)))
                        .add(String.valueOf(Model.round(diff[i])))
                        .add(String.valueOf(Model.round(std[i])))
                        .add(String.valueOf(Model.round(diff[i]-1.96*std[i])))
                        .add(String.valueOf(Model.round(diff[i]+1.96*std[i])))
                        .add(String.valueOf(timeInfo.getTime(i)))
                        .add(String.valueOf(Model.round(popVarInfo.getPopVar(i))))
                        .add(String.valueOf(Model.round(delta[i])))
                        .add(String.valueOf(Model.round(pvalue[i])));
                bw.write(sj.toString());
                bw.newLine();
            }
        }
    }

    @Override
    void writeHeader(BufferedWriter bw) throws IOException {
        StringJoiner sj = new StringJoiner("\t");
        sj.add("SNP ID")
                .add("Ancestral allele")
                .add("Derived allele")
                .add("Population 1")
                .add("Ancestral allele count")
                .add("Derived allele count")
                .add("Population 2")
                .add("Ancestral allele count")
                .add("Derived allele count")
                .add("Selection difference (Population 1 - Population 2)")
                .add("Std")
                .add("Lower bound of 95% CI")
                .add("Upper bound of 95% CI")
                .add("Divergence time")
                .add("Variance of drift")
                .add("Delta")
                .add("p-value");
        bw.write(sj.toString());
        bw.newLine();
    }

}
