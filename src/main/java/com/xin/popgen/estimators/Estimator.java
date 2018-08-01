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
import java.io.FileWriter;
import java.io.IOException;

import com.xin.popgen.infos.*;

/**
 * Class {@code Estimator} defines an abstract class
 * for different kinds of estimations in SeleDiff.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public abstract class Estimator {
	
	private static final int POW10[] = {1, 10, 100, 1000, 10000, 100000, 1000000};
	
    // a GenoInfo instance stores the genotype information
	final CountInfo genoInfo;

	final PopInfo popInfo;

	// a SampleInfo instance stores the sample information
    IndInfo sampleInfo;

    // an integer stores how many populations in the sample
    final int popNum;
    
    // an integer stores how many individuals in the sample
    int indNum = 0;
    
    // an integer stores how many population pairs in the sample
    final int popPairNum;
    
    // a String array stores population Ids of each pair
    final String[][] popPairIds;

    String snpFileName;

    // a String stores the name of the output file
    final String outputFileName;
    
    /**
     * Constructor of {@code Estimator}.
     * 
     * @param indFileName an EIGENSTRAT .ind file name
     * @param snpFileName an EIGENSTRAT .snp file name
     */
    Estimator(String genoFileName, String indFileName, String snpFileName, String outputFileName, char format) {
        this.snpFileName = snpFileName;
        if ((format == 'v') || (format == 'e')) {
            this.sampleInfo = new IndInfo(indFileName);
            this.indNum = sampleInfo.getIndNum();
            this.popInfo = sampleInfo.getPopInfo();
    	    if (format == 'v')
                this.genoInfo = new VcfInfo(genoFileName, sampleInfo, popInfo, true);
    	    else
                this.genoInfo = new GenoInfo(genoFileName, sampleInfo, popInfo, snpFileName);
        }
        else {
    	    this.genoInfo = new CountInfo(genoFileName);
    	    this.popInfo = genoInfo.getPopInfo();
        }

    	this.popNum = popInfo.getPopNum();
    	this.popPairNum = (popNum * (popNum - 1))/2;
        this.outputFileName = outputFileName;
    	
    	// get population Ids of different pairs
    	this.popPairIds = new String[popPairNum][2];
    	for (int i = 0; i < popPairNum; i++) {
    		this.popPairIds[i] = popInfo.getPopPair(i);
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
    	BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(outputFileName));
            writeHeader(bw);
            writeLine(bw);
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
