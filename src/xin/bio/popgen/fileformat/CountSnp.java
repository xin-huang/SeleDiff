package xin.bio.popgen.fileformat;

import java.util.HashMap;

public class CountSnp implements CountBehavior {
	
	private GeneticVariant[] variants;
	private Individual[] inds;
	private HashMap<String, Integer> popIndices;
	
	public CountSnp(GeneticVariant[] variants, Individual[] inds, HashMap<String, Integer> popIndices) {
		this.variants = variants;
		this.inds = inds;
		this.popIndices = popIndices;
	}

	@Override
	public int[][][] count() {
		int[][][] alleleCounts = new int[variants.length][][];
		for (int i = 0; i < variants.length; i++) {
			int[][] counts = new int[popIndices.keySet().size()][variants[i].getAlleleSize()];
			for (int j = 0; j < inds.length; j++) {
				String allele1 = variants[i].getGenotypes().substring(2*j, 2*j+1);
				String allele2 = variants[i].getGenotypes().substring(2*j+1, 2*j+2);
				if (allele1.equals("0")) {
					counts[popIndices.get(inds[j].getPopId())][variants[i].getRefAlleleIndex()]++;
				}
				else if (allele1.equals("1")) {
					counts[popIndices.get(inds[j].getPopId())][variants[i].getAlleleIndex(variants[i].getAltAlleles()[0])]++;
				}
				if (allele2.equals("0")) {
					counts[popIndices.get(inds[j].getPopId())][variants[i].getRefAlleleIndex()]++;
				}
				else if (allele2.equals("1")) {
					counts[popIndices.get(inds[j].getPopId())][variants[i].getAlleleIndex(variants[i].getAltAlleles()[0])]++;
				}
			}
			alleleCounts[i] = counts;
		}
		return alleleCounts;
	}

}
