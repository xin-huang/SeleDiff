/*
  Copyright (C) 2017 Xin Huang

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
package xin.bio.popgen.infos;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Class {@code SampleInfo} stores the sample information, including:
 * population IDs, individual IDs, population numbers and individual numbers.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class SampleInfo implements Info {

    // a HashMap stores individual IDs and their corresponding population IDs
    // key: individual ID; value: population ID
    private final HashMap<String, String> ind2pop;

    // a HashMap stores population IDs and their corresponding indices
    // key: population ID; value: index
    private final HashMap<String, Integer> popIndex;

    // a HashSet stores population IDs
    private final HashSet<String> popSet;

    // a String array stores population IDs
    private final String[] popIds;

    // an integer stores how many populations in the sample
    private final int popNum;

    // an integer stores how many individuals in the sample
    private final int indNum;

    /**
     * Constructor of {@code SampleInfo}.
     * A sample file is space delimited without header,
     * where the first column is individual ID,
     * and the second column is population ID.
     *
     * @param sampleFileName the file name of a sample file
     */
    public SampleInfo(String sampleFileName) {
        ind2pop = new HashMap<>(); // key: individual id; value: pop id
        popIndex = new HashMap<>(); // key: pop id; value: pop index
        popSet = new HashSet<>();
        readFile(sampleFileName);

        int i = 0;
        indNum = ind2pop.keySet().size();
        popNum = popSet.size();
        popIds = new String[popNum];
        for (String popId:popSet) {
            popIndex.put(popId,i);
            popIds[i++] = popId;
        }
    }

    /**
     * Returns the population ID of an individual.
     *
     * @param indId a individual ID
     * @return the population ID
     */
    public String getPopId(String indId) {
        return ind2pop.get(indId);
    }

    /**
     * Returns the population ID of an index.
     *
     * @param i a index
     * @return the population ID
     */
    private String getPopId(int i) { return popIds[i]; }

    /**
     * Returns the population index of a population ID.
     *
     * @param popId a population ID
     * @return the population index
     */
    public int getPopIndex(String popId) {
        return popIndex.get(popId);
    }

    /**
     * Returns the index of a triangular array given two population IDs.
     *
     * @param popi the first population ID
     * @param popj the second population ID
     * @return the index of the triangular array
     */
    public int getPopPairIndex(String popi, String popj) {
        return getPopPairIndex(getPopIndex(popi), getPopIndex(popj));
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
            k = i * popNum - i * (i+1)/2 + j - i - 1;
        else
            k = j * popNum - j * (j+1)/2 + i - j - 1;
        return k;
    }

    /**
     * Returns the IDs of a population pair given a population pair index.
     *
     * @param k a population pair index
     * @return the IDs of the population pair
     */
    public String[] getPopPair(int k) {
        int i = 2 * (k + 1) / popNum - 1;
        if (i < 0) i = 0;
        int j = k - i * popNum + i * (i+1)/2 + i + 1;
        return new String[]{getPopId(i), getPopId(j)};
    }

    /**
     * Returns how many populations in the sample.
     *
     * @return how many populations in the sample
     */
    public int getPopNum() { return popNum; }

    /**
     * Returns how many individuals in the sample.
     *
     * @return how many individuals in the sample
     */
    int getIndNum() { return indNum; }

    /**
     * Checks whether a population ID exists in the sample.
     *
     * @param popId a population ID
     * @return true, the population ID exists; false, the population ID does not exist
     */
    public boolean containsPopId(String popId) {
        return popIndex.containsKey(popId);
    }

    @Override
    public void parseLine(String line) {
        String[] elements = line.trim().split("\\s+");
        // elements[0]: individual id
        // elements[1]: pop id
        if (ind2pop.containsKey(elements[0])) {
            throw new IllegalArgumentException("Find duplicated individual id: " + elements[0]);
        }
        else {
            popSet.add(elements[1]);
            ind2pop.put(elements[0], elements[1]);
        }
    }

}
