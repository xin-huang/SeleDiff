/*
  Copyright (C) 2018 Xin Huang

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
package com.xin.popgen.estimators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.xin.popgen.infos.CountSnpNumInfo;
import com.xin.popgen.infos.IndInfo;

/**
 * Class {@code Estimator} defines an abstract class
 * for different kinds of estimations in SeleDiff.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public abstract class Estimator {
	
	protected static final int POW10[] = {1, 10, 100, 1000, 10000, 100000, 1000000};
	
    // a SampleInfo instance stores the sample information
    protected final IndInfo sampleInfo;

    // an integer stores how many populations in the sample
    protected final int popNum;
    
    // an integer stores how many individuals in the sample
    protected final int indNum;
    
    // an integet stores how many SNPs in the sample
    protected final int snpNum;
    
    // an integer stores how many population pairs in the sample
    protected final int popPairNum;
    
    // a String array stores population Ids of each pair
    protected final String[][] popPairIds;
    
    protected int snpIndex = 0;
    
    /**
     * Constructor of {@code Estimator}.
     * 
     * @param sampleInfo a SampleInfo instance containing sample information
     * @param timeInfo a TimeInfo instance containing divergence times between populations
     */
    public Estimator(String indFileName, String snpFileName) {
    	this.sampleInfo = new IndInfo(indFileName);
    	this.popNum = sampleInfo.getPopNum();
    	this.indNum = sampleInfo.getIndNum();
    	this.snpNum = new CountSnpNumInfo(snpFileName).getSnpNum();
    	this.popPairNum = (popNum * (popNum - 1))/2;
    	
    	// get population Ids of different pairs
    	this.popPairIds = new String[popPairNum][2];
    	for (int i = 0; i < popPairNum; i++) {
    		this.popPairIds[i] = sampleInfo.getPopPair(i);
    	}
    }
    
    public abstract void analyze(List<String> genoFileNames);
   
    /**
     * Helper function for outputting results to files.
     *
     * @param outputFileName the output file name
     * @throws IOException 
     */
    public void writeResults(String outputFileName) {
    	long start = System.currentTimeMillis();
    	BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(outputFileName));
            writeHeader(bw);
            writeLine(bw);
            //bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        long end = System.currentTimeMillis();
        System.out.println("Used Time for writing: " + ((end-start)/1000) + " seconds");
    }
    
    protected abstract void parseLine(char[] cbuf);
    
    /**
     * An abstract method for writing lines to the output file.
     *
     * @param bw a BufferedWriter instance to the output file
     * @throws IOException
     */
    protected abstract void writeLine(BufferedWriter bw) throws IOException;

    /**
     * An abstract method for writing header to the output file.
     *
     * @param bw a BufferedWriter instance to the output file
     * @throws IOException
     */
    protected abstract void writeHeader(BufferedWriter bw) throws IOException;
    
    /**
     * Helper function for reading files
     * 
     * @param br a BufferedReader instance to the input file
     */
    protected void readFile(BufferedReader br) {
    	char[] cbuf = new char[indNum];
    	try {
    		for (int i = 0; i < snpNum; i++) {
    			br.read(cbuf);
    			parseLine(cbuf);
    			br.read();
    		}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
    protected String format(double[] vals, int precision) {
    	StringBuilder sb = new StringBuilder();
    	for (double val:vals) {
	    	if (val < 0) {
	    		sb.append('-');
	    		val = -val;
	    	}
	    	int exp = POW10[precision];
	    	long lval = (long) (val * exp + 0.5);
	    	sb.append(String.valueOf(lval / exp)).append('.');
	    	long fval = lval % exp;
	    	for (int p = precision - 1; p > 0 && fval < POW10[p]; p-- ) { sb.append('0'); }
	    	sb.append(String.valueOf(fval));
	    	sb.append("\t");
    	}
    	return sb.toString();
    }
    
    protected String format(double val, int precision) {
    	StringBuilder sb = new StringBuilder();
    	if (val < 0) {
    		sb.append('-');
    		val = -val;
    	}
    	int exp = POW10[precision];
    	long lval = (long) (val * exp + 0.5);
    	sb.append(String.valueOf(lval / exp)).append('.');
    	long fval = lval % exp;
    	for (int p = precision - 1; p > 0 && fval < POW10[p]; p-- ) { sb.append('0'); }
    	sb.append(String.valueOf(fval));
    	return sb.toString();
    }
    
    /**
     * Helper function for counting alleles 
     * 
     * @param line a String allele counts of each individual
     * @return a 2-D integer array containing counts of each allele
     */
    protected int[][] countAlleles(char[] cbuf) {
    	int[][] alleleCounts = new int[popNum][2];
    	if (indNum != cbuf.length)
    		throw new IllegalArgumentException();
    	for (int i = 0; i < indNum; i++) {
    		int popIndex = sampleInfo.getPopIndex(i);
    		int count = cbuf[i] - 48;
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