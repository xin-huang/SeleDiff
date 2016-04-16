package xin.bio.popgen.IO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public interface Readable {
	
	public void parseLine(int i, String line);
	
	public default void readFile(String fileName, int skipLines) {
		System.out.println("Reading  " + fileName + "  ...");
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = null;
			int i = 0;
			while (i < skipLines) {
				br.readLine();
				i++;
			}
			i = 0;
			while ((line = br.readLine()) != null) {
				parseLine(i++, line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
