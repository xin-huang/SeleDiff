package xin.bio.popgen.fileformat;

import java.util.HashMap;

/**
 * The Snp class stores the information of a SNP in the genome.
 * 
 * @author Xin Huang
 *
 */
public class Snp extends GeneticVariant {

	private double genPos;    // the genetic position of a SNP.
	private int phyPos;       // the physical position of a SNP.
	
	/**
	 * Initializes a Snp instance.
	 * @param id         the rsID of a SNP.
	 * @param chrName    the chromosome name of a SNP.
	 * @param genPos     the genetic position of a SNP.
	 * @param phyPos     the physical position of a SNP.
	 * @param refAllele  the reference allele of a SNP.
	 * @param ancAllele  the ancestral allele of a SNP.
	 * @param alleles    a String array contains all the alleles of a SNP.
	 */
	public Snp(String id, String chrName, double genPos, int phyPos, String refAllele, String ancAllele, String[] alleles, String genotypes) {
		this.id        = id;
		this.chrName   = chrName;
		this.genPos    = genPos;
		this.phyPos    = phyPos;
		this.refAllele = refAllele;
		this.ancAllele = ancAllele;
		this.alleles   = alleles; 
		this.genotypes = genotypes;
		alleleIndices = new HashMap<String, Integer>();
		int i = 0;
		for (String a:alleles) {
			alleleIndices.put(a, i++);
		}
	}
	
	/**
	 * Returns the genetic position of a SNP.
	 * @return the genetic position of a SNP.
	 */
	public double getGenPos() {
		return genPos;
	}
	
	/**
	 * Returns the physical position of a SNP.
	 * @return the physical position of a SNP.
	 */
	public int getPhyPos() {
		return phyPos;
	}
	
	/**
	 * Sets the genetic position of the SNP.
	 * @param genPos  the genetic position of the SNP.
	 */
	public void setGenPos(double genPos) {
		this.genPos = genPos;
	}
	
	/**
	 * Sets the physical position of the SNP.
	 * @param phyPos  the physical position of the SNP.
	 */
	public void setPhyPos(int phyPos) {
		this.phyPos = phyPos;
	}
	
}
