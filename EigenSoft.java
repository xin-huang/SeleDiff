package xin.bio.popgen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import edu.princeton.cs.algs4.LinkedQueue;

/**
 * This <tt>EigenSoft</tt> class stores genotype information,
 * individual information and SNP information using by EigenSoft, 
 * which is a popular software using in population genetics.
 * 
 * The genotype information is stored in a geno file with suffix .geno.
 * In the geno file, rows represent snps and columns represent individual.
 * 0 and 2 represent homozygotes; 1 represents heterozygotes; 9 represents missing alleles.
 * e.g.
 * 0001020001999220
 * 0101129919920000
 * 
 * The individual information is stored in an ind file with suffix .ind.
 * In the ind file, rows represent individuals. 
 * The first column is an ID of an individual. The second column is the gender
 * of an individual, where M is male and F is female. The third column is the 
 * population ID of an individual.
 * e.g.
 * Sample1 M Pop1
 * Sample2 F Pop2
 * 
 * The SNP information is stored in a snp file with suffix .snp.
 * In the snp file, rows represent snps.
 * The first column is an rsID of a snp. The second column is the reference allele
 * of a snp. The third column is the derived allele of a snp. The fourth column is 
 * the position of a snp.
 * e.g.
 * rsID1 0 1 100
 * rsID2 0 1 102
 * 
 * 
 * @author Xin Huang 2015-10-15 v1
 *
 */
public class EigenSoft {
	
	private String[] geno;                  // a string array stores genotypes of SNPs
	private String[] indId;                 // a string array stores each individual ID
	private HashMap<Integer, String> popId; // a hash table converts integers to population IDs
	private int[] popIdIndex;               // an integer array stores integers represent population IDs for individuals
	private Snp[] snps;                     // an array stores Snp data type
	private int indSize;                    // how many individuals are there in the sample
	private int popSize;                    // how many populations are there in the sample
	private int snpSize;                    // how many SNPs are there in the sample

	/**
	 * Initialize a <tt>EigenSoft</tt> data type with 
	 * the given geno file, ind file and snp file
	 * @param genoFileName the name of the geno file
	 * @param indFileName  the name of the ind file
	 * @param snpFileName  the name of the snp file
	 */
	public EigenSoft(String genoFileName, String indFileName, String snpFileName) {
		readGenoFile(genoFileName);
		readIndFile(indFileName);
		readSnpFile(snpFileName);
	}
	
	/**
	 * Read the geno file
	 * @param genoFileName the name of the geno file
	 */
	private void readGenoFile(String genoFileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(genoFileName));
			LinkedQueue<String> genoQueue = new LinkedQueue<String>();
			String line = null;
			while ((line = br.readLine()) != null) {
				genoQueue.enqueue(line.trim());
			}
			br.close();
			geno = new String[genoQueue.size()];
			int i = 0;
			for (String s:genoQueue) {
				geno[i++] = s;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Read the ind file
	 * @param indFileName the name of the ind file
	 */
	private void readIndFile(String indFileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(indFileName));
			LinkedQueue<String> indIdQueue = new LinkedQueue<String>();
			LinkedQueue<String> popIdQueue = new LinkedQueue<String>();
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] elements = line.trim().split("\\s+");
				indIdQueue.enqueue(elements[0]);
				popIdQueue.enqueue(elements[2]);
			}
			br.close();
			indSize = indIdQueue.size();
			indId = new String[indSize];
			int i = 0;
			for (String s:indIdQueue) {
				indId[i++] = s;
			}
			HashMap<String, Integer> popIdMap = new HashMap<String, Integer>();
			i = 0;
			for (String s:popIdQueue) {
				if (!popIdMap.containsKey(s)) {
					popIdMap.put(s, i++);
				}
			}
			popSize = popIdMap.keySet().size();
			popId = new HashMap<Integer, String>();
			for (Iterator<String> iter = popIdMap.keySet().iterator(); iter.hasNext();) {
				String id = iter.next();
				popId.put(popIdMap.get(id), id);
			}
			popIdIndex = new int[indSize];
			i = 0;
			for (String s:popIdQueue) {
				popIdIndex[i++] = popIdMap.get(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Read the snp file
	 * @param snpFileName the name of the snp file
	 */
	private void readSnpFile(String snpFileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(snpFileName));
			String line = null;
			LinkedQueue<Snp> snpQueue = new LinkedQueue<Snp>();
			while ((line = br.readLine()) != null) {
				String[] elements = line.trim().split("\\s+");
				snpQueue.enqueue(new Snp(elements[0], elements[1], elements[2], elements[3]));
			}
			br.close();
			snpSize = snpQueue.size();
			snps = new Snp[snpSize];
			int i = 0;
			for (Snp s:snpQueue) {
				snps[i++] = s;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Return the String array of genotypes
	 * @return the String array of genotypes
	 */
	public String[] getGeno() {
		return geno;
	}
	
	/**
	 * Return the individual ID with the given index i
	 * @param i the given index
	 * @return the individual ID with the given index i
	 */
	public String getIndId(int i) {
		validate(i,0);
		return indId[i];
	}
	
	/**
	 * Return the population ID with the i-th population
	 * @param i the i-th population
	 * @return the population ID with the i-th population
	 */
	public String getPopId(int i) {
		validate(i,0);
		return popId.get(i);
	}
	
	/**
	 * Return the integer array stores integers represent population IDs for individuals
	 * @return the integer array stores integers represent population IDs for individuals
	 */
	public int[] getPopIdIndex() {
		return popIdIndex;
	}
	
	/**
	 * Return the Snp data type with the given index i
	 * @param i the given index
	 * @return the Snp data type with the given index i
	 */
	public Snp getSnp(int i) {
		validate(i,1);
		return snps[i];
	}
	
	/**
	 * Return the size of individuals in the sample
	 * @return the size of individuals in the sample
	 */
	public int getIndSize() {
		return indSize;
	}
	
	/**
	 * Return the size of populations in the sample
	 * @return the size of populations in the sample
	 */
	public int getPopSize() {
		return popSize;
	}
	
	/**
	 * Return the size of SNPs in the sample
	 * @return the size of SNPs in the sample
	 */
	public int getSnpSize() {
		return snpSize;
	}
	
	/**
	 * Validate whether the given index i is available or not
	 * @param i the given index
	 * @param j 0 represents checking the population IDs; 1 represents checking the SNPs
	 * @throws IllegalArgumentException if i >= popSize || i >= snpSize
	 */
	private void validate(int i, int j) {
		if (j == 0) {
			if (i >= indSize) throw new IllegalArgumentException("Individual index i should be less than " + indSize);
		}
		else if (j == 1) {
			if (i >= snpSize) throw new IllegalArgumentException("Snp index i should be less than " + snpSize);
		}
	}
	
	/**
	 * Unit tests the <tt>EigenSoft<tt> data type 
	 */
	public static void main(String[] args) {
		String genoFileName = args[0];
		String indFileName = args[1];
		String snpFileName = args[2];
		EigenSoft eigen = new EigenSoft(genoFileName, indFileName, snpFileName);
		String[] geno = eigen.getGeno();
		System.out.println(geno[0]);
		System.out.println(eigen.getIndId(0));
		System.out.println(eigen.getPopId(1));
		System.out.println(eigen.getPopIdIndex()[200]);
		System.out.println(eigen.getPopSize());
		System.out.println(eigen.getSnpSize());
		System.out.println(eigen.getSnp(7788));
	}

}
