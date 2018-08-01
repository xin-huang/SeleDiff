package com.xin.popgen.infos;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * Class {@code CountInfo} is used for counting alleles and obtaining SNP information from a .count file.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public class CountInfo implements Info {

    // a Pattern for splitting lines
    private final Pattern pattern = Pattern.compile("\\s+");

    // an integer stores how many populations in the sample
    private int popNum = 0;

    // a PopInfo instance stores population information
    private PopInfo popInfo;

    // a BufferedReader instances points to the input data
    BufferedReader br = null;

    // a String stores the information of a SNP
    String info;

    // default constructor
    CountInfo() {}

    public CountInfo(String countFileName) {
        this.br = getBufferedReader(countFileName);
        try {
            String[] elements = pattern.split(br.readLine().trim());
            HashSet<String> popSet = new HashSet<>();
            HashMap<String, Integer> popIndex = new HashMap<>();
            for (int i = 5; i < elements.length; i += 2) {
                popSet.add(elements[i]);
            }
            this.popNum = popSet.size();
            if (popNum == 1) throw new IllegalArgumentException("Find Only 1 population in " + countFileName
                    + ", please check your input file.");

            int i = 0;
            String[] popIds = new String[popNum];
            for (String popId:popSet) {
                popIndex.put(popId,i);
                popIds[i++] = popId;
            }

            this.popInfo = new PopInfo(popIndex, popIds);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(popNum + " populations are read from " + countFileName);
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
     * Returns the information of a SNP.
     *
     * @return the information of a SNP
     */
    public String getSnpInfo() { return this.info; }

    /**
     * Returns a PopInfo instance containing population information.
     *
     * @return a PopInfo instance
     */
    public PopInfo getPopInfo() { return popInfo; }

    /**
     * Returns the counts of alleles.
     *
     * @return the counts of alleles
     */
    public int[][] countAlleles() {
        int[][] alleleCounts = new int[popNum][2];
        try {
            String line = br.readLine();
            if (line != null)
                alleleCounts = countAlleles(line.trim());
            else return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return alleleCounts;
    }

    /**
     * Helper function for counting alleles.
     *
     * @param line a String represents one line in the VCF file
     * @return a 2-D integer array containing counts of each allele
     */
    private int[][] countAlleles(String line) {
        int[][] alleleCounts = new int[popNum][2];
        String[] elements = pattern.split(line);
        StringJoiner sj = new StringJoiner("\t");
        sj.add(elements[2]).add(elements[3]).add(elements[4]);
        this.info = sj.toString();
        int j = 5;
        for (int i = 0; i < popNum; i++) {
            alleleCounts[i][0] = Integer.parseInt(elements[j++]);
            alleleCounts[i][1] = Integer.parseInt(elements[j++]);
        }
        return  alleleCounts;
    }

    @Override
    public void parseLine(String line) {}
}
