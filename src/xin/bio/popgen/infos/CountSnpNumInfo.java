package xin.bio.popgen.infos;

import java.io.BufferedReader;

public class CountSnpNumInfo implements Info {
	
	private int snpNum = 0;
	
	public CountSnpNumInfo(String snpFileName, BufferedReader br) {
		readFile(br);
		System.out.println(snpNum + " variants are read from " + snpFileName);
	}

	@Override
	public void parseLine(String line) {
		snpNum++;
	}
	
	public int getSnpNum() {
		return snpNum;
	}

}
