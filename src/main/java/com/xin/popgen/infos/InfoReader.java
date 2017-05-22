package com.xin.popgen.infos;

import java.io.BufferedReader;

public class InfoReader implements Info {
	
	private final String fileName;
	
	public InfoReader(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void parseLine(String line) {}
	
	public BufferedReader getBufferedReader() {
		return getBufferedReader(fileName);
	}

}
