package xin.bio.popgen.count;

import java.util.HashMap;

import xin.bio.popgen.datatype.GeneticData;
import xin.bio.popgen.selediff.Model;

public class EstimateVarLogOdds implements CountStrategy {
	
	private HashMap<String, String[]> admixedPops;
	
	public EstimateVarLogOdds(HashMap<String, String[]> admixedPops) {
		this.admixedPops = admixedPops;
	}

	@Override
	public void count(GeneticData gd) {
		double[][] varLogOdds = new double[gd.getVariantSize()][gd.getPopSize()];
		int noAdmixPopSize = gd.getPopSize() - 2*admixedPops.keySet().size();
		for (int i = 0; i < gd.getVariantSize(); i++) {
			for (int j = 0; j < noAdmixPopSize; j++) {
				String descendant = gd.getPopId(j);
				if ((admixedPops != null) && (admixedPops.containsKey(descendant))) {
					String parentId1   = admixedPops.get(descendant)[0];
					String parentId2   = admixedPops.get(descendant)[1];
					String missingPopId1  = descendant + "(" + parentId1 + ")";
					String missingPopId2  = descendant + "(" + parentId2 + ")";
					double lambda      = Double.parseDouble(admixedPops.get(descendant)[2]);
					int descSampleSize = 0;
					int ancSampleSize1 = 0;
					int ancSampleSize2 = 0;
					
					int descIndex = gd.getPopIndex(descendant);
					int parentIndex1 = gd.getPopIndex(parentId1);
					int parentIndex2 = gd.getPopIndex(parentId2);
					
					descSampleSize = gd.getAlleleCount(i, descIndex, 0) + gd.getAlleleCount(i, descIndex, 1);
					ancSampleSize1 = gd.getAlleleCount(i, parentIndex1, 0) + gd.getAlleleCount(i, parentIndex1, 1);
					ancSampleSize2 = gd.getAlleleCount(i, parentIndex2, 0) + gd.getAlleleCount(i, parentIndex2, 1);
					
					int descAlleleCount = gd.getAlleleCount(i, descIndex, 0);
					int ancAlleleCount1 = gd.getAlleleCount(i, parentIndex1, 0);
					int ancAlleleCount2 = gd.getAlleleCount(i, parentIndex2, 0);
					//System.out.println(gd.getVariant(i).getId() + " " + descendant + " " + descAlleleCount + " " + descSampleSize);
					//System.out.println(gd.getVariant(i).getId() + " " + parentId1 + " " + ancAlleleCount1 + " " + ancSampleSize1);
					//System.out.println(gd.getVariant(i).getId() + " " + parentId2 + " " + ancAlleleCount2 + " " + ancSampleSize2);
					// miss parent 1
					varLogOdds[i][gd.getPopIndex(missingPopId1)] = Model.calAdmixedVarLogOdds(lambda, descAlleleCount, descSampleSize, ancAlleleCount2, ancSampleSize2);
					/*System.out.println(gd.getVariant(i).getId() + " " + missingPopId1 + " " 
							+ Model.calMissFreq(lambda, descAlleleCount, descSampleSize, ancAlleleCount2, ancSampleSize2) 
							+ " " + varLogOdds[i][gd.getPopIndex(missingPopId1)]);*/
					// miss parent 2
					varLogOdds[i][gd.getPopIndex(missingPopId2)] = Model.calAdmixedVarLogOdds(1-lambda, descAlleleCount, descSampleSize, ancAlleleCount1, ancSampleSize1);
					/*System.out.println(gd.getVariant(i).getId() + " " + missingPopId2 + " "
							+ Model.calMissFreq(1-lambda, descAlleleCount, descSampleSize, ancAlleleCount1, ancSampleSize1)
							+ " " + varLogOdds[i][gd.getPopIndex(missingPopId2)]);
					System.out.println();*/
				}
				
				if (gd.getAlleleCount(i, j, 0) + gd.getAlleleCount(i, j, 1) != 0) 
					varLogOdds[i][j] = 1/Model.correction(gd.getAlleleCount(i, j, 0)) + 1/Model.correction(gd.getAlleleCount(i, j, 1));
				else
					varLogOdds[i][j] = Double.NaN;
			}
		}
		
		gd.setVarLogOdds(varLogOdds);
	}

}
