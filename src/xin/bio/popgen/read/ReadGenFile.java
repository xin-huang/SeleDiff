package xin.bio.popgen.read;

import xin.bio.popgen.datatype.GeneticData;
import xin.bio.popgen.datatype.Snp;
import xin.bio.popgen.selediff.LinkedQueue;

public class ReadGenFile extends Readable {
	
	private double threshold;
	
	public ReadGenFile(String fileName, GeneticData gd, double threshold) {
		this.threshold = threshold;
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
		for (int j = 5; j < elements.length; j += 3) {
			double threshold1 = Double.parseDouble(elements[j]);
			double threshold2 = Double.parseDouble(elements[j+1]);
			double threshold3 = Double.parseDouble(elements[j+2]);
			if (threshold1 >= threshold) {
				genotypes.append("00");
			}
			else if (threshold2 >= threshold) {
				genotypes.append("01");
			}
			else if (threshold3 >= threshold) {
				genotypes.append("11");
			}
			else {
				genotypes.append("99");
			}
		}
		snpQueue.enqueue(new Snp(id, chr, 0, pos, ref, null, new String[] {ref, alt}, genotypes.toString()));
	}

	@Override
	protected void passData(GeneticData gd) {
		passSnp(gd);
	}

}
