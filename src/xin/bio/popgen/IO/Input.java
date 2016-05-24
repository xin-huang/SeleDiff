package xin.bio.popgen.IO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import xin.bio.popgen.datatype.GeneticData;
import xin.bio.popgen.datatype.GeneticVariant;
import xin.bio.popgen.read.ReadEigenStratFactory;
import xin.bio.popgen.read.ReadHapsFactory;
import xin.bio.popgen.read.ReadOxfordFactory;
import xin.bio.popgen.selediff.Model;


/**
 * The Input class is used for reading input files.
 * @author Xin Huang
 *
 * 29 Mar 2016
 */
public class Input {
	
	public static GeneticData create(List<String> fileNames, String format, double threshold) {
		GeneticData gd = new GeneticData();
		if (format.equalsIgnoreCase("eigenstrat")) {
			new ReadEigenStratFactory(fileNames, gd);
			return gd;
		}
		else if (format.equalsIgnoreCase("oxford")) {
			new ReadOxfordFactory(fileNames, gd, threshold);
			return gd;
		}
		else if (format.equalsIgnoreCase("haps")) {
			new ReadHapsFactory(fileNames, gd);
			return gd;
		}
		return null;
	}
	
	/**
	 * Help function for reading the file contains pairwise population divergence time.
	 * @param divergenceTimeFileName  the file contains pairwise population divergence time.
	 * @return a hash map contains pairwise population divergence time, where keys are population IDs and values are divergence times. 
	 * 22 Mar 2016
	 */
	public static HashMap<String, Double> readDivergenceTimeFile(String divergenceTimeFileName) {
		System.out.println("Reading " + divergenceTimeFileName + " ... ");
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
	public static void readAncAlleleFile(String ancAlleleFileName, GeneticData all, GeneticData candidate) {
		System.out.println("Reading  " + ancAlleleFileName + "  ...");
		HashMap<String, String> ancAlleles = new HashMap<String, String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(ancAlleleFileName));
			String line = null;
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] elements = line.trim().split("\\s+");
				String snpId     = elements[0];
				String ancAllele = elements[1];
				// System.out.println(snpId);
				if (all.containsVariant(snpId) || candidate.containsVariant(snpId)) {
					ancAlleles.put(snpId, ancAllele);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		alignAncSnp(ancAlleles, all);
		alignAncSnp(ancAlleles, candidate);
		System.out.println();
	}
	
	private static void alignAncSnp(HashMap<String, String> ancAlleles, GeneticData gd) {
		HashMap<String, String> complementaryAllele = new HashMap<String, String>();
		complementaryAllele.put("A", "T");
		complementaryAllele.put("T", "A");
		complementaryAllele.put("C", "G");
		complementaryAllele.put("G", "C");
		
		for (int i = 0; i < gd.getVariantSize(); i++) {
			GeneticVariant snp = gd.getVariant(i);
			String ancAllele = ancAlleles.get(snp.getId());
			String snpRefAllele = snp.getRefAllele();
			String snpAltAllele = snp.getAltAlleles()[0];
			if (snpRefAllele.equals(complementaryAllele.get(snpAltAllele))) {
				System.out.println("WARNING: SNP ID " + snp.getId() 
						+ "'s reference allele " + snpRefAllele 
						+ " is the complement of its alternative allele " + snpAltAllele);
			}
			if (snpRefAllele.equals(ancAllele)) {
				// if the reference allele is the ancestral allele
				snp.setAncAllele(ancAllele);
				continue;
			}
			else if (complementaryAllele.get(snpRefAllele).equals(ancAllele)) {
				// if the reference allele is the ancestral allele
				System.out.println("The ancestral allele of SNP ID " + snp.getId() + " is " + ancAllele
						+ ", which is complement of the reference allele " + snpRefAllele);
				snp.setAncAllele(complementaryAllele.get(ancAllele));
				continue;
			}
			else if (snpAltAllele.equals(ancAllele)) {
				// if the alternative allele is the ancestral allele
				// then exchange the allele counts to make sure
				// 0 represents ancestral allele
				System.out.println("The ancestral allele of SNP ID " + snp.getId() + " is " + ancAllele 
						+ ", which is the alternative allele " + snpAltAllele);
				snp.setAncAllele(ancAllele);
			}
			else if (complementaryAllele.get(snpAltAllele).equals(ancAllele)) {
				// if the alternative allele is the ancestral allele
				// then exchange the allele counts to make sure
				// 0 represents ancestral allele
				System.out.println("The ancestral allele of SNP ID " + snp.getId() + " is " + ancAllele 
						+ ", which is the complement of the alternative allele " + snpAltAllele);
				snp.setAncAllele(complementaryAllele.get(ancAllele));
			}
			else {
				System.out.println("The ancestral allele of SNP ID " + snp.getId() 
						+ " is " + ancAllele + ", however, it can't match" 
						+ " the reference allele " + snpRefAllele
						+ " or the alternative allele " + snpAltAllele
						+ ", using the reference allele as the ancestral allele");
				snp.setAncAllele(snpRefAllele);
			}
		}
	}
	
