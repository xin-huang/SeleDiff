package xin.bio.popgen.infos;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeInfoTest {
	
	private final TimeInfo timeInfo = new TimeInfo("test/data/test.time", 
			new IndInfo("test/data/test.ind"));
	private final TimeInfo gzTimeInfo = new TimeInfo("test/data/test.time.gz",
			new IndInfo("test/data/test.ind.gz"));

	@Test
	public void testGetTime() {
		assertEquals(1000, timeInfo.getTime("pop1","pop2"));
		assertEquals(1000, gzTimeInfo.getTime("pop0", "pop9"));
	}

}
