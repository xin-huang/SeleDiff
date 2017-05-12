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
import java.io.FileWriter;
import java.io.IOException;

import xin.bio.popgen.infos.IndInfo;

/**
 * Class {@code Estimator} defines an abstract class
 * for different kinds of estimations in SeleDiff.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public abstract class Estimator {

    // a SampleInfo instance stores the sample information
    IndInfo sampleInfo;

    // an integer stores how many populations in the sample
    int samplePopNum;
    
    // an integer stores how many individuals in the sample
    int sampleIndNum;
    
    // an integer stores how many population pairs in the sample
    int popPairNum;
    
    // a String array stores population Ids of each pair
    String[][] popPairIds;
    
    /**
     * Constructor of {@code Estimator}.
     * 
     * @param sampleInfo a SampleInfo instance containing sample information
     * @param timeInfo a TimeInfo instance containing divergence times between populations
     */
    public Estimator(IndInfo sampleInfo) {
    	this.sampleInfo = sampleInfo;
    	samplePopNum = sampleInfo.getPopNum();
    	sampleIndNum = sampleInfo.getIndNum();
    	popPairNum = (samplePopNum * (samplePopNum - 1))/2;
    	
    	// get population Ids of different pairs
    	popPairIds = new String[popPairNum][2];
    	for (int i = 0; i < popPairNum; i++) {
    		popPairIds[i] = sampleInfo.getPopPair(i);
    	}
    }
    
    public abstract void analyze(BufferedReader br);
   
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

    /**
     * Helper function for outputting results to files.
     *
     * @param outputFileName the output file name
     */
    public void writeResults(String outputFileName) {
    	BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(outputFileName));
            writeHeader(bw);
            writeLine(bw);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
    
    /**
     * Helper function for counting alleles 
     * 
     * @param line a String allele counts of each indiviudal
     * @return a 2-D integer array containing counts of each allele
     */
    int[][] countAlleles(String line) {
    	int[][] alleleCounts = new int[samplePopNum][2];
    	int indNum = line.length();
    	for (int i = 0; i < indNum; i++) {
    		int popIndex = sampleInfo.getPopIndex(i);
    		int count = line.charAt(i) - 48;
    		switch (count) {
	    		case 0:
	    			alleleCounts[popIndex][1] += 2; 
	    			break;
	    		case 1:
	    			alleleCounts[popIndex][0] += 1;
	    			alleleCounts[popIndex][1] += 1;
	    			break;
	    		case 2:
	    			alleleCounts[popIndex][0] += 2;
	    			break;
	    		default: break;
    		}
    	}
        return alleleCounts;
    }

}