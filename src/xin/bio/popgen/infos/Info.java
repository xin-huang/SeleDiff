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
package xin.bio.popgen.infos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Interface {@code Info} defines common methods for reading files
 * in SeleDiff.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
interface Info {

    /**
     * Helper function for reading files.
     *
     * @param fileName a file name
     */
    default void readFile(String fileName) {
    	//long end = System.currentTimeMillis();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //long start = System.currentTimeMillis();
        //System.out.println("Read " + fileName + " time: " + ((end-start)/1000));
    }
    
    /**
     * Helper function for parsing a line in a file.
     *
     * @param line a String containing information of a line in a file
     */
    void parseLine(String line);

}