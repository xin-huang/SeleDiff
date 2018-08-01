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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IndInfoTest {

    private final IndInfo indInfo = new IndInfo("examples/data/example.ind");
    private final IndInfo gzIndInfo = new IndInfo("examples/compressed_data/example.ind.gz");
    private final PopInfo popInfo = indInfo.getPopInfo();
    private final PopInfo gzPopInfo = gzIndInfo.getPopInfo();

    @Test
    public void testGetPopIndex() {
        assertEquals(1, indInfo.getPopIndex(0));
        assertEquals(1, gzIndInfo.getPopIndex(0));
    }

    @Test
    public void testGetPopPairIndex() {
        assertEquals(0, popInfo.getPopPairIndex("YRI", "CEU"));
        assertEquals(1, popInfo.getPopPairIndex("YRI", "CHS"));
        assertEquals(2, popInfo.getPopPairIndex("CEU", "CHS"));

        assertEquals(0, gzPopInfo.getPopPairIndex("YRI", "CEU"));
        assertEquals(1, gzPopInfo.getPopPairIndex("YRI", "CHS"));
        assertEquals(2, gzPopInfo.getPopPairIndex("CEU", "CHS"));
    }

    @Test
    public void testGetPopPair() {
        String[] popPair0 = popInfo.getPopPair(0);
        assertEquals("YRI", popPair0[0]);
        assertEquals("CEU", popPair0[1]);

        String[] popPair1 = gzPopInfo.getPopPair(1);
        assertEquals("YRI", popPair1[0]);
        assertEquals("CHS", popPair1[1]);
    }

    @Test
    public void testGetPopNum() {
        assertEquals(3, popInfo.getPopNum());
        assertEquals(3, gzPopInfo.getPopNum());

    }

    @Test
    public void testGetIndNum() {
        assertEquals(505, indInfo.getIndNum());
        assertEquals(505, gzIndInfo.getIndNum());
    }

    @Test
    public void testContainsPopId() {
        assertTrue(popInfo.containsPopId("YRI"));
        assertTrue(popInfo.containsPopId("CEU"));
        assertTrue(popInfo.containsPopId("CHS"));

        assertTrue(gzPopInfo.containsPopId("YRI"));
        assertTrue(gzPopInfo.containsPopId("CEU"));
        assertTrue(gzPopInfo.containsPopId("CHS"));
    }

}
