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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IndInfoTest {

    private final IndInfo indInfo = new IndInfo("examples/data/example.ind");
    private final IndInfo gzIndInfo = new IndInfo("examples/compressed_data/example.ind.gz");

    @Test
    public void testGetPopIndex() {
        assertEquals(1, indInfo.getPopIndex(0));
        assertEquals(1, gzIndInfo.getPopIndex(0));
    }

    @Test
    public void testGetPopPairIndex() {
        assertEquals(0, indInfo.getPopPairIndex("YRI", "CEU"));
        assertEquals(1, indInfo.getPopPairIndex("YRI", "CHS"));
        assertEquals(2, indInfo.getPopPairIndex("CEU", "CHS"));

        assertEquals(0, gzIndInfo.getPopPairIndex("YRI", "CEU"));
        assertEquals(1, gzIndInfo.getPopPairIndex("YRI", "CHS"));
        assertEquals(2, gzIndInfo.getPopPairIndex("CEU", "CHS"));
    }

    @Test
    public void testGetPopPair() {
        String[] popPair0 = indInfo.getPopPair(0);
        assertEquals("YRI", popPair0[0]);
        assertEquals("CEU", popPair0[1]);

        String[] popPair1 = gzIndInfo.getPopPair(1);
        assertEquals("YRI", popPair1[0]);
        assertEquals("CHS", popPair1[1]);
    }

    @Test
    public void testGetPopNum() {
        assertEquals(3, indInfo.getPopNum());
        assertEquals(3, gzIndInfo.getPopNum());

    }

    @Test
    public void testGetIndNum() {
        assertEquals(505, indInfo.getIndNum());
        assertEquals(505, gzIndInfo.getIndNum());
    }

    @Test
    public void testContainsPopId() {
        assertTrue(indInfo.containsPopId("YRI"));
        assertTrue(indInfo.containsPopId("CEU"));
        assertTrue(indInfo.containsPopId("CHS"));

        assertTrue(gzIndInfo.containsPopId("YRI"));
        assertTrue(gzIndInfo.containsPopId("CEU"));
        assertTrue(gzIndInfo.containsPopId("CHS"));
    }

}
