package xin.bio.popgen.read;

import java.util.HashSet;

import xin.bio.popgen.datatype.GeneticData;
import xin.bio.popgen.datatype.Individual;
import xin.bio.popgen.selediff.LinkedQueue;

public class ReadSampleFile extends Readable  {
	

	public ReadSampleFile(String fileName, GeneticData gd) {
		popSet = new HashSet<String>();
		indQueue = new LinkedQueue<Individual>();
		readFile(fileName, 2);
		int i = 0;
		for (String line:lines) {
			parseLine(i++, line);
		}
		passData(gd);
	}
	
	@Override
	protected void parseLine(int i, String line) {
		String[] elements = line.trim().split("\\s+");
		String popId      = elements[0];
		String indId      = elements[1];
		String sex        = elements[3];
		String phenotype  = elements[4];
		Individual ind = new Individual(popId, indId, null, null, sex, phenotype);
		popSet.add(popId);
		indQueue.enqueue(ind);
	}

	@Override
	protected void passData(GeneticData gd) {
		passInd(gd);
	}

}