	/**
	 * Hepler function for estimating the admixed proportion of given pairs of admixed populations
	 * @param eigensoft      an EigenSoft class storing data
	 * @param seleDiff       a SelectionDiff class for analyzing data
	 * @param outputFileName the output file name
	 */
	public static HashMap<String, String[]> estimateAdmixedProportion(GeneticData genetic, String admixedFileName) {
		System.out.println("Reading  " + admixedFileName + " ...");
		int variantSize = genetic.getVariantSize();
		HashMap<String, String[]> admixedPops = new HashMap<String, String[]>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(admixedFileName));
			br.readLine();
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] elements = line.split("\\s+");
				int popi = genetic.getPopIndex(elements[0]);
				int popj = genetic.getPopIndex(elements[1]);
				int descendant = genetic.getPopIndex(elements[2]);
				double proportion = 0;
				int count = 0;
				
				for (int s = 0; s < variantSize; s++) {
					if ((genetic.getAlleleCount(s, popi, 0) + genetic.getAlleleCount(s, popi, 1) == 0) 
							|| (genetic.getAlleleCount(s, popj, 0) + genetic.getAlleleCount(s, popj, 1) == 0)
							|| (genetic.getAlleleCount(s, descendant, 0) + genetic.getAlleleCount(s, descendant, 1) == 0)) {
					}
					else {
						/*int ci = genetic.getAlleleCount(s, popi, 0);
						int ti = genetic.getAlleleCount(s, popi, 0) + genetic.getAlleleCount(s, popi, 1);
						int cj = genetic.getAlleleCount(s, popj, 0);
						int tj = genetic.getAlleleCount(s, popj, 0) + genetic.getAlleleCount(s, popj, 1);
						int cd = genetic.getAlleleCount(s, descendant, 0);
						int td = genetic.getAlleleCount(s, descendant, 0) + genetic.getAlleleCount(s, descendant, 1);
						
						if (ci*tj-cj*ti != 0) {
							double tmp = (double) ti*(cd*tj - cj*td) / (td*(ci*tj - cj*ti));
							proportion += tmp;
							System.out.print("admixture " + genetic.getVariant(s).getId() + " ");
							System.out.print(ci + " " + ti + " " + cj + " " + tj + " " + cd + " " + td + " ");
							System.out.print(tmp + " ");
							//System.out.println((cd/td - cj/tj)/(ci/ti - cj/tj));
							count++;
						}*/
						double fpopi = (double) genetic.getAlleleCount(s, popi, 0) 
								/ (genetic.getAlleleCount(s, popi, 0) + genetic.getAlleleCount(s, popi, 1));
						double fpopj = (double) genetic.getAlleleCount(s, popj, 0)
								/ (genetic.getAlleleCount(s, popj, 0) + genetic.getAlleleCount(s, popj, 1));
						double fdesc = (double) genetic.getAlleleCount(s, descendant, 0)
								/ (genetic.getAlleleCount(s, descendant, 0) + genetic.getAlleleCount(s, descendant, 1));
						if (Math.abs(fpopi - fpopj) > 1e-3) {
							double freq = (fdesc - fpopj) / (fpopi - fpopj);
							//System.out.println(freq);
							proportion += Model.round(freq);
							count++;
						}
					}
				}
				
				double lambda = proportion / count;
				// System.out.println(lambda);
				admixedPops.put(elements[2], new String[] {elements[0], elements[1], String.valueOf(Model.round(lambda))});
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(admixedPops.keySet().size() + " admixed populations are read");
		for (String desc:admixedPops.keySet()) {
			System.out.println("The admixture proportion of " + desc + " from " 
								+ admixedPops.get(desc)[0] + " is " + admixedPops.get(desc)[2]);
		}
		System.out.println();
		
		return admixedPops;
	}
	
}
