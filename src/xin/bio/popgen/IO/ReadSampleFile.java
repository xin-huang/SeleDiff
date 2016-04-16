package xin.bio.popgen.IO;

import xin.bio.popgen.fileformat.Individual;

public class ReadSampleFile extends IndFile implements Readable  {

	public ReadSampleFile(String fileName) {
		initializes();
		readFile(fileName, 2);
		parseData();
	}
	
	@Override
	public void parseLine(int i, String line) {
		String[] elements = line.trim().split("\\s+");
		String popId      = elements[0];
		String indId      = elements[1];
		String sex        = elements[3];
		String phenotype  = elements[4];
		Individual ind = new Individual(popId, indId, null, null, sex, phenotype, null);
		popSet.add(popId);
		indQueue.enqueue(ind);
	}

}
