package com.xin.popgen.estimators;

import com.beust.jcommander.JCommander;
import com.xin.popgen.main.ComputeDiff;
import com.xin.popgen.main.ComputeVar;

public class EstimatorFactory {

    public static Estimator create(JCommander jc, ComputeVar var, ComputeDiff diff) {

        Estimator estimator = null;

        if (jc.getParsedCommand().equals("compute-var")) {
            char format = checkParameters(var.vcfFileName, var.genoFileName, var.snpFileName);
            if (format == 'e')
                estimator = new TDigestPopVarMedianEstimator(
                        var.genoFileName,
                        var.indFileName,
                        var.snpFileName,
                        var.outputFileName
                );
            if (format == 'v')
                estimator = new TDigestPopVarMedianEstimator(
                        var.vcfFileName,
                        var.indFileName,
                        var.vcfFileName,
                        var.outputFileName
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
                        diff.outputFileName
                );
            if (format == 'v')
                estimator = new SeleDiffEstimator(
                        diff.vcfFileName,
                        diff.indFileName,
                        diff.vcfFileName,
                        diff.popVarFileName,
                        diff.timeFileName,
                        diff.outputFileName
                );
        }

        return estimator;

    }

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
