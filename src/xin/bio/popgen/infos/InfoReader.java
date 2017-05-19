package xin.bio.popgen.infos;

import java.io.BufferedReader;

public class InfoReader implements Info {
	
	private final String genoFileName;
	
	public InfoReader(String genoFileName) {
		this.genoFileName = genoFileName;
	}

	@Override
	public void parseLine(String line) {}
	
	public BufferedReader getBufferedReader() {
		return getBufferedReader(genoFileName);
	}

}
