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
