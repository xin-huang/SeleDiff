package xin.bio.popgen;

/**
 * The <tt>Snp</tt> data type stores the information of a SNP
 * 
 * @author Xin Huang
 *
 */
public class Snp {

	private String id;       // the rsID of a SNP
	private String ref;      // the reference allele of a SNP
	private String derived;  // the derived allele of a SNP
	private String position; // the position of a SNP
	
	/**
	 * Initialize a <tt>Snp</tt> data type with the given
	 * rsID, reference allele, derived allele and position
	 * @param id       the rsID of a SNP
	 * @param ref      the reference allele of a SNP
	 * @param derived  the derived allele of a SNP
	 * @param position the position of a SNP
	 */
	public Snp(String id, String ref, String derived, String position) {
		this.id = id;
		this.ref = ref;
		this.derived = derived;
		this.position = position;
	}
	
	/**
	 * Return the rsID of a SNP
	 * @return the rsID of a SNP
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Return the reference allele of a SNP
	 * @return the reference allele of a SNP
	 */
	public String getRefAllele() {
		return ref;
	}
	
	/**
	 * Return the derived allele of a SNP
	 * @return the derived allele of a SNP
	 */
	public String getDerivedAllele() {
		return derived;
	}
	
	/**
	 * Return the position of a SNP
	 * @return the position of a SNP
	 */
	public String getPosition() {
		return position;
	}
	
	@Override
	public String toString() {
		return id + " " + ref + " " + derived + " " + position;
	}
	
	public static void main(String[] args) {
	}

}
