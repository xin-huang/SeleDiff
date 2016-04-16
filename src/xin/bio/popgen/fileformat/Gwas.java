package xin.bio.popgen.fileformat;

import xin.bio.popgen.IO.Output;
import xin.bio.popgen.IO.ReadGenFile;
import xin.bio.popgen.IO.ReadSampleFile;

/**
 * The Gwas class is used for reading GWAS format files.
 * In the GWAS genotype file (.gen), each row represents the information 
 * and genotypes of a SNP for all the individuals in the data.
 * It looks like:
 * 		20 rs001 100 C T 1 0 0 1 0 0 1 0 0 0.98 0 0
 *		20 rs002 200 T C 0 0 1 0 0 1 0 0 0.95 1 0 0
 *		20 rs003 300 G A 1 0 0 0 1 0 1 0 0 0 1 0
 *		20 rs004 400 A C 0 1 0 0 1 0 0 1 0 0 0 1
 * The first column is a SNP's chromosome code in the data.
 * The second column is a SNP's ID in the data.
 * The third column is a SNP's physical position in the data.
 * The fourth column is a SNP's reference allele in the data.
 * The fifth column is a SNP's alternative allele in the data.
 * From the sixth column, every three columns represent probabilites of homozygotes
 * with two reference alleles, heterozygotes and homozygotes with two alternative alleles
 * of an individual.
 * 
 * In GWAS sample file (.sample), each row represents an individual's information except the first two lines.
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
 * @author Xin Huang
 *
 * 23 Mar 2016
 */
public class Gwas extends GeneticData {
	
	private String genFileName;
	private String sampleFileName;
	private double threshold;

	/**
	 * Initializes with a .gen file and a .sample file.
	 * @param genFileName     the name of .gen file.
	 * @param sampleFileName  the name of .sample file.
	 */
	public Gwas(String genFileName, String sampleFileName, double threshold) {
		this.genFileName = genFileName;
		this.sampleFileName = sampleFileName;
		this.threshold = threshold;
		
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
		
		ReadGenFile genFile = new ReadGenFile(genFileName, threshold);
		this.variants = genFile.getVariants();
		this.variantIndices = genFile.getVariantIndices();
	}
	
	/**
	 * Unit test. 
	 * 23 Mar 2016
	 */
	public static void main(String[] args) {
		String genFileName = args[0];
		String sampleFileName = args[1];
		Gwas gwas = new Gwas(genFileName, sampleFileName, 0.9);
		System.out.println("Population number: " + gwas.getPopSize());
		System.out.println("SNP number: " + gwas.getVariantSize());
		System.out.println("Individual number: " + gwas.getIndSize());
		for (int i = 0; i < gwas.getVariantSize(); i++) {
			System.out.println(gwas.getVariant(i).getId() + "\t" + gwas.getVariant(i).getRefAllele() + "\t" + gwas.getVariant(i).getAltAlleles()[0]);
			for (int j = 0; j < gwas.getPopSize(); j++) {
				System.out.println(gwas.getPopId(j) + "\t" + gwas.getAlleleCount(i, j, 0) + "\t" + gwas.getAlleleCount(i, j, 1));
			}
		}
	}

}
