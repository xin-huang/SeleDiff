package xin.bio.popgen.count;

import java.util.HashMap;

import xin.bio.popgen.datatype.GeneticData;
import xin.bio.popgen.selediff.Model;

public class CountAdmixedPopsAllele implements CountStrategy {
	
	private HashMap<String, String[]> admixedPops;
	
	public CountAdmixedPopsAllele(HashMap<String, String[]> admixedPops) {
		this.admixedPops = admixedPops;
	}

	@Override
	public void count(GeneticData gd) {
		int variantSize = gd.getVariantSize();
		
		// update population IDs and population indices
		int popSize = gd.getPopSize();
		int noAdmixPopSize = gd.getPopSize();
		HashMap<String, Integer> popIndices = gd.getPopIndices();
		HashMap<Integer, String> popIds = gd.getPopIds();
		for (String descendant:admixedPops.keySet()) {
			String missingPopId1 = descendant+"("+admixedPops.get(descendant)[0]+")";
			String missingPopId2 = descendant+"("+admixedPops.get(descendant)[1]+")";
			popIndices.put(missingPopId1, popSize);
			popIds.put(popSize++, missingPopId1);
			popIndices.put(missingPopId2, popSize);
			popIds.put(popSize++, missingPopId2);
		}
		gd.setPopIds(popIds);
		gd.setPopIndices(popIndices);
		
		// update allele counts
		int[][][] alleleCounts = gd.getAlleleCounts();
		int[][][] admixedAlleleCounts = new int[variantSize][popSize][];
		for (int i = 0; i < variantSize; i++) {
			for (int j = 0; j < noAdmixPopSize; j++) {
				String descendant = gd.getPopId(j);
				if (admixedPops.containsKey(descendant)) {
					String parentId1   = admixedPops.get(descendant)[0];
					String parentId2   = admixedPops.get(descendant)[1];
					String missingPopId1  = descendant + "(" + parentId1 + ")";
					String missingPopId2  = descendant + "(" + parentId2 + ")";
					double lambda      = Double.parseDouble(admixedPops.get(descendant)[2]);
					int descSampleSize = 0;
					int ancSampleSize1 = 0;
					int ancSampleSize2 = 0;
					
					int alleleSize = alleleCounts[i][j].length;
					int[] count1 = new int[alleleSize];
					int[] count2 = new int[alleleSize];
					int descIndex = gd.getPopIndex(descendant);
					int parentIndex1 = gd.getPopIndex(parentId1);
					int parentIndex2 = gd.getPopIndex(parentId2);
					
					for (int p = 0; p < alleleSize; p++) {
						descSampleSize += alleleCounts[i][descIndex][p];
						ancSampleSize1 += alleleCounts[i][parentIndex1][p];
						ancSampleSize2 += alleleCounts[i][parentIndex2][p];
					}
					
					for (int p = 0; p < alleleSize; p++) {
						int descAlleleCount = alleleCounts[i][descIndex][p];
						int ancAlleleCount1 = alleleCounts[i][parentIndex1][p];
						int ancAlleleCount2 = alleleCounts[i][parentIndex2][p];
						// assume sample size is 1000
						// miss parent 1
						count1[p] = (int) Math.round(1000*Model.calMissFreq(lambda, descAlleleCount, descSampleSize, ancAlleleCount2, ancSampleSize2));
						// miss parent 2
						count2[p] = (int) Math.round(1000*Model.calMissFreq(1 - lambda, descAlleleCount, descSampleSize, ancAlleleCount1, ancSampleSize1));
					}
					admixedAlleleCounts[i][gd.getPopIndex(missingPopId1)] = count1;
					admixedAlleleCounts[i][gd.getPopIndex(missingPopId2)] = count2;
				}
				
				admixedAlleleCounts[i][j] = alleleCounts[i][j];
				
			}
		}
		gd.setAlleleCounts(admixedAlleleCounts);
	}

}
