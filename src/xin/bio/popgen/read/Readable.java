package xin.bio.popgen.read;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import xin.bio.popgen.datatype.GeneticData;
import xin.bio.popgen.datatype.GeneticVariant;
import xin.bio.popgen.datatype.Individual;
import xin.bio.popgen.datatype.Snp;
import xin.bio.popgen.selediff.LinkedQueue;

public abstract class Readable {
	
	protected LinkedQueue<Snp> snpQueue = new LinkedQueue<Snp>();
	protected LinkedQueue<Individual> indQueue = new LinkedQueue<Individual>();
	protected HashSet<String> popSet  = new HashSet<String>();
	protected LinkedQueue<String> lines = new LinkedQueue<String>();
	
	protected abstract void parseLine(int i, String line);
	protected abstract void passData(GeneticData gd);
	
	protected void readFile(String fileName, int skipLines) {
		System.out.println("Reading  " + fileName + "  ...");
//long start = System.currentTimeMillis();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = null;
			int i = 0;
			while (i < skipLines) {
				br.readLine();
				i++;
			}
			while ((line = br.readLine()) != null) {
				lines.enqueue(line.trim());
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//long end = System.currentTimeMillis();
//long diff = end - start;
//System.out.println("Finished Reading " + fileName + " ellapsed time: " + diff);
	}
	
	protected void passSnp(GeneticData gd) {
		GeneticVariant[] variants = new GeneticVariant[snpQueue.size()];
		HashMap<String, Integer> variantIndices = new HashMap<String, Integer>();
		int i = 0;
		for (Snp s:snpQueue) {
			variants[i] = s;
			variantIndices.put(s.getId(), i++);
		}
		gd.setVariants(variants);
		gd.setVariantIndices(variantIndices);
	}
	
	protected void passInd(GeneticData gd) {
		Individual[] inds = new Individual[indQueue.size()];
		HashMap<String, Integer> indIndices = new HashMap<String, Integer>();
		HashMap<String, Integer> popIndices = new HashMap<String, Integer>();
		HashMap<Integer, String> popIds = new HashMap<Integer, String>();
		int i = 0;
		for (Individual ind:indQueue) {
			inds[i] = ind;
			indIndices.put(ind.getId(), i++);
		}
		i = 0;
		for (String id:popSet) {
			popIds.put(i, id);
			popIndices.put(id, i++);
		}
		gd.setInds(inds);
		gd.setIndIndices(indIndices);
		gd.setPopIds(popIds);
		gd.setPopIndices(popIndices);
	}
	
}
