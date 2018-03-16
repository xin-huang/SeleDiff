/*
  Copyright (C) 2018 Xin Huang

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
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

/**
 * Class {@code CommandMain} is the class for parsing command line arguments
 * in SeleDiff.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
@Parameters(commandDescription = "Sub-command for computing population variances")
class CommandVar {
	
	@Parameter(names = "--geno", required = true, 
			description = "The EIGENSTRAT GENO file stores allele counts: "
					+ "0, zero copy of the reference allele; 1, one copy of the reference allele "
					+ "and one copy of the alternative allele; 2, two copies of the reference allele; "
					+ "9, missing values.", 
					validateWith = FileValidator.class)
    List<String> genoFileNames = new ArrayList<>();
    
    @Parameter(names = "--snp", required = true, 
    		description = "The EIGENSTRAT SNP file stores information of variants.", 
    		validateWith = FileValidator.class)
    String snpFileName;
    
    @Parameter(names = "--ind", required = true, 
    		description = "The EIGENSTRAT IND file stores information of individuals and populations.", 
    		validateWith = FileValidator.class)
    String indFileName;

    @Parameter(names = "--output", required = true,
            description = "The output file.", validateWith = FileValidator.class)
    String outputFileName;
    
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
