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
import xin.bio.popgen.infos.TimeInfo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class {@code Estimator} defines an abstract class
 * for different kinds of estimations in SeleDiff.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public abstract class Estimator {

    // a SampleInfo instance stores the sample information
    SampleInfo sampleInfo;

    // a TimeInfo instance stores the information of divergence times
    TimeInfo timeInfo;

    // an integer stores how many population pairs in the sample
    int popPairNum;
    
    // a String array stores population Ids of each pair
    String[][] popPairIds;
    
    // an ArrayList stores SNP IDs
    ArrayList<String> snpIds;
    
    // an ArrayList stores reference alleles
    ArrayList<String> refAlleles;
    
    // an ArrayList stores alternative alleles
    ArrayList<String> altAlleles;
    
    /**
     * Constructor of {@code Estimator}.
     * 
     * @param sampleInfo a SampleInfo instance containing sample information
     * @param timeInfo a TimeInfo instance containing divergence times between populations
     */
    public Estimator(SampleInfo sampleInfo, TimeInfo timeInfo) {
    	this.sampleInfo = sampleInfo;
    	this.timeInfo = timeInfo;
    	popPairNum = (sampleInfo.getPopNum() * (sampleInfo.getPopNum() - 1))/2;
    	
    	// get population Ids of different pairs
    	popPairIds = new String[popPairNum][2];
    	for (int i = 0; i < popPairNum; i++) {
    		popPairIds[i] = sampleInfo.getPopPair(i);
    	}
    }
    
    /**
     * An abstract method for performing estimations from variant counts.
     * 
     * @param alleleCounts an integer array containing allele counts
     */
    public abstract void accept(int[][] alleleCounts);

    /**
     *An abstract method for running estimation.
     */
    public abstract void estimate();
   
    /**
     * An abstract method for adding variant information
     * 
     * @param snpId a SNP ID
     * @param refAllele a reference allele
     * @param altAllele an alternative allele
     */
    public abstract void addSnpInfo(String snpId, String refAllele, String altAllele);

    /**
     * An abstract method for outputting results to files.
     *
     * @param outputFileName the output file name
     */
    public void writeResults(String outputFileName) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
            writeHeader(bw);
            writeLine(bw);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * An abstract method for writing lines to the output file.
     *
     * @param bw a BufferedWriter instance to the output file
     * @throws IOException
     */
    abstract void writeLine(BufferedWriter bw) throws IOException;

    /**
     * An abstract method for writing header to the output file.
     *
     * @param bw a BufferedWriter instance to the output file
     * @throws IOException
     */
    abstract void writeHeader(BufferedWriter bw) throws IOException;

}