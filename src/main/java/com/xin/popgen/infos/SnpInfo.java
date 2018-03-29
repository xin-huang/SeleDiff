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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * Class {@code SnpInfo} stores the number of SNPs
 * in the sample.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public class SnpInfo implements Info {
	
	// a Pattern instance for splitting lines
    private final Pattern pattern = Pattern.compile("\\s+");

    // a BufferedReader instance points to an EIGENSTRAT SNP file
    private BufferedReader br = null;

	/**
	 * Constructor of {@code SnpInfo}.
	 *
	 * @param snpFileName the name of an EIGENSTRAT SNP file
	 */
	public SnpInfo(String snpFileName) {
        this.br = getBufferedReader(snpFileName);
        //System.out.println(snpNum + " variants are read from " + snpFileName);
	}

    /**
     * Close the file storing SNP information.
     */
	public void close() {
        try {
            this.br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the information of a SNP.
     * @return the SNP ID, the reference allele, and the alternative allele
     */
    public String get() {
        StringJoiner sj = new StringJoiner("\t");
        try {
        	String line = br.readLine();
        	if (line != null) {
                String[] snpInfo = pattern.split(line.trim());
                sj.add(snpInfo[0]).add(snpInfo[4]).add(snpInfo[5]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sj.toString();
    }

	@Override
	public void parseLine(String line) {}
	
}
