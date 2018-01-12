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

import java.util.HashMap;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

class ChiSquareTable {
	
	private final HashMap<String, String> pvalues;
	private final ChiSquaredDistribution chisq = new ChiSquaredDistribution(1);
	
	ChiSquareTable() {
		this.pvalues = new HashMap<>();
		double i = 0;
		while (i < 30) {
			pvalues.put(String.format("%.3f", i), 
					String.format("%.6f", 1-chisq.cumulativeProbability(i)));
			i += 0.001;
		}
	}
	
	String getPvalue(String num) {
		if (Double.parseDouble(num) >= 30) return "0.000000";
		else 
			return pvalues.get(num);
	}

}
