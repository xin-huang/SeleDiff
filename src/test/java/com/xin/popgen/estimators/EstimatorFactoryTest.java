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
        thrown.expectMessage("Cannot find --vcf or --geno to specify the genotype file.");
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
