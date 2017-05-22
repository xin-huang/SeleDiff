package com.xin.popgen.infos;

public class CountSnpNumInfo implements Info {
	
	private int snpNum = 0;
	
	public CountSnpNumInfo(String snpFileName) {
		readFile(getBufferedReader(snpFileName));
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
