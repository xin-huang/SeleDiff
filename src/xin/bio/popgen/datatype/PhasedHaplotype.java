package xin.bio.popgen.datatype;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import xin.bio.popgen.IO.Output;
import xin.bio.popgen.selediff.LinkedQueue;

public class PhasedHaplotype extends GeneticData {
	
	private GeneticData gd;

	public PhasedHaplotype (GeneticData gd, String hapListFileName) {
		this.gd = gd;
		System.out.println("Reading  " + hapListFileName + "  ...");
		readHapListFile(hapListFileName);
		this.inds = gd.inds;
		this.indIndices = gd.indIndices;
		this.popIndices = gd.popIndices;
		this.popIds = gd.popIds;
		Output.log(getVariantSize(), getIndSize(), getPopSize(), "Haplotypes");
	}
	
	private void readHapListFile(String hapListFileName) {
		LinkedQueue<Haplotype> haplotypeQueue = new LinkedQueue<Haplotype>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(hapListFileName));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] elements = line.trim().split("\\s+");
				Snp[] snps = new Snp[elements.length];
				for (int i = 0; i < snps.length; i++) {
					if (gd.getVariant(elements[i]) instanceof Snp) snps[i] = (Snp) gd.getVariant(elements[i]);
				}
				haplotypeQueue.enqueue(new Haplotype(snps));
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		variants = new GeneticVariant[haplotypeQueue.size()];
		variantIndices = new HashMap<String, Integer>();
		int i = 0;
		for (Haplotype h:haplotypeQueue) {
			variants[i] = h;
			variantIndices.put(h.getId(), i++);
		}
	}

}
