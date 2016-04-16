package xin.bio.popgen.fileformat;

import xin.bio.popgen.IO.Output;
import xin.bio.popgen.IO.ReadHapsFile;
import xin.bio.popgen.IO.ReadSampleFile;


/**
 * The Haps class is used for reading HAPS/SAMPLE files, which are generated by SHAPEIT and IMPUTE2,
 * and counting alleles or haplotypes in different populations.
 * 
 * In a HAPS file, each row represents genotypes of a SNP for all the individuals in the data.
 * It looks like:
 * 		20 rs001 100 C T 0 0 0 0 0 0 0 0
 *		20 rs002 200 T C 1 1 1 1 1 1 0 0
 *		20 rs003 300 G A 0 0 0 1 0 0 1 0
 *		20 rs004 400 A C 0 1 0 1 0 1 1 1
 * The first column is a SNP's chromosome code.
 * The second column is a SNP's ID.
 * The third column is a SNP's physical position.
 * The fourth column is a SNP's reference allele.
 * The fifth column is a SNP's alternative allele.
 * From the sixth column, every two columns represent two haplotypes in an individual,
 * where 0 represents the reference allele, and 1 represents the alternative allele.
 *
 * In a SAMPLE file, each row represents an individual's information except the first two lines.
 * It looks like:
 * 		ID_1 ID_2 missing sex phenotype
 *		0 0 0 D P
 *		pop1 A 0 1 -9
 *		pop1 B 0 1 -9
 *		pop2 C 0 1 -9
 *		pop3 D 0 1 -9
 * The first column is an individual's population ID.
 * The second column is an individual's ID.
 * The third column is an individual's missing rate.
 * The fourth column is an individual's sex.
 * The fifth column is an individual's phenotype.
 *
 * In a haplotype list file, each row represents a haplotype with several SNPs, which are separated by ",".
 * It looks like:
 * 		rs001,rs004
 *		rs002,rs003,rs004
 * The first line is a haplotype consisted of rs001 and rs004.
 * The second line is a haplotype consisted of rs002, rs003 and rs004.
 * 
 * @author Xin Huang
 * 
 * 22 Mar 2016
 *
 */
public class Haps extends GeneticData {
	
	private String hapsFileName;
	private String sampleFileName;

	/**
	 * Initialize with the given .hap file, .sample file, .hap_confidence file, .hap_list file and a confidence threshold.
	 * @param hapFileName      the name of .hap file.
	 * @param sampleFileName   the name of .sample file.
	 * @param confFileName     the name of .hap_confidence file.
	 * @param hapListFileName  the name of .hap_list file.
	 * @param confidence       the confidence threshold.
	 */
	public Haps(String hapsFileName, String sampleFileName) {
		this.hapsFileName = hapsFileName;
		this.sampleFileName = sampleFileName;
		
		readFiles();
		countBehavior = new CountSnp(variants, inds, popIndices);
		performCount();
		
		Output.log(getVariantSize(), getIndSize(), getPopSize(), "SNPs");
	}
	
	@Override
	protected void readFiles() {
		ReadSampleFile sampleFile = new ReadSampleFile(sampleFileName);
		this.inds = sampleFile.getInds();
		this.indIndices = sampleFile.getIndIndices();
		this.popIds = sampleFile.getPopIds();
		this.popIndices = sampleFile.getPopIndices();
		
		ReadHapsFile hapsFile = new ReadHapsFile(hapsFileName);
		this.variants = hapsFile.getVariants();
		this.variantIndices = hapsFile.getVariantIndices();
	}
	
	/**
	 * Unit test
	 */
	public static void main(String[] args) {
		String hapsFileName = args[0];
		String sampleFileName = args[1];
		Haps haps = new Haps(hapsFileName, sampleFileName);
		System.out.println("Population number: " + haps.getPopSize());
		System.out.println("SNP number: " + haps.getVariantSize());
		System.out.println("Individual number: " + haps.getIndSize());
		for (int i = 0; i < haps.getVariantSize(); i++) {
			System.out.println(haps.getVariant(i).getId() + "\t" + haps.getVariant(i).getRefAllele() + "\t" + haps.getVariant(i).getAltAlleles()[0]);
			for (int j = 0; j < haps.getPopSize(); j++) {
				System.out.println(haps.getPopId(j) + "\t" + haps.getAlleleCount(i, j, 0) + "\t" + haps.getAlleleCount(i, j, 1));
			}
		}
	}

}
