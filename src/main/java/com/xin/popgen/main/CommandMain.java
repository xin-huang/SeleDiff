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
package com.xin.popgen.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.xin.popgen.estimators.ArrayPopVarMedianEstimator;
import com.xin.popgen.estimators.ConcurrentPopVarMedianEstimator;
import com.xin.popgen.estimators.ConcurrentSeleDiffEstimator;
import com.xin.popgen.estimators.Estimator;
import com.xin.popgen.estimators.SeleDiffEstimator;
import com.xin.popgen.estimators.TDigestPopVarMedianEstimator;

/**
 * Class {@code CommandMain} is the class for parsing command line arguments
 * in SeleDiff.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
@Parameters(commandDescription = "The main command line of SeleDiff")
final class CommandMain {
	
	@Parameter(names = "--geno", required = true, 
			description = "The EIGENSTRAT GENO file stores allele counts: "
					+ "0, zero copy of the reference allele; 1, one copy of the reference allele "
					+ "and one copy of the alternative allele; 2, two copies of the reference allele; "
					+ "9, missing values.", 
					validateWith = FileValidator.class)
    private List<String> genoFileNames = new ArrayList<>();
    
    @Parameter(names = "--snp", required = true, 
    		description = "The EIGENSTRAT SNP file stores information of variants.", 
    		validateWith = FileValidator.class)
    private String snpFileName;
    
    @Parameter(names = "--ind", required = true, 
    		description = "The EIGENSTRAT IND file stores information of individuals and populations.", 
    		validateWith = FileValidator.class)
    private String indFileName;

    @Parameter(names = "--time",
            description = "The file stores divergence times between populations. " +
                    "A divergence time file is space delimited without header, " +
                    "where the first column is the population ID of the first population, " +
                    "the second column is the population ID of the second population, " +
                    "the third column is the divergence time of this population pair. "
                    + "This file is needed when estimating selection differences.", validateWith = FileValidator.class)
    private String timeFileName;

    @Parameter(names = "--output", required = true,
            description = "The output file.", validateWith = FileValidator.class)
    private String outputFileName;

    @Parameter(names = "--estimator", required = true,
            description = "The type of an estimator to be used: array-median | digest-median | sele-diff.",
            validateWith = EstimatorValidator.class)
    private String estimatorType;

    @Parameter(names = "--popvar",
            description = "The file stores variances of drift between populations, " +
                    "which is space delimited without header " +
                    "the first column is the first population ID " +
                    "the second column is the second population ID " +
                    "the third column is the variance of drift of this population pair. "
                    + "This file is needed when estimating selection differences.", 
                    validateWith = FileValidator.class)
    private String popVarFileName;

    @Parameter(names = "--thread", description = "The number of threads to be used by SeleDiff. "
    		+ "The default value is the available threads in the machine.", 
    		validateWith = ThreadValidator.class)
    private int nThreads = 1;
    

    /**
     * Executes SeleDiff.
     * @throws IOException 
     */
    void execute() {
    	checkParameters();
        Estimator estimator = newEstimator();
        estimator.analyze(genoFileNames);
        estimator.writeResults(outputFileName);
    }
    
    /**
     * Helper function for checking parameters.
     */
    
    private void checkParameters() {
    	if (estimatorType.equals("sele-diff")) {
    		if (popVarFileName == null)
    			throw new ParameterException("Parameter --popvar should be used "
    					+ "when --estimator sele-diff is used");
    		if (timeFileName == null)
    			throw new ParameterException("Parameter --time should be used "
    					+ "when --estimator sele-diff is used");
    	}
    }
    
    /**
     * Helper function for generating an estimator.
     * 
     * @return the estimator for analysis
     */
    private Estimator newEstimator() {
    	if (estimatorType.equals("sele-diff")) {
    		if (nThreads != 1)
    			return new ConcurrentSeleDiffEstimator(indFileName, snpFileName, popVarFileName, timeFileName, nThreads);
    		return new SeleDiffEstimator(indFileName, snpFileName, popVarFileName, timeFileName);
    	}
    	else if (estimatorType.equals("digest-median")) {
    		if (nThreads != 1) {
    			return new ConcurrentPopVarMedianEstimator(indFileName, snpFileName, nThreads);
    		}
   			return new TDigestPopVarMedianEstimator(indFileName, snpFileName);
    	}
    	else if (estimatorType.equals("array-median")) {
    		return new ArrayPopVarMedianEstimator(indFileName, snpFileName);
    	}
    	return null;
    }
    
    // Validators for checking parameters
    
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
            if (!value.equals("digest-median")
            		&&!value.equals("array-median")
            		&&!value.equals("sele-diff")) {
                throw new ParameterException("Parameter " + name + " does not accept " + value);
            }
        }

    }
    
    /**
     * Validates thread number is larger than 0
     */
    public static class ThreadValidator implements IParameterValidator {

		@Override
		public void validate(String name, String value) throws ParameterException {
			int thread = Integer.parseInt(value);
			if (thread <= 0)
				throw new ParameterException("Paramether " + name + " should be larger than 0");
		}
    	
    }

}
