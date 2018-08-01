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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

/**
 * Class {@code ComputeVar} is the class for parsing command line arguments
 * for sub-command compute-var in SeleDiff.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
@Parameters(commandDescription = "Sub-command for estimating variances of Omega")
public class ComputeVar {

    @Parameter(names = "--count",
            description = "The COUNT file stores SNP information and allele counts of SNPs",
            validateWith = FileValidator.class)
    public String countFileName;

	@Parameter(names = "--vcf",
            description = "The VCF file stores SNP information and genotype data.",
            validateWith = FileValidator.class)
    public String vcfFileName;

    @Parameter(names = "--geno",
			description = "The EIGENSTRAT GENO file stores allele counts: "
					+ "0, zero copy of the reference allele; 1, one copy of the reference allele "
					+ "and one copy of the alternative allele; 2, two copies of the reference allele; "
					+ "9, missing values.", 
            validateWith = FileValidator.class)
    public String genoFileName;
    
    @Parameter(names = "--snp",
    		description = "The EIGENSTRAT SNP file stores information of variants.", 
    		validateWith = FileValidator.class)
    public String snpFileName;
    
    @Parameter(names = "--ind",
    		description = "The EIGENSTRAT IND file stores information of individuals and populations.", 
    		validateWith = FileValidator.class)
    public String indFileName;

    @Parameter(names = "--output", required = true,
            description = "The output file.", validateWith = FileValidator.class)
    public String outputFileName;
    
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
                        throw new ParameterException("Parameter " + name + ": Path " + path + " does not exist");
                }
            }
            else if (!f.exists()) 
            	throw new ParameterException("Parameter " + name + ": File " + value + " does not exist");
            else if (f.isDirectory()) 
            	throw new ParameterException("Parameter " + name + ": " + value + " is a directory");
        }

    }

}
