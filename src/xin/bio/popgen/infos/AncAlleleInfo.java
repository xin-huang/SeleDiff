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

/**
 * Class {@code AncAlleleInfo} stores the information of ancestral alleles
 * of variants in the sample.
 *
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 */
public final class AncAlleleInfo implements Info {

    // a VcfInfo instance stores variant information
    private final VcfInfo vcfInfo;

    /**
     * Constructor of class {@code AncAlleleInfo}.
     * A ancestral allele file is space delimited without header,
     * where the first column is the SNP ID, and the second column
     * is the ancestral allele. SeleDiff assumes these alleles are
     * in the forward strand of the reference genome.
     *
     * @param ancFileName the file name of an ancestral allele file
     * @param vcfInfo a VcfInfo instance containing variant information
     */
    public AncAlleleInfo(String ancFileName, VcfInfo vcfInfo) {
        this.vcfInfo = vcfInfo;
        readFile(ancFileName);
    }

    @Override
    public void parseLine(String line) {
        String[] elements = line.trim().split("\\s+");
        String snpId = elements[0];
        String ancAllele = elements[1];
        if (vcfInfo.containsSnp(snpId)) {
            vcfInfo.setAncAllele(snpId, ancAllele);
            if (ancAllele.equals(vcfInfo.getRefAllele(snpId))) {
                System.out.println("The ancestral allele of " + snpId + " is the alternative allele in the VCF file");
                vcfInfo.setDerAllele(snpId, vcfInfo.getAltAllele(snpId));
            }
            else {
                swapCounts(vcfInfo.getCounts(snpId));
                vcfInfo.setDerAllele(snpId, vcfInfo.getRefAllele(snpId));
            }
        }
        else {
            System.out.println(snpId + " is not in the VCF file");
        }
    }

    /**
     * Helper function for swapping allele counts for a variant
     *
     * @param counts an integer array containing allele counts of a variant
     */
    private void swapCounts(int[][] counts) {
        for (int i = 0; i < counts.length; i++) {
            int tmp = counts[i][0];
            counts[i][0] = counts[i][1];
            counts[i][1] = tmp;
        }
    }

}
