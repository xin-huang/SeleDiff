package com.xin.popgen.estimators;

import java.util.List;
import java.util.ArrayList;
import org.openjdk.jmh.annotations.Benchmark;

public class ArrayPopVarMedianEstimatorBenchmark {

	@Benchmark
	public void testMethod() {
		ArrayPopVarMedianEstimator estimator = new ArrayPopVarMedianEstimator(
				"src/jmh/resources/test_pop_10_ind_1000_snp_1000000.ind",
				"src/jmh/resources/test_pop_10_ind_1000_snp_1000000.snp"
			);
		List<String> genoFileNames = new ArrayList<>();
		genoFileNames.add("src/jmh/resources/test_pop_10_ind_1000_snp_1000000.geno");
		estimator.analyze(genoFileNames);
	}

}
