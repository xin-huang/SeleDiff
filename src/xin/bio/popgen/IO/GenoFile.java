package xin.bio.popgen.IO;

import xin.bio.popgen.fileformat.GeneticVariant;


public abstract class GenoFile {

	protected GeneticVariant[] variants;
	
	public GeneticVariant[] getVariants() {
		return variants;
	}
	
}
