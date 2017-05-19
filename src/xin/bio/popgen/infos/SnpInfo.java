package xin.bio.popgen.infos;

import java.util.regex.Pattern;

/**
 * 
 * 
 * @author Xin Huang {@code <huangxin@picb.ac.cn>}
 *
 */
public class SnpInfo implements Info {
	
	private final Snp[] snps;
	private long byteBufferSize = 0;
	
	// an ArrayList stores SNP IDs
    private final String[] snpIds;
    
    // an ArrayList stores reference alleles
    private final String[] refAlleles;
    
    // an ArrayList stores alternative alleles
    private final String[] altAlleles;
    
    // an integer to record the index of the SNP currently parsing
    private int snpIndex = 0;
    
    // a Pattern for splitting lines
    private final Pattern pattern = Pattern.compile("\\s+");
    
    /**
     * Constructor of {@code SnpInfo}.
     * 
     * @param snpFile
     * @param snpNum
     */
    public SnpInfo(String snpFileName, int snpNum) {
    	snpIds = new String[snpNum];
    	refAlleles = new String[snpNum];
    	altAlleles = new String[snpNum];
    	this.snps = new Snp[snpNum];
    	readFile(getBufferedReader(snpFileName));
    }

	@Override
	public void parseLine(String line) {
		String[] elements = pattern.split(line);
		snpIds[snpIndex] = elements[0];
		refAlleles[snpIndex] = elements[4];
		altAlleles[snpIndex] = elements[5];
		snps[snpIndex] = new Snp(elements[0], elements[4], elements[5]);
		byteBufferSize += getSnpId(snpIndex).length + getRefAllele(snpIndex).length + getAltAllele(snpIndex).length;
		snpIndex++;
	}
	
	public long getByteBufferSize() { return byteBufferSize; }
	
	public byte[] getSnpId(int i) { return snps[i].getId(); }
	
	public byte[] getRefAllele(int i) { return snps[i].getRefAllele(); }
	
	public byte[] getAltAllele(int i) { return snps[i].getAltAllele(); }
	
	/**
	 * Returns a String array containing SNP IDs.
	 * 
	 * @return a String array containing SNP IDs
	 */
	public String[] getSnpIds() {
		return snpIds;
	}
	
	/**
	 * Returns a String array containing references alleles.
	 * 
	 * @return a String array containing references alleles
	 */
	public String[] getRefAlleles() {
		return refAlleles;
	}
	
	/**
	 * Returns a String array containing alternative alleles.
	 * 
	 * @return a String array containing alternative alleles
	 */
	public String[] getAltAlleles() {
		return altAlleles;
	}

}
