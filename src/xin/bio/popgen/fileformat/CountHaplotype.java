package xin.bio.popgen.fileformat;

import java.util.HashMap;

public class CountHaplotype implements CountBehavior {
	
	private GeneticVariant[] variants;
	private Individual[] inds;
	private HashMap<String, Integer> popIndices;
	
	public CountHaplotype(GeneticVariant[] variants, Individual[] inds, HashMap<String, Integer> popIndices) {
		this.variants = variants;
		this.inds = inds;
		this.popIndices = popIndices;
	}

	@Override
	public int[][][] count() {
		int[][][] alleleCounts = new int[variants.length][][];
		for (int i = 0; i < variants.length; i++) {
			int[][] count = new int[popIndices.keySet().size()][variants[i].getAlleleSize()];
			for (int j = 0; j < inds.length; j++) {
				String haplotype1 = "";
				String haplotype2 = "";
				for (Snp s:((Haplotype) variants[i]).getSnps()) {
					String allele1 = s.getGenotypes().substring(2*j, 2*j+1);
					String allele2 = s.getGenotypes().substring(2*j+1, 2*j+2);
					if (allele1.equals("0")) {
						haplotype1 = haplotype1.concat(s.getRefAllele());
					}
					else {
						haplotype1 = haplotype1.concat(s.getAltAlleles()[0]);
					}
					if (allele2.equals("0")) {
						haplotype2 = haplotype2.concat(s.getRefAllele());
					}
					else {
						haplotype2 = haplotype2.concat(s.getAltAlleles()[0]);
					}
				}
				count[popIndices.get(inds[j].getPopId())][variants[i].getAlleleIndex(haplotype1)]++;
				count[popIndices.get(inds[j].getPopId())][variants[i].getAlleleIndex(haplotype2)]++;
			}
			alleleCounts[i] = count;
		}
		return alleleCounts;
	}

}
