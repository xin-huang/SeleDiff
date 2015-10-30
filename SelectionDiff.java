package xin.bio.popgen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringJoiner;

import xin.bio.popgen.InputOptions.ModeEnum;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import edu.princeton.cs.algs4.LinkedQueue;

/**
 * The <tt>SelectionDiff</tt> data type represents an algorithm that
 * calculating selection difference between two populations.
 * In this algorithm, we only consider bi-allelic markers.
 * 
 * Ref: He et al, Genome Research, 2015.
 * 
 * @author Xin Huang
 *
 */
public class SelectionDiff {
	
	private int[][][] alleleCounts; // an integer array stores allele counts, the first dimension represents SNPs
                                    // the second dimension represents populations
	                                // the third dimension represents reference allele (0) and derived allele (1)
	private int snpSize;            // how many SNPs are there in the sample
	private int popSize;            // how many populations are there in the sample
	
	
	/**
	 * Initialize a <tt>SelectionDiff</tt> data type with
	 * the given genotypes, population IDs, the size of SNPs and
	 * the size of populations
	 * @param geno       the string array of genotypes in the sample
	 * @param popIdIndex the integer of array representing populations in the sample
	 * @param snpSize    the size of SNPs in the sample
	 * @param popSize    the size of population in the sample
	 */
	public SelectionDiff(String[] geno, int[] popIdIndex, int snpSize, int popSize) {
		this.snpSize = snpSize;
		this.popSize = popSize;
		alleleCounts = new int[snpSize][popSize][2];
		
		int snpIndex = 0;
		for (String s:geno) {
			String[] counts = s.split("");
			for (int i = 0; i < counts.length; i++) {
				int popIndex = popIdIndex[i];
				if (counts[i].equals("0")) {
					alleleCounts[snpIndex][popIndex][0] += 2;
				}
				else if (counts[i].equals("1")) {
					alleleCounts[snpIndex][popIndex][0] += 1;
					alleleCounts[snpIndex][popIndex][1] += 1;
				}
				else if (counts[i].equals("2")) {
					alleleCounts[snpIndex][popIndex][1] += 2;
				}
			}
			snpIndex++;
		}
	}
	
	/**
	 * Return the count of the k-th allele of i-th SNP in j-th population
	 * @param i the i-th SNP
	 * @param j the j-th population
	 * @param k the k-th allele, if 0 reference allele; if 1 derived allele
	 * @return the count of the k-th allele of i-th SNP in j-th population
	 */
	public int getAlleleCount(int i, int j, int k) {
		return alleleCounts[i][j][k];
	}
	
	/**
	 * Estimate the variance of Omega between two given
	 * populations i and j
	 * @param i the i-th population
	 * @param j the j-th population
	 * @return the variance of Omega
	 */
	public double getVarOmega(int i, int j) {
		validate(i,j,0);
		LinkedQueue<Double> omegaQueue = new LinkedQueue<Double>();
		for (int k = 0; k < snpSize; k++) {
			if (!validateBiallelic(i, j, k))
				continue;
			double logOdds = calLogOdds(alleleCounts[k][i][0], alleleCounts[k][i][1], alleleCounts[k][j][0], alleleCounts[k][j][1]);
			double logOddsVar = calVarLogOdds(alleleCounts[k][i][0], alleleCounts[k][i][1], alleleCounts[k][j][0], alleleCounts[k][j][1]);
			omegaQueue.enqueue(logOdds * logOdds / 0.455 - logOddsVar);
		}
		int effectedSnpSize = omegaQueue.size();
		double[] omegas = new double[effectedSnpSize];
		int p = 0;
		for (double d:omegaQueue) {
			omegas[p++] = d;
		}
		Arrays.sort(omegas);
		return omegas[effectedSnpSize/2]; // select the median of omegas, we can do these in O(N), while using sort would require O(NlogN)
	}
	
	/**
	 * Calculate the delta statistic of the k-th SNP
	 * in the i-th population and j-th population with
	 * the estimated variance of Omega 
	 * @param i the i-th population
	 * @param j the j-th population
	 * @param k the k-th SNP
	 * @param varOmega the estimated variance of Omega
	 * @return the delta statistic
	 */
	public double calDelta(int i, int j, int k, double varOmega) {
		validate(i,j,k);
		if (!validateBiallelic(i, j, k))
			return Double.NaN;
		double logOdds = calLogOdds(alleleCounts[k][i][0], alleleCounts[k][i][1], alleleCounts[k][j][0], alleleCounts[k][j][1]);
		double varLogOdds = calVarLogOdds(alleleCounts[k][i][0], alleleCounts[k][i][1], alleleCounts[k][j][0], alleleCounts[k][j][1]);
		return logOdds * logOdds / (varLogOdds + varOmega);
	}
	
