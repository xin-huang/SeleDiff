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
        thrown.expectMessage("The following options are required: [--time], [--output], [--var]");
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
