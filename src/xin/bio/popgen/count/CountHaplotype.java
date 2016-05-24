package xin.bio.popgen.count;

import xin.bio.popgen.datatype.GeneticData;
import xin.bio.popgen.datatype.GeneticVariant;
import xin.bio.popgen.datatype.Haplotype;
import xin.bio.popgen.datatype.Snp;

public class CountHaplotype implements CountStrategy {

	@Override
	public void count(GeneticData gd) {
		int[][][] alleleCounts = new int[gd.getVariantSize()][][];
		for (int i = 0; i < alleleCounts.length; i++) {
			GeneticVariant variant = gd.getVariant(i);
			int[][] count = new int[gd.getPopSize()][variant.getAlleleSize()];
			for (int j = 0; j < gd.getIndSize(); j++) {
				StringBuilder haplotype1 = new StringBuilder();
				StringBuilder haplotype2 = new StringBuilder();
				int haplotypeLength = ((Haplotype) variant).getSnpSize();
				for (Snp s:((Haplotype) variant).getSnps()) { // assume s is bi-allelic
					char allele1 = s.getGenotypes().charAt(2*j);
					char allele2 = s.getGenotypes().charAt(2*j+1);
					if (allele1 == '0') {
						haplotype1 = haplotype1.append(s.getRefAllele());
					}
					else if (allele1 == '1') {
						haplotype1 = haplotype1.append(s.getAltAlleles()[0]);
					}
					if (allele2 == '0') {
						haplotype2 = haplotype2.append(s.getRefAllele());
					}
					else if (allele2 == '1') {
						haplotype2 = haplotype2.append(s.getAltAlleles()[0]);
					}
				}
				int popIndex = gd.getPopIndex(gd.getInd(j).getPopId());
				if (haplotype1.length() == haplotypeLength)
					count[popIndex][variant.getAlleleIndex(haplotype1.toString())]++;
				if (haplotype2.length() == haplotypeLength)
					count[popIndex][variant.getAlleleIndex(haplotype2.toString())]++;
			}
			alleleCounts[i] = count;
		}
		
		gd.setAlleleCounts(alleleCounts);
	}

}
