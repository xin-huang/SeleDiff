package com.xin.popgen.infos;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * Created by deep on 18-7-27.
 */
public class CountInfo implements Info {

    // a Pattern for splitting lines
    private final Pattern pattern = Pattern.compile("\\s+");

    private int popNum;

    private PopInfo popInfo;

    // a BufferedReader instances points to the input data
    BufferedReader br = null;

    // a String stores the information of a SNP
    private String info;

    public CountInfo() {}

    public CountInfo(String countFileName) {
        this.br = getBufferedReader(countFileName);
        try {
            String[] elements = pattern.split(br.readLine().trim());
            HashSet<String> popSet = new HashSet<>();
            HashMap<String, Integer> popIndex = new HashMap<>();
            for (int i = 4; i < elements.length; i += 2) {
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

    public String getSnpInfo() { return this.info; }

    public PopInfo getPopInfo() { return popInfo; }

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

    private int[][] countAlleles(String line) {
        int[][] alleleCounts = new int[popNum][2];
        String[] elements = pattern.split(line);
        StringJoiner sj = new StringJoiner("\t");
        for (int i = 0; i < 3; i++) {
            sj.add(elements[i]);
        }
        this.info = sj.toString();
        int j = 4;
        for (int i = 0; i < popNum; i++) {
            alleleCounts[i][0] = Integer.parseInt(elements[j]);
            alleleCounts[i][1] = Integer.parseInt(elements[++j]);
        }
        return  alleleCounts;
    }

    @Override
    public void parseLine(String line) {}
}
