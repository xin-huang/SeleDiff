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

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

public class SeleDiffTest {

    @Test
    public void testMainWithoutParameters() {
        String[] args = {};
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        SeleDiff.main(args);
        String stdout = out.toString();
        assertTrue(stdout.startsWith("Usage"));
    }

    @Test
    public void testMainVcfVar() {
        String[] args = {"compute-var",
                "--vcf", "examples/data/example.vcf",
                "--ind", "examples/data/example.ind",
                "--output", "examples/results/example.test.vcf.var"};
        SeleDiff.main(args);
    }

    @Test
    public void testMainVcfDiff() {
        String[] args = {"compute-diff",
                "--vcf", "examples/data/example.candidates.vcf",
                "--ind", "examples/data/example.candidates.ind",
                "--var", "examples/results/example.var",
                "--time", "examples/data/example.time",
                "--output", "examples/results/example.candidates.test.vcf.results"};
        SeleDiff.main(args);
    }

    @Test
    public void testMainEigenVar() {
        String[] args = {"compute-var",
                "--geno", "examples/data/example.geno",
                "--ind", "examples/data/example.ind",
                "--snp", "examples/data/example.snp",
                "--output", "examples/results/example.test.geno.var"};
        SeleDiff.main(args);
    }

    @Test
    public void testMainEigenDiff() {
        String[] args = {"compute-diff",
                "--geno", "examples/data/example.candidates.geno",
                "--ind", "examples/data/example.candidates.ind",
                "--snp", "examples/data/example.candidates.snp",
                "--var", "examples/results/example.var",
                "--time", "examples/data/example.time",
                "--output", "examples/results/example.candidates.test.geno.results"};
        SeleDiff.main(args);
    }

}
