package xin.bio.popgen.selediff;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import xin.bio.popgen.IO.Input;
import xin.bio.popgen.IO.Output;
import xin.bio.popgen.datatype.GeneticData;

public class Model {
	
	/**
	 * Calculate the logarithm of Odds ratio
	 * E(\Phi_A - \Phi_B)
	 * @param countAw counts of ancestral allele in population A
	 * @param countAm counts of derived allele in population A
	 * @param countBw counts of ancestral allele in population B
	 * @param countBm counts of derived allele in population B
	 * @return the Odds ratio
	 */
	public static double calLogOdds(double countAw, double countAm, double countBw, double countBm) {
		return Math.log((correction(countAm) * correction(countBw)) / (correction(countAw) * correction(countBm)));
	}
	
	/**
	 * Calculate the variance of the logarithm of Odds ratio
	 * @param countAw counts of ancestral allele in population A
	 * @param countAm counts of derived allele in population A
	 * @param countBw counts of ancestral allele in population B
	 * @param countBm counts of derived allele in population B
	 * @return the variance of the logarithm of Odds ratio
	 */
	public static double calVarLogOdds(double countAw, double countAm, double countBw, double countBm) {
		return 1/correction(countAw) + 1/correction(countAm) + 1/correction(countBw) + 1/correction(countBm);
	}
	
	/**
	 * Helper function for calculating allele frequency in the missing parental population
	 * @param lambda          the admixture proportion
	 * @param descAlleleCount the allele count in the descendant population
	 * @param descSampleSize  the sample size of the descendant population
	 * @param ancAlleleCount  the allele count in the non-missing parental population
	 * @param ancSampleSize   the sample size of the non-missing parental population
	 * @return the allele frequency in the missing parental population
	 */
	public static double calMissFreq(double lambda, int descAlleleCount, int descSampleSize,
			int ancAlleleCount, int ancSampleSize) {
		double freq = (descAlleleCount * 1.0 / descSampleSize - (1 - lambda) * ancAlleleCount / ancSampleSize) / lambda;
		return ((freq >= 0) && (freq <= 1)) ? freq : (freq > 1 ?  1 : 0);
	}
	
	/**
	 * 
	 * @param count
	 * @return
	 * 31 Mar 2016
	 */
	private static double correction(double count) {
		return count < 5 ? count + 0.5 : count;
	}
	
	/**
	 * Help function for rounding a double value to 6 decimal points
	 * @param value the value to be round
	 * @return the rounded value
	 */
	public static double round(double value) {
		return (new Double(value).equals(Double.NaN)) ? value : ((double) Math.round(value * 1000000d) / 1000000d);
	}
	
	/**
	 * Help function for estimating the variance of population drift Omega 
	 * between two given populations i and j.
	 * @param i             the i-th population.
	 * @param j             the j-th population.
	 * @param alleleCounts  the array stores allele counts in different populations.
	 * @return the variance of Omega.
	 * 22 Mar 2016
	 */
	public static double estimateVarOmega(String popiId, String popjId, GeneticData genetic) {
		LinkedQueue<Double> omegaQueue = new LinkedQueue<Double>();
		int[][][] alleleCounts = genetic.getAlleleCounts();
		int i = genetic.getPopIndex(popiId);
		int j = genetic.getPopIndex(popjId);
		int variantSize = genetic.getVariantSize();
		for (int k = 0; k < variantSize; k++) {
			if ((alleleCounts[k][i][0] + alleleCounts[k][i][1] == 0) || (alleleCounts[k][j][0] + alleleCounts[k][j][1] == 0))
				continue;
			double logOdds = Model.calLogOdds(alleleCounts[k][i][0], alleleCounts[k][i][1], alleleCounts[k][j][0], alleleCounts[k][j][1]);
			double logOddsVar = Model.calVarLogOdds(alleleCounts[k][i][0], alleleCounts[k][i][1], alleleCounts[k][j][0], alleleCounts[k][j][1]);
			omegaQueue.enqueue(logOdds * logOdds / 0.455 - logOddsVar);
		}
		int effectedVariantSize = omegaQueue.size();
		double[] omegas = new double[effectedVariantSize];
		int p = 0;
		for (double d:omegaQueue) {
			omegas[p++] = d;
		}
		Arrays.sort(omegas);
		return omegas[effectedVariantSize/2];
	}
	
