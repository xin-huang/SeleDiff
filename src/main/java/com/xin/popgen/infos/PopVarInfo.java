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

/**
 * Class {@code PopVarInfo} stores variances of drift between populations.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public final class PopVarInfo implements Info {

    // a Double array stores variances of drift between populations
    private final Double[] popVars;

    // a SampleInfo instance stores sample information
    private final IndInfo indInfo;

    /**
     * Constructor of class {@code PopVarInfo}.
     *
     * @param popVarFileName the file name of a file containing variances of drift between populations
     * @param indInfo a IndInfo instance containing sample information
     */
    public PopVarInfo(String popVarFileName, IndInfo indInfo) {
        int popPairNum = indInfo.getPopNum() * (indInfo.getPopNum() - 1)/2;
        this.indInfo = indInfo;
        popVars = new Double[popPairNum];
        readFile(getBufferedReader(popVarFileName));
        checkPopPairs();

        System.out.println(popPairNum + " population pairs with variances of Omega are read from "
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
        return popVars[indInfo.getPopPairIndex(popi,popj)];
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

    /**
     * Helper function for checking whether variances of drift of
     * all the population pairs exist.
     */
    private void checkPopPairs() {
        for (int k = 0; k < popVars.length; k++) {
            if (popVars[k] == null) {
                String[] popPair = indInfo.getPopPair(k);
                throw new IllegalArgumentException("Cannot find the variance of drift of the population pair {"
                        + popPair[0] + "," + popPair[1] + "}");
            }
        }
    }

    @Override
    public void parseLine(String line) {
        String[] elements = line.trim().split("\\s+");
        if (indInfo.containsPopId(elements[0]) && indInfo.containsPopId(elements[1])) {
            int popPairIndex = indInfo.getPopPairIndex(elements[0], elements[1]);
            popVars[popPairIndex] = Double.parseDouble(elements[2]);
        }
        else {
            if (!indInfo.containsPopId(elements[0]))
                throw new IllegalArgumentException("Cannot find population: " + elements[0]);
            if (!indInfo.containsPopId(elements[1]))
                throw new IllegalArgumentException("Cannot find population: " + elements[1]);
        }
    }

}
