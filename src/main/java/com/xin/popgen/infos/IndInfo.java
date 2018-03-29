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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Class {@code IndInfo} stores the sample information, including:
 * population IDs, individual IDs, population numbers and individual numbers.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public final class IndInfo implements Info {

	// a String array stores population IDs for each individuals
	// The order of individual IDs in the sample file should be 
	// CONSISTENT with those in the header of the VCF file
    
    private int[] indId2popIndex;
    
    // a LinkedQueue stores population IDs for each individuals 
    private final ArrayList<String> ind2popQueue;

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
    
    // a Pattern for splitting lines
    private final Pattern pattern = Pattern.compile("\\s+");

    /**
     * Constructor of {@code IndInfo}.
     *
     * @param indFileName the name of a EIGENSTRAT IND file
     */
    public IndInfo(String indFileName) {
        ind2popQueue = new ArrayList<>();
        popIndex = new HashMap<>(); // key: pop id; value: pop index
        popSet = new HashSet<>();
        readFile(getBufferedReader(indFileName));
        
        int i = 0;
        indNum = ind2popQueue.size();
        popNum = popSet.size();
        popIds = new String[popNum];
        for (String popId:popSet) {
            popIndex.put(popId,i);
            popIds[i++] = popId;
        }
        
        indId2popIndex = new int[indNum];
        for (int j = 0; j < indNum; j++) {
        	indId2popIndex[j] = popIndex.get(ind2popQueue.get(j));
        }

        if (popNum == 1) throw new IllegalArgumentException("Find Only 1 population in " + indFileName
                + ", please check your input file.");

        System.out.println(indNum + " individuals with " + popNum
                + " populations are read from " + indFileName);
    }


    /**
     * Returns the population index of an individual.
     *
     * @param i the index of an individual
     * @return the population index of the given individual
     */
    public int getPopIndex(int i) {
    	return indId2popIndex[i];
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
    public int getIndNum() { return indNum; }

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

    @Override
    public void parseLine(String line) {
        String[] elements = pattern.split(line.trim());
        ind2popQueue.add(elements[2]);
        popSet.add(elements[2]);
    }

}
