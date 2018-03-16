/*
  Copyright (C) 2018 Xin Huang

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
package com.xin.popgen.infos;

/**
 * Class {@code TimeInfo} stores the information of divergence times
 * for population pairs in the sample.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class TimeInfo implements Info {

    // an Integer array stores the divergence time of the population pair {i, j} in a triangular array times[k]
    // with 0 <= i < j <= n-1, where
    // k = i * (n - (i+1)/2) + j - i - 1
    private final Integer[] times;

    // a SampleInfo instance stores the information of samples
    private final IndInfo indInfo;

    /**
     * Constructor of class {@code TimeInfo}.
     * A divergence time file is space delimited without header,
     * where the first column is the population ID of the first population,
     * the second column is the population ID of the second population,
     * the third column is the divergence time of the population pair
     *
     * @param timeFileName the file name of a divergence time file
     * @param indInfo an IndInfo instance
     */
    public TimeInfo(String timeFileName, IndInfo indInfo) {
        int popPairNum = (indInfo.getPopNum()*(indInfo.getPopNum()-1))/2;
        times = new Integer[popPairNum];
        this.indInfo = indInfo;
        readFile(getBufferedReader(timeFileName));
        checkPopPairs();

        System.out.println(popPairNum + " population pairs with divergence times are read from "
                + timeFileName);
    }

    /**
     * Returns the divergence time of a population pair.
     *
     * @param popi the population ID of the first population
     * @param popj the population ID of the second population
     * @return the divergence time of the population pair {popi,popj}
     */
    public int getTime(String popi, String popj) {
        return times[indInfo.getPopPairIndex(popi,popj)];
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
                String[] popPair = indInfo.getPopPair(k);
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
        if (indInfo.containsPopId(elements[0]) && indInfo.containsPopId(elements[1])) {
            times[indInfo.getPopPairIndex(elements[0],elements[1])] = Integer.parseInt(elements[2]);
        }
        else {
            if (!indInfo.containsPopId(elements[0]))
                throw new IllegalArgumentException("Cannot find population: " + elements[0]);
            if (!indInfo.containsPopId(elements[1]))
                throw new IllegalArgumentException("Cannot find population: " + elements[1]);
        }
    }

}
