/*
	Copyright (c) 2018 Xin Huang

	This file is part of SeleDiff.

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.
 */
package com.xin.popgen.infos;

import static org.junit.Assert.*;

import org.junit.Test;

public class PopVarInfoTest {

    private final IndInfo indInfo = new IndInfo("examples/data/example.ind");
    private final IndInfo gzIndInfo = new IndInfo("examples/compressed_data/example.ind.gz");
	
	private final PopVarInfo popVarInfo = new PopVarInfo("examples/results/example.var", indInfo);
	private final PopVarInfo gzPopVarInfo = new PopVarInfo("examples/compressed_data/example.var.gz", gzIndInfo);

	@Test
    public void testGetPopVar() {
	    // test uncompressed data
        assertEquals(1.542796, popVarInfo.getPopVar(indInfo.getPopPairIndex("YRI", "CEU")), 0.000001);
        assertEquals(1.633976, popVarInfo.getPopVar(indInfo.getPopPairIndex("YRI", "CHS")), 0.000001);
        assertEquals(0.988984, popVarInfo.getPopVar(indInfo.getPopPairIndex("CEU", "CHS")), 0.000001);
        assertEquals(1.542796, popVarInfo.getPopVar(indInfo.getPopPairIndex("CEU", "YRI")), 0.000001);
        assertEquals(1.633976, popVarInfo.getPopVar(indInfo.getPopPairIndex("CHS", "YRI")), 0.000001);
        assertEquals(0.988984, popVarInfo.getPopVar(indInfo.getPopPairIndex("CHS", "CEU")), 0.000001);

        // test compressed data
        assertEquals(1.542796, gzPopVarInfo.getPopVar(gzIndInfo.getPopPairIndex("YRI", "CEU")), 0.000001);
        assertEquals(1.633976, gzPopVarInfo.getPopVar(gzIndInfo.getPopPairIndex("YRI", "CHS")), 0.000001);
        assertEquals(0.988984, gzPopVarInfo.getPopVar(gzIndInfo.getPopPairIndex("CEU", "CHS")), 0.000001);
        assertEquals(1.542796, gzPopVarInfo.getPopVar(gzIndInfo.getPopPairIndex("CEU", "YRI")), 0.000001);
        assertEquals(1.633976, gzPopVarInfo.getPopVar(gzIndInfo.getPopPairIndex("CHS", "YRI")), 0.000001);
        assertEquals(0.988984, gzPopVarInfo.getPopVar(gzIndInfo.getPopPairIndex("CHS", "CEU")), 0.000001);
    }

}
