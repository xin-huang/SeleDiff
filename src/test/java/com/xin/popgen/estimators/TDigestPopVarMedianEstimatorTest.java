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
package com.xin.popgen.estimators;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.*;

public class TDigestPopVarMedianEstimatorTest {

	@Test
	public void testFindMedians() {
		TDigestPopVarMedianEstimator td = new TDigestPopVarMedianEstimator("examples/data/example.geno",
                                              "examples/data/example.ind",
                                              "examples/data/example.snp",
                                              "examples/results/example.var", 'e');
		td.findMedians();
		assertEquals(1.542796, td.popPairVarMedians[0], 0.000001);
		assertEquals(1.633976, td.popPairVarMedians[1], 0.000001);
		assertEquals(0.988984, td.popPairVarMedians[2], 0.000001);
	}

	@Test
    public void testAnalyze() {
        TDigestPopVarMedianEstimator td = new TDigestPopVarMedianEstimator("examples/data/example.geno",
                "examples/data/example.ind",
                "examples/data/example.snp",
                "selediff.var", 'e');
        td.analyze();
        try (BufferedReader br = new BufferedReader(new FileReader("selediff.var"))) {
            String line = br.readLine().trim();
            assertEquals("YRI\tCEU\t1.542796", line);
            br.close();
            File file = new File("selediff.var");
            file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
