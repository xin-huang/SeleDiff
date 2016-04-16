package xin.bio.popgen.fileformat;

import xin.bio.popgen.IO.Output;
import xin.bio.popgen.IO.ReadGenoFile;
import xin.bio.popgen.IO.ReadIndFile;
import xin.bio.popgen.IO.ReadSnpFile;

/**
 * This EigenStrat class stores genotype information,
 * individual information and SNP information using by EigenSoft, 
 * which is a popular software using in population genetics.
 * 
 * In the EIGENSTRAT genotype file (.geno), each row represents the genotypes of a SNP for all the individuals in the data.
 * It looks like:
 * 		0001020001999220
 * 		0101129919920000
 * 0 means zero copies of reference allele.
 * 1 means one copy of reference allele.
 * 2 means two copies of reference allele.
 * 9 means missing data.
 * 
 * In the EIGENSTRAT individual file (.ind), each row represents the information of an individual in the data.
 * It looks like:
 * 		Sample1 M Pop1
 * 		Sample2 F Pop2
 * The first column is the IDs of the individuals in the data.
 * The second column is the sex of the individuals in the data (F=female, M=male, U=unknown).
 * The third column is the population IDs of the individuals in the data.
 * 
 * In the EIGENSTRAT snp file (.snp), each row represents the information of a SNP in the data.
 * It looks like:
 * 		rsID1 0 1 100 A T
 * 		rsID2 0 1 102 C G
 * The first column is the IDs of the SNPs in the data.
 * The second column is the chromosome codes of the SNPs in the data.
 * The third column is the genetic positions of the SNPs in the data.
 * The fourth column is the physical positions of the SNPs in the data.
 * The fifth column is the reference allele of the SNPs in the data.
 * The sixth column is the alternative allele of the SNPs in the data.
 * 
 * @author Xin Huang
 * 
 * 22 Mar 2016
 *
 */

public class EigenStrat extends GeneticData {
	
	private String genoFileName;
	private String indFileName;
	private String snpFileName;
	
	/**
	 * Initializes a <tt>EigenStrat</tt> data type with 
	 * the given geno file, ind file and snp file
	 * @param genoFileName the name of the geno file
	 * @param indFileName  the name of the ind file
	 * @param snpFileName  the name of the snp file
	 */
	public EigenStrat(String genoFileName, String indFileName, String snpFileName) {
		this.genoFileName = genoFileName;
		this.indFileName = indFileName;
		this.snpFileName = snpFileName;
		
		readFiles();
		countBehavior = new CountSnp(variants, inds, popIndices);
		performCount();
		
		Output.log(getVariantSize(), getIndSize(), getPopSize(), "SNPs");
	}
	
	@Override
	protected void readFiles() {
		ReadIndFile indFile = new ReadIndFile(indFileName);
		this.inds = indFile.getInds();
		this.indIndices = indFile.getIndIndices();
		this.popIds = indFile.getPopIds();
		this.popIndices = indFile.getPopIndices();
		
		ReadSnpFile snpFile = new ReadSnpFile(snpFileName);
		this.variants = snpFile.getVariants();
		this.variantIndices = snpFile.getVariantIndices();
		
		ReadGenoFile genoFile = new ReadGenoFile(genoFileName, this.variants);
		this.variants = genoFile.getVariants();
	}
	
	/**
	 * Unit test.
	 */
	public static void main(String[] args) {
		String genoFileName = args[0];
		String indFileName = args[1];
		String snpFileName = args[2];
		
		EigenStrat eigen = new EigenStrat(genoFileName, indFileName, snpFileName);
		System.out.println("Population number: " + eigen.getPopSize());
		System.out.println("SNP number: " + eigen.getVariantSize());
		System.out.println("Individual number: " + eigen.getIndSize());
		for (int i = 0; i < eigen.getVariantSize(); i++) {
			System.out.println(eigen.getVariant(i).getId() + "\t" + eigen.getVariant(i).getRefAllele() + "\t" + eigen.getVariant(i).getAltAlleles()[0]);
			for (int j = 0; j < eigen.getPopSize(); j++) {
				System.out.println(eigen.getPopId(j) + "\t" + eigen.getAlleleCount(i, j, 0) + "\t" + eigen.getAlleleCount(i, j, 1));
			}
		}
	}

}
