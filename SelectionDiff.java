package xin.bio.popgen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringJoiner;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import xin.bio.popgen.InputOptions.ModeEnum;

import com.beust.jcommander.JCommander;

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
	
	private int[][][] alleleCounts;      // an integer array stores allele counts, the first dimension represents SNPs
                                         // the second dimension represents populations
	                                     // the third dimension represents reference allele (0) and derived allele (1)
	private boolean[][] alleleAvailable; // a boolean array stores whether a specific SNP is available for analysis in a population
	                                     // the first dimension represents SNPs
	                                     // the second dimension represents populations
                                         // all the elements are false by default
	private int snpSize;                 // how many SNPs are there in the sample
	private int popSize;                 // how many populations are there in the sample
	
	
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
		alleleAvailable = new boolean[snpSize][popSize];
		
		int snpIndex = 0;
		for (String s:geno) {
			String[] counts = s.split("");
			for (int i = 0; i < counts.length; i++) {
				int popIndex = popIdIndex[i]; // get the population index of individual i
				
				// if at least an allele is not 9 in the population,
				// then this SNP can be used for analysis
				if (counts[i].equals("0")) {
					alleleCounts[snpIndex][popIndex][0] += 2;
					alleleAvailable[snpIndex][popIndex] = true;
				}
				else if (counts[i].equals("1")) {
					alleleCounts[snpIndex][popIndex][0] += 1;
					alleleCounts[snpIndex][popIndex][1] += 1;
					alleleAvailable[snpIndex][popIndex] = true;
				}
				else if (counts[i].equals("2")) {
					alleleCounts[snpIndex][popIndex][1] += 2;
					alleleAvailable[snpIndex][popIndex] = true;
				}
			}
			snpIndex++;
		}
	}
	
	/**
	 * Estimate the variance of population drift Omega between two given
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
			if ((alleleAvailable[k][i] == false) || (alleleAvailable[k][j] == false))
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
		if ((alleleAvailable[k][i] == false) || (alleleAvailable[k][j] == false))
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
	 * Helper function for estimating the variance of population drift Omega
	 * @param eigensoft an EigenSoft data type for storing data
	 * @param seleDiff an SelectionDiff data type for analysing data
	 * @param outputFileName the output file name
	 */
	private static void estimateOmega(EigenSoft eigensoft, SelectionDiff seleDiff, String outputFileName) {
		
		int popSize = eigensoft.getPopSize();
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
			
			// write header of the output
			bw.write(new String("#Pop1\tPop2\tVar(Omega)"));
			bw.newLine();
			
			for (int i = 0; i < popSize; i++) {
				for (int j = i + 1; j < popSize; j++) {
					double varOmega = seleDiff.getVarOmega(i, j);
					
					// output results
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
	 * Helper function for estimating the delta statistic
	 * @param eigensoft eigensoft an EigenSoft data type for storing data
	 * @param seleDiff seleDiff an SelectionDiff data type for analysing data
	 * @param omegaFileName the name of the file stores the variance of population drift Omega
	 * @param outputFileName outputFileName the output file name
	 */
	private static void estimateDelta(EigenSoft eigensoft, SelectionDiff seleDiff, String omegaFileName, String outputFileName) {
		
		int popSize = eigensoft.getPopSize();
		int snpSize = eigensoft.getSnpSize();
		
		double[] varOmegas = new double[popSize * (popSize - 1) / 2]; // a one-dimensional triangular array represents population pairs
		                                        // see Chapter 6 of Mining of Massive Datasets
		
		try {
			if (omegaFileName != null) { // read omega file if omegaFileName is specified
				BufferedReader br = new BufferedReader(new FileReader(omegaFileName));
				String line = null;
				br.readLine(); // remove the header
				while ((line = br.readLine()) != null) {
					String[] elements = line.trim().split("\\s+");
					int popi = eigensoft.getPopIndex(elements[0]);
					int popj = eigensoft.getPopIndex(elements[1]);
					int index = (popi * (2 * popSize - (popi + 1)) + 2 * (popj - popi - 1)) / 2;
					varOmegas[index] = Double.parseDouble(elements[2]);
				}
				br.close();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
			
			// write header of the output
			bw.write(new String("#SNP Id\tPop1\tPop2\tlogOdds\tVar(logOdds)\tVar(Omega)\tdelta\tpvalue"));
			bw.newLine();
			
			for (int i = 0; i < popSize; i++) {
				for (int j = i + 1; j < popSize; j++) {
					double varOmega;
					if (omegaFileName != null) {
						int index = (i * (2 * popSize - (i + 1)) + 2 * (j - i - 1)) / 2;
						varOmega = varOmegas[index];
					}
					else {
						varOmega = seleDiff.getVarOmega(i, j);
					}
					for (int k = 0; k < snpSize; k++) {
						double delta = seleDiff.calDelta(i, j, k, varOmega);
						double logOdds;
						double varLogOdds;
						double pvalue;
						if ((seleDiff.alleleAvailable[k][i] == false) || (seleDiff.alleleAvailable[k][j] == false)) {
							logOdds = Double.NaN;
							varLogOdds = Double.NaN;
						}
						else {
							logOdds = seleDiff.calLogOdds(seleDiff.alleleCounts[k][i][0], seleDiff.alleleCounts[k][i][1], seleDiff.alleleCounts[k][j][0], seleDiff.alleleCounts[k][j][1]);
							varLogOdds = seleDiff.calVarLogOdds(seleDiff.alleleCounts[k][i][0], seleDiff.alleleCounts[k][i][1], seleDiff.alleleCounts[k][j][0], seleDiff.alleleCounts[k][j][1]);
						}
						
						// calculate p-value associated with the delta statistic
						ChiSquaredDistribution chisq = new ChiSquaredDistribution(1);
						if (delta != Double.NaN)
							pvalue = 1.0 - chisq.cumulativeProbability(delta);
						else 
							pvalue = Double.NaN;
						
						// output results
						StringJoiner output = new StringJoiner("\t");
						output.add(eigensoft.getSnp(k).getId());
						output.add(eigensoft.getPopId(i));
						output.add(eigensoft.getPopId(j));
						output.add(String.valueOf(logOdds));
						output.add(String.valueOf(varLogOdds));
						output.add(String.valueOf(varOmega));
						output.add(String.valueOf(delta));
						output.add(String.valueOf(pvalue));
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
	private static void showHelp() {
		System.out.println("\nSelectionDiff - 1.0.0 2015/11/03 (Xin Huang)\n");
		System.out.println("Usage: java -jar SelectionDiff.jar "
				+ "--geno <.geno file> "
				+ "--ind <.ind file> "
				+ "--snp <.snp file> "
				+ "--output <output file> "
				+ "--mode {o, d, s} "
				+ "[--omega <omega file>]\n");
		System.out.println("Options:");
		System.out.println("--geno\t\tThe .geno file contains genotype information, required");
		System.out.println("--ind\t\tThe .ind file contains individual information, required");
		System.out.println("--snp\t\tThe .snp file contains SNP information, required");
		System.out.println("--output\tThe file stores results, required");
		System.out.println("--mode\t\tSelect a analysis mode to perform, required\n"
				+ "\t\to: estimate the variance of pairwise population drift Omega only\n"
				+ "\t\td: estimate the delta statistics\n"
				+ "\t\ts: estimate the delta statistics with the given Omega");
		System.out.println("--omega\t\tThe file stores the variance of pairwise population drift Omega\n"
				+ "\t\twhich can be obtained by performing mode o analysis first");
	}

	/**
	 *	Main program of the <tt>SelectionDiff</tt> data type
	 */
	public static void main(String[] args) {
		
		if (args.length == 0) 
			showHelp();
		else {
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
				estimateDelta(eigensoft, seleDiff, null, parameters.getOutputFileName());
			}
			else if (parameters.getMode().equals(ModeEnum.s)) {
				estimateDelta(eigensoft, seleDiff, parameters.getOmegaFileName(), parameters.getOutputFileName());
			}
		}
		
	}
}
