package com.xin.popgen.infos;

import static org.junit.Assert.*;

import org.junit.Test;

import com.xin.popgen.infos.CountSnpNumInfo;

public class CountSnpNumInfoTest {
	
	private final CountSnpNumInfo info = new CountSnpNumInfo("src/test/resources/test.snp");
	private final CountSnpNumInfo gzInfo = new CountSnpNumInfo("src/test/resources/test.snp.gz");

	@Test
	public void testGetSnpNum() {
		assertEquals(100, info.getSnpNum());
		assertEquals(100, gzInfo.getSnpNum());
	}

}
