package xin.bio.popgen.IO;

import xin.bio.popgen.fileformat.Snp;

public class ReadSnpFile extends SnpFile implements Readable {
	
	public ReadSnpFile(String fileName) {
		initializes();
		readFile(fileName, 0);
		parseData();
	}

	@Override
	public void parseLine(int i, String line) {
		String[] elements = line.trim().split("\\s+");
		String     id = elements[0];
		String    chr = elements[1];
		double genPos = Double.parseDouble(elements[2]);
		int    phyPos = Integer.parseInt(elements[3]);
		String    ref = elements[4];
		String    alt = elements[5];
		Snp s = new Snp(id, chr, genPos, phyPos, ref, null, new String[] {ref, alt}, "");
		snpQueue.enqueue(s);
	}
	
}
