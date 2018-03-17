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

import java.util.HashMap;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

/**
 * Class {@code ChiSquareTable} computes p-values corresponding
 * to a chi-squared statistic in order to speed up SeleDiff.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
class ChiSquareTable {

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
