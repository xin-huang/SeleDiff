package xin.bio.popgen.count;

import xin.bio.popgen.datatype.GeneticData;
import xin.bio.popgen.datatype.GeneticVariant;

public class CountSnp implements CountStrategy {
	
	@Override
	public void count(GeneticData gd) {
		int[][][] alleleCounts = new int[gd.getVariantSize()][][];
		for (int i = 0; i < alleleCounts.length; i++) {
			GeneticVariant variant = gd.getVariant(i);
			int[][] counts = new int[gd.getPopSize()][variant.getAlleleSize()];
			for (int j = 0; j < gd.getIndSize(); j++) {
				int popIndex = gd.getPopIndex(gd.getInd(j).getPopId());
				int allele1 = Character.getNumericValue(variant.getGenotypes().charAt(2*j));
				int allele2 = Character.getNumericValue(variant.getGenotypes().charAt(2*j+1));
				if (allele1 != 9) counts[popIndex][allele1]++; // assume bi-allelic SNP
				if (allele2 != 9) counts[popIndex][allele2]++;
			}
			alleleCounts[i] = counts;
		}
		
		gd.setAlleleCounts(alleleCounts);
	}
	
}
