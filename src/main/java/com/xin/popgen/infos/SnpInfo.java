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
                sj.add(snpInfo[1]).add(snpInfo[3]).add(snpInfo[0]).add(snpInfo[4]).add(snpInfo[5]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sj.toString();
    }

	@Override
	public void parseLine(String line) {}
	
}
