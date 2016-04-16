package xin.bio.popgen.fileformat;

import java.util.HashMap;
import java.util.StringJoiner;

/**
 * The HaploidType class stores SNP information of a haploid type. 
 * @author Xin Huang
 *
 */
public class Haplotype extends GeneticVariant {

	private Snp[] snps; // an array stores bi-allelelic SNPs in the haploid type. 
	private String formatPattern;
	
	/**
	 * Initialize with an array that stores Snp instances, 
	 * which contain SNP information of the haploid type.
	 * @param snps  an array that stores Snp instances.
	 */
	public Haplotype(Snp[] snps) {
		this.snps     = snps;
		alleles       = new String[(int) Math.pow(2, snps.length)];
		alleleIndices = new HashMap<String, Integer>();
		formatPattern = "%" + getSnpSize() + "s";
		StringJoiner sj = new StringJoiner(",");
		refAllele = "";
		ancAllele = "";
		for (Snp s:snps) {
			sj.add(s.getId());
			if (s.getAncAllele() == null) {
				ancAllele = ancAllele.concat("");
			}
			else {
				ancAllele = ancAllele.concat(s.getAncAllele());
			}
			if (s.getRefAllele() == null) {
				refAllele = refAllele.concat("");
			}
			else {
				refAllele = refAllele.concat(s.getRefAllele());
			}
		}
		id = sj.toString();
		
		for (int i = 0; i < getAlleleSize(); i++) {
			String allele = convertToAlleles(Integer.toBinaryString(i), snps.length);
			alleles[i] = allele;
			alleleIndices.put(allele, i);
		}
	}
	
	/**
	 * 
	 * @return
	 * 6 Apr 2016
	 */
	public Snp[] getSnps() {
		return snps;
	}
	
	/**
	 * Returns a Snp instance with its index.
	 * @param i  a Snp's index.
	 * @return   a Snp instance with its index.
	 */
	public Snp getSnp(int i) {
		return snps[i];
	}
	
	/**
	 * Returns the SNP number in the haploid type.
	 * @return the SNP number in the haploid type.
	 * 6 Apr 2016
	 */
	public int getSnpSize() {
		return snps.length;
	}
	
	/**
	 * Convert an binary string into allele configuration.
	 * e.g., 011 means the haploid type consists of 1st SNP's reference allele,
	 * 2nd SNP's alternative allele and 3rd SNP's alternative allele.
	 * @param s    the binary string to be converted.
	 * @param len  the haploid type's length.
	 * @return the haploid type's allele configuration.
	 */
	public String convertToAlleles(String s, int len) {
		s = String.format(formatPattern, s).replace(' ', '0');
		StringBuilder output = new StringBuilder();
		// convert the binary string into allele configuration.
		for (int i = 0; i < len; i++) {
			String allele = s.substring(i, i+1);
			if (allele.equals("0")) {
				output.append(snps[i].getRefAllele());
			}
			else if (allele.equals("1")) {
				output.append(snps[i].getAltAlleles()[0]);  // bi-allelic SNP
			}
		}
			
		return output.toString();
	}
	
	/**
	 * Unit tests.
	 * @param args
	 * 6 Apr 2016
	 */
	public static void main(String[] args) {
		Snp[] snps = new Snp[] {
				new Snp("rs001", "1", 100, 0, "A", "A", new String[] {"A","C"}, ""),
				new Snp("rs002", "2", 200, 0, "C", "C", new String[] {"C","T"}, ""),
				new Snp("rs003", "3", 300, 0, "A", "G", new String[] {"A","G"}, "")
		};
		
		Haplotype h = new Haplotype(snps);
		System.out.println(h.getId());
		System.out.println(h.getAncAllele());
		System.out.println(h.getAncAlleleIndex());
		System.out.println(h.getAlleleIndex("ACG"));
		System.out.println(h.getAllele(h.getAncAlleleIndex()));
		System.out.println(h.getAllele(2));
		System.out.println(h.containsAllele("CCC"));
	}
	
}
