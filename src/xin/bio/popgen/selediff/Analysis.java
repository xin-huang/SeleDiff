package xin.bio.popgen.selediff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import xin.bio.popgen.IO.Input;
import xin.bio.popgen.IO.Output;
import xin.bio.popgen.fileformat.GeneticData;

public class Analysis {

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
	 * Hepler function for estimating the admixed proportion of given pairs of admixed populations
	 * @param eigensoft      an EigenSoft class storing data
	 * @param seleDiff       a SelectionDiff class for analyzing data
	 * @param outputFileName the output file name
	 */
	public static HashMap<String, String[]> estimateAdmixedProportion(GeneticData genetic, String admixedFileName) {
		System.out.println("Reading  " + admixedFileName + " ...");
		int variantSize = genetic.getVariantSize();
		HashMap<String, String[]> admixedPops = new HashMap<String, String[]>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(admixedFileName));
			br.readLine();
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] elements = line.split("\\s+");
				int popi = genetic.getPopIndex(elements[0]);
				int popj = genetic.getPopIndex(elements[1]);
				int descendant = genetic.getPopIndex(elements[2]);
				double proportion = 0;
				int count = 0;
				
				for (int s = 0; s < variantSize; s++) {
					if ((genetic.getAlleleCount(s, popi, 0) + genetic.getAlleleCount(s, popi, 1) == 0) 
							|| (genetic.getAlleleCount(s, popj, 0) + genetic.getAlleleCount(s, popj, 1) == 0)
							|| (genetic.getAlleleCount(s, descendant, 0) + genetic.getAlleleCount(s, descendant, 1) == 0)) {
					}
					else {
						double fpopi = (double) genetic.getAlleleCount(s, popi, 0) 
								/ (genetic.getAlleleCount(s, popi, 0) + genetic.getAlleleCount(s, popi, 1));
						double fpopj = (double) genetic.getAlleleCount(s, popj, 0)
								/ (genetic.getAlleleCount(s, popj, 0) + genetic.getAlleleCount(s, popj, 1));
						double fdesc = (double) genetic.getAlleleCount(s, descendant, 0)
								/ (genetic.getAlleleCount(s, descendant, 0) + genetic.getAlleleCount(s, descendant, 1));
						if (Math.abs(fpopi - fpopj) > 1e-2) {
							proportion += (fdesc - fpopj) / (fpopi - fpopj);
							count++;
						}
					}
				}
				
				double lambda = proportion / count;
				// System.out.println(lambda);
				admixedPops.put(elements[2], new String[] {elements[0], elements[1], String.valueOf(Model.round(lambda))});
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(admixedPops.keySet().size() + " admixed populations are read");
		for (String desc:admixedPops.keySet()) {
			System.out.println("The admixture proportion of " + desc + " from " 
								+ admixedPops.get(desc)[0] + " is " + admixedPops.get(desc)[2]);
		}
		System.out.println();
		
		return admixedPops;
	}
	
	/**
	 * Helper function for estimating the delta statistic.
	 * @param admixedFileName         the file stores admixed population information.
	 * @param divergenceTimeFileName  the file stores divergence times of each pair of populations.
	 */
	public static void estimateDelta(GeneticData all, GeneticData candidates,
			String admixedFileName, String divergenceTimeFileName, String outputFileName, boolean containsHaploidType) {
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
		HashMap<String, String[]> admixedPops = null; // a hash map stores the information of admixed populations
		                                              // key is the admixed population ID, value is a String array,
		                                              // the first two elements are the parental population IDs
		                                              // the third element is the admixed proportion from the first parental population
		if (admixedFileName != null) 
			admixedPops = estimateAdmixedProportion(all, admixedFileName);
		ChiSquaredDistribution chisq = new ChiSquaredDistribution(1); // Chi-square distribution for testing the delta statistic
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
			// write header of the output
			if (containsHaploidType)
				bw.write(new String("Haploid Type\tAncestral Haploid Type\tDerived Haploid Type\t"
					+ "Population1\tAncestral Haploid Type Count\tDerived Haploid Type Count\t"
					+ "Population2\tAncestral Haploid Type Count\tDerived Haploid Type Count\t"
					+ "Selection Difference\tStd(Selection Difference)\t"
					+ "Divergence Time\tlog(Odds Ratio)\tVar(log(Odds Ratio))\tVar(Omega)\tdelta\tp-value"));
			else
				bw.write(new String("SNP Id\tAncestral Allele\tDerived Allele\t"
					+ "Population1\tAncestral Allele Count\tDerived Allele Count\t"
					+ "Population2\tAncestral Allele Count\tDerived Allele Count\t"
					+ "Selection Difference\tStd(Selection Difference)\t"
					+ "Divergence Time\tlog(Odds Ratio)\tVar(log(Odds Ratio))\tVar(Omega)\tdelta\tp-value"));
			bw.newLine();
			
			for (int k = 0; k < variantSize; k++) {
				HashSet<String> outputedAdmixedPops = new HashSet<String>();
				for (int i = 0; i < popSize; i++) {
					for (int j = i + 1; j < popSize; j++) {
						int index = (i * (2 * popSize - (i + 1)) + 2 * (j - i - 1)) / 2;
						String popiId = candidates.getPopId(i);
						String popjId = candidates.getPopId(j);
						double[][] freqs = null;
						String[] admixedPopIds = null;
						
						if (admixedPops != null && admixedPops.containsKey(popiId) && admixedPops.containsKey(popjId)) {
							// i and j are both admixed populations
							admixedPopIds = new String[] {popiId, popjId};
							freqs = estimateMissFreqs(k, admixedPopIds, admixedPops, candidates);  
							for (int m = 0; m < 2; m++) {
								for (int n = 0; n < 2; n++) {
									Output.getResults(candidates.getVariant(k), popiId + String.valueOf(m), popjId + String.valueOf(n), 
											new int[] {(int)Math.round(1000*freqs[0][m]), (int)Math.round(1000*(1-freqs[0][m]))}, 
											new int[] {(int)Math.round(1000*freqs[1][n]), (int)Math.round(1000*(1-freqs[1][n]))}, 
											varOmegas[index], divergenceTimes.get(popiId + String.valueOf(m) + "_" + popjId + String.valueOf(n)), 
											chisq, bw, containsHaploidType);
								}
							}
						}
						else if (admixedPops != null && admixedPops.containsKey(popiId)) {
							admixedPopIds = new String[] {popiId};
							freqs = estimateMissFreqs(k, admixedPopIds, admixedPops, candidates);
							for (int m = 0; m < 2; m++) {
								Output.getResults(candidates.getVariant(k), popiId + String.valueOf(m), popjId, 
										new int[] {(int)Math.round(1000*freqs[0][m]), (int)Math.round(1000*(1-freqs[0][m]))}, 
										alleleCounts[k][j], 
										varOmegas[index], divergenceTimes.get(popiId + String.valueOf(m) + "_" + popjId), chisq, bw, containsHaploidType);
							}
						}
						else if (admixedPops != null && admixedPops.containsKey(popjId)) {
							admixedPopIds = new String[] {popjId};
							freqs = estimateMissFreqs(k, admixedPopIds, admixedPops, candidates);
							for (int n = 0; n < 2; n++) {
								Output.getResults(candidates.getVariant(k), popiId, popjId + String.valueOf(n), 
										alleleCounts[k][i], 
										new int[] {(int)Math.round(1000*freqs[0][n]), (int)Math.round(1000*(1-freqs[0][n]))}, 
										varOmegas[index], divergenceTimes.get(popiId + "_" + popjId + String.valueOf(n)), chisq, bw, containsHaploidType);
							}
						}
						else {
							//System.out.println(popiId + "_" + popjId);
							Output.getResults(candidates.getVariant(k), popiId, popjId, 
									alleleCounts[k][i], alleleCounts[k][j], varOmegas[index], 
									divergenceTimes.get(popiId + "_" + popjId), chisq, bw, containsHaploidType);
						}
						if (freqs != null) {
							// compare selection difference between two parent populations of a admixed population.
							for (int m = 0; m < admixedPopIds.length; m++) {
								if (!outputedAdmixedPops.contains(admixedPopIds[m])) {
									outputedAdmixedPops.add(admixedPopIds[m]);
									int parentIndex1 = candidates.getPopIndex(admixedPops.get(admixedPopIds[m])[0]); 
									int parentIndex2 = candidates.getPopIndex(admixedPops.get(admixedPopIds[m])[1]);
									double divergenceTime = divergenceTimes.get(candidates.getPopId(parentIndex1) + "_" + candidates.getPopId(parentIndex2));
									Output.getResults(candidates.getVariant(k), admixedPopIds[m] + "0", admixedPopIds[m] + "1", 
											new int[] {(int)Math.round(1000*freqs[m][0]), (int)Math.round(1000*(1-freqs[m][0]))}, 
											new int[] {(int)Math.round(1000*freqs[m][1]), (int)Math.round(1000*(1-freqs[m][1]))}, 
											varOmegas[index], divergenceTime, chisq, bw, true);
								}
							}
						}
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
	 * Help function for estimating allele frequencies in the missing parental population of a admixed population.
	 * @param snpIndex       an integer specifies a Snp instance. 
	 * @param admixedPopIds  a String array stores admixed populations' IDs.
	 * @param admixedPops    a hash map stores admixed populations' information.
	 * @param genetic        a genetic data type stores genetic data.
	 * @return a double array stores allele frequencies in admixed populations' missing parental populations.
	 * 22 Mar 2016
	 */
	private static double[][] estimateMissFreqs(int snpIndex, String[] admixedPopIds, 
			HashMap<String, String[]> admixedPops, GeneticData genetic) {
		double[][] freqs = new double[admixedPopIds.length][2]; // an array stores allele frequencies in admixed populations' missing parental populations.
		                                                        // the first dimension represents admixed population indices.
		                                                        // the second dimension represents missing parents.
		int i = 0;
		for (String descendant:admixedPopIds) {
			String parentId1    = admixedPops.get(descendant)[0];
			String parentId2    = admixedPops.get(descendant)[1];
			double lambda       = Double.parseDouble(admixedPops.get(descendant)[2]);
			int descAlleleCount = genetic.getAlleleCount(snpIndex, genetic.getPopIndex(descendant), 0);
			int descSampleSize  = genetic.getAlleleCount(snpIndex, genetic.getPopIndex(descendant), 0) 
					+ genetic.getAlleleCount(snpIndex, genetic.getPopIndex(descendant), 1);
			int ancAlleleCount1 = genetic.getAlleleCount(snpIndex, genetic.getPopIndex(parentId1), 0);
			int ancSampleSize1  = genetic.getAlleleCount(snpIndex, genetic.getPopIndex(parentId1), 0) 
					+ genetic.getAlleleCount(snpIndex, genetic.getPopIndex(parentId1), 1);
			int ancAlleleCount2 = genetic.getAlleleCount(snpIndex, genetic.getPopIndex(parentId2), 0);
			int ancSampleSize2  = genetic.getAlleleCount(snpIndex, genetic.getPopIndex(parentId2), 0) 
					+ genetic.getAlleleCount(snpIndex, genetic.getPopIndex(parentId2), 1);
			// parent1 is missing
			freqs[i][0] = Model.calMissFreq(lambda, descAlleleCount, descSampleSize, ancAlleleCount2, ancSampleSize2);
			// parent2 is missing
			freqs[i++][1] = Model.calMissFreq(1 - lambda, descAlleleCount, descSampleSize, ancAlleleCount1, ancSampleSize1);
		}
		return freqs;
	}
	
}
