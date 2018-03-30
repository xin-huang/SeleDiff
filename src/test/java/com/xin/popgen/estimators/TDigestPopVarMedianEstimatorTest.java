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
