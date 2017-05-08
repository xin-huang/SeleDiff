/*
  Copyright (C) 2017 Xin Huang

  This file is part of SeleDiff.

  SeleDiff is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version

  SeleDiff is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package xin.bio.popgen.main;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import xin.bio.popgen.estimators.PopVarMedianEstimator;
import xin.bio.popgen.estimators.SeleDiffEstimator;
import xin.bio.popgen.estimators.Estimator;
import xin.bio.popgen.estimators.PopVarMeanEstimator;
import xin.bio.popgen.infos.*;

import java.io.File;

/**
 * Class {@code CommandMain} is the class for parsing command line arguments
 * in SeleDiff.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
@Parameters(commandDescription = "The main command line of SeleDiff")
final class CommandMain {

	@Parameter(names = "--vcf", required = true,
            description = "The VCF file stores variant information.", 
            validateWith = FileValidator.class)
    private String vcfFileName;
    
    @Parameter(names = "--sample", required = true,
            description = "The sample file stores individual information. " +
                    "A sample file is space delimited without header, " +
                    "where the first column is individual ID, " +
                    "and the second column is population ID. " + 
                    "The order of individual IDs should be consistent with " +
                    "those in the VCF file", validateWith = FileValidator.class)
    private String sampleFileName;

    @Parameter(names = "--time", required = true,
            description = "The file stores divergence times between populations. " +
                    "A divergence time file is space delimited without header, " +
                    "where the first column is the population ID of the first population, " +
                    "the second column is the population ID of the second population, " +
                    "the third column is the divergence time of this population pair.", validateWith = FileValidator.class)
    private String timeFileName;

    @Parameter(names = "--output", required = true,
            description = "The output file.", validateWith = FileValidator.class)
    private String outputFileName;

    @Parameter(names = "--estimator", required = true,
            description = "The type of an estimator to used: pop-var-mean | pop-var-median | sele-diff.",
            validateWith = EstimatorValidator.class)
    private String estimatorType;

    @Parameter(names = "--popvar",
            description = "The file stores variances of drift between populations, " +
                    "which is space delimited without header " +
                    "the first column is the first population ID " +
                    "the second column is the second population ID " +
                    "the third column is the variance of drift of this population pair", validateWith = FileValidator.class)
    private String popVarFileName;

    @Parameter(names = "--ancestral-allele",
            description = "The file stores the information of ancestral alleles. " +
                    "Without this file, SeleDiff would assume the REF allele is ancestral and the ALT allele is derived " +
                    "in the VCF file. A ancestral allele file is space delimited without header, " +
                    "where the first column is the SNP ID, and the second column " +
                    "is the ancestral allele. SeleDiff assumes these alleles are " +
                    "in the forward strand of the reference genome.", validateWith = FileValidator.class)
    private String ancAlleleFileName;

    /**
     * Executes SeleDiff.
     */
    void execute() {
        if (estimatorType.equals("sele-diff") && popVarFileName == null)
            throw new ParameterException("--popvar should be used when --estimator sele-diff is used");

        SampleInfo sampleInfo = new SampleInfo(sampleFileName);
        TimeInfo timeInfo = new TimeInfo(timeFileName, sampleInfo);
        Estimator estimator = null;
        // Select one kind of estimators
        if (estimatorType.equals("pop-var-median")) {
            estimator = new PopVarMedianEstimator(sampleInfo);
        }
        else if (estimatorType.equals("pop-var-mean")) {
        	estimator = new PopVarMeanEstimator(sampleInfo);
        }
        else if (estimatorType.equals("sele-diff")) {
            PopVarInfo popVarInfo = new PopVarInfo(popVarFileName, sampleInfo);
            estimator = new SeleDiffEstimator(ancAlleleFileName, popVarInfo, sampleInfo, timeInfo);
        }

        new VCFInfo(vcfFileName, sampleInfo.getIndNum(), estimator);
    
        estimator.writeResults(outputFileName);
    }

    /**
     * Validates whether a file exists and whether a path is a directory.
     */
    public static class FileValidator implements IParameterValidator {

        @Override
        public void validate(String name, String value) throws ParameterException {
            File f = new File(value);
            
            if (name.equals("--output")) {
                String path = f.getPath();
                if (path.lastIndexOf(File.separator) > 0) {
                    path = path.substring(0, path.lastIndexOf(File.separator));
                    if (!new File(path).exists())
                        throw new ParameterException("Parameter " + name + " " + value + " does not exist");
                }
            }
            else if (!f.exists()) 
            	throw new ParameterException("Parameter " + name + " " + value + " does not exist");
            else if (f.isDirectory()) 
            	throw new ParameterException("Parameter " + name + " " + value + " is a directory");
        }

    }
    
    /**
     * Validates select a correct estimator in SeleDiff.
     */
    public static class EstimatorValidator implements IParameterValidator {

        @Override
        public void validate(String name, String value) throws ParameterException {
            if (!value.equals("pop-var-median")
            		&&!value.equals("pop-var-mean")
            		&&!value.equals("sele-diff")) {
                throw new ParameterException("Parameter " + name + " does not accept " + value);
            }
        }

    }

}
