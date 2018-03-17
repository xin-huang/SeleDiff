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

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * Class {@code CommandVar} is the class for parsing command line arguments
 * for sub-command scan in SeleDiff.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
@Parameters(commandDescription = "Sub-command for scanning loci under natural selection")
final class CommandScan extends CommandVar {

    @Parameter(names = "--time", required = true,
            description = "The file stores divergence times between populations. " +
                    "A divergence time file is space delimited without header, " +
                    "where the first column is the population ID of the first population, " +
                    "the second column is the population ID of the second population, " +
                    "the third column is the divergence time of this population pair. "
                    + "This file is needed when estimating selection differences.", validateWith = FileValidator.class)
    String timeFileName;

    @Parameter(names = "--var", required = true,
            description = "The file stores variances of population demography parameters, " +
                    "which is space delimited without header " +
                    "the first column is the first population ID " +
                    "the second column is the second population ID " +
                    "the third column is the variance of drift of this population pair. "
                    + "This file is needed when estimating selection differences.",
            validateWith = FileValidator.class)
    String popVarFileName;

}
