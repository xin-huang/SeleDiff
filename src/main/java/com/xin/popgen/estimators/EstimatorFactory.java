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

/**
 * Class {@code EstimatorFactory} generates different instances of
 * Class {@code Estimator} according to different input parameters.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public class EstimatorFactory {

    /**
     * Static method to create an Estimator instance.
     * @param jc a JCommander instance storing the input parameters
     * @param var a ComputeVar instance storing the input parameters of sub-command compute-var
     * @param diff a ComputeDiff instance storing the input parameters of sub-command compute-diff
     * @return an Estimator instance for further analysis
     */
    public static Estimator create(JCommander jc, ComputeVar var, ComputeDiff diff) {

        Estimator estimator = null;

        if (jc.getParsedCommand().equals("compute-var")) {
            char format = checkParameters(var.vcfFileName, var.genoFileName, var.snpFileName);
            if (format == 'e')
                estimator = new TDigestPopVarMedianEstimator(
                        var.genoFileName,
                        var.indFileName,
                        var.snpFileName,
                        var.outputFileName,
                        format
                );
            if (format == 'v')
                estimator = new TDigestPopVarMedianEstimator(
                        var.vcfFileName,
                        var.indFileName,
                        var.vcfFileName,
                        var.outputFileName,
                        format
                );
        } else if (jc.getParsedCommand().equals("compute-diff")) {
            char format = checkParameters(diff.vcfFileName, diff.genoFileName, diff.snpFileName);
            if (format == 'e')
                estimator = new SeleDiffEstimator(
                        diff.genoFileName,
                        diff.indFileName,
                        diff.snpFileName,
                        diff.popVarFileName,
                        diff.timeFileName,
                        diff.outputFileName,
                        format
                );
            if (format == 'v')
                estimator = new SeleDiffEstimator(
                        diff.vcfFileName,
                        diff.indFileName,
                        diff.vcfFileName,
                        diff.popVarFileName,
                        diff.timeFileName,
                        diff.outputFileName,
                        format
                );
        }

        return estimator;

    }

    /**
     * Helper function for validating input parameters.
     * @param vcfFileName the name of a VCF file
     * @param genoFileName the name of a EIGENSTRAT GENO file
     * @param snpFileName the name of a EIGENSTRAT SNP file
     * @return the format of the input files, 'e' for EIGENSTRAT format, 'v' for VCF format
     */
    private static char checkParameters(String vcfFileName, String genoFileName, String snpFileName) {

        char format = 'e';

        if ((vcfFileName != null) && (genoFileName != null))
            throw new IllegalArgumentException("Only use --vcf or --geno to specify the genotype file.");
        if ((vcfFileName != null) && (snpFileName != null))
            throw new IllegalArgumentException("Cannot use --vcf with --snp.");
        if ((vcfFileName == null) && (genoFileName == null))
            throw new IllegalArgumentException("Cannot find --vcf or --geno to specify the genotype file.");
        if ((genoFileName != null) && (snpFileName == null))
            throw new IllegalArgumentException("Cannot find --snp when using --geno.");
        if (vcfFileName != null) format = 'v';

        return format;

    }

}
