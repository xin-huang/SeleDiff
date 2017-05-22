package xin.bio.popgen.infos;

import java.util.regex.Pattern;

/**
 * 
 * 
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 *
 */
public class SnpInfo implements Info {
	
	// a String array stores SNP information: SNP ID, Ref Allele, Alt Allele
	private final String[] snps;
	
    // an integer to record the index of the SNP currently parsing
    private int snpIndex = 0;
    
    // a Pattern for splitting lines
    private final Pattern pattern = Pattern.compile("\\s+");
    
    /**
     * Constructor of {@code SnpInfo}.
     * 
     * @param snpFileName the name of the .snp file
     * @param snpNum the number of SNPs
     */
    public SnpInfo(String snpFileName, int snpNum) {
    	snps = new String[snpNum];
    	readFile(getBufferedReader(snpFileName));
    }

	@Override
	public void parseLine(String line) {
		String[] elements = pattern.split(line);
		StringBuilder sb = new StringBuilder();
		snps[snpIndex] = sb.append(elements[0]).append("\t")
				.append(elements[4]).append("\t")
				.append(elements[5]).append("\t")
				.toString();
		snpIndex++;
	}
	
	/**
	 * Returns the information of the i-th SNP.
	 * 
	 * @param i the i-th SNP
	 * @return the information of the i-th SNP
	 */
	public String getSnp(int i) { return snps[i]; }
	
}
