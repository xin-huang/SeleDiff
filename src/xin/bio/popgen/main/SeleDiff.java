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

import com.beust.jcommander.JCommander;

import xin.bio.popgen.utils.TimeMeasurement;

/**
 * Class {@code SeleDiff} is the entry class for the SeleDiff program.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class SeleDiff {

    public static void main(String[] args) {
    	
    	long start = System.currentTimeMillis();
    	long startSystemTimeNano = TimeMeasurement.getSystemTime();
    	long startUserTimeNano = TimeMeasurement.getUserTime();
    	long startCpuTimeNano = TimeMeasurement.getCpuTime();
    	
        CommandMain cm = new CommandMain();
        JCommander jc = new JCommander(cm);
        jc.setProgramName("SeleDiff");

        if (args.length == 0) {
            jc.usage();
            System.exit(-1);
        }
        else {
            jc.parse(args);
            cm.execute();
        }
        
        long taskWallTime = System.currentTimeMillis() - start;
        long taskCpuTimeNano = TimeMeasurement.getCpuTime() - startCpuTimeNano;
        long taskUserTimeNano = TimeMeasurement.getUserTime() - startUserTimeNano;
        long taskSystemTimeNano = TimeMeasurement.getSystemTime() - startSystemTimeNano;
        System.out.println("Wall time: " + taskWallTime/1000);
        System.out.println("CPU time: " + taskCpuTimeNano/1000000000);
        System.out.println("User Time: " + taskUserTimeNano/1000000000);
        System.out.println("System Time: " + taskSystemTimeNano/1000000000);
    }

}
