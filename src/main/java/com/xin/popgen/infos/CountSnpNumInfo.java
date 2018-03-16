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
package com.xin.popgen.infos;

/**
 * Class {@code CountSnpNumInfo} stores the number of SNPs
 * in the sample.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
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
