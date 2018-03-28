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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TimeInfoTest {

    private final IndInfo indInfo = new IndInfo("examples/data/example.ind");
    private final IndInfo gzIndInfo = new IndInfo("examples/compressed_data/example.ind.gz");
	
	private final TimeInfo timeInfo = new TimeInfo("examples/data/example.time", indInfo);
	private final TimeInfo gzTimeInfo = new TimeInfo("examples/compressed_data/example.time.gz", gzIndInfo);

	@Test
	public void testGetTime() {
	    // test uncompressed data
		assertEquals(5000, timeInfo.getTime(indInfo.getPopPairIndex("YRI", "CEU")));
		assertEquals(5000, timeInfo.getTime(indInfo.getPopPairIndex("YRI", "CHS")));
		assertEquals(3000, timeInfo.getTime(indInfo.getPopPairIndex("CEU", "CHS")));
        assertEquals(5000, timeInfo.getTime(indInfo.getPopPairIndex("CEU", "YRI")));
        assertEquals(5000, timeInfo.getTime(indInfo.getPopPairIndex("CHS", "YRI")));
        assertEquals(3000, timeInfo.getTime(indInfo.getPopPairIndex("CHS", "CEU")));

        // test compressed data
        assertEquals(5000, gzTimeInfo.getTime(gzIndInfo.getPopPairIndex("YRI", "CEU")));
        assertEquals(5000, gzTimeInfo.getTime(gzIndInfo.getPopPairIndex("YRI", "CHS")));
        assertEquals(3000, gzTimeInfo.getTime(gzIndInfo.getPopPairIndex("CEU", "CHS")));
        assertEquals(5000, gzTimeInfo.getTime(gzIndInfo.getPopPairIndex("CEU", "YRI")));
        assertEquals(5000, gzTimeInfo.getTime(gzIndInfo.getPopPairIndex("CHS", "YRI")));
        assertEquals(3000, gzTimeInfo.getTime(gzIndInfo.getPopPairIndex("CHS", "CEU")));
	}

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testParseLine1() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Cannot find population: WAF");
        timeInfo.parseLine("WAF\tEUR\t3.000000");
    }

    @Test
    public void testParseLine2() {
        thrown.expectMessage("Cannot find population: EUR");
        timeInfo.parseLine("YRI\tEUR\t3.000000");
    }

    @Test
    public void testCheckPopPairs() {
        thrown.expectMessage("Cannot find the divergence time of the population pair {YRI,CEU}");
        TimeInfo t = new TimeInfo("examples/data/example.test.time", indInfo);
    }

}
