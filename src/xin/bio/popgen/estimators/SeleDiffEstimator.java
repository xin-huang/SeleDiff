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
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import xin.bio.popgen.infos.PopVarInfo;
import xin.bio.popgen.infos.SampleInfo;
import xin.bio.popgen.infos.TimeInfo;

/**
 * Class {@code SeleDiffEstimator} extends {@code Estimator} to
 * estimate selection differences between populations.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class SeleDiffEstimator extends Estimator {

    // a PopVarInfo instance stores variances of drift between populations
    private final PopVarInfo popVarInfo;

    // a double array stores log-Odds ratios between populations
    private final DoubleArrayList[] logOdds;
    
    // a double array stores variances of log-Odds ratios between populations
    private final DoubleArrayList[] varLogOdds;
    
    // a ChiSquaredDistribution instance for performing chi-square tests
    private final ChiSquaredDistribution chisq;
    
    // a String stores the name of the file containing ancestral allele information
    private final String ancAlleleFileName;
    
    /**
     * Constructor of class {@code SeleDiffEstimator}.
     *
     * @param ancAlleleFileName the name of the file containing ancestral allele information
     * @param popVarInfo a PopVarInfo instance containing variances of drift between populations
     * @param sampleInfo a SampleInfo instance containing sample information
     * @param timeInfo a TimeInfo instance containing divergence times between populations
     */
    public SeleDiffEstimator(String ancAlleleFileName, 
    		PopVarInfo popVarInfo, SampleInfo sampleInfo, TimeInfo timeInfo) {
    	super(sampleInfo, timeInfo);
        this.popVarInfo = popVarInfo;
        this.chisq = new ChiSquaredDistribution(1);
        this.ancAlleleFileName = ancAlleleFileName;
        this.snpIds = new ArrayList<>();
        this.refAlleles = new ArrayList<>();
        this.altAlleles = new ArrayList<>();
        
        logOdds = new DoubleArrayList[popPairNum];
        varLogOdds = new DoubleArrayList[popPairNum];
        for (int i = 0; i < popPairNum; i++) {
        	logOdds[i] = new DoubleArrayList();
        	varLogOdds[i] = new DoubleArrayList();
        }
    }
    
    @Override
    public void parseSnpInfo(String line) {
		// Read SNP information
		int start = 0, end = 0;
		for (int i = 0; i < 9; i++) {
			end = line.indexOf("\t", start);
			if (i == 2) snpIds.add(line.substring(start, end));
			if (i == 3) refAlleles.add(line.substring(start, end));
			if (i == 4) altAlleles.add(line.substring(start, end));
			start = end + 1;
		}
        // Read allele counts of individuals
		int[][] alleleCounts = countAlleles(line, start);
    	for (int m = 0; m < alleleCounts.length; m++) {
			for (int n = m + 1; n < alleleCounts.length; n++) {
				int popPairIndex = sampleInfo.getPopPairIndex(m,n);
				if ((alleleCounts[m][0] + alleleCounts[m][1] == 0) 
						|| (alleleCounts[n][0] + alleleCounts[n][1] == 0))
                    continue;
				logOdds[popPairIndex].add(Model.calLogOdds(alleleCounts[m][0], alleleCounts[m][1], 
						alleleCounts[n][0], alleleCounts[n][1]));
				varLogOdds[popPairIndex].add(Model.calVarLogOdds(alleleCounts[m][0], alleleCounts[m][1], 
						alleleCounts[n][0], alleleCounts[n][1]));
			}
		}
    }
    
    @Override
    public void estimate() {
    	if (ancAlleleFileName == null)
    		return;
    	int i = 0;
    	HashMap<String, Integer> snpIndices = new HashMap<>(snpIds.size());
    	for (String snpId:snpIds) {
    		snpIndices.put(snpId, i++);
    	}
    	try {
			BufferedReader br = new BufferedReader(new FileReader(ancAlleleFileName));
			String line;
			while ((line = br.readLine()) != null) {
				String[] elements = line.trim().split("\\s+");
				String snpId = elements[0];
				String ancAllele = elements[1];
				if (snpIndices.containsKey(snpId)) {
					int index = snpIndices.get(snpId);
					String refAllele = refAlleles.get(index);
					if (!ancAllele.equals(refAlleles.get(index))) {
						altAlleles.set(index, refAllele);
						refAlleles.set(index, ancAllele);
						for (int j = 0; j < popPairNum; j++) {
							double logOdd = logOdds[index].getDouble(j);
							logOdds[index].set(j, -1*logOdd);
						}
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    void writeLine(BufferedWriter bw) throws IOException {
    	int snpNum = snpIds.size();
    	for (int i = 0; i < snpNum; i++) {
    		String snpId = snpIds.get(i);
    		String ancAllele = refAlleles.get(i);
    		String derAllele = altAlleles.get(i);
    		for (int j = 0; j < popPairNum; j++) {
    			double logOdd = logOdds[j].getDouble(i);
    			double varLogOdd = varLogOdds[j].getDouble(i);
    			double diff = logOdd / timeInfo.getTime(j);
    			double std = Math.sqrt(varLogOdd + popVarInfo.getPopVar(j))
    					/ timeInfo.getTime(j);
    			double delta = logOdd * logOdd / (varLogOdd + popVarInfo.getPopVar(j));
    			double pvalue = 1.0 - chisq.cumulativeProbability(delta);
    			
    			StringJoiner sj = new StringJoiner("\t");
    			sj.add(snpId)
    				.add(ancAllele)
    				.add(derAllele)
    				.add(popPairIds[j][0])
    				.add(popPairIds[j][1])
    				.add(String.valueOf(Model.round(diff)))
    				.add(String.valueOf(Model.round(std)))
    				.add(String.valueOf(Model.round(diff-1.96*std)))
    				.add(String.valueOf(Model.round(diff+1.96*std)))
    				.add(String.valueOf(timeInfo.getTime(j)))
    				.add(String.valueOf(Model.round(popVarInfo.getPopVar(j))))
    				.add(String.valueOf(Model.round(delta)))
    				.add(String.valueOf(Model.round(pvalue)));
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
                .add("Population 2")
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
