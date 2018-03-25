package com.xin.popgen.infos;

import static org.junit.Assert.*;

import org.junit.Test;

import com.xin.popgen.infos.IndInfo;
import com.xin.popgen.infos.PopVarInfo;

public class PopVarInfoTest {
	
	private final PopVarInfo popVarInfo = new PopVarInfo("../../examples/results/example.var",
			new IndInfo("../../examples/data/example.ind"));
	private final PopVarInfo gzPopVarInfo = new PopVarInfo("../../examples/compressed_data/example.var.gz",
			new IndInfo("../../examples/compressed_data/example.ind.gz"));

	@Test
	public void test() {
	    // test uncompressed data
		assertEquals(1.542796, popVarInfo.getPopVar("YRI", "CEU"), 0.000001);
		assertEquals(1.633976, popVarInfo.getPopVar("YRI", "CHS"), 0.000001);
        assertEquals(0.988984, popVarInfo.getPopVar("CEU", "CHS"), 0.000001);
        assertEquals(1.542796, popVarInfo.getPopVar("CEU", "YRI"), 0.000001);
        assertEquals(1.633976, popVarInfo.getPopVar("CHS", "YRI"), 0.000001);
        assertEquals(0.988984, popVarInfo.getPopVar("CHS", "CEU"), 0.000001);

        // test compressed data
        assertEquals(1.542796, gzPopVarInfo.getPopVar("YRI", "CEU"), 0.000001);
        assertEquals(1.633976, gzPopVarInfo.getPopVar("YRI", "CHS"), 0.000001);
        assertEquals(0.988984, gzPopVarInfo.getPopVar("CEU", "CHS"), 0.000001);
        assertEquals(1.542796, gzPopVarInfo.getPopVar("CEU", "YRI"), 0.000001);
        assertEquals(1.633976, gzPopVarInfo.getPopVar("CHS", "YRI"), 0.000001);
        assertEquals(0.988984, gzPopVarInfo.getPopVar("CHS", "CEU"), 0.000001);
	}

}
