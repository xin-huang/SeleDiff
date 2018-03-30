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

import java.util.HashMap;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

/**
 * Class {@code ChiSquareTable} computes p-values corresponding
 * to a chi-squared statistic in order to speed up SeleDiff.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public class ChiSquareTable {

	// a HashMap stores chi-squared statistics and corresponding p-values
	// key: a chi-squared statistic
	// value: a p-value
	private final HashMap<String, String> pvalues;

	/**
	 * Constructor of {@code ChiSquareTable}.
	 */
	ChiSquareTable() {
		ChiSquaredDistribution chisq = new ChiSquaredDistribution(1);
		this.pvalues = new HashMap<>();
		double i = 0;
		while (i < 30) {
			pvalues.put(String.format("%.3f", i), 
					String.format("%.6f", 1-chisq.cumulativeProbability(i)));
			i += 0.001;
		}
	}

	/**
	 * Returns a p-value corresponding to a chi-squared statistic.
	 *
	 * @param num a chi-squared statistic
	 * @return the p-value corresponding to the chi-squared statistic
	 */
	String getPvalue(String num) {
		if (Double.parseDouble(num) >= 30) return "0.000000";
		else 
			return pvalues.get(num);
	}

}