	/**
	 * Calculate the logarithm of Odds ratio
	 * @param countAw counts of reference allele in population A
	 * @param countAm counts of derived allele in population A
	 * @param countBw counts of reference allele in population B
	 * @param countBm counts of derived allele in population B
	 * @return the Odds ratio
	 */
	public double calLogOdds(double countAw, double countAm, double countBw, double countBm) {
		if (countAw < 5) countAw += 0.5; // correction for small counts
		if (countAm < 5) countAm += 0.5;
		if (countBw < 5) countBw += 0.5;
		if (countBm < 5) countBm += 0.5;
		return Math.log((countAm * countBw) / (countAw * countBm));
	}
	
	/**
	 * Calculate the variance of the logarithm of Odds ratio
	 * @param countAw counts of reference allele in population A
	 * @param countAm counts of derived allele in population A
	 * @param countBw counts of reference allele in population B
	 * @param countBm counts of reference allele in population B
	 * @return the variance of the logarithm of Odds ratio
	 */
	public double calVarLogOdds(double countAw, double countAm, double countBw, double countBm) {
		if (countAw < 5) countAw += 0.5; // correction for small counts
		if (countAm < 5) countAm += 0.5;
		if (countBw < 5) countBw += 0.5;
		if (countBm < 5) countBm += 0.5;
		return 1/countAw + 1/countAm + 1/countBw + 1/countBm;
	}
	
	/**
	 * Validate whether the counts of k-th SNP between the i-th population and the j-th population
	 * is available
	 * @param i the i-th population
	 * @param j the j-th population
	 * @param k the k-th SNP
	 * @throws IllegalArgumentException if i >= popSize || j >= popSize || k >= snpSize
	 */
	private void validate(int i, int j, int k) {
		if (i >= popSize) throw new IllegalArgumentException("Population index i should be less than " + popSize);
		if (j >= popSize) throw new IllegalArgumentException("Population index j should be less than " + popSize);
		if (k >= snpSize) throw new IllegalArgumentException("SNP index k should be less than " + snpSize);
	}
	
	/**
	 * Validate whether the k-th SNP between the i-th population and the j-th population is bi-allelic
	 * @param i the i-th population
	 * @param j the j-th population
	 * @param k the k-th SNP
	 * @return TRUE if bi-allelic; otherwise FALSE
	 */
	private boolean validateBiallelic(int i, int j, int k) {
		/*if ((alleleCounts[k][i][0] == 0) || (alleleCounts[k][j][0] == 0) || (alleleCounts[k][i][1] == 0) || (alleleCounts[k][j][1] == 0))
			return Boolean.FALSE;*/ // fixation in any population is not allowed
		if ((alleleCounts[k][i][0] == 0) && (alleleCounts[k][j][0] == 0))
			return false;
		else if ((alleleCounts[k][i][1] == 0) && (alleleCounts[k][j][1] == 0))
			return false;
		return true;
	}
	
	
	/**
	 *	Main program of the <tt>SelectionDiff</tt> data type
	 */
	public static void main(String[] args) {
		
		// validate input options
		InputOptions parameters = new InputOptions();
		new JCommander(parameters, args);
		if (parameters.getMode().equals(ModeEnum.s) && (parameters.getOmegaFileName() == null)) 
			throw new IllegalArgumentException("Parameter --mode s should be used with --omega");
		
		// initialization
		EigenSoft eigensoft = new EigenSoft(parameters.getGenoFileName(), parameters.getIndFileName(), parameters.getSnpFileName());
		SelectionDiff seleDiff = new SelectionDiff(eigensoft.getGeno(), eigensoft.getPopIdIndex(), eigensoft.getSnpSize(), eigensoft.getPopSize());
		
		// estimation
		if (parameters.getMode().equals(ModeEnum.o)) {
			estimateOmega(eigensoft, seleDiff, parameters.getOutputFileName());
		}
		else if (parameters.getMode().equals(ModeEnum.d)) {
			estimateDelta(eigensoft, seleDiff, parameters.getOutputFileName());
		}
		else if (parameters.getMode().equals(ModeEnum.s)) {
			estimateDelta(eigensoft, seleDiff, parameters.getOmegaFileName(), parameters.getOutputFileName());
		}
		
	}
	
