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
package com.xin.popgen.infos;

import java.util.regex.Pattern;

/**
 * Class {@code SnpInfo}
 * 
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public class SnpInfo implements Info {
	
	// a String array stores SNP information: SNP ID, Ref Allele, Alt Allele
	private final String[] snps;
	
    // an integer to record the index of the SNP currently parsing
    private int snpIndex = 0;
    
    // a Pattern for splitting lines
    private final Pattern pattern = Pattern.compile("\\s+");
    
    /**
     * Constructor of {@code SnpInfo}.
     * 
     * @param snpFileName the name of a EIGENSTRAT SNP file
     * @param snpNum the number of SNPs
     */
    public SnpInfo(String snpFileName, int snpNum) {
    	snps = new String[snpNum];
    	readFile(getBufferedReader(snpFileName));
    }

	/**
	 * Returns the information of the i-th SNP.
	 * 
	 * @param i the i-th SNP
	 * @return the information of the i-th SNP
	 */
	public String getSnp(int i) { return snps[i]; }

	@Override
	public void parseLine(String line) {
		String[] elements = pattern.split(line);
		StringBuilder sb = new StringBuilder();
		snps[snpIndex] = sb.append(elements[0]).append("\t")
				.append(elements[4]).append("\t")
				.append(elements[5]).append("\t")
				.toString();
		snpIndex++;
	}
	
}
