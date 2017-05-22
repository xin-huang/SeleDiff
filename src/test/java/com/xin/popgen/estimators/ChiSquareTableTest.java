package com.xin.popgen.estimators;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.junit.Test;

import com.xin.popgen.estimators.ChiSquareTable;

public class ChiSquareTableTest {
	
	private final ChiSquareTable table = new ChiSquareTable();
	private final ChiSquaredDistribution chisq = new ChiSquaredDistribution(1);

	@Test
	public void testGetPvalue() {
		assertEquals("0.000000", table.getPvalue("100"));
		assertEquals(table.getPvalue("1.333"), String.format("%.6f", 1-chisq.cumulativeProbability(1.333)));
	}

}
