package xin.bio.popgen.datatype;

import java.util.HashMap;

import xin.bio.popgen.count.CountStrategy;

/**
 * The GeneticData class defines several variables and methods
 * for storing genetic data from different formats, such as PLINK PED/MAP, GEN/SAMPLE, HAPS/SAMPLE, EIGENSTRAT GENO/SNP/IND.
 * New file format can be implemented easily by inheriting this class.
 * 
 * See http://pngu.mgh.harvard.edu/~purcell/plink/data.shtml#ped 
 * 		for a detailed description of PLINK PED/MAP file format.
 * See http://www.stats.ox.ac.uk/~marchini/software/gwas/file_format.html 
 * 		for a detailed description of Oxford GEN/SAMPLE file format.
 * See https://mathgen.stats.ox.ac.uk/genetics_software/shapeit/shapeit.html#formats 
 * 		for a detailed description of HAPS/SAMPLE file format.
 * See http://genepath.med.harvard.edu/~reich/InputFileFormats.htm
 * 		for a detailed description of EIGENSTRAT GENO/SNP/IND file format.
 * 
 * @author Xin Huang
 *
 * 23 Mar 2016
 */
public class GeneticData {

	protected GeneticVariant[] variants;           // an array stores GeneticVariant data types.
	protected Individual[] inds;                   // an array stores information of each individual.
	protected HashMap<String, Integer> variantIndices; // a hash map converts a variant's ID into a integer index.
	protected HashMap<String, Integer> indIndices; // a hash map converts a individual's ID into a integer index.
	protected HashMap<String, Integer> popIndices; // a hash map converts a population's ID into a integer index.
	protected HashMap<Integer, String> popIds;     // a hash map converts a integer index into a population ID.
	protected int[][][] alleleCounts;              // an integer array stores allele counts, the first dimension represents SNPs.
	                                               // the second dimension represents populations.
	                                               // the third dimension represents the reference allele (0) and the alternative allele (1).
	protected double[][] varLogOdds;
	
	/**
	 * Returns the i-th individual in the data.
	 * @return the i-th individual in the data.
	 */
	public Individual getInd(int i) {
		return inds[i];
	}
	
	/**
	 * Returns the i-th variant in the data.
	 * @return the i-th variant in the data.
	 */
	public GeneticVariant getVariant(int i) {
		return variants[i];
	}
	
	/**
	 * Returns a variant with a given ID. 
	 * @param id  a variant's ID.
	 * @return    a variant with a given ID.
	 * 6 Apr 2016
	 */
	public GeneticVariant getVariant(String id) {
		return variants[variantIndices.get(id)];
	}
	
	public GeneticVariant[] getVariants() {
		return variants;
	}
	
	public HashMap<String, Integer> getVariantIndices() {
		return variantIndices;
	}
	
	public Individual[] getInds() {
		return inds;
	}
	
	public HashMap<String, Integer> getIndIndices() {
		return indIndices;
	}
	
	public HashMap<String, Integer> getPopIndices() {
		return popIndices;
	}
	
	public HashMap<Integer, String> getPopIds() {
		return popIds;
	}
	
	/**
	 * 
	 * @return
	 * 7 Apr 2016
	 */
	public int[][][] getAlleleCounts() {
		return alleleCounts;
	}
	
	/**
	 * Returns the k-th allele count in the i-th variant of the j-th population.
	 * @param i  the i-th SNP.
	 * @param j  the j-th population.
	 * @param k  the k-th allele. 0 means the reference allele, 1 means the alternative allele.
	 * @return   the the k-th allele count in the i-th variant of the j-th population.
	 */
	public int getAlleleCount(int i, int j, int k) {
		return alleleCounts[i][j][k];
	}
	
	/**
	 * Returns the number of variants in the data.
	 * @return the number of variants in the data.
	 */
	public int getVariantSize() {
		return variants.length;
	}
	
	/**
	 * Returns the number of populations in the data.
	 * @return the number of populations in the data.
	 */
	public int getPopSize() {
		return popIndices.keySet().size();
	}
	
	/**
	 * Returns the number of individuals in the data.
	 * @return the number of individuals in the data.
	 */
	public int getIndSize() {
		return inds.length;
	}
	
	/**
	 * Returns a variant's index in the data.
	 * @param id  a variant's ID.
	 * @return    a variant's index in the data.
	 * 23 Mar 2016
	 */
	public int getVariantIndex(String id) {
		return variantIndices.get(id);
	}
	
	public double getVarLogOdds(int i, int j) {
		return varLogOdds[i][j];
	}
	
	/**
	 * Returns the index of a population.
	 * @param id  a population ID
	 * @return    the index of a population.
	 */
	public int getPopIndex(String id) {
		return popIndices.get(id);
	}
	
	/**
	 * Returns a i-th population's ID in the data.
	 * @param i  the i-th population.
	 * @return   a i-th population's ID in the data.
	 */
	public String getPopId(int i) {
		return popIds.get(i);
	}
	
	/**
	 * Checks whether a variant's ID is in the data. 
	 * @param id  a variant's ID.
	 * @return    true, if the ID is in the data; false, otherwise.
	 * 5 Apr 2016
	 */
	public boolean containsVariant(String id) {
		return variantIndices.containsKey(id);
	}
	
	/**
	 * Checks whether a population's ID is in the data.
	 * @param id  a population's ID.
	 * @return    true, if the ID is in the data; false, otherwise.
	 * 5 Apr 2016
	 */
	public boolean containsPop(String id) {
		return popIndices.containsKey(id);
	}
	
	public void performCount(CountStrategy countStrategy) {
		countStrategy.count(this);
	}
	
	public void setVariants(GeneticVariant[] variants) {
		this.variants = variants;
	}

	public void setInds(Individual[] inds) {
		this.inds = inds;
	}

	public void setVariantIndices(HashMap<String, Integer> variantIndices) {
		this.variantIndices = variantIndices;
	}

	public void setIndIndices(HashMap<String, Integer> indIndices) {
		this.indIndices = indIndices;
	}

	public void setPopIndices(HashMap<String, Integer> popIndices) {
		this.popIndices = popIndices;
	}

	public void setPopIds(HashMap<Integer, String> popIds) {
		this.popIds = popIds;
	}

	public void setAlleleCounts(int[][][] alleleCounts) {
		this.alleleCounts = alleleCounts;
	}
	
	public void setVarLogOdds(double[][] varLogOdds) {
		this.varLogOdds = varLogOdds;
	}
	
}
