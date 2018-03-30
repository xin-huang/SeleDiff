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

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class SeleDiffEstimatorTest {

    private final SeleDiffEstimator selediff = new SeleDiffEstimator("examples/data/example.candidates.geno",
                                                                     "examples/data/example.candidates.ind",
                                                                     "examples/data/example.candidates.snp",
                                                                     "examples/results/example.var",
                                                                     "examples/data/example.time",
                                                                     "selediff.results",
                                                                     'e');

    @Test
    public void testAnalyze() {
        selediff.analyze();
        try (BufferedReader br = new BufferedReader(new FileReader("selediff.results"))) {
            String header = br.readLine().trim();
            assertEquals("SNP ID\tRef\tAlt\tPopulation 1\tPopulation 2\tSelection difference (Population 1 - Population 2)\tStd\tLower bound of 95% CI\tUpper bound of 95% CI\tDelta\tp-value", header);
            String line = br.readLine().trim();
            assertEquals("rs1800407\tC\tT\tYRI\tCEU\t-0.000773\t0.000380\t-0.001517\t-0.000028\t\t4.135\t0.042005", line);
            br.close();
            File file = new File("selediff.results");
            file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
