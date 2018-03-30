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
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public interface Info {

    /**
     * Helper function for reading files.
     *
     * @param br a BufferedReader corresponding to an input file
     */
    default void readFile(BufferedReader br) {
        try {
	        String line;
            while ((line = br.readLine()) != null) {
                parseLine(line);
            }
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
	int bufferSize = 1000 * 1024;
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
				return new BufferedReader(new InputStreamReader(gzip), bufferSize);
			}
			else {
				return new BufferedReader(new FileReader(fileName), bufferSize);
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
