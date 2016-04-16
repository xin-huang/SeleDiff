package xin.bio.popgen.IO;

import java.util.HashMap;
import java.util.HashSet;

import xin.bio.popgen.fileformat.Individual;
import xin.bio.popgen.selediff.LinkedQueue;

public abstract class IndFile {

	protected LinkedQueue<Individual> indQueue;
	protected HashSet<String> popSet;
	protected Individual[] inds;
	protected HashMap<String, Integer> indIndices;
	protected HashMap<Integer, String> popIds;
	protected HashMap<String, Integer> popIndices;
	
	protected void initializes() {
		this.indQueue = new LinkedQueue<Individual>();
		this.popSet = new HashSet<String>();
	}
	
	protected void parseData() {
		inds = new Individual[indQueue.size()];
		indIndices = new HashMap<String, Integer>();
		int i = 0;
		for (Individual ind:indQueue) {
			inds[i] = ind;
			indIndices.put(ind.getId(), i++);
		}
		
		popIndices = new HashMap<String, Integer>();
		popIds = new HashMap<Integer, String>();
		i = 0;
		for (String popId : popSet) {
			popIndices.put(popId, i);
			popIds.put(i++, popId);
		}
	}
	
	public Individual[] getInds() {
		return inds;
	}
	
	public HashMap<String, Integer> getIndIndices() {
		return indIndices;
	}
	
	public HashMap<Integer, String> getPopIds() {
		return popIds;
	}
	
	public HashMap<String, Integer> getPopIndices() {
		return popIndices;
	}
	
}
