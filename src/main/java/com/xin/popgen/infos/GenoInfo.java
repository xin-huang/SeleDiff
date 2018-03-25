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

/**
 * Class {@code GenoInfo} is used for counting alleles from genotype file in EIGENSTRAT format.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public class GenoInfo implements Info{

    // an IndInfo instance stores the individual information
    final IndInfo sampleInfo;

    // an integer indicates how many individuals in the sample
    final int indNum;

    // an integer indicates how many populations in the sample
    final int popNum;

    // a BufferedReader instances points to the genotype data
    BufferedReader br = null;

    // a SnpInfo instances stores the information of the SNPs
    SnpInfo snpInfo;

    /**
     * Constructor of {@code GenoInfo}.
     *
     * @param genoFileName the name of the file containing genotype data in EIGENSTRAT format
     * @param sampleInfo a IndInfo instance storing the individual information
     */
    public GenoInfo(String genoFileName, IndInfo sampleInfo) {
        this.sampleInfo = sampleInfo;
        this.indNum = sampleInfo.getIndNum();
        this.popNum = sampleInfo.getPopNum();
        this.br = getBufferedReader(genoFileName);
    }

    /**
     * Close the file storing genotype information.
     */
    public void close() {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function to set the SNP information corresponding to the genotype data.
     * @param snpInfo the SNP information
     */
    public void setSnpInfo(SnpInfo snpInfo) {
        this.snpInfo = snpInfo;
    }

    /**
     * Returns the information of a SNP.
     * @return the information of a SNP
     */
    public String getSnpInfo() { return snpInfo.get(); }

    /**
     * Returns the counts of alleles.
     * @return the counts of alleles
     */
    public int[][] countAlleles() {
        char[] cbuf = new char[indNum];
        int[][] alleleCounts = new int[popNum][2];
        try {
            br.read(cbuf);
            alleleCounts = countAlleles(cbuf);
            br.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alleleCounts;
    }

    /**
     * Helper function for counting alleles.
     *
     * @param cbuf a char array containing alleles
     * @return a 2-D integer array containing counts of each allele
     */
    private int[][] countAlleles(char[] cbuf) {
        int[][] alleleCounts = new int[popNum][2];
        if (indNum != cbuf.length)
            throw new IllegalArgumentException("The column in .geno file is not consistent with individual number.");
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

    @Override
    public void parseLine(String line) {}

}
