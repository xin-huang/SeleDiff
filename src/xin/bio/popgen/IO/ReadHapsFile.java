package xin.bio.popgen.IO;

import xin.bio.popgen.fileformat.Snp;

public class ReadHapsFile extends SnpFile implements Readable {
	
	public ReadHapsFile(String fileName) {
		initializes();
		readFile(fileName, 0);
		parseData();
	}

	@Override
	public void parseLine(int i, String line) {
		String[] elements = line.trim().split("\\s+");
		String chr = elements[0];
		String id  = elements[1];
		int    pos = Integer.parseInt(elements[2]);
		String ref = elements[3];
		String alt = elements[4];
		String genotypes = "";
		for (int j = 5; j < elements.length; j++) {
			genotypes = genotypes.concat(elements[j]);
		}
		snpQueue.enqueue(new Snp(id, chr, 0, pos, ref, null, new String[] {ref, alt}, genotypes));
	}

}
