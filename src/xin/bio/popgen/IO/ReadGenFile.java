package xin.bio.popgen.IO;

import xin.bio.popgen.fileformat.Snp;

public class ReadGenFile extends SnpFile implements Readable {
	
	private double threshold;
	
	public ReadGenFile(String fileName, double threshold) {
		this.threshold = threshold;
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
		StringBuilder genotypes = new StringBuilder();
		for (int j = 5; j < elements.length; j += 3) {
			if (Double.parseDouble(elements[j]) >= threshold) {
				genotypes.append("00");
			}
			else if (Double.parseDouble(elements[j+1]) >= threshold) {
				genotypes.append("01");
			}
			else if (Double.parseDouble(elements[j+2]) >= threshold) {
				genotypes.append("11");
			}
		}
		snpQueue.enqueue(new Snp(id, chr, 0, pos, ref, null, new String[] {ref, alt}, genotypes.toString()));
	}

}
