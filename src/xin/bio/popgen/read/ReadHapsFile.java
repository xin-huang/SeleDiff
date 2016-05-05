package xin.bio.popgen.read;

import xin.bio.popgen.datatype.GeneticData;
import xin.bio.popgen.datatype.Snp;
import xin.bio.popgen.selediff.LinkedQueue;

public class ReadHapsFile extends Readable {
	
	public ReadHapsFile(String fileName, GeneticData gd) {
		snpQueue = new LinkedQueue<Snp>();
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
		String chr = elements[0];
		String id  = elements[1];
		int    pos = Integer.parseInt(elements[2]);
		String ref = elements[3];
		String alt = elements[4];
		StringBuilder genotypes = new StringBuilder();
		for (int j = 5; j < elements.length; j++) {
			genotypes.append(elements[j]);
		}
		snpQueue.enqueue(new Snp(id, chr, 0, pos, ref, null, new String[] {ref, alt}, genotypes.toString()));
	}

	@Override
	protected void passData(GeneticData gd) {
		passSnp(gd);
	}

}
