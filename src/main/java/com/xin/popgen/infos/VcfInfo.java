package com.xin.popgen.infos;

import java.io.IOException;

public class VcfInfo extends GenoInfo {
    /**
     * Constructor of {@code GenoInfo}.
     *
     * @param genoFileName the name of the file containing genotype data
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

    private int[][] countAlleles(String line) {
        int[][] alleleCounts = new int[popNum][2];
        String[] elements = line.split("\\s+");
        for (int i = 9; i < elements.length; i++) {
            String[] alleles = elements[i].split("[|/]");
            int popIndex = sampleInfo.getPopIndex(i - 9);
            if (alleles[0].equals("0")) alleleCounts[popIndex][0]++;
            else if (alleles[0].equals("1")) alleleCounts[popIndex][1]++;
            if (alleles[1].equals("0")) alleleCounts[popIndex][0]++;
            else if (alleles[1].equals("1")) alleleCounts[popIndex][1]++;
        }
        return alleleCounts;
    }
}
