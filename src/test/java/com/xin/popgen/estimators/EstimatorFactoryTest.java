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
package com.xin.popgen.estimators;

import com.beust.jcommander.JCommander;
import com.xin.popgen.main.ComputeDiff;
import com.xin.popgen.main.ComputeVar;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertTrue;

public class EstimatorFactoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCheckParameters1() {
        ComputeVar var = new ComputeVar();
        ComputeDiff diff = new ComputeDiff();
        JCommander jc = JCommander.newBuilder()
                .addCommand("compute-var", var)
                .addCommand("compute-diff", diff)
                .build();
        thrown.expect(com.beust.jcommander.ParameterException.class);
        thrown.expectMessage("Only use --vcf or --geno to specify the genotype file.");
        jc.parse("compute-var", "--vcf", "examples/data/example.vcf",
                "--geno", "examples/data/example.geno",
                "--ind", "examples/data/example.ind",
                "--snp", "examples/data/example.snp",
                "--output", "examples/data/example.var");
        EstimatorFactory.create(jc, var, diff);
    }

    @Test
    public void testCheckParameters2() {
        ComputeVar var = new ComputeVar();
        ComputeDiff diff = new ComputeDiff();
        JCommander jc = JCommander.newBuilder()
                .addCommand("compute-var", var)
                .addCommand("compute-diff", diff)
                .build();
        thrown.expectMessage("Cannot use --vcf with --snp.");
        jc.parse("compute-var", "--vcf", "examples/data/example.vcf",
                "--ind", "examples/data/example.ind",
                "--snp", "examples/data/example.snp",
                "--output", "examples/data/example.var");
        EstimatorFactory.create(jc, var, diff);
    }

    @Test
    public void testCheckParameters3() {
        ComputeVar var = new ComputeVar();
        ComputeDiff diff = new ComputeDiff();
        JCommander jc = JCommander.newBuilder()
                .addCommand("compute-var", var)
                .addCommand("compute-diff", diff)
                .build();
        thrown.expectMessage("Cannot find --count, --vcf or --geno to specify the allele count/genotype file.");
        jc.parse("compute-var",
                "--ind", "examples/data/example.ind",
                "--snp", "examples/data/example.snp",
                "--output", "examples/data/example.var");
        EstimatorFactory.create(jc, var, diff);
    }

    @Test
    public void testCheckParameters4() {
        ComputeVar var = new ComputeVar();
        ComputeDiff diff = new ComputeDiff();
        JCommander jc = JCommander.newBuilder()
                .addCommand("compute-var", var)
                .addCommand("compute-diff", diff)
                .build();
        thrown.expectMessage("Cannot find --snp when using --geno.");
        jc.parse("compute-var",
                "--geno", "examples/data/example.geno",
                "--ind", "examples/data/example.ind",
                "--output", "examples/data/example.var");
        EstimatorFactory.create(jc, var, diff);
    }

    @Test
    public void testCheckParameters5() {
        ComputeVar var = new ComputeVar();
        ComputeDiff diff = new ComputeDiff();
        JCommander jc = JCommander.newBuilder()
                .addCommand("compute-var", var)
                .addCommand("compute-diff", diff)
                .build();
        thrown.expectMessage("Cannot use --count with --geno.");
        jc.parse("compute-var",
                "--geno", "examples/data/example.geno",
                "--count", "examples/data/example.count",
                "--output", "examples/data/example.var");
        EstimatorFactory.create(jc, var, diff);
    }

    @Test
    public void testCheckParameters6() {
        ComputeVar var = new ComputeVar();
        ComputeDiff diff = new ComputeDiff();
        JCommander jc = JCommander.newBuilder()
                .addCommand("compute-var", var)
                .addCommand("compute-diff", diff)
                .build();
        thrown.expectMessage("Cannot use --count with --vcf.");
        jc.parse("compute-var",
                "--vcf", "examples/data/example.vcf",
                "--count", "examples/data/example.count",
                "--output", "examples/data/example.var");
        EstimatorFactory.create(jc, var, diff);
    }

    @Test
    public void testCreateTDigestPopVarMedianEstimatorWithEigenStrat() {
        ComputeVar var = new ComputeVar();
        ComputeDiff diff = new ComputeDiff();
        JCommander jc = JCommander.newBuilder()
                .addCommand("compute-var", var)
                .addCommand("compute-diff", diff)
                .build();
        jc.parse("compute-var",
                "--geno", "examples/data/example.geno",
                "--ind", "examples/data/example.ind",
                "--snp", "examples/data/example.snp",
                "--output", "examples/data/example.var");
        Estimator estimator = EstimatorFactory.create(jc, var, diff);
        assertTrue(estimator instanceof TDigestPopVarMedianEstimator);
    }

    @Test
    public void testCreateTDigestPopVarMedianEstimatorWithVcf() {
        ComputeVar var = new ComputeVar();
        ComputeDiff diff = new ComputeDiff();
        JCommander jc = JCommander.newBuilder()
                .addCommand("compute-var", var)
                .addCommand("compute-diff", diff)
                .build();
        jc.parse("compute-var", "--vcf", "examples/data/example.vcf",
                "--ind", "examples/data/example.ind",
                "--output", "examples/data/example.var");
        Estimator estimator = EstimatorFactory.create(jc, var, diff);
        assertTrue(estimator instanceof TDigestPopVarMedianEstimator);
    }

    @Test
    public void testCreateSeleDiffEstimatorWithEigenStrat() {
        ComputeVar var = new ComputeVar();
        ComputeDiff diff = new ComputeDiff();
        JCommander jc = JCommander.newBuilder()
                .addCommand("compute-var", var)
                .addCommand("compute-diff", diff)
                .build();
        jc.parse("compute-diff",
                "--geno", "examples/data/example.geno",
                "--ind", "examples/data/example.ind",
                "--snp", "examples/data/example.snp",
                "--var", "examples/results/example.var",
                "--time", "examples/data/example.time",
                "--output", "examples/data/example.diff");
        Estimator estimator = EstimatorFactory.create(jc, var, diff);
        assertTrue(estimator instanceof SeleDiffEstimator);
    }

    @Test
    public void testCreateSeleDiffEstimatorWithVcf() {
        ComputeVar var = new ComputeVar();
        ComputeDiff diff = new ComputeDiff();
        JCommander jc = JCommander.newBuilder()
                .addCommand("compute-var", var)
                .addCommand("compute-diff", diff)
                .build();
        jc.parse("compute-diff", "--vcf", "examples/data/example.vcf",
                "--ind", "examples/data/example.ind",
                "--var", "examples/results/example.var",
                "--time", "examples/data/example.time",
                "--output", "examples/data/example.diff");
        Estimator estimator = EstimatorFactory.create(jc, var, diff);
        assertTrue(estimator instanceof SeleDiffEstimator);
    }

}
