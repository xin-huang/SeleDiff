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

import java.io.IOException;

/**
 * Class {@code VcfInfo} extends Class {@code GenoInfo}
 * and is used for counting alleles and obtaining SNP information from a VCF file.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public final class VcfInfo extends GenoInfo {

    private String info;

    /**
     * Constructor of {@code VcfInfo}.
     *
     * @param genoFileName the name of the file containing genotype data in VCF format
     * @param sampleInfo   a IndInfo instance storing the individual information
     */
    public VcfInfo(String genoFileName, IndInfo sampleInfo, int skip) {
        super(genoFileName, sampleInfo);
        try {
            for (int i = 0; i < skip; i++)
                br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getSnpInfo() {
        return info;
    }

    @Override
    public int[][] countAlleles() {
        int[][] alleleCounts = new int[popNum][2];
        try {
            String line = br.readLine();
            if (line != null)
                alleleCounts = countAlleles(line.trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alleleCounts;
    }

    /**
     * Helper function for counting alleles.
     * @param line a String represents one line in the VCF file
     * @return a 2-D integer array containing counts of each allele
     */
    private int[][] countAlleles(String line) {
        int[][] alleleCounts = new int[popNum][2];
        int end = 0;
        for (int i = 0; i < 2; i++) {
            end = line.indexOf("\t", end+1);
        }
        int start = end + 1;
        for (int i = 0; i < 3; i++) {
            end = line.indexOf("\t", end+1);
        }
        info = line.substring(start, end);
        for (int i = 0; i < 4; i++) {
            end = line.indexOf("\t", end+1);
        }
        for (int i = 0; i < indNum; i++) {
            int popIndex = sampleInfo.getPopIndex(i);
            int allele1 = line.charAt(4*i+end+1) - 48;
            int allele2 = line.charAt(4*i+end+3) - 48;
            if (allele1 >= 0) alleleCounts[popIndex][allele1]++;
            if (allele2 >= 0) alleleCounts[popIndex][allele2]++;
        }
        return alleleCounts;
    }
}
