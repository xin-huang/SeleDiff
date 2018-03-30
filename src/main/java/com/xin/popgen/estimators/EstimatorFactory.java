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
import com.beust.jcommander.ParameterException;
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
            switch (format) {
                case ('e'):
                    estimator = new TDigestPopVarMedianEstimator(
                            var.genoFileName,
                            var.indFileName,
                            var.snpFileName,
                            var.outputFileName,
                            format
                    );
                    break;
                case('v'):
                    estimator = new TDigestPopVarMedianEstimator(
                            var.vcfFileName,
                            var.indFileName,
                            var.vcfFileName,
                            var.outputFileName,
                            format
                    );
                    break;
                default: break;
            }
        } else if (jc.getParsedCommand().equals("compute-diff")) {
            char format = checkParameters(diff.vcfFileName, diff.genoFileName, diff.snpFileName);
            switch (format) {
                case ('e'):
                    estimator = new SeleDiffEstimator(
                            diff.genoFileName,
                            diff.indFileName,
                            diff.snpFileName,
                            diff.popVarFileName,
                            diff.timeFileName,
                            diff.outputFileName,
                            format
                    );
                    break;
                case ('v'):
                    estimator = new SeleDiffEstimator(
                            diff.vcfFileName,
                            diff.indFileName,
                            diff.vcfFileName,
                            diff.popVarFileName,
                            diff.timeFileName,
                            diff.outputFileName,
                            format
                    );
                    break;
                default: break;
            }
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
            throw new ParameterException("Only use --vcf or --geno to specify the genotype file.");
        if ((vcfFileName != null) && (snpFileName != null))
            throw new ParameterException("Cannot use --vcf with --snp.");
        if ((vcfFileName == null) && (genoFileName == null))
            throw new ParameterException("Cannot find --vcf or --geno to specify the genotype file.");
        if ((genoFileName != null) && (snpFileName == null))
            throw new ParameterException("Cannot find --snp when using --geno.");
        if (vcfFileName != null) format = 'v';

        return format;

    }

}
