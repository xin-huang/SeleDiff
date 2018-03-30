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

import com.beust.jcommander.JCommander;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class ComputeVarTest {

    private final ComputeVar var = new ComputeVar();
    private final JCommander jc = JCommander.newBuilder()
            .addCommand("compute-var", var)
            .build();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testComputeVar() {
        thrown.expect(com.beust.jcommander.ParameterException.class);
        thrown.expectMessage("The following options are required: [--output], [--ind]");
        jc.parse("compute-var", "--geno", "examples/data/example.geno");

        jc.parse("compute-var", "--vcf", "examples/data/example.vcf",
                                        "--geno", "examples/data/example.geno",
                                        "--ind", "examples/data/example.ind",
                                        "--snp", "examples/data/example.snp",
                                        "--output", "examples/data/example.var");
        assertEquals(jc.getParsedCommand(), "compute-var");
        assertEquals(var.vcfFileName, "examples/data/example.vcf");
        assertEquals(var.genoFileName, "examples/data/example.geno");
        assertEquals(var.indFileName, "examples/data/example.ind");
        assertEquals(var.snpFileName, "examples/data/example.snp");
        assertEquals(var.outputFileName, "examples/data/example.var");
    }

    @Test
    public void testFileValidator1() {
        thrown.expectMessage("Parameter --geno: File test.geno does not exist");
        jc.parse("compute-var", "--geno", "test.geno");
    }

    @Test
    public void testFileValidator2() {
        thrown.expectMessage("Parameter --geno: examples is a directory");
        jc.parse("compute-var", "--geno", "examples");
    }

    @Test
    public void testFileValidator3() {
        thrown.expectMessage("Parameter --output: Path test does not exist");
        jc.parse("compute-var", "--ind", "examples/data/example.ind", "--output", "test/test.output");
    }

}
