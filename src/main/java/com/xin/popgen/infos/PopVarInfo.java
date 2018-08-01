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
 * Class {@code PopVarInfo} stores variances of Omega between populations.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public final class PopVarInfo implements Info {

    // a Double array stores variances of Omega between populations
    private final Double[] popVars;

    // a PopInfo instance stores population information
    private final PopInfo popInfo;

    /**
     * Constructor of class {@code PopVarInfo}.
     *
     * @param popVarFileName the file name of a file containing variances of Omega between populations
     * @param popInfo a PopInfo instance containing population information
     */
    public PopVarInfo(String popVarFileName, PopInfo popInfo) {
        this.popInfo = popInfo;
        int popPairNum = popInfo.getPopNum() * (popInfo.getPopNum() - 1)/2;
        popVars = new Double[popPairNum];
        readFile(getBufferedReader(popVarFileName));
        checkPopPairs();

        System.out.println(popPairNum + " population pairs with variances of Omega are read from "
        		+ popVarFileName);
    }

    /**
     * Returns the variance of Omega with a given population pair index.
     *
     * @param i a population pair index
     * @return the variance of Omega
     */
    public double getPopVar(int i) {
        return popVars[i];
    }

    /**
     * Helper function for checking whether variances of Omega of
     * all the population pairs exist.
     */
    private void checkPopPairs() {
        for (int k = 0; k < popVars.length; k++) {
            if (popVars[k] == null) {
                String[] popPair = popInfo.getPopPair(k);
                throw new IllegalArgumentException("Cannot find the variance of Omega of the population pair {"
                        + popPair[0] + "," + popPair[1] + "}");
            }
        }
    }

    @Override
    public void parseLine(String line) {
        String[] elements = line.trim().split("\\s+");
        if (popInfo.containsPopId(elements[0]) && popInfo.containsPopId(elements[1])) {
            int popPairIndex = popInfo.getPopPairIndex(elements[0], elements[1]);
            popVars[popPairIndex] = Double.parseDouble(elements[2]);
        }
        else {
            if (!popInfo.containsPopId(elements[0]))
                throw new IllegalArgumentException("Cannot find population: " + elements[0]);
            if (!popInfo.containsPopId(elements[1]))
                throw new IllegalArgumentException("Cannot find population: " + elements[1]);
        }
    }

}
