package xin.bio.popgen.read;

import java.util.List;

import xin.bio.popgen.datatype.GeneticData;

public class ReadOxfordFactory implements ReadFileFactory {
	
	private double threshold;
	
	public ReadOxfordFactory(List<String> fileNames, GeneticData gd, double threshold) {
		this.threshold = threshold;
		readFiles(fileNames, gd);
	}

	@Override
	public void readFiles(List<String> fileNames, GeneticData gd) {
		new ReadGenFile(fileNames.get(0), gd, threshold);
		new ReadSampleFile(fileNames.get(1), gd);
	}

}
