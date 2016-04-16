package xin.bio.popgen.IO;

import java.util.HashMap;

import xin.bio.popgen.fileformat.GeneticVariant;
import xin.bio.popgen.fileformat.Snp;
import xin.bio.popgen.selediff.LinkedQueue;

public abstract class SnpFile {

	protected LinkedQueue<Snp> snpQueue;
	protected GeneticVariant[] variants;
	protected HashMap<String, Integer> variantIndices;
	
	protected void initializes() {
		this.snpQueue = new LinkedQueue<Snp>();
	}
	
	protected void parseData() {
		variants = new Snp[snpQueue.size()];
		variantIndices = new HashMap<String, Integer>();
		int i = 0;
		for (Snp s:snpQueue) {
			variantIndices.put(s.getId(), i);
			variants[i++] = s;
		}
	}
	
	public GeneticVariant[] getVariants() {
		return variants;
	}
	
	public HashMap<String, Integer> getVariantIndices() {
		return variantIndices;
	}
	
}
