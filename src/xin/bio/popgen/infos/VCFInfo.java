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
package xin.bio.popgen.infos;

import xin.bio.popgen.estimators.Estimator;


/**
 * Class {@code VcfInfo} stores variant information of the sample.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class VCFInfo implements Info  {

    // a SampleInfo instance stores sample information
    private final SampleInfo sampleInfo;
    
    // an Estimator instance stores which kind of estimation to be performed
    private final Estimator estimator;
    
    // an integer stores how many variants in the sample
    private long snpNum;

    // an integer stores how many individuals in the sample
    private final int sampleIndNum;

    // an integer stores how many populations in the sample
    private final int samplePopNum;

    // a String array stores individual IDs in the VCF file
    private String[] indIds;
    
    /**
     * Constructor of class {@code VcfInfo}.
     *
     * @param vcfFileName the file name of a VCF file
     * @param sampleInfo a SampleInfo instance containing sample information
     */
    public VCFInfo(String vcfFileName, SampleInfo sampleInfo, Estimator estimator) {
        this.sampleInfo = sampleInfo;
        this.estimator = estimator;
        snpNum = 0;
        
        sampleIndNum = sampleInfo.getIndNum();
        samplePopNum = sampleInfo.getPopNum();
        
        readFile(vcfFileName);
        estimator.estimate();

        System.out.println(snpNum + " variants are read from " + vcfFileName);
    }

    @Override
    public void parseLine(String line) {
    	if (line.startsWith("##")) return;
    	else if(line.startsWith("#C")) {
			int start = 0, end = 0, vcfIndNum = 0;
			// Read individual IDs
			indIds = new String[sampleIndNum];
			for (int i = 0; i < 9; i++) {
				end = line.indexOf("\t", start);
				start = end + 1;
			}
			while ((end = line.indexOf("\t", start)) > 0) {
				indIds[vcfIndNum++] = line.substring(start, end);
				start = end + 1;
			}
		}
		else {
			snpNum++;
			int[][] alleleCounts = new int[samplePopNum][2];
			// Read SNP information
			String snpId = null, refAllele = null, altAllele = null;
			int start = 0, end = 0, i = 0;
			for (i = 0; i < 9; i++) {
				end = line.indexOf("\t", start);
				if (i == 2) snpId = line.substring(start, end);
				if (i == 3) refAllele = line.substring(start, end);
				if (i == 4) altAllele = line.substring(start, end);
				start = end + 1;
			}
			estimator.addSnpInfo(snpId, refAllele, altAllele);
            // Read allele counts of individuals
            i = 0;
            while ((end = line.indexOf("\t", start)) > 0) {
				int popIndex = sampleInfo.getPopIndex(sampleInfo.ind2PopId(i++));
				int allele1 = line.charAt(start) - 48;
				int allele2 = line.charAt(end-1) - 48;
				if (allele1 >= 0)
					alleleCounts[popIndex][allele1]++;
				if (allele2 >= 0)
					alleleCounts[popIndex][allele2]++;
				start = end + 1;
			}
            estimator.accept(alleleCounts);
		}
    }

}
