package xin.bio.popgen.infos;

import static org.junit.Assert.*;

import org.junit.Test;

public class CountSnpNumInfoTest {
	
	private final CountSnpNumInfo info = new CountSnpNumInfo("test/data/test.snp");
	private final CountSnpNumInfo gzInfo = new CountSnpNumInfo("test/data/test.snp.gz");

	@Test
	public void testGetSnpNum() {
		assertEquals(100, info.getSnpNum());
		assertEquals(100, gzInfo.getSnpNum());
	}

}
