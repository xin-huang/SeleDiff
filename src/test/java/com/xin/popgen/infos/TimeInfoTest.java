package com.xin.popgen.infos;

import static org.junit.Assert.*;

import org.junit.Test;

import com.xin.popgen.infos.IndInfo;
import com.xin.popgen.infos.TimeInfo;

public class TimeInfoTest {
	
	private final TimeInfo timeInfo = new TimeInfo("../../examples/data/example.time",
			new IndInfo("../../examples/data/example.ind"));
	private final TimeInfo gzTimeInfo = new TimeInfo("../../examples/compressed_data/example.time.gz",
			new IndInfo("../../examples/compressed_data/example.ind.gz"));

	@Test
	public void testGetTime() {
	    // test uncompressed data
		assertEquals(5000, timeInfo.getTime("YRI","CEU"));
		assertEquals(5000, timeInfo.getTime("YRI","CHS"));
		assertEquals(3000, timeInfo.getTime("CEU","CHS"));
        assertEquals(5000, timeInfo.getTime("CEU","YRI"));
        assertEquals(5000, timeInfo.getTime("CHS","YRI"));
        assertEquals(3000, timeInfo.getTime("CHS","CEU"));

        // test compressed data
        assertEquals(5000, gzTimeInfo.getTime("YRI","CEU"));
        assertEquals(5000, gzTimeInfo.getTime("YRI","CHS"));
        assertEquals(3000, gzTimeInfo.getTime("CEU","CHS"));
        assertEquals(5000, gzTimeInfo.getTime("CEU","YRI"));
        assertEquals(5000, gzTimeInfo.getTime("CHS","YRI"));
        assertEquals(3000, gzTimeInfo.getTime("CHS","CEU"));
	}

}
