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
