/*
    Copyright 2018 Xin Huang

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.xin.popgen.infos;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TimeInfoTest {

    private final IndInfo indInfo = new IndInfo("examples/data/example.ind");
    private final IndInfo gzIndInfo = new IndInfo("examples/compressed_data/example.ind.gz");
    private final PopInfo popInfo = indInfo.getPopInfo();
    private final PopInfo gzPopInfo = gzIndInfo.getPopInfo();
	
	private final TimeInfo timeInfo = new TimeInfo("examples/data/example.time", popInfo);
	private final TimeInfo gzTimeInfo = new TimeInfo("examples/compressed_data/example.time.gz", gzPopInfo);

	@Test
	public void testGetTime() {
	    // test uncompressed data
		assertEquals(5000, timeInfo.getTime(popInfo.getPopPairIndex("YRI", "CEU")));
		assertEquals(5000, timeInfo.getTime(popInfo.getPopPairIndex("YRI", "CHS")));
		assertEquals(3000, timeInfo.getTime(popInfo.getPopPairIndex("CEU", "CHS")));
        assertEquals(5000, timeInfo.getTime(popInfo.getPopPairIndex("CEU", "YRI")));
        assertEquals(5000, timeInfo.getTime(popInfo.getPopPairIndex("CHS", "YRI")));
        assertEquals(3000, timeInfo.getTime(popInfo.getPopPairIndex("CHS", "CEU")));

        // test compressed data
        assertEquals(5000, gzTimeInfo.getTime(gzPopInfo.getPopPairIndex("YRI", "CEU")));
        assertEquals(5000, gzTimeInfo.getTime(gzPopInfo.getPopPairIndex("YRI", "CHS")));
        assertEquals(3000, gzTimeInfo.getTime(gzPopInfo.getPopPairIndex("CEU", "CHS")));
        assertEquals(5000, gzTimeInfo.getTime(gzPopInfo.getPopPairIndex("CEU", "YRI")));
        assertEquals(5000, gzTimeInfo.getTime(gzPopInfo.getPopPairIndex("CHS", "YRI")));
        assertEquals(3000, gzTimeInfo.getTime(gzPopInfo.getPopPairIndex("CHS", "CEU")));
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
        TimeInfo t = new TimeInfo("examples/data/example.test.time", popInfo);
    }

}
