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
