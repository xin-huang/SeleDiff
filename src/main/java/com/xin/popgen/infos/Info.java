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
package com.xin.popgen.infos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

/**
 * Interface {@code Info} defines common methods for reading files
 * in SeleDiff.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public interface Info {

    /**
     * Helper function for reading files.
     *
     * @param fileName a file name
     */
    default void readFile(BufferedReader br) {
        try {
	        String line;
            while ((line = br.readLine()) != null) {
                parseLine(line);
            }
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
    }
    
    /**
     * Helper function for returning a BufferedReader from a ungzipped or gzipped file.
     * 
     * @param fileName the name of a file
     * @return a BufferedReader instance from a ungzipped or gzipped file
     */
    default BufferedReader getBufferedReader(String fileName) {
    	if (fileName == null)
    		return null;
    	InputStream in = null;
    	try {
			in = new FileInputStream(new File(fileName));
			byte[] signature = new byte[2];
			int nread = in.read(signature);
			if (nread == 2 
					&& signature[0] == (byte) 0x1f 
					&& signature[1] == (byte) 0x8b) {
				GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(fileName));
				return new BufferedReader(new InputStreamReader(gzip));
			}
			else {
				return new BufferedReader(new FileReader(fileName));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	return null;
    }
    
    /**
     * Helper function for parsing a line in a file.
     *
     * @param line a String containing information of a line in a file
     */
    void parseLine(String line);

}
