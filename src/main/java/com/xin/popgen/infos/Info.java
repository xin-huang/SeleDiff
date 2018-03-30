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
