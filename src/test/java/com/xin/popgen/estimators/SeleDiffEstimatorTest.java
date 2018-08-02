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
            assertEquals("#CHROMO\tPOS\tID\tRef\tAlt\tPopulation 1\tPopulation 2\tSelection difference (Population 1 - Population 2)\tStd\tLower bound of 95% CI\tUpper bound of 95% CI\tDelta\tp-value", header);
            String line = br.readLine().trim();
            assertEquals("15\t25903913\trs1800407\tC\tT\tYRI\tCEU\t-0.000773\t0.000380\t-0.001517\t-0.000028\t\t4.135\t0.042005", line);
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
