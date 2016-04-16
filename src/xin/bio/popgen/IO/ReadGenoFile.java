package xin.bio.popgen.IO;

import xin.bio.popgen.fileformat.GeneticVariant;


public class ReadGenoFile extends GenoFile implements Readable {
	
	public ReadGenoFile(String fileName, GeneticVariant[] variants) {
		this.variants = variants;
		readFile(fileName, 0);
	}

	@Override
	public void parseLine(int i, String line) {
		String genotypes = "";
		String[] elements = line.trim().split("");
		for (int j = 0; j < elements.length; j++) {
			if (elements[j].equals("0")) {
				genotypes = genotypes.concat("11");
			}
			else if (elements[j].equals("1")) {
				genotypes = genotypes.concat("01");
			}
			else if (elements[j].equals("2")) {
				genotypes = genotypes.concat("00");
			}
			else if (elements[j].equals("9")) {
				genotypes = genotypes.concat("99");
			}
		}
		variants[i++].setGenotypes(genotypes);
	}
	
}
