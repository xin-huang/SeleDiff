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
@Parameters(commandDescription = "Sub-command for estimating variances of population demography parameters")
public class ComputeVar {
	
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
    
    @Parameter(names = "--ind", required = true, 
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
                        throw new ParameterException("Parameter " + name + ": File " + value + " does not exist");
                }
            }
            else if (!f.exists()) 
            	throw new ParameterException("Parameter " + name + ": File " + value + " does not exist");
            else if (f.isDirectory()) 
            	throw new ParameterException("Parameter " + name + ": " + value + " is a directory");
        }

    }

}
