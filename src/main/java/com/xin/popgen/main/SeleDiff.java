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

import com.beust.jcommander.JCommander;
import com.xin.popgen.estimators.*;

/**
 * Class {@code SeleDiff} is the entry class for the SeleDiff program.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public final class SeleDiff {

    public static void main(String[] args) {
    	
        ComputeVar var = new ComputeVar();
        ComputeDiff diff = new ComputeDiff();
        JCommander jc = JCommander.newBuilder()
                .addCommand("compute-var", var)
                .addCommand("compute-diff", diff)
                .build();
        jc.setProgramName("SeleDiff");

        if (args.length == 0) {
            jc.usage();
        }
        else {
            jc.parse(args);
            Estimator estimator = EstimatorFactory.create(jc, var, diff);
            estimator.analyze();
        }
        
    }

}
