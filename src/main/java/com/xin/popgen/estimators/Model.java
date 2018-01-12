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
package com.xin.popgen.estimators;

/**
 * Class {@code Model} defines several methods for estimations in SeleDiff.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
final class Model {
	
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
    static double calDriftVar(int countAw, int countAm, int countBw, int countBm) {
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
    static double correctContinuous(int count) {
        return (count < 5 ? count + 0.5 : count);
    }

    /**
     * Rounds a double value to 6 decimal points.
     *
     * @param value a value to be rounded
     * @return the rounded value
     */
    static double round(double value) {
        return (new Double(value).equals(Double.NaN)) ? value
                : (Math.round(value * 1000000d) / 1000000d);
    }
    
}
