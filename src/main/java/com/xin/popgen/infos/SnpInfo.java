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
	
	// an integer stores the number of SNPs in the sample
	private int snpNum = 0;

	// an integer indicates how many start line to skip
	private int skip = 0;

	// an integer indicates which column stores SNP ID
	private int idCol = 0;

	// an integer indicates which column stores reference allele
	private int refCol = 4;

	// an integer indicates which column stores alternative allele
	private int altCol = 5;

	// a String stores the name of the file containing SNP information
	private final String snpFileName;

    private final Pattern pattern = Pattern.compile("\\s+");

    private BufferedReader br = null;

	/**
	 * Constructor of {@code SnpInfo}.
	 *
	 * @param snpFileName the name of an EIGENSTRAT SNP file
	 */
	public SnpInfo(String snpFileName) {
	    this.snpFileName = snpFileName;
	}

    /**
     * Open the file storing SNP information.
     */
	public void open() {
        this.br = getBufferedReader(snpFileName);
        try {
            for (int i = 0; i < skip; i++)
                br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     *
     * @param format
     */
    public void setFormat(char format) {
	    if (format == 'v') {
	        idCol = 2;
	        refCol = 3;
	        altCol = 4;
        }
    }

    /**
     * Returns the information of a SNP.
     * @return the SNP ID, the reference allele, and the alternative allele
     */
    public String get() {
        StringJoiner sj = new StringJoiner("\t");
        try {
            String[] snpInfo = pattern.split(br.readLine().trim());
            sj.add(snpInfo[idCol]).add(snpInfo[refCol]).add(snpInfo[altCol]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sj.toString();
    }

	/**
	 * Returns how many SNPs in the sample.
	 * @return the number of SNPs in the sample
	 */
	public int getSnpNum() {
        readFile(getBufferedReader(snpFileName));
        System.out.println(snpNum + " variants are read from " + snpFileName);
	    return snpNum;
	}

    /**
     *
     * @return
     */
	public int getSkipNum() {
	    return skip;
    }

	@Override
	public void parseLine(String line) {
		if (line.startsWith("#"))
		    skip++;
	    snpNum++;
	}
	
}
