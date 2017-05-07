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

    // an Estimator instance stores which kind of estimation to be performed
    private final Estimator estimator;
    
    // an integer stores how many variants in the sample
    private long snpNum;

    // an integer stores how many individuals in the sample
    private final int sampleIndNum;

    // a String array stores individual IDs in the VCF file
    private String[] indIds;
    
    /**
     * Constructor of class {@code VcfInfo}.
     *
     * @param vcfFileName the file name of a VCF file
     * @param sampleInfo a SampleInfo instance containing sample information
     */
    public VCFInfo(String vcfFileName, int sampleIndNum, Estimator estimator) {
        this.estimator = estimator;
        snpNum = 0;
        
        this.sampleIndNum = sampleIndNum;
        
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
			estimator.parseSnpInfo(line);
		}
    }

}
