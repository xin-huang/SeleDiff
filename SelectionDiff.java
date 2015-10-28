package xin.bio.popgen;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import edu.princeton.cs.algs4.LinkedQueue;

/**
 * The <tt>SelectionDiff</tt> data type represents an algorithm that
 * calculating selection difference between two populations.
 * In this algorithm, we only consider bi-allelic markers.
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
	public double getOmegaVariance(int i, int j) {
		validate(i,j,0);
		LinkedQueue<Double> omegaQueue = new LinkedQueue<Double>();
		for (int k = 0; k < snpSize; k++) {
			if (!validateBiallelic(i, j, k))
				continue;
			double logOdds = calLogOdds(alleleCounts[k][i][0], alleleCounts[k][i][1], alleleCounts[k][j][0], alleleCounts[k][j][1]);
			double logOddsVar = calLogOddsVar(alleleCounts[k][i][0], alleleCounts[k][i][1], alleleCounts[k][j][0], alleleCounts[k][j][1]);
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
	 * @param omegaVar the estimated variance of Omega
	 * @return the delta statistic
	 */
	public double calDelta(int i, int j, int k, double omegaVar) {
		validate(i,j,k);
		if (!validateBiallelic(i, j, k))
			return Double.NaN;
		double logOdds = calLogOdds(alleleCounts[k][i][0], alleleCounts[k][i][1], alleleCounts[k][j][0], alleleCounts[k][j][1]);
		double logOddsVar = calLogOddsVar(alleleCounts[k][i][0], alleleCounts[k][i][1], alleleCounts[k][j][0], alleleCounts[k][j][1]);
		// System.out.print(alleleCounts[k][i][0] + "  " + alleleCounts[k][i][1] + " " + alleleCounts[k][j][0] + " " + alleleCounts[k][j][1] + " ");
		// System.out.print(logOdds + " ");
		// System.out.print(logOddsVar + " ");
		return logOdds * logOdds / (logOddsVar + omegaVar);
	}
	
	/**
	 * Calculate the logarithm of Odds ratio
	 * @param countAw counts of reference allele in population A
	 * @param countAm counts of derived allele in population A
	 * @param countBw counts of reference allele in population B
	 * @param countBm counts of reference allele in population B
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
	public double calLogOddsVar(double countAw, double countAm, double countBw, double countBm) {
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
			return Boolean.FALSE;
		else if ((alleleCounts[k][i][1] == 0) && (alleleCounts[k][j][1] == 0))
			return Boolean.FALSE;
		return Boolean.TRUE;
	}
	
	
	/**
	 *	Unit tests the <tt>SelectionDiff</tt> data type
	 */
	public static void main(String[] args) {
		String genoFileName = args[0];
		String indFileName = args[1];
		String snpFileName = args[2];
		String outputFileName = args[3];
		EigenSoft eigensoft = new EigenSoft(genoFileName, indFileName, snpFileName);
		int popSize = eigensoft.getPopSize();
		int snpSize = eigensoft.getSnpSize();
		SelectionDiff seleDiff = new SelectionDiff(eigensoft.getGeno(), eigensoft.getPopIdIndex(), snpSize, popSize);
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
			for (int i = 0; i < popSize; i++) {
				for (int j = i + 1; j < popSize; j++) {
					double omegaVar = seleDiff.getOmegaVariance(i, j);
					for (int k = 0; k < snpSize; k++) {
						double delta = seleDiff.calDelta(i, j, k, omegaVar);
						double logOdds = seleDiff.calLogOdds(seleDiff.alleleCounts[k][i][0], seleDiff.alleleCounts[k][i][1], seleDiff.alleleCounts[k][j][0], seleDiff.alleleCounts[k][j][1]);
						double logOddsVar = seleDiff.calLogOddsVar(seleDiff.alleleCounts[k][i][0], seleDiff.alleleCounts[k][i][1], seleDiff.alleleCounts[k][j][0], seleDiff.alleleCounts[k][j][1]);
						bw.write(eigensoft.getSnp(k).getId() + "\t");
						bw.write(eigensoft.getPopId(i) + "\t");
						bw.write(eigensoft.getPopId(j) + "\t");
						bw.write(String.valueOf(logOdds) + "\t");
						bw.write(String.valueOf(logOddsVar) + "\t");
						bw.write(String.valueOf(omegaVar) + "\t");
						bw.write(String.valueOf(delta));
						bw.newLine();
					}
				}
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*double omegaVar = seleDiff.getOmegaVariance(1, 2);
		System.out.println(omegaVar);*/
		/*System.out.println(eigensoft.getPopSize());
		System.out.println(omegaVar);*/
		
		/*for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 2; k++) {
					System.out.println(i + " " + j + " " + k + ":" + seleDiff.getAlleleCount(i, j, k));
				}
			}
		}*/
		// int j = 0;
		/*for (int i = 0; i < 10000; i++) {
		    System.out.print(eigensoft.getSnp(i).getId() + "\t");
			System.out.print(eigensoft.getPopId(1) + "\t");
			System.out.print(eigensoft.getPopId(2) + "\t");
			double delta = seleDiff.calDelta(0, 1, i, omegaVar);
			System.out.println(delta);
		}*/
		// System.out.println(j);
	}

}
