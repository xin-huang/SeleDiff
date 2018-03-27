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
package com.xin.popgen.main;

import com.beust.jcommander.JCommander;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class ComputeDiffTest {

    private final ComputeDiff diff = new ComputeDiff();
    private final JCommander jc = JCommander.newBuilder()
            .addCommand("compute-diff", diff)
            .build();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testComputeDiff() {
        thrown.expect(com.beust.jcommander.ParameterException.class);
        thrown.expectMessage("The following options are required: [--time], [--output], [--var], [--ind]");
        jc.parse("compute-diff", "--geno", "examples/data/example.geno");

        jc.parse("compute-diff", "--vcf", "examples/data/example.vcf",
                "--geno", "examples/data/example.geno",
                "--ind", "examples/data/example.ind",
                "--snp", "examples/data/example.snp",
                "--var", "examples/data/example.var",
                "--time", "examples/data/example.time",
                "--output", "examples/data/example.diff");
        assertEquals(jc.getParsedCommand(), "compute-diff");
        assertEquals(diff.vcfFileName, "examples/data/example.vcf");
        assertEquals(diff.genoFileName, "examples/data/example.geno");
        assertEquals(diff.indFileName, "examples/data/example.ind");
        assertEquals(diff.snpFileName, "examples/data/example.snp");
        assertEquals(diff.popVarFileName, "examples/data/example.var");
        assertEquals(diff.timeFileName, "examples/data/example.time");
        assertEquals(diff.outputFileName, "examples/data/example.diff");
    }

}
