package xin.bio.popgen.IO;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringJoiner;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import xin.bio.popgen.datatype.GeneticVariant;
import xin.bio.popgen.selediff.Model;

public class Output {

	public static void log(int variantSize, int indSize, int popSize,
			String variantType) {
		System.out.println(variantSize + " " + variantType + " are read.");
		System.out.println(indSize + " individuals are read.");
		System.out.println(popSize + " populations are read.");
		System.out.println();
	}

	public static void writeTitle(BufferedWriter bw, boolean isHaplotype) {
		try {
			if (isHaplotype)
				bw.write(new String(
						"Haplotype\tAncestral Haplotype\tDerived Haplotype\t"
						+ "Population1\tAncestral Haplotype Count\tDerived Haplotype Count\t"
						+ "Population2\tAncestral Haplotype Count\tDerived Haplotype Count\t"
						+ "Selection Coefficient Difference (Population1 - Population2)\tStd\t"
						+ "Divergence Time\tlog(Odds Ratio)\tVar(log(Odds Ratio))\tPopulation Variance\tDelta\tp-value"));
			else
				bw.write(new String("SNP Id\tAncestral Allele\tDerived Allele\t"
					+ "Population1\tAncestral Allele Count\tDerived Allele Count\t"
					+ "Population2\tAncestral Allele Count\tDerived Allele Count\t"
					+ "Selection Coefficient Difference (Population1 - Population2)\tStd\t"
					+ "Divergence Time\tlog(Odds Ratio)\tVar(log(Odds Ratio))\tPopulation Variance\tDelta\tp-value"));
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper function for calculating selection differences
	 * 
	 * @param snpId
	 *            the name of the SNP
	 * @param popiId
	 *            the population name of the i-th population
	 * @param popjId
	 *            the population name of the j-th population
	 * @param seleDiff
	 *            a SelectionDiff class for analyzing data
	 * @param countAw
	 *            counts of reference allele in population A
	 * @param countAm
	 *            counts of derived allele in population A
	 * @param countBw
	 *            countBw counts of reference allele in population B
	 * @param countBm
	 *            countBm counts of reference allele in population B
	 * @param varOmega
	 *            the variance of Omega between the i-th population and the j-th
	 *            population
	 * @param divergenceTime
	 *            the divergence time between the i-th population and the j-th
	 *            population
	 * @param chisq
	 *            a ChiSquaredDistribution class for calculating p-value
	 * @param bw
	 *            a BufferedWriter class for output results
	 */
	public static void getResults(GeneticVariant g, String popiId,
			String popjId, int[] popaAlleleCount, int[] popbAlleleCount,
			double varOmega, double divergenceTime,
			ChiSquaredDistribution chisq, BufferedWriter bw) {
		int ancAlleleIndex = g.getAncAlleleIndex();
		double countAw = popaAlleleCount[ancAlleleIndex];
		double countBw = popbAlleleCount[ancAlleleIndex];
		for (int i = 0; i < popaAlleleCount.length; i++) {
			if (i == ancAlleleIndex)
				continue;

			double countAm = popaAlleleCount[i];
			double countBm = popbAlleleCount[i];
			double logOdds = Double.NaN;
			double varLogOdds = Double.NaN;
			double seleStrength = Double.NaN;
			double stdSeleStrength = Double.NaN;
			double delta = Double.NaN;
			double pvalue = Double.NaN;
			if ((countAw + countAm != 0) && (countBw + countBm != 0)) {
				logOdds = Model.calLogOdds(countAw, countAm, countBw, countBm);
				varLogOdds = Model.calVarLogOdds(countAw, countAm, countBw,
						countBm);
				seleStrength = logOdds / divergenceTime;
				stdSeleStrength = Math.sqrt(varLogOdds) / divergenceTime;
				delta = logOdds * logOdds / (varLogOdds + varOmega);
				pvalue = 1.0 - chisq.cumulativeProbability(delta);
			}

			StringJoiner output = new StringJoiner("\t");
			output.add(g.getId()).add(g.getAncAllele()).add(g.getAllele(i))
					.add(popiId).add(String.valueOf((int) countAw))
					.add(String.valueOf((int) countAm)).add(popjId)
					.add(String.valueOf((int) countBw))
					.add(String.valueOf((int) countBm))
					.add(String.valueOf(Model.round(seleStrength)))
					.add(String.valueOf(Model.round(stdSeleStrength)))
					.add(String.valueOf((int) divergenceTime))
					.add(String.valueOf(Model.round(logOdds)))
					.add(String.valueOf(Model.round(varLogOdds)))
					.add(String.valueOf(Model.round(varOmega)))
					.add(String.valueOf(Model.round(delta)))
					.add(String.valueOf(Model.round(pvalue)));
			try {
				bw.write(output.toString());
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
