package xin.bio.popgen.read;

import java.util.List;

import xin.bio.popgen.datatype.GeneticData;

public class ReadEigenStratFactory implements ReadFileFactory {
	
	public ReadEigenStratFactory(List<String> fileNames, GeneticData gd) {
		readFiles(fileNames, gd);
	}

	@Override
	public void readFiles(List<String> fileNames, GeneticData gd) {
//long start = System.currentTimeMillis();
		new ReadIndFile(fileNames.get(1), gd);  // the second file is .ind
		new ReadSnpFile(fileNames.get(2), gd);  // the third file is .snp
		new ReadGenoFile(fileNames.get(0), gd); // the first file is .geno 
//long end = System.currentTimeMillis();
//long diff = end - start;
//System.out.println("Ellapsed time: " + diff);
	}

}
