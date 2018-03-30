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

import java.io.IOException;

/**
 * Class {@code GenoInfo} is used for counting alleles from genotype file in EIGENSTRAT format.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public final class GenoInfo extends VcfInfo{

    // a SnpInfo instances stores the information of the SNPs
    private final SnpInfo snpInfo;

    /**
     * Constructor of {@code GenoInfo}.
     *
     * @param genoFileName the name of the file containing genotype data in EIGENSTRAT format
     * @param sampleInfo a IndInfo instance storing the individual information
     */
    public GenoInfo(String genoFileName, IndInfo sampleInfo, String snpFileName) {
        super(genoFileName, sampleInfo, false);
        this.snpInfo = new SnpInfo(snpFileName);
    }

    @Override
    public void close() {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        snpInfo.close();
    }

    @Override
    public String getSnpInfo() { return snpInfo.get(); }

    @Override
    public int[][] countAlleles() {
        char[] cbuf = new char[indNum];
        int[][] alleleCounts = new int[popNum][2];
        try {
            if (br.read(cbuf) == -1) return null;
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

}
