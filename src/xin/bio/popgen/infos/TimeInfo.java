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

import java.io.BufferedReader;

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
    private final IndInfo sampleInfo;

    /**
     * Constructor of class {@code TimeInfo}.
     * A divergence time file is space delimited without header,
     * where the first column is the population ID of the first population,
     * the second column is the population ID of the second population,
     * the third column is the divergence time of the population pair
     *
     * @param timeFileName the file name of a divergence time file
     * @param sampleInfo a SampleInfo instance
     */
    public TimeInfo(String timeFileName, BufferedReader br, IndInfo sampleInfo) {
        int popPairNum = (sampleInfo.getPopNum()*(sampleInfo.getPopNum()-1))/2;
        times = new Integer[popPairNum];
        this.sampleInfo = sampleInfo;
        readFile(br);
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
    public double getTime(String popi, String popj) {
        return times[sampleInfo.getPopPairIndex(popi,popj)];
    }

    /**
     * Returns the divergence time of a population pair with a given index.
     *
     * @param k the index of the population pair
     * @return the divergence time of the population pair
     */
    public double getTime(int k) {
        String[] popPair = sampleInfo.getPopPair(k);
        return getTime(popPair[0],popPair[1]);
    }

    @Override
    public void parseLine(String line) {
        String[] elements = line.trim().split("\\s+");
        // elements[0]: the first population
        // elements[1]: the second population
        // elements[2]: divergence time between the first and second population
        if (sampleInfo.containsPopId(elements[0]) && sampleInfo.containsPopId(elements[1])) {
            times[sampleInfo.getPopPairIndex(elements[0],elements[1])] = Integer.parseInt(elements[2]);
        }
        else {
            if (!sampleInfo.containsPopId(elements[0]))
                throw new IllegalArgumentException("Cannot find population: " + elements[0]);
            if (!sampleInfo.containsPopId(elements[1]))
                throw new IllegalArgumentException("Cannot find population: " + elements[1]);
        }
    }

    /**
     * Helper function for checking whether divergence times of
     * all the population pairs exist.
     */
    private void checkPopPairs() {
        for (int k = 0; k < times.length; k++) {
            if (times[k] == null) {
                String[] popPair = sampleInfo.getPopPair(k);
                throw new IllegalArgumentException("Cannot find the divergence time of the population pair {"
                        + popPair[0] + "," + popPair[1] + "}");
            }
        }
    }

}
