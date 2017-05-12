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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

import xin.bio.popgen.estimators.ConcurrentPopVarMedianEstimator;
import xin.bio.popgen.estimators.Estimator;
import xin.bio.popgen.estimators.PopVarMeanEstimator;
import xin.bio.popgen.estimators.PopVarMedianEstimator;
import xin.bio.popgen.estimators.SeleDiffEstimator;
import xin.bio.popgen.infos.CountSnpNumInfo;
import xin.bio.popgen.infos.IndInfo;
import xin.bio.popgen.infos.PopVarInfo;
import xin.bio.popgen.infos.TimeInfo;

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
    private String genoFileName;
    
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
            description = "The type of an estimator to be used: pop-var-mean | pop-var-median | sele-diff.",
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

    @Parameter(names = "--ancestral-allele",
            description = "The file stores the information of ancestral alleles. " +
                    "Without this file, SeleDiff would assume the REF allele is ancestral and the ALT allele is derived " +
                    "in the GENO file. A ancestral allele file is space delimited without header, " +
                    "where the first column is the SNP ID, and the second column " +
                    "is the ancestral allele. SeleDiff assumes these alleles are " +
                    "in the forward strand of the reference genome.", 
                    validateWith = FileValidator.class)
    private String ancAlleleFileName;
    
    @Parameter(names = "--thread", description = "The number of threads to be used by SeleDiff. "
    		+ "The default value is the available threads in the machine.", 
    		validateWith = ThreadValidator.class)
    private int thread = Runtime.getRuntime().availableProcessors();
    

    /**
     * Executes SeleDiff.
     */
    void execute() {
    	if (estimatorType.equals("sele-diff")) {
    		if (popVarFileName == null)
    			throw new ParameterException("Parameter --popvar should be used "
    					+ "when --estimator sele-diff is used");
    		if (timeFileName == null)
    			throw new ParameterException("Parameter --time should be used "
    					+ "when --estimator sele-diff is used");
    	}
        
        Estimator estimator = newEstimator();
        estimator.analyze(getBufferedReader(genoFileName));
        estimator.writeResults(outputFileName);
    }
    
    /**
     * Helper function for generating an estimator.
     * 
     * @return the estimator for analysis
     */
    private Estimator newEstimator() {
    	IndInfo sampleInfo = new IndInfo(indFileName, getBufferedReader(indFileName));
    	if (estimatorType.equals("sele-diff")) {
    		PopVarInfo popVarInfo = new PopVarInfo(popVarFileName, 
    				getBufferedReader(popVarFileName), sampleInfo);
    		TimeInfo timeInfo = new TimeInfo(timeFileName, 
    				getBufferedReader(timeFileName), sampleInfo);
    		return new SeleDiffEstimator(ancAlleleFileName, popVarInfo, sampleInfo, timeInfo);
    	}
    	else if (estimatorType.equals("pop-var-median")) {
    		int snpNum = new CountSnpNumInfo(snpFileName, 
    				getBufferedReader(snpFileName)).getSnpNum();
    		switch (thread) {
	    		case 1:
	    			return new PopVarMedianEstimator(sampleInfo, snpNum);
    			default:
    				return new ConcurrentPopVarMedianEstimator(sampleInfo, thread, snpNum);
    		}
    	}
    	else {
    		return new PopVarMeanEstimator(sampleInfo);
    	}
    }
    
    /**
     * Helper function for returning a BufferedReader from a ungzipped or gzipped file.
     * 
     * @param fileName the name of a file
     * @return a BufferedReader instance from a ungzipped or gzipped file
     */
    private BufferedReader getBufferedReader(String fileName) {
    	InputStream in = null;
    	try {
			in = new FileInputStream(new File(fileName));
			byte[] signature = new byte[2];
			int nread = in.read(signature);
			if (nread == 2 
					&& signature[0] == (byte) 0x1f 
					&& signature[1] == (byte) 0x8b) {
				GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(fileName));
				return new BufferedReader(new InputStreamReader(gzip));
			}
			else {
				return new BufferedReader(new FileReader(fileName));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	return null;
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
    
    public static class ThreadValidator implements IParameterValidator {

		@Override
		public void validate(String name, String value) throws ParameterException {
			int thread = Integer.parseInt(value);
			if (thread <= 0)
				throw new ParameterException("Paramether " + name + " should be larger than 0");
		}
    	
    }

}
