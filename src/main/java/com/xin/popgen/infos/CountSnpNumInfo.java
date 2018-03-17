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

/**
 * Class {@code CountSnpNumInfo} stores the number of SNPs
 * in the sample.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public final class CountSnpNumInfo implements Info {
	
	// an integer stores the number of SNPs in the sample
	private int snpNum = 0;

	/**
	 * Constructor of {@code CountSnpNumInfo}.
	 *
	 * @param snpFileName the name of an EIGENSTRAT SNP file
	 */
	public CountSnpNumInfo(String snpFileName) {
		readFile(getBufferedReader(snpFileName));
		System.out.println(snpNum + " variants are read from " + snpFileName);
	}

	/**
	 * Returns how many SNPs in the sample.
	 * @return the number of SNPs in the sample
	 */
	public int getSnpNum() {
		return snpNum;
	}

	@Override
	public void parseLine(String line) {
		snpNum++;
	}
	
}
