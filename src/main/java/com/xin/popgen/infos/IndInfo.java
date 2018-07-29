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

    // an integer stores how many individuals in the sample
    private final int indNum;

    // a HashSet stores population IDs
    private final HashSet<String> popSet;
    
    // a Pattern for splitting lines
    private final Pattern pattern = Pattern.compile("\\s+");

    private final PopInfo popInfo;

    /**
     * Constructor of {@code IndInfo}.
     *
     * @param indFileName the name of a EIGENSTRAT IND file
     */
    public IndInfo(String indFileName) {
        ind2popQueue = new ArrayList<>();
        popSet = new HashSet<>();
        HashMap<String, Integer> popIndex = new HashMap<>(); // key: pop id; value: pop index
        readFile(getBufferedReader(indFileName));
        
        int i = 0;
        indNum = ind2popQueue.size();
        int popNum = popSet.size();
        if (popNum == 1) throw new IllegalArgumentException("Find Only 1 population in " + indFileName
                + ", please check your input file.");

        String[] popIds = new String[popNum];
        for (String popId:popSet) {
            popIndex.put(popId,i);
            popIds[i++] = popId;
        }

        this.popInfo = new PopInfo(popIndex, popIds);
        
        indId2popIndex = new int[indNum];
        for (int j = 0; j < indNum; j++) {
        	indId2popIndex[j] = popIndex.get(ind2popQueue.get(j));
        }

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
     * Returns how many individuals in the sample.
     *
     * @return how many individuals in the sample
     */
    public int getIndNum() { return indNum; }

    public PopInfo getPopInfo() { return popInfo; }

    @Override
    public void parseLine(String line) {
        String[] elements = pattern.split(line.trim());
        ind2popQueue.add(elements[2]);
        popSet.add(elements[2]);
    }

}
