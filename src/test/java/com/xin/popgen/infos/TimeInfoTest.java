package com.xin.popgen.infos;

import static org.junit.Assert.*;

import org.junit.Test;

import com.xin.popgen.infos.IndInfo;
import com.xin.popgen.infos.TimeInfo;

public class TimeInfoTest {
	
	private final TimeInfo timeInfo = new TimeInfo("src/test/resources/test.time", 
			new IndInfo("src/test/resources/test.ind"));
	private final TimeInfo gzTimeInfo = new TimeInfo("src/test/resources/test.time.gz",
			new IndInfo("src/test/resources/test.ind.gz"));

	@Test
	public void testGetTime() {
		assertEquals(1000, timeInfo.getTime("pop1","pop2"));
		assertEquals(1000, gzTimeInfo.getTime("pop0", "pop9"));
	}

}
