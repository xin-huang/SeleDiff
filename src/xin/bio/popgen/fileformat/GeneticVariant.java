package xin.bio.popgen.fileformat;

import java.util.HashMap;

public abstract class GeneticVariant {
	
	protected String id;
	protected String chrName;
	protected String refAllele;
	protected String ancAllele;
	protected String[] alleles;
	protected HashMap<String, Integer> alleleIndices;
	protected String genotypes;
			
	public String getId() {
		return id;
	};
	
	public String getChrName() {
		return chrName;
	}
	
	public String getRefAllele() {
		return refAllele;
	}
	
	public String[] getAltAlleles() {
		String[] altAlleles = new String[alleles.length - 1];
		int i = 0;
		for (String allele:alleles) {
			if (!allele.equals(refAllele)) {
				altAlleles[i++] = allele;
			}
		}
		return altAlleles;
	}
	
	public String getAncAllele() {
		return ancAllele;
	};
	
	public String[] getDerAlleles() {
		String[] derAlleles = new String[getAlleleSize() - 1];
		int i = 0;
		for (String allele:alleles) {
			if (!allele.equals(ancAllele)) {
				derAlleles[i++] = allele;
			}
		}
		return derAlleles;
	}
	
	public int getRefAlleleIndex() {
		return alleleIndices.get(refAllele);
	}
	
	public int getAncAlleleIndex() {
		return alleleIndices.get(ancAllele);
	}
	
	public int getAlleleIndex(String allele) {
		return alleleIndices.get(allele);
	}
	
	public String getAllele(int i) {
		return alleles[i];
	}
	
	public int getAlleleSize() {
		return alleles.length;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setChrName(String chrName) {
		this.chrName = chrName;
	}
	
	public void setRefAllele(String allele) {
		if (containsAllele(allele)) {
			this.refAllele = allele;
		}
		else {
			String[] newAlleles = new String[getAlleleSize()+1];
			this.refAllele = allele;
			int i = 0;
			for (String a:alleles) {
				newAlleles[i++] = a;
			}
			newAlleles[i] = allele;
			alleleIndices.put(allele, i);
			alleles = newAlleles;
		}
	}
	
	public void setAncAllele(String allele) {
		if (containsAllele(allele)) {
			this.ancAllele = allele;
		}
		else {
			String[] newAlleles = new String[getAlleleSize()+1];
			this.ancAllele = allele;
			int i = 0;
			for (String a:alleles) {
				newAlleles[i++] = a;
			}
			newAlleles[i] = allele;
			alleleIndices.put(allele, i);
			alleles = newAlleles;
		}
	}
	
	public boolean containsAllele(String allele) {
		return alleleIndices.containsKey(allele);
	}
	
	public String getGenotypes() {
		return genotypes;
	}
	
	public void setGenotypes(String genotypes) {
		this.genotypes = genotypes;
	}
	
}
