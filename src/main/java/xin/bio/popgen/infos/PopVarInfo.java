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

/**
 * Class {@code PopVarInfo} stores variances of drift between populations.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class PopVarInfo implements Info {

    // a Double array stores variances of drift between populations
    private final Double[] popVars;

    // a SampleInfo instance stores sample information
    private final IndInfo sampleInfo;

    /**
     * Constructor of class {@code PopVarInfo}.
     *
     * @param popVarFileName the file name of a file containing variances of drift between populations
     * @param sampleInfo a SampleInfo instance containing sample information
     */
    public PopVarInfo(String popVarFileName, IndInfo sampleInfo) {
        int popPairNum = sampleInfo.getPopNum() * (sampleInfo.getPopNum() - 1)/2;
        this.sampleInfo = sampleInfo;
        popVars = new Double[popPairNum];
        readFile(getBufferedReader(popVarFileName));
        checkPopPairs();

        System.out.println(popPairNum + " population pairs with variances of drift are read from " 
        		+ popVarFileName);
    }
    
    /**
     * Returns the variance of drift with a given population pair.
     *
     * @param popi the population ID of the first population
     * @param popj the population ID of the second population
     * @return the variance of drift between the population pair {popi,popj}
     */
    public double getPopVar(String popi, String popj) {
        return popVars[sampleInfo.getPopPairIndex(popi,popj)];
    }

    /**
     * Returns the variance of drift with a given population pair index.
     *
     * @param i a population pair index
     * @return the variance of drift
     */
    public double getPopVar(int i) {
        return popVars[i];
    }

    @Override
    public void parseLine(String line) {
        String[] elements = line.trim().split("\\s+");
        if (sampleInfo.containsPopId(elements[0]) && sampleInfo.containsPopId(elements[1])) {
            int popPairIndex = sampleInfo.getPopPairIndex(elements[0], elements[1]);
            popVars[popPairIndex] = Double.parseDouble(elements[2]);
        }
        else {
            if (!sampleInfo.containsPopId(elements[0]))
                throw new IllegalArgumentException("Cannot find population: " + elements[0]);
            if (!sampleInfo.containsPopId(elements[1]))
                throw new IllegalArgumentException("Cannot find population: " + elements[1]);
        }
    }

    /**
     * Helper function for checking whether variances of drift of
     * all the population pairs exist.
     */
    private void checkPopPairs() {
        for (int k = 0; k < popVars.length; k++) {
            if (popVars[k] == null) {
                String[] popPair = sampleInfo.getPopPair(k);
                throw new IllegalArgumentException("Cannot find the variance of drift of the population pair {"
                        + popPair[0] + "," + popPair[1] + "}");
            }
        }
    }

}
