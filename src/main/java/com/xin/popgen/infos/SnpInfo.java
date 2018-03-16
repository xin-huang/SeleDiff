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

import java.util.regex.Pattern;

/**
 * Class {@code SnpInfo}
 * 
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
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
