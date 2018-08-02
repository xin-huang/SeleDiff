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

public class VcfInfoTest {

    private final IndInfo indInfo = new IndInfo("examples/data/example.candidates.ind");
    private final IndInfo gzIndInfo = new IndInfo("examples/compressed_data/example.candidates.ind.gz");
    private final PopInfo popInfo = indInfo.getPopInfo();
    private final PopInfo gzPopInfo = gzIndInfo.getPopInfo();

    private final VcfInfo vcfInfo = new VcfInfo("examples/data/example.candidates.vcf",
            indInfo, popInfo, true);
    private final VcfInfo gzVcfInfo = new VcfInfo("examples/compressed_data/example.candidates.vcf.gz",
            gzIndInfo, gzPopInfo, true);

    @Test
    public void testCountAlleles() {
        // rs1800407
        int[][] alleleCounts = vcfInfo.countAlleles();
        // YRI
        assertEquals(290, alleleCounts[0][0]);
        assertEquals(0, alleleCounts[0][1]);
        // CEU
        assertEquals(207, alleleCounts[1][0]);
        assertEquals(17, alleleCounts[1][1]);
        // CHS
        assertEquals(486, alleleCounts[2][0]);
        assertEquals(4, alleleCounts[2][1]);

        assertEquals("15\t25903913\trs1800407\tC\tT", vcfInfo.getSnpInfo());

        vcfInfo.close();

        alleleCounts = gzVcfInfo.countAlleles();
        // YRI
        assertEquals(290, alleleCounts[0][0]);
        assertEquals(0, alleleCounts[0][1]);
        // CEU
        assertEquals(207, alleleCounts[1][0]);
        assertEquals(17, alleleCounts[1][1]);
        // CHS
        assertEquals(486, alleleCounts[2][0]);
        assertEquals(4, alleleCounts[2][1]);

        assertEquals("15\t25903913\trs1800407\tC\tT", gzVcfInfo.getSnpInfo());

        gzVcfInfo.close();
    }

}
