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

public class VcfInfoTest {

    private final IndInfo indInfo = new IndInfo("examples/data/example.candidates.ind");
    private final IndInfo gzIndInfo = new IndInfo("examples/compressed_data/example.candidates.ind.gz");

    private final GenoInfo vcfInfo = new VcfInfo("examples/data/example.candidates.vcf", indInfo, 7);
    private final GenoInfo gzVcfInfo = new VcfInfo("examples/compressed_data/example.candidates.vcf.gz", gzIndInfo, 7);

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

        assertEquals("rs1800407\tC\tT", vcfInfo.getSnpInfo());

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

        assertEquals("rs1800407\tC\tT", gzVcfInfo.getSnpInfo());

        gzVcfInfo.close();
    }

}
