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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import xin.bio.popgen.estimators.Estimator;


/**
 * Class {@code VcfInfo} stores variant information of the sample.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class VCFInfo implements Info  {

    // an Estimator instance stores which kind of estimation to be performed
    private final Estimator estimator;
    
    // an integer stores how many variants in the sample
    private long snpNum;

    // an integer stores how many individuals in the sample
    //private final int sampleIndNum;

    // a String array stores individual IDs in the VCF file
    //private String[] indIds;
    
    /**
     * Constructor of class {@code VcfInfo}.
     *
     * @param vcfFileName the file name of a VCF file
     * @param sampleInfo a SampleInfo instance containing sample information
     */
    public VCFInfo(String vcfFileName, int sampleIndNum, Estimator estimator) {
        this.estimator = estimator;
        snpNum = 0;
        
        //this.sampleIndNum = sampleIndNum;
        boolean isGzip = isGzipped(vcfFileName);

        readFile(vcfFileName, isGzip);
        System.out.println("Used time for counting: " + estimator.getTime()/1000 + " seconds");
        estimator.estimate();

        System.out.println(snpNum + " variants are read from " + vcfFileName);
    }
    
    /**
     * Helper function for checking whether a file is gzipped.
     * 
     * @param fileName the name of a file
     * @return true, if gzipped; false, otherwise
     */
    private boolean isGzipped(String fileName) {
    	InputStream in = null;
    	try {
			in = new FileInputStream(new File(fileName));
			byte[] signature = new byte[2];
			int nread = in.read(signature);
			return nread == 2 
					&& signature[0] == (byte) 0x1f 
					&& signature[1] == (byte) 0x8b;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	return false;
    }

    /**
     * Helper function for reading a VCF file.
     * 
     * @param fileName the name of a VCF file
     * @param isGzip indicates whether the file is gzipped
     */
    private void readFile(String fileName, boolean isGzip) {
    	BufferedReader br = null;
        try {
	        if (isGzip) {
	        	GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(fileName));
	        	br = new BufferedReader(new InputStreamReader(gzip));
	        }
	        else {
	        	br = new BufferedReader(new FileReader(fileName));
	        }
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
    
    @Override
    public void parseLine(String line) {
    	if (line.startsWith("#")) return;
		else {
			snpNum++;
			estimator.parseSnpInfo(line);
		}
    }

}
