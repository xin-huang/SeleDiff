package xin.bio.popgen.IO;

import xin.bio.popgen.fileformat.GeneticVariant;


public class ReadGenoFile extends GenoFile implements Readable {
	
	public ReadGenoFile(String fileName, GeneticVariant[] variants) {
		this.variants = variants;
		readFile(fileName, 0);
	}

	@Override
	public void parseLine(int i, String line) {
		StringBuilder genotypes = new StringBuilder();
		int length = line.length();
		for (int j = 0; j < length; j++) {
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
		}
		variants[i++].setGenotypes(genotypes.toString());
	}
	
}
