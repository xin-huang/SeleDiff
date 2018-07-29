package com.xin.popgen.infos;

import java.util.HashMap;

/**
 * Created by deep on 18-7-27.
 */
public class PopInfo {

    // a HashMap stores population IDs and their corresponding indices
    // key: population ID; value: index
    private final HashMap<String, Integer> popIndex;

    // a String array stores population IDs
    private final String[] popIds;

    // an integer stores how many populations in the sample
    private final int popNum;

    public PopInfo(HashMap<String, Integer> popIndex, String[] popIds) {
        this.popIndex = popIndex;
        this.popIds = popIds;
        this.popNum = popIds.length;
    }

    /**
     * Returns how many populations in the sample.
     *
     * @return how many populations in the sample
     */
    public int getPopNum() { return popNum; }

    /**
     * Checks whether a population ID exists in the sample.
     *
     * @param popId a population ID
     * @return true, the population ID exists; false, the population ID does not exist
     */
    boolean containsPopId(String popId) {
        return popIndex.containsKey(popId);
    }

    /**
     * Returns the index of a triangular array given two population IDs.
     *
     * @param popi the first population ID
     * @param popj the second population ID
     * @return the index of the triangular array
     */
    int getPopPairIndex(String popi, String popj) {
        return getPopPairIndex(getPopIndex(popi), getPopIndex(popj));
    }

    /**
     * Returns the population ID of an index.
     *
     * @param i a population index
     * @return the population ID
     */
    private String getPopId(int i) { return popIds[i]; }

    /**
     * Returns the population index of a population ID.
     *
     * @param popId a population ID
     * @return the population index
     */
    private int getPopIndex(String popId) {
        return popIndex.get(popId);
    }

    /**
     * Returns the index of a triangular array given two population indices.
     *
     * @param i the index of the first population
     * @param j the index of the second population
     * @return the index of the triangular array
     */
    public int getPopPairIndex(int i, int j) {
        int k;
        if (i < j)
            k = i*popNum - (i+2)*(i+1)/2 + j;
        else
            k = j*popNum - (j+2)*(j+1)/2 + i;
        return k;
    }

    /**
     * Returns the IDs of a population pair given a population pair index.
     *
     * @param k a population pair index
     * @return the IDs of the population pair
     */
    public String[] getPopPair(int k) {
        int i = 0;
        while (((k+(i+1)*(i+2)/2)/popNum) != i) {
            i++;
        }
        int j = k - i * popNum + (i+2)*(i+1)/2;
        return new String[]{getPopId(i), getPopId(j)};
    }

}