	/**
	 * 
	 * @param eigensoft
	 * @param seleDiff
	 * @param outputFileName
	 */
	private static void estimateOmega(EigenSoft eigensoft, SelectionDiff seleDiff, String outputFileName) {
		
		int popSize = eigensoft.getPopSize();
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
			for (int i = 0; i < popSize; i++) {
				for (int j = i + 1; j < popSize; j++) {
					double varOmega = seleDiff.getVarOmega(i, j);
					StringJoiner output = new StringJoiner("\t");
					output.add(eigensoft.getPopId(i));
					output.add(eigensoft.getPopId(j));
					output.add(String.valueOf(varOmega));
					bw.write(output.toString());
					bw.newLine();
				}
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param eigensoft
	 * @param seleDiff
	 * @param outputFileName
	 */
	private static void estimateDelta(EigenSoft eigensoft, SelectionDiff seleDiff, String outputFileName) {
		
		int popSize = eigensoft.getPopSize();
		int snpSize = eigensoft.getSnpSize();
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
			for (int i = 0; i < popSize; i++) {
				for (int j = i + 1; j < popSize; j++) {
					double varOmega = seleDiff.getVarOmega(i, j);
					for (int k = 0; k < snpSize; k++) {
						double delta = seleDiff.calDelta(i, j, k, varOmega);
						double logOdds = seleDiff.calLogOdds(seleDiff.alleleCounts[k][i][0], seleDiff.alleleCounts[k][i][1], seleDiff.alleleCounts[k][j][0], seleDiff.alleleCounts[k][j][1]);
						double varLogOdds = seleDiff.calVarLogOdds(seleDiff.alleleCounts[k][i][0], seleDiff.alleleCounts[k][i][1], seleDiff.alleleCounts[k][j][0], seleDiff.alleleCounts[k][j][1]);
						StringJoiner output = new StringJoiner("\t");
						output.add(eigensoft.getSnp(k).getId());
						output.add(eigensoft.getPopId(i));
						output.add(eigensoft.getPopId(j));
						output.add(String.valueOf(logOdds));
						output.add(String.valueOf(varLogOdds));
						output.add(String.valueOf(varOmega));
						output.add(String.valueOf(delta));
						bw.write(output.toString());
						bw.newLine();
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
	 * 
	 * @param eigensoft
	 * @param seleDiff
	 * @param omegaFileName
	 * @param outputFileName
	 */
	private static void estimateDelta(EigenSoft eigensoft, SelectionDiff seleDiff, String omegaFileName, String outputFileName) {
		
		int popSize = eigensoft.getPopSize();
		int snpSize = eigensoft.getSnpSize();
		
		double[] varOmegas = new double[popSize * (popSize - 1) / 2]; // a one-dimensional triangular array represents population pairs
		                                        // see Chapter 6 of Mining of Massive Datasets
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(omegaFileName));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] elements = line.trim().split("\\s+");
				int popi = eigensoft.getPopIndex(elements[0]);
				int popj = eigensoft.getPopIndex(elements[1]);
				int index = (popi * (2 * popSize - (popi + 1)) + 2 * (popj - popi - 1)) / 2;
				varOmegas[index] = Double.parseDouble(elements[2]);
			}
			br.close();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
			for (int i = 0; i < popSize; i++) {
				for (int j = i + 1; j < popSize; j++) {
					for (int k = 0; k < snpSize; k++) {
						int index = (i * (2 * popSize - (i + 1)) + 2 * (j - i - 1)) / 2;
						double delta = seleDiff.calDelta(i, j, k, varOmegas[index]);
						double logOdds = seleDiff.calLogOdds(seleDiff.alleleCounts[k][i][0], seleDiff.alleleCounts[k][i][1], seleDiff.alleleCounts[k][j][0], seleDiff.alleleCounts[k][j][1]);
						double varLogOdds = seleDiff.calVarLogOdds(seleDiff.alleleCounts[k][i][0], seleDiff.alleleCounts[k][i][1], seleDiff.alleleCounts[k][j][0], seleDiff.alleleCounts[k][j][1]);
						StringJoiner output = new StringJoiner("\t");
						output.add(eigensoft.getSnp(k).getId());
						output.add(eigensoft.getPopId(i));
						output.add(eigensoft.getPopId(j));
						output.add(String.valueOf(logOdds));
						output.add(String.valueOf(varLogOdds));
						output.add(String.valueOf(varOmegas[index]));
						output.add(String.valueOf(delta));
						bw.write(output.toString());
						bw.newLine();
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
	 * Show help information
	 */
	private void showHelp() {}

}
