package xin.bio.popgen.read;

import java.util.List;

import xin.bio.popgen.datatype.GeneticData;

public class ReadHapsFactory implements ReadFileFactory {
	
	public ReadHapsFactory(List<String> fileNames, GeneticData gd) {
		readFiles(fileNames, gd);
	}

	@Override
	public void readFiles(List<String> fileNames, GeneticData gd) {
		new ReadHapsFile(fileNames.get(0), gd);
		new ReadSampleFile(fileNames.get(1), gd);
	}

}
