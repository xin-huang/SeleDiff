package xin.bio.popgen.read;

import xin.bio.popgen.datatype.GeneticData;
import xin.bio.popgen.datatype.Individual;

public class ReadIndFile extends Readable {
	
	public ReadIndFile(String fileName, GeneticData gd) {
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
		String indId = elements[0];
		String sex   = elements[1];
		String popId = elements[2];
		Individual ind = new Individual(popId, indId, null, null, sex, "-9");
		popSet.add(popId);
		indQueue.enqueue(ind);
	}

	@Override
	protected void passData(GeneticData gd) {
		passInd(gd);
	}
	
}
