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

/**
 * Class {@code TimeInfo} stores the information of divergence times
 * for population pairs in the sample.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public final class TimeInfo implements Info {

    // an Integer array stores the divergence time of the population pair {i, j} in a triangular array times[k]
    // with 0 <= i < j <= n-1, where
    // k = i * (n - (i+1)/2) + j - i - 1
    private final Integer[] times;

    // a PopInfo instance stores population information
    private final PopInfo popInfo;

    /**
     * Constructor of class {@code TimeInfo}.
     * A divergence time file is space delimited without header,
     * where the first column is the population ID of the first population,
     * the second column is the population ID of the second population,
     * the third column is the divergence time of the population pair
     *
     * @param timeFileName the file name of a divergence time file
     * @param popInfo a PopInfo instance
     */
    public TimeInfo(String timeFileName, PopInfo popInfo) {
        this.popInfo = popInfo;
        int popPairNum = (popInfo.getPopNum()*(popInfo.getPopNum()-1))/2;
        times = new Integer[popPairNum];
        readFile(getBufferedReader(timeFileName));
        checkPopPairs();

        System.out.println(popPairNum + " population pairs with divergence times are read from "
                + timeFileName);
    }

    /**
     * Returns the divergence time of a population pair with a given index.
     *
     * @param k the index of the population pair
     * @return the divergence time of the population pair
     */
    public int getTime(int k) {
    	return times[k];
    }

    /**
     * Helper function for checking whether divergence times of
     * all the population pairs exist.
     */
    private void checkPopPairs() {
        for (int k = 0; k < times.length; k++) {
            if (times[k] == null) {
                String[] popPair = popInfo.getPopPair(k);
                throw new IllegalArgumentException("Cannot find the divergence time of the population pair {"
                        + popPair[0] + "," + popPair[1] + "}");
            }
        }
    }

    @Override
    public void parseLine(String line) {
        String[] elements = line.trim().split("\\s+");
        // elements[0]: the first population
        // elements[1]: the second population
        // elements[2]: divergence time between the first and second population
        if (popInfo.containsPopId(elements[0]) && popInfo.containsPopId(elements[1])) {
            times[popInfo.getPopPairIndex(elements[0],elements[1])] = Integer.parseInt(elements[2]);
        }
        else {
            if (!popInfo.containsPopId(elements[0]))
                throw new IllegalArgumentException("Cannot find population: " + elements[0]);
            if (!popInfo.containsPopId(elements[1]))
                throw new IllegalArgumentException("Cannot find population: " + elements[1]);
        }
    }

}
