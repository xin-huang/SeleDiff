package xin.bio.popgen.infos;

import static org.junit.Assert.*;

import org.junit.Test;

public class PopVarInfoTest {
	
	private final PopVarInfo popVarInfo = new PopVarInfo("test/data/test.popvar",
			new IndInfo("test/data/test.ind"));
	private final PopVarInfo gzPopVarInfo = new PopVarInfo("test/data/test.popvar.gz",
			new IndInfo("test/data/test.ind.gz"));

	@Test
	public void test() {
		assertEquals(0.012841, popVarInfo.getPopVar("pop5", "pop6"), 0.000001);
		assertEquals(0.012841, popVarInfo.getPopVar("pop6", "pop5"), 0.000001);
		assertEquals(0.010108, gzPopVarInfo.getPopVar("pop3", "pop4"), 0.000001);
		assertEquals(0.010108, gzPopVarInfo.getPopVar("pop4", "pop3"), 0.000001);
	}

}
