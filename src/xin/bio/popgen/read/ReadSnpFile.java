package xin.bio.popgen.read;

import xin.bio.popgen.datatype.GeneticData;
import xin.bio.popgen.datatype.Snp;

public class ReadSnpFile extends Readable {
	
	public ReadSnpFile(String fileName, GeneticData gd) {
		readFile(fileName, 0);
		int i = 0;
		for (String line:lines) {
			parseLine(i++, line);
		}
		passData(gd);
	}

	@Override
	protected void parseLine(int i, String line) {
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

	@Override
	protected void passData(GeneticData gd) {
		passSnp(gd);
	}
	
}
