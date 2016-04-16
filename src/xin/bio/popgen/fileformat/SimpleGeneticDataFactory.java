package xin.bio.popgen.fileformat;

import java.util.List;

public class SimpleGeneticDataFactory {

	public static GeneticData create(List<String> fileNames, String format, 
			double threshold) {
		if (format.equalsIgnoreCase("eigenstrat"))
			return new EigenStrat(fileNames.get(0), fileNames.get(1), fileNames.get(2));
		else if (format.equalsIgnoreCase("gwas"))
			return new Gwas(fileNames.get(0), fileNames.get(1), threshold);
		else if (format.equalsIgnoreCase("haps"))
			return new Haps(fileNames.get(0), fileNames.get(1));
		return null;
	}
	
}

