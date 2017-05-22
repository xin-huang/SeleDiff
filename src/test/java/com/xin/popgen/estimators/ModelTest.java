package com.xin.popgen.estimators;

import static org.junit.Assert.*;

import org.junit.Test;

import com.xin.popgen.estimators.Model;

public class ModelTest {

	@Test
	public void testCalLogOdds() {
		assertEquals(0.405465, Model.calLogOdds(10, 20, 30, 40), 0.000001);
		assertEquals(0.259511, Model.calLogOdds(1, 2, 3, 4), 0.000001);
		assertEquals(0.223144, Model.calLogOdds(1, 2, 30, 40), 0.000001);
	}
	
	@Test
	public void testCalVarLogOdds() {
		assertEquals(0.208333, Model.calVarLogOdds(10, 20, 30, 40), 0.000001);
		assertEquals(1.574603, Model.calVarLogOdds(1, 2, 3, 4), 0.000001);
		assertEquals(1.125, Model.calVarLogOdds(1, 2, 30, 40), 0.000001);
	}
	
	@Test
	public void testCalDriftVar() {
		assertEquals(0.152990, Model.calDriftVar(10, 20, 30, 40), 0.000001);
		assertEquals(-1.426590, Model.calDriftVar(1, 2, 3, 4), 0.000001);
	}
	
	@Test
	public void testCorrectContinuous() {
		assertEquals(6, Model.correctContinuous(6), 0.000001);
		assertEquals(5, Model.correctContinuous(5), 0.000001);
		assertEquals(4.5, Model.correctContinuous(4), 0.000001);
		assertEquals(0.5, Model.correctContinuous(0), 0.000001);
	}
	
	@Test
	public void testRound() {
		assertEquals(Double.NaN, Model.round(Double.NaN), 0.000001);
		assertEquals(1.002, Model.round(1.002), 0.000001);
		assertEquals(12.345679, Model.round(12.3456789123456789), 0.000001);
	}

}
