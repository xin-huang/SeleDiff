package com.xin.popgen.infos;

import static org.junit.Assert.*;

import org.junit.Test;

import com.xin.popgen.infos.IndInfo;

public class IndInfoTest {
	
	private final IndInfo sampleInfo = new IndInfo("src/test/resources/test.ind");
	private final IndInfo gzSampleInfo = new IndInfo("src/test/resources/test.ind.gz");

	@Test
	public void testGetPopNum() {
		assertEquals(10, sampleInfo.getPopNum());
		assertEquals(10, gzSampleInfo.getPopNum());
	}
	
	@Test
	public void testGetIndNum() {
		assertEquals(1000, sampleInfo.getIndNum());
		assertEquals(1000, gzSampleInfo.getIndNum());
	}
	
	@Test
	public void testGetPopId() {
		assertEquals("pop1", sampleInfo.getPopId(sampleInfo.getPopIndex("pop1")));
		assertEquals("pop2", gzSampleInfo.getPopId(gzSampleInfo.getPopIndex("pop2")));
	}
	
	@Test
	public void testContainsPopId() {
		assertTrue(sampleInfo.containsPopId("pop3"));
		assertTrue(gzSampleInfo.containsPopId("pop0"));
		assertFalse(sampleInfo.containsPopId("jiji"));
		assertFalse(gzSampleInfo.containsPopId("gigi"));
	}

}
