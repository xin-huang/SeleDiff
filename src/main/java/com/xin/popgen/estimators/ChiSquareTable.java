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
