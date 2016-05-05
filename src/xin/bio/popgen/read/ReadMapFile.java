package xin.bio.popgen.read;

import xin.bio.popgen.datatype.GeneticData;
import xin.bio.popgen.datatype.Snp;

public class ReadMapFile extends Readable {

	@Override
	protected void parseLine(int i, String line) {
		String[] elements = line.trim().split("\\s+");
		String chr = elements[0];
		String id = elements[1];
		double genPos = Double.parseDouble(elements[2]);
		int phyPos = Integer.parseInt(elements[3]);
		Snp s = new Snp(id, chr, genPos, phyPos, null, null, null, null);
		snpQueue.enqueue(s);
	}

	@Override
	protected void passData(GeneticData gd) {
		passSnp(gd);
	}

}
