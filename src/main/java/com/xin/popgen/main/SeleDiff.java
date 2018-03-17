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

import com.beust.jcommander.JCommander;
import com.xin.popgen.estimators.*;
import com.xin.popgen.utils.TimeMeasurement;

/**
 * Class {@code SeleDiff} is the entry class for the SeleDiff program.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public final class SeleDiff {

    public static void main(String[] args) {
    	
    	long start = System.currentTimeMillis();
    	long startSystemTimeNano = TimeMeasurement.getSystemTime();
    	long startUserTimeNano = TimeMeasurement.getUserTime();
    	long startCpuTimeNano = TimeMeasurement.getCpuTime();
    	long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    	
        CommandVar var = new CommandVar();
        CommandScan scan = new CommandScan();
        JCommander jc = JCommander.newBuilder()
                .addCommand("var", var)
                .addCommand("scan", scan)
                .build();
        jc.setProgramName("SeleDiff");

        if (args.length == 0) {
            jc.usage();
            System.exit(-1);
        }
        else {
            jc.parse(args);
            Estimator estimator;
            if (jc.getParsedCommand().equals("var")) {
                estimator = new TDigestPopVarMedianEstimator(
                        var.indFileName,
                        var.snpFileName
                );
                estimator.analyze(var.genoFileNames);
                estimator.writeResults(var.outputFileName);
            } else if (jc.getParsedCommand().equals("scan")) {
                estimator = new SeleDiffEstimator(
                        scan.indFileName,
                        scan.snpFileName,
                        scan.popVarFileName,
                        scan.timeFileName
                );
                estimator.analyze(scan.genoFileNames);
                estimator.writeResults(scan.outputFileName);
            }

        }
        
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long taskWallTime = System.currentTimeMillis() - start;
        long taskCpuTimeNano = TimeMeasurement.getCpuTime() - startCpuTimeNano;
        long taskUserTimeNano = TimeMeasurement.getUserTime() - startUserTimeNano;
        long taskSystemTimeNano = TimeMeasurement.getSystemTime() - startSystemTimeNano;
        System.out.println("Wall time: " + taskWallTime/1000 + " seconds");
        System.out.println("CPU time: " + taskCpuTimeNano/1000000000 + " seconds");
        System.out.println("User Time: " + taskUserTimeNano/1000000000 + " seconds");
        System.out.println("System Time: " + taskSystemTimeNano/1000000000 + " seconds");
        System.out.println("Actual Used Memory: " + ((afterUsedMem - beforeUsedMem)/1000000) + "MB");
    }

}
