package xin.bio.popgen.IO;

import xin.bio.popgen.fileformat.Individual;

public class ReadIndFile extends IndFile implements Readable {
	
	public ReadIndFile(String fileName) {
		initializes();
		readFile(fileName, 0);
		parseData();
	}

	@Override
	public void parseLine(int i, String line) {
		String[] elements = line.trim().split("\\s+");
		String indId = elements[0];
		String sex   = elements[1];
		String popId = elements[2];
		Individual ind = new Individual(popId, indId, null, null, sex, null, null);
		popSet.add(popId);
		indQueue.enqueue(ind);
	}
	
}
