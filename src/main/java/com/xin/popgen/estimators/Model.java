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
package com.xin.popgen.estimators;

/**
 * Class {@code Model} defines several methods for estimations in SeleDiff.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public final class Model {
	
    /**
     * Calculates the logarithm of Odds ratio.
     *
     * @param countAw counts of ancestral allele in population A
     * @param countAm counts of derived allele in population A
     * @param countBw counts of ancestral allele in population B
     * @param countBm counts of derived allele in population B
     * @return the logarithm of Odds ratio
     */
    static double calLogOdds(int countAw, int countAm, int countBw, int countBm) {
        return round(Math.log((correctContinuous(countAm) * correctContinuous(countBw))
                / (correctContinuous(countAw) * correctContinuous(countBm))));
    }

    /**
     * Calculates the variance of the logarithm of Odds ratio.
     *
     * @param countAw counts of ancestral allele in population A
     * @param countAm counts of derived allele in population A
     * @param countBw counts of ancestral allele in population B
     * @param countBm counts of derived allele in population B
     * @return the variance of the logarithm of Odds ratio
     */
    static double calVarLogOdds(int countAw, int countAm, int countBw, int countBm) {
        return round(1/correctContinuous(countAw) + 1/correctContinuous(countAm)
                + 1/correctContinuous(countBw) + 1/correctContinuous(countBm));
    }

    /**
     * Calculates the drift variance between two populations.
     *
     * @param countAw counts of ancestral allele in population A
     * @param countAm counts of derived allele in population A
     * @param countBw counts of ancestral allele in population B
     * @param countBm counts of derived allele in population B
     * @return the drift variance between two populations
     */
    static double calVarOmega(int countAw, int countAm, int countBw, int countBm) {
        double logOdds = calLogOdds(countAw, countAm, countBw, countBm);
        double varLogOdds = calVarLogOdds(countAw, countAm, countBw, countBm);
        return round(logOdds * logOdds / 0.455 - varLogOdds);
    }

    /**
     * Helper function for continuously correction when count < 5.
     *
     * @param count a count number for correction
     * @return the corrected count
     */
    private static double correctContinuous(int count) {
        return (count < 5 ? count + 0.5 : count);
    }

    /**
     * Rounds a double value to 6 decimal points.
     *
     * @param value a value to be rounded
     * @return the rounded value
     */
    private static double round(double value) {
        return (new Double(value).equals(Double.NaN)) ? value
                : (Math.round(value * 1000000d) / 1000000d);
    }
    
}
