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

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * Class {@code ComputeDiff} is the class for parsing command line arguments
 * for sub-command compute-diff in SeleDiff.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
@Parameters(commandDescription = "Sub-command for estimating selection differences of loci")
public final class ComputeDiff extends ComputeVar {

    @Parameter(names = "--time", required = true,
            description = "The file stores divergence times between populations. " +
                    "A divergence time file is space delimited without header, " +
                    "where the first column is the population ID of the first population, " +
                    "the second column is the population ID of the second population, " +
                    "the third column is the divergence time of this population pair. "
                    + "This file is needed when estimating selection differences.", validateWith = FileValidator.class)
    public String timeFileName;

    @Parameter(names = "--var", required = true,
            description = "The file stores variances of Omega, " +
                    "which is space delimited without header " +
                    "the first column is the first population ID " +
                    "the second column is the second population ID " +
                    "the third column is the variance of drift of this population pair. "
                    + "This file is needed when estimating selection differences.",
            validateWith = FileValidator.class)
    public String popVarFileName;

}
