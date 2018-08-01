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

public class PopVarInfoTest {

    private final IndInfo indInfo = new IndInfo("examples/data/example.ind");
    private final IndInfo gzIndInfo = new IndInfo("examples/compressed_data/example.ind.gz");
    private final PopInfo popInfo = indInfo.getPopInfo();
    private final PopInfo gzPopInfo = gzIndInfo.getPopInfo();
	
	private final PopVarInfo popVarInfo = new PopVarInfo("examples/results/example.var", popInfo);
	private final PopVarInfo gzPopVarInfo = new PopVarInfo("examples/compressed_data/example.var.gz", gzPopInfo);

	@Test
    public void testGetPopVar() {
	    // test uncompressed data
        assertEquals(1.542796, popVarInfo.getPopVar(popInfo.getPopPairIndex("YRI", "CEU")), 0.000001);
        assertEquals(1.633976, popVarInfo.getPopVar(popInfo.getPopPairIndex("YRI", "CHS")), 0.000001);
        assertEquals(0.988984, popVarInfo.getPopVar(popInfo.getPopPairIndex("CEU", "CHS")), 0.000001);
        assertEquals(1.542796, popVarInfo.getPopVar(popInfo.getPopPairIndex("CEU", "YRI")), 0.000001);
        assertEquals(1.633976, popVarInfo.getPopVar(popInfo.getPopPairIndex("CHS", "YRI")), 0.000001);
        assertEquals(0.988984, popVarInfo.getPopVar(popInfo.getPopPairIndex("CHS", "CEU")), 0.000001);

        // test compressed data
        assertEquals(1.542796, gzPopVarInfo.getPopVar(gzPopInfo.getPopPairIndex("YRI", "CEU")), 0.000001);
        assertEquals(1.633976, gzPopVarInfo.getPopVar(gzPopInfo.getPopPairIndex("YRI", "CHS")), 0.000001);
        assertEquals(0.988984, gzPopVarInfo.getPopVar(gzPopInfo.getPopPairIndex("CEU", "CHS")), 0.000001);
        assertEquals(1.542796, gzPopVarInfo.getPopVar(gzPopInfo.getPopPairIndex("CEU", "YRI")), 0.000001);
        assertEquals(1.633976, gzPopVarInfo.getPopVar(gzPopInfo.getPopPairIndex("CHS", "YRI")), 0.000001);
        assertEquals(0.988984, gzPopVarInfo.getPopVar(gzPopInfo.getPopPairIndex("CHS", "CEU")), 0.000001);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testParseLine1() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Cannot find population: WAF");
	    popVarInfo.parseLine("WAF\tEUR\t3.000000");
    }

    @Test
    public void testParseLine2() {
        thrown.expectMessage("Cannot find population: EUR");
        popVarInfo.parseLine("YRI\tEUR\t3.000000");
    }

    @Test
    public void testCheckPopPairs() {
        thrown.expectMessage("Cannot find the variance of Omega of the population pair {CEU,CHS}");
        PopVarInfo p = new PopVarInfo("examples/data/example.test.var", popInfo);
    }

}