	/**
	 * Helper function for estimating the delta statistic.
	 * @param admixedFileName         the file stores admixed population information.
	 * @param divergenceTimeFileName  the file stores divergence times of each pair of populations.
	 */
	public static void estimateDelta(GeneticData all, GeneticData candidates,
			HashMap<String, String[]> admixedPops, String divergenceTimeFileName, String outputFileName, boolean containsHaploidType) {
		int popSize = candidates.getPopSize();
		int variantSize = candidates.getVariantSize();
		int[][][] alleleCounts = candidates.getAlleleCounts();
		double[] varOmegas = new double[popSize * (popSize - 1) / 2]; // a one-dimensional triangular array represents population pairs
		                                        // see Chapter 6 of Mining of Massive Datasets
		for (int i = 0; i < popSize; i++) {
			for (int j = i + 1; j < popSize; j++) {
				int index = (i * (2 * popSize - (i + 1)) + 2 * (j - i - 1)) / 2;
				varOmegas[index] = estimateVarOmega(candidates.getPopId(i), candidates.getPopId(j), all);
			}
		}
		HashMap<String, Double> divergenceTimes = Input.readDivergenceTimeFile(divergenceTimeFileName);
		ChiSquaredDistribution chisq = new ChiSquaredDistribution(1); // Chi-square distribution for testing the delta statistic
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
			// write header of the output
			if (containsHaploidType)
				bw.write(new String("Haplotype\tAncestral Haplotype\tDerived Haplotype\t"
					+ "Population1\tAncestral Haplotype Count\tDerived Haplotype Count\t"
					+ "Population2\tAncestral Haplotype Count\tDerived Haplotype Count\t"
					+ "Selection Difference (Population1 - Population2)\tStd(Selection Difference)\t"
					+ "Divergence Time\tlog(Odds Ratio)\tVar(log(Odds Ratio))\tPopulation Variance\tDelta\tp-value"));
			else
				bw.write(new String("SNP Id\tAncestral Allele\tDerived Allele\t"
					+ "Population1\tAncestral Allele Count\tDerived Allele Count\t"
					+ "Population2\tAncestral Allele Count\tDerived Allele Count\t"
					+ "Selection Difference (Population1 - Population2)\tStd(Selection Difference)\t"
					+ "Divergence Time\tlog(Odds Ratio)\tVar(log(Odds Ratio))\tPopulation Variance\tDelta\tp-value"));
			bw.newLine();
			
			for (int k = 0; k < variantSize; k++) {
				for (int i = 0; i < popSize; i++) {
					for (int j = i + 1; j < popSize; j++) {
						int index = (i * (2 * popSize - (i + 1)) + 2 * (j - i - 1)) / 2;
						String popiId = candidates.getPopId(i);
						String popjId = candidates.getPopId(j);
						if (admixedPops.containsKey(popiId) || admixedPops.containsKey(popjId)) {
							continue;
						}
						Output.getResults(candidates.getVariant(k), popiId, popjId, 
									alleleCounts[k][i], alleleCounts[k][j], varOmegas[index], 
									divergenceTimes.get(popiId + "_" + popjId), chisq, bw);
					}
				}
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Unit test.
	 * @param args
	 * 21 Apr 2016
	 */
	public static void main(String[] args) {
		double countAw = 1000;
		double countAm = 0;
		double countBw = 2532;
		double countBm = 2;
		double divergenceTime = 3000;
		System.out.println(calLogOdds(countAw, countAm, countBw, countBm));
		System.out.println(calLogOdds(countAw, countAm, countBw, countBm)/divergenceTime);
	}
	
}
