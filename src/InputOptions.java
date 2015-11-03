package xin.bio.popgen;

import com.beust.jcommander.Parameter;

/**
 * This <tt>InputOptions</tt> data type using JCommander
 * for parsing parameters from standard input to the main program
 * 
 * @author Xin Huang
 *
 */
public class InputOptions {

	/**
	 * A Enum data type for selecting performance mode
	 * o: Estimating variance of omega for pairwise populations with the given files
	 * d: Estimating the delta staitstics with the given files
	 * s: Estimating the delta statistics of the specified SNPs given variance of omega
	 */
	public enum ModeEnum {
		o,
		d,
		s;
	}
	
	@Parameter(names = "--geno", description = ".geno file contains genotype information", required = true, validateWith = FileValidator.class)
	private String genoFileName; // the name of the .geno file, required
	
	@Parameter(names = "--ind", description = ".ind file contains individual information", required = true, validateWith = FileValidator.class)
	private String indFileName;  // the name of the .ind file, required
	
	@Parameter(names = "--snp", description = ".snp file contains SNP information", required = true, validateWith = FileValidator.class)
	private String snpFileName;  // the name of the .snp file, required
	
	@Parameter(names = "--output", description = "ouput file", required = true, validateWith = FileValidator.class)
	private String outputFileName; // the name of the output file, required
	
	@Parameter(names = "--mode", description = "o:estimate omega;d:estimate delta;s: estimate delta with given SNPs", required = true)
	private ModeEnum mode; // select which mode to perform
	
	@Parameter(names = "--omega", description = "the variance of population drift")
	private String omegaFileName;  // the name of the file storing variance of omega for pairwise populations
	
	/**
	 * Return the name of the .geno file
	 * @return the name of the .geno file
	 */
	public String getGenoFileName() {
		return genoFileName;
	}

	/**
	 * Return the name of the .ind file
	 * @return the name of the .ind file
	 */
	public String getIndFileName() {
		return indFileName;
	}

	/**
	 * Return the name of the .snp file
	 * @return the name of the .snp file
	 */
	public String getSnpFileName() {
		return snpFileName;
	}

	/**
	 * Return the name of the output file
	 * @return the name of the output file
	 */
	public String getOutputFileName() {
		return outputFileName;
	}
	
	/**
	 * Return the name of the file storing variance of omega for pairwise populations
	 * @return the name of the file storing variance of omega for pairwise populations
	 */
	public String getOmegaFileName() {
		return omegaFileName;
	}
	
	/*
	 * Return the performance mode
	 * @return the performance mode 
	 */
	public ModeEnum getMode() {
		return mode;
	}
	
}
