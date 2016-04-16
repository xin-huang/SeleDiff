package xin.bio.popgen.IO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import xin.bio.popgen.fileformat.GeneticData;
import xin.bio.popgen.fileformat.GeneticVariant;

/**
 * The Input class is used for reading input files.
 * @author Xin Huang
 *
 * 29 Mar 2016
 */
public class Input {
	
	/**
	 * Help function for reading the file contains pairwise population divergence time.
	 * @param divergenceTimeFileName  the file contains pairwise population divergence time.
	 * @return a hash map contains pairwise population divergence time, where keys are population IDs and values are divergence times. 
	 * 22 Mar 2016
	 */
	public static HashMap<String, Double> readDivergenceTimeFile(String divergenceTimeFileName) {
		HashMap<String, Double> divergenceTimes = new HashMap<String, Double>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(divergenceTimeFileName));
			String line = null;
			br.readLine(); // remove the header
			while ((line = br.readLine()) != null) {
				String[] elements = line.trim().split("\\s+");
				String key1 = elements[0] + '_' + elements[1];
				String key2 = elements[1] + '_' + elements[0];
				double value = Double.parseDouble(elements[2]);
				divergenceTimes.put(key1, value);
				divergenceTimes.put(key2, value);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return divergenceTimes;
	}
	
	/**
	 * Help function for reading the file contains ancestral allele information.
	 * @param ancAlleleFileName  the file contains ancestral allele information.
	 * @param genetic            a genetic data type contains genetic data.
	 * 24 Mar 2016
	 */
	public static void readAncAlleleFile(String ancAlleleFileName, GeneticData genetic) {
		HashMap<String, String> complementaryAllele = new HashMap<String, String>();
		complementaryAllele.put("A", "T");
		complementaryAllele.put("T", "A");
		complementaryAllele.put("C", "G");
		complementaryAllele.put("G", "C");
		System.out.println("Reading  " + ancAlleleFileName + "  ...");
		try {
			BufferedReader br = new BufferedReader(new FileReader(ancAlleleFileName));
			String line = null;
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] elements = line.trim().split("\\s+");
				String snpId     = elements[0];
				String ancAllele = elements[1];
				// System.out.println(snpId);
				if (!genetic.containsVariant(snpId)) {
					continue; 
				}
				else {
					GeneticVariant snp = genetic.getVariant(snpId);
					if (snp.getRefAllele().equals(ancAllele)) {
						// if the reference allele is the ancestral allele
						snp.setAncAllele(ancAllele);
						// snp.setDerAllele(snp.getAltAllele());
						continue;
					}
					else if (complementaryAllele.get(snp.getRefAllele()).equals(ancAllele)) {
						// if the reference allele is the ancestral allele
						System.out.println("The ancestral allele of SNP ID " + snp.getId() + " is " + ancAllele
								+ ", which is complement of the reference allele " + snp.getRefAllele());
						snp.setAncAllele(complementaryAllele.get(ancAllele));
						// snp.setDerAllele(snp.getAltAllele());
						continue;
					}
					else if (snp.getAltAlleles()[0].equals(ancAllele)) {
						// if the alternative allele is the ancestral allele
						// then exchange the allele counts to make sure
						// 0 represents ancestral allele
						System.out.println("The ancestral allele of SNP ID " + snp.getId() + " is " + ancAllele 
								+ ", which is the non-reference allele " + snp.getAltAlleles()[0]);
						snp.setAncAllele(ancAllele);
						// snp.setDerAllele(snp.getRefAllele());
						// snp.setAncAllele(snp.getAltAllele());
						// snp.setDerAllele(ancAllele);
					}
					else if (complementaryAllele.get(snp.getAltAlleles()[0]).equals(ancAllele)) {
						// if the alternative allele is the ancestral allele
						// then exchange the allele counts to make sure
						// 0 represents ancestral allele
						System.out.println("The ancestral allele of SNP ID " + snp.getId() + " is " + ancAllele 
								+ ", which is the complement of the non-reference allele " + snp.getAltAlleles()[0]);
						snp.setAncAllele(complementaryAllele.get(ancAllele));
						// snp.setDerAllele(snp.getRefAllele());
						// snp.setAncAllele(complementaryAllele.get(snp.getAltAllele()));
						// snp.setDerAllele(ancAllele);
					}
					else {
						System.out.println(snp.getId() + "\tmismatches ancestral allele" + ancAllele 
								+ "\treference allele: " + snp.getRefAllele()
								+ "\talternative allele: " + snp.getAltAlleles()[0]
								+ ", using the reference allele as the ancestral allele");
					}
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
}
