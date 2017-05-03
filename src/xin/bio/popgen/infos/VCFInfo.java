/*
  Copyright (C) 2017 Xin Huang

  This file is part of SeleDiff.

  SeleDiff is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version

  SeleDiff is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package xin.bio.popgen.infos;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Class {@code VcfInfo} stores variant information of the sample.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class VCFInfo implements Info {

    // a SampleInfo instance stores sample information
    private final SampleInfo sampleInfo;

    // a HashMap stores allele counts of each variant in different populations
    // key: SNP ID
    // value: an integer array, the first dimension is the population index
    // the second dimension is the allele: 0, reference allele; 1, alternative allele
    private final HashMap<String, int[][]> counts;

    // a HashMap stores reference alleles of each variant
    // key: SNP ID
    // value: reference allele, SeleDiff assumes reference alleles are in the forward strand of the reference genome
    private final HashMap<String, String> refAlleles;

    // a HashMap stores alternative alleles of each variant
    // key: SNP ID
    // value: alternative allele, SeleDiff assumes alternative alleles are in the forward strand of the reference genome
    private final HashMap<String, String> altAlleles;

    // a HashMap stores ancestral alleles of each variant
    // key: SNP ID
    // value: ancestral allele, SeleDiff assumes ancestral alleles are in the forward strand of the reference genome
    private final HashMap<String, String> ancAlleles;

    // a HashMap stores derived alleles of each variant
    // key: SNP ID
    // value: derived allele, SeleDiff assumes derived alleles are in the forward strand of the reference genome
    private final HashMap<String, String> derAlleles;

    // an integer stores how many variants in the sample
    private final int snpNum;

    // an integer stores how many individuals in the sample
    private final int sampleIndNum;

    // an integer stores how many populations in the sample
    private final int samplePopNum;

    // a String array stores individual IDs in the VCF file
    private String[] indIds;

    /**
     * Constructor of class {@code VcfInfo}.
     *
     * @param vcfFileName the file name of a VCF file
     * @param sampleInfo a SampleInfo instance containing sample information
     */
    public VCFInfo(String vcfFileName, SampleInfo sampleInfo) {
        this.sampleInfo = sampleInfo;
        counts = new HashMap<>();
        refAlleles = new HashMap<>();
        altAlleles = new HashMap<>();
        ancAlleles = new HashMap<>();
        derAlleles = new HashMap<>();
        sampleIndNum = sampleInfo.getIndNum();
        samplePopNum = sampleInfo.getPopNum();
        readFile(vcfFileName);
        snpNum = refAlleles.keySet().size();

        System.out.println(snpNum + " variants are read from " + vcfFileName);
    }

    /**
     * Returns an ancestral allele with a given SNP ID.
     *
     * @param snpId a SNP ID
     * @return an ancestral allele
     */
    public String getAncAllele(String snpId) { return ancAlleles.get(snpId); }

    /**
     * Sets the ancestral allele of a given SNP ID.
     *
     * @param snpId a SNP ID
     * @param ancAllele an ancestral allele
     */
    void setAncAllele(String snpId, String ancAllele) { ancAlleles.put(snpId, ancAllele); }

    /**
     * Returns a derived allele with a given SNP ID.
     *
     * @param snpId a SNP ID
     * @return a derived allele
     */
    public String getDerAllele(String snpId) { return derAlleles.get(snpId); }

    /**
     * Sets the derived allele of a given SNP ID.
     *
     * @param snpId a SNP ID
     * @param derAllele a derived allele
     */
    void setDerAllele(String snpId, String derAllele) { derAlleles.put(snpId, derAllele); }

    /**
     * Returns an integer array stores allele counts for a given SNP in different populations.
     *
     * @return an integer array containing allele counts for each SNP
     */
    public int[][] getCounts(String snpId) { return counts.get(snpId); }

    /**
     * Returns a reference allele with a given SNP ID.
     *
     * @param snpId a SNP ID
     * @return a reference allele
     */
    String getRefAllele(String snpId) { return refAlleles.get(snpId); }

    /**
     * Returns an alternative allele with a given SNP ID.
     *
     * @param snpId a SNP ID
     * @return an alternative allele
     */
    String getAltAllele(String snpId) { return altAlleles.get(snpId); }

    /**
     * Returns SNP IDs in the sample.
     *
     * @return SNP IDs in the sample
     */
    public Set<String> getSnps() { return refAlleles.keySet(); }

    /**
     * Checks whether a SNP is in the sample or not.
     *
     * @param snpId a SNP ID
     * @return true, the SNP is in; otherwise, false
     */
    boolean containsSnp(String snpId) { return refAlleles.containsKey(snpId); }

    /**
     * Returns how many variants in the sample.
     *
     * @return how many variants in the sample
     */
    public int getSnpNum() { return snpNum; }

    /**
     * Returns allele count of a given SNP ID in a given population
     *
     * @param snpId a SNP ID
     * @param popIndex a population index
     * @param allele an allele: 0, the reference allele; 1, the alternative allele.
     * @return allele count of a given SNP ID in a given population
     */
    public int getAlleleCount(String snpId, int popIndex, int allele) { return counts.get(snpId)[popIndex][allele]; }

    @Override
    public void parseLine(String line) {
    	if (line.startsWith("##")) return;
    	else if (line.startsWith("#C")) {
    		StringTokenizer st = new StringTokenizer(line.trim());
            for (int i = 0; i < 9; i++) {
            	st.nextToken();
            }
            indIds = new String[sampleIndNum];
            int vcfIndNum = 0;
            while (st.hasMoreTokens()) {
            		indIds[vcfIndNum++] = st.nextToken();
            }
    	}
		else { 
			StringTokenizer st = new StringTokenizer(line.trim());
			st.nextToken();
			String snpId = st.nextToken();
			refAlleles.put(snpId, st.nextToken());
            altAlleles.put(snpId, st.nextToken());
            ancAlleles.put(snpId, st.nextToken());
            derAlleles.put(snpId, st.nextToken());
			for (int i = 6; i < 9; i++) {
				st.nextToken();
			}
			int[][] alleleCounts = new int[samplePopNum][2];
			for (int i = 9; i < sampleIndNum + 9; i++) {
				int popIndex = sampleInfo.getPopIndex(sampleInfo.getPopId(indIds[i-9]));
				String gt = new StringTokenizer(st.nextToken(),":").nextToken();
				int allele1 = gt.charAt(0)-48;
				int allele2 = gt.charAt(2)-48;
				if (allele1 >= 0)
					alleleCounts[popIndex][allele1]++;
				if (allele2 >= 0)
					alleleCounts[popIndex][allele2]++;
			}
			counts.put(snpId, alleleCounts);
		}
    	/*if (line.startsWith("##")) {
    		return;
    	}
    	else if (line.startsWith("#C")) {
            String[] elements = line.trim().split("\\s+");
            int vcfIndNum = elements.length - 9;
            if (vcfIndNum != sampleIndNum)
                throw new IllegalArgumentException("Individual number NOT matched: Find "
                        + vcfIndNum + " individuals in VCF file, while find "
                        + sampleIndNum + " individuals in sample file.");
            indIds = new String[sampleIndNum];
            indIds = Arrays.copyOfRange(elements, 9, elements.length);
        }
        else {
            String[] elements = line.trim().split("\\s+");
            String snpId = elements[2];
            refAlleles.put(snpId, elements[3]);
            altAlleles.put(snpId, elements[4]);
            ancAlleles.put(snpId, elements[3]);
            derAlleles.put(snpId, elements[4]);
            int[][] alleleCounts = new int[samplePopNum][2];
            for (int i = 9; i < elements.length; i++) {
            	int popIndex = sampleInfo.getPopIndex(sampleInfo.getPopId(indIds[i-9]));
                String gt = elements[i].split(":")[0];
                String[] alleles = gt.split("[|/]");
                if (!alleles[0].equals("."))
                    alleleCounts[popIndex][Integer.parseInt(alleles[0])]++;
                if (!alleles[1].equals("."))
                    alleleCounts[popIndex][Integer.parseInt(alleles[1])]++;
            }
            counts.put(snpId, alleleCounts);
        }*/
    }

}
