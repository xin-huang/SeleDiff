package xin.bio.popgen.infos;

import static org.junit.Assert.*;

import org.junit.Test;

public class SnpInfoTest {
	
	private final SnpInfo snpInfo = new SnpInfo("test/data/test.snp", 100);
	private final SnpInfo gzSnpInfo = new SnpInfo("test/data/test.snp.gz", 100);

	@Test
	public void testGetSnp() {
		assertEquals(String.format("%s\t%s\t%s\t", "rs0", "G", "C"), snpInfo.getSnp(0));
		assertEquals(String.format("%s\t%s\t%s\t", "rs1", "A", "T"), gzSnpInfo.getSnp(1));
	}

}
