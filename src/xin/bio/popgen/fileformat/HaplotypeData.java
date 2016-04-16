package xin.bio.popgen.fileformat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringJoiner;

import xin.bio.popgen.IO.Output;
import xin.bio.popgen.selediff.LinkedQueue;

public class HaplotypeData extends GeneticData {
	
	private GeneticData data;

	public HaplotypeData (GeneticData data, String hapListFileName) {
		this.data = data;
		System.out.println("Reading  " + hapListFileName + "  ...");
		readHapListFile(hapListFileName);
		inds = data.inds;
		indIndices = data.indIndices;
		popIndices = data.popIndices;
		popIds = data.popIds;
		countBehavior = new CountHaplotype(variants, inds, popIndices);
		performCount();
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
					snps[i] = (Snp) data.getVariant(elements[i]);
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

	public static void main(String[] args) {
		String hapsFileName = args[0];
		String sampleFileName = args[1];
		String hapListFileName = args[2];
		Haps haps = new Haps(hapsFileName, sampleFileName);
		HaplotypeData h = new HaplotypeData(haps, hapListFileName);
		for (int i = 0; i < h.getVariantSize(); i++) {
			StringJoiner display = new StringJoiner("\t");
			System.out.println(h.getVariant(i).getAncAlleleIndex());
			display.add(h.getVariant(i).getId())
			       .add(h.getVariant(i).getRefAllele());
			for (String allele:h.getVariant(i).getAltAlleles()) {
				display.add(allele);
			}
			System.out.println(display.toString());
			for (int j = 0; j < h.getPopSize(); j++) {
				StringJoiner result = new StringJoiner("\t");
				result.add(h.getPopId(j));
				for (int k = 0; k < h.getVariant(i).getAlleleSize(); k++) {
					result.add(String.valueOf(h.getAlleleCount(i, j, k)));
				}
				System.out.println(result.toString());
			}
		}
	}

	@Override
	protected void readFiles() {
	}

}
