package xin.bio.popgen.read;

import xin.bio.popgen.datatype.GeneticData;

public class ReadGenoFile extends Readable {
	
	private GeneticData gd;
	
	public ReadGenoFile(String fileName, GeneticData gd) {
		this.gd = gd;
		readFile(fileName, 0);
		int i = 0;
		for (String line:lines) {
			parseLine(i++, line);
		}
	}

	@Override
	protected void parseLine(int i, String line) {
		if (line.length() != gd.getIndSize()) {
			throw new IllegalArgumentException("Individual number is not consistent in GENO file and IND file (find " 
					+ line.length() + " individuals in the " + i + "th row of GENO file, while " + gd.getIndSize() 
					+ " individuals in IND file).");
		}
		StringBuilder genotypes = new StringBuilder();
		for (int j = 0; j < gd.getIndSize(); j++) {
			if (line.charAt(j) == '0') {
				genotypes.append("11");
			}
			else if (line.charAt(j) == '1') {
				genotypes.append("01");
			}
			else if (line.charAt(j) == '2') {
				genotypes.append("00");
			}
			else if (line.charAt(j) == '9') {
				genotypes.append("99");
			}
			else {
				throw new IllegalArgumentException("No supported genotype code for EIGENSTRAT format (find " 
						+ line.charAt(j) + " at" + (j+1) + "-th indiviudal).");
			}
		}
		gd.getVariant(i++).setGenotypes(genotypes.toString());
	}

	@Override
	protected void passData(GeneticData gd) {
	}

}
