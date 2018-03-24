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
import java.io.FileWriter;
import java.io.IOException;

import com.xin.popgen.infos.GenoInfo;
import com.xin.popgen.infos.SnpInfo;
import com.xin.popgen.infos.IndInfo;
import com.xin.popgen.infos.VcfInfo;

/**
 * Class {@code Estimator} defines an abstract class
 * for different kinds of estimations in SeleDiff.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public abstract class Estimator {
	
	private static final int POW10[] = {1, 10, 100, 1000, 10000, 100000, 1000000};
	
    // a GenoInfo instance stores the genotype information
	final GenoInfo genoInfo;

	// a SampleInfo instance stores the sample information
    final IndInfo sampleInfo;

    final SnpInfo snpInfo;

    // an integer stores how many populations in the sample
    final int popNum;
    
    // an integer stores how many individuals in the sample
    final int indNum;
    
    // an integer stores how many SNPs in the sample
    final int snpNum;
    
    // an integer stores how many population pairs in the sample
    final int popPairNum;
    
    // a String array stores population Ids of each pair
    final String[][] popPairIds;

    final String outputFileName;
    
    /**
     * Constructor of {@code Estimator}.
     * 
     * @param indFileName an EIGENSTRAT .ind file name
     * @param snpFileName an EIGENSTRAT .snp file name
     */
    Estimator(String genoFileName, String indFileName, String snpFileName, String outputFileName, char format) {
    	this.sampleInfo = new IndInfo(indFileName);
    	this.snpInfo = new SnpInfo(snpFileName);
    	snpInfo.setFormat(format);
    	this.popNum = sampleInfo.getPopNum();
    	this.indNum = sampleInfo.getIndNum();
    	this.snpNum = snpInfo.getSnpNum();
    	this.popPairNum = (popNum * (popNum - 1))/2;
    	if (format == 'v')
    	    this.genoInfo = new VcfInfo(genoFileName, sampleInfo, snpInfo.getSkipNum());
    	else
            this.genoInfo = new GenoInfo(genoFileName, sampleInfo);
        this.outputFileName = outputFileName;
    	
    	// get population Ids of different pairs
    	this.popPairIds = new String[popPairNum][2];
    	for (int i = 0; i < popPairNum; i++) {
    		this.popPairIds[i] = sampleInfo.getPopPair(i);
    	}
    }

	/**
	 * An abstract method for analyzing genotypes.
	 */
	public abstract void analyze();
   
    /**
     * Helper function for outputting results to files.
     *
     * @throws IOException 
     */
    protected void writeResults() {
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
    
    String format(double[] vals, int precision) {
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
    
    String format(double val, int precision) {
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
    
}