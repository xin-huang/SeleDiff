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

public class GenoInfoTest {

    private final IndInfo indInfo = new IndInfo("examples/data/example.candidates.ind");
    private final IndInfo gzIndInfo = new IndInfo("examples/compressed_data/example.candidates.ind.gz");

    private final GenoInfo genoInfo = new GenoInfo("examples/data/example.candidates.geno",
            indInfo, "examples/data/example.candidates.snp");
    private final GenoInfo gzGenoInfo = new GenoInfo("examples/compressed_data/example.candidates.geno.gz",
            gzIndInfo, "examples/compressed_data/example.candidates.snp.gz");

    @Test
    public void testGetSnpInfo() {
        // test uncompressed data
        assertEquals("rs1800407\tC\tT", genoInfo.getSnpInfo());

        // test compressed data
        assertEquals("rs1800407\tC\tT", gzGenoInfo.getSnpInfo());
    }

    @Test
    public void testCountAlleles() {
        // rs1800407
        int[][] alleleCounts = genoInfo.countAlleles();
        // YRI
        assertEquals(290, alleleCounts[0][0]);
        assertEquals(0, alleleCounts[0][1]);
        // CEU
        assertEquals(207, alleleCounts[1][0]);
        assertEquals(17, alleleCounts[1][1]);
        // CHS
        assertEquals(486, alleleCounts[2][0]);
        assertEquals(4, alleleCounts[2][1]);
        genoInfo.close();

        alleleCounts = gzGenoInfo.countAlleles();
        // YRI
        assertEquals(290, alleleCounts[0][0]);
        assertEquals(0, alleleCounts[0][1]);
        // CEU
        assertEquals(207, alleleCounts[1][0]);
        assertEquals(17, alleleCounts[1][1]);
        // CHS
        assertEquals(486, alleleCounts[2][0]);
        assertEquals(4, alleleCounts[2][1]);
        gzGenoInfo.close();
    }

}
