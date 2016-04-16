package xin.bio.popgen.selediff;

public class Model {
	
	/**
	 * Calculate the logarithm of Odds ratio
	 * E(\Phi_B - \Phi_A)
	 * @param countAw counts of reference allele in population A
	 * @param countAm counts of derived allele in population A
	 * @param countBw counts of reference allele in population B
	 * @param countBm counts of derived allele in population B
	 * @return the Odds ratio
	 */
	public static double calLogOdds(double countAw, double countAm, double countBw, double countBm) {
		return Math.log((correction(countAm) * correction(countBw)) / (correction(countAw) * correction(countBm)));
	}
	
	/**
	 * Calculate the variance of the logarithm of Odds ratio
	 * @param countAw counts of reference allele in population A
	 * @param countAm counts of derived allele in population A
	 * @param countBw counts of reference allele in population B
	 * @param countBm counts of reference allele in population B
	 * @return the variance of the logarithm of Odds ratio
	 */
	public static double calVarLogOdds(double countAw, double countAm, double countBw, double countBm) {
		return 1/correction(countAw) + 1/correction(countAm) + 1/correction(countBw) + 1/correction(countBm);
	}
	
	/**
	 * Help function for calculating the delta statistic of the k-th SNP
	 * in the i-th population and j-th population with
	 * the estimated variance of Omega.
	 * @param i             the i-th population.
	 * @param j             the j-th population.
	 * @param k             the k-th SNP.
	 * @param varOmega      the estimated variance of Omega.
	 * @param alleleCounts  the array stores allele counts in different populations.
	 * @return the delta statistic.
	 * 22 Mar 2016
	 */
	public static double calDelta(int i, int j, int k, double varOmega, int[][][] alleleCounts) {
		if ((alleleCounts[k][i][0] + alleleCounts[k][i][1] == 0) || (alleleCounts[k][j][0] + alleleCounts[k][j][1] == 0))
			return Double.NaN;
		double logOdds = calLogOdds(alleleCounts[k][i][0], alleleCounts[k][i][1], alleleCounts[k][j][0], alleleCounts[k][j][1]);
		double varLogOdds = calVarLogOdds(alleleCounts[k][i][0], alleleCounts[k][i][1], alleleCounts[k][j][0], alleleCounts[k][j][1]);
		return logOdds * logOdds / (varLogOdds + varOmega);
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
	
}
