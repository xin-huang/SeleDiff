package com.xin.popgen.infos;

import static org.junit.Assert.*;

import org.junit.Test;

import com.xin.popgen.infos.SnpInfo;

public class SnpInfoTest {
	
	private final SnpInfo snpInfo = new SnpInfo("src/test/resources/test.snp", 100);
	private final SnpInfo gzSnpInfo = new SnpInfo("src/test/resources/test.snp.gz", 100);

	@Test
	public void testGetSnp() {
		assertEquals(String.format("%s\t%s\t%s\t", "rs0", "G", "C"), snpInfo.getSnp(0));
		assertEquals(String.format("%s\t%s\t%s\t", "rs1", "A", "T"), gzSnpInfo.getSnp(1));
	}

}
