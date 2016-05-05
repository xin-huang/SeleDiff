package xin.bio.popgen.read;

import java.util.List;

import xin.bio.popgen.datatype.GeneticData;

public interface ReadFileFactory {
	void readFiles(List<String> fileNames, GeneticData gd);
}
