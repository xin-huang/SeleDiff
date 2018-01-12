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

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Sub-command for scanning loci under natural selection")
final class CommandScan extends CommandMain {

    @Parameter(names = "--time", required = true,
            description = "The file stores divergence times between populations. " +
                    "A divergence time file is space delimited without header, " +
                    "where the first column is the population ID of the first population, " +
                    "the second column is the population ID of the second population, " +
                    "the third column is the divergence time of this population pair. "
                    + "This file is needed when estimating selection differences.", validateWith = FileValidator.class)
    String timeFileName;

    @Parameter(names = "--popvar", required = true,
            description = "The file stores variances of drift between populations, " +
                    "which is space delimited without header " +
                    "the first column is the first population ID " +
                    "the second column is the second population ID " +
                    "the third column is the variance of drift of this population pair. "
                    + "This file is needed when estimating selection differences.",
            validateWith = FileValidator.class)
    String popVarFileName;

}
