package xin.bio.popgen.selediff;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xin.bio.popgen.IO.Input;
import xin.bio.popgen.IO.Output;
import xin.bio.popgen.count.CountAdmixedPopsAllele;
import xin.bio.popgen.count.CountHaplotype;
import xin.bio.popgen.count.CountSnp;
import xin.bio.popgen.count.EstimateVarLogOdds;
import xin.bio.popgen.datatype.GeneticData;
import xin.bio.popgen.datatype.PhasedHaplotype;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

/**
 * This CommandLineArgs class using JCommander
 * for parsing parameters from standard input to the main program
 * 
 * @author Xin Huang
 *
 */
public class CommandLineArgs {
	
	@Parameter(names = "--all-geno", description = "A EIGENSTRAT GENO file contains all the sample genotype data.", validateWith = FileValidator.class)
	private String allGeno;
	
	@Parameter(names = "--all-ind", description = "A EIGENSTRAT IND file contains all the sample individuals' information.", validateWith = FileValidator.class)
	private String allInd;
	
	@Parameter(names = "--all-snp", description = "A EIGENSTRAT SNP file contains all the sample SNPs' information.", validateWith = FileValidator.class)
	private String allSnp;
	
	@Parameter(names = "--all-gen", description = "A Oxford GEN file contains all the sample SNPs' information and genotype data.", validateWith = FileValidator.class)
	private String allGen;
	
	@Parameter(names = "--all-sample", description = "A Oxford SAMPLE file contains all the sample individuals' information.", validateWith = FileValidator.class)
	private String allSample;
	
	@Parameter(names = "--all-haps", description = "A HAPS file contains all the sample SNPs' information and genotype data.", validateWith = FileValidator.class)
	private String allHaps;
	
	@Parameter(names = "--candidate-geno", description = "A EIGENSTRAT GENO file contains the candidate genotype data.", validateWith = FileValidator.class)
	private String canGeno;
	
	@Parameter(names = "--candidate-ind", description = "A EIGENSTRAT IND file contains the candidate individuals' information.", validateWith = FileValidator.class)
	private String canInd;
	
	@Parameter(names = "--candidate-snp", description = "A EIGENSTRAT SNP file contains the candidate SNPs' information.", validateWith = FileValidator.class)
	private String canSnp;
	
	@Parameter(names = "--candidate-gen", description = "A Oxford GEN file contains the candidate SNPs' information and genotype data.", validateWith = FileValidator.class)
	private String canGen;
	
	@Parameter(names = "--candidate-sample", description = "A Oxford SAMPLE file contains the candidate individuals' information.", validateWith = FileValidator.class)
	private String canSample;
	
	@Parameter(names = "--candidate-haps", description = "A HAPS file contains the candidate SNPs' information and genotype data.", validateWith = FileValidator.class)
	private String canHaps;
	
	@Parameter(names = "--output", description = "The output file.", required = true, validateWith = FileValidator.class)
	private String outputFileName; // the name of the output file, required
	
	@Parameter(names = "--ancestral-allele", description = "A file specifies ancestral alleles.", required = true, validateWith = FileValidator.class)
	private String ancAlleleFileName; // the name of the ancestral allele file, required
	
	@Parameter(names = "--admixed-population", description = "A file specifies admixed population.", validateWith = FileValidator.class)
	private String admixedPopulationFileName; // the name of the admixed population file
	
	@Parameter(names = "--divergence-time", description = "A file specifies divergence time.", required = true, validateWith = FileValidator.class)
	private String divergenceTimeFileName; // the name of the divergence time file name, required
	
	@Parameter(names = "--haplotype", description = "A file specifies haplotypes.", validateWith = FileValidator.class)
	private String hapList;
	
	@Parameter(names = "--all-gen-threshold", description = "A threshold specifes the confidence of genotype in all the sample data, if Oxford GEN/SAMPLE format is used.", validateWith = ThresholdValidator.class)
	private double allThreshold = 0.9;
	
	@Parameter(names = "--candidate-gen-threshold", description = "A threshold specifies the confidence of genotypes in the candidate data, if Oxford GEN/SAMPLE format is used.", validateWith = ThresholdValidator.class)
	private double canThreshold = 0.9;
	
	@Parameter(names = "--help", description = "Show SeleDiff's usage.")
	public boolean help;
	
	@Parameter(names = "--log", description = "Redirect log into a file.")
	public String log;
	
	private List<String> allInputs;
	private List<String> canInputs;
	private String allFormat;
	private String canFormat;
	private boolean containsHaplotype;
	
	public void checkParameters() {
		int isAllEigenStratParamValid = (allGeno != null && allInd != null && allSnp != null) ? 1 : 0;
		int isAllGwasParamValid = (allGen != null && allSample != null) ? 1 : 0;
		int isAllHapsParamValid = (allHaps != null && allSample != null) ? 1 : 0;
		int isCanEigenStratParamValid = (canGeno != null && canInd != null && canSnp != null) ? 1 : 0;
		int isCanGwasParamValid = (canGen != null && canSample != null) ? 1 : 0;
		int isCanHapsParamValid = (canHaps != null && canSample != null) ? 1 : 0;
		containsHaplotype = hapList != null;
		
		if (containsHaplotype && isCanHapsParamValid == 0) {
			throw new ParameterException("Haps format should be specified, when --haplotype is used.");
		}
		
		if ((isAllEigenStratParamValid + isAllGwasParamValid + isAllHapsParamValid) != 1) {
			throw new ParameterException("Exactly one format should be specified for all the sample data.");
		}
		if ((isCanEigenStratParamValid + isCanGwasParamValid + isCanHapsParamValid) != 1) {
			throw new ParameterException("Exactly one format should be specified for the candidate data.");
		}
		
		allInputs = new ArrayList<String>();
		canInputs = new ArrayList<String>();
		
		if (isAllEigenStratParamValid == 1) {
			allInputs.add(allGeno);
			allInputs.add(allInd);
			allInputs.add(allSnp);
			allFormat = "eigenstrat";
		}
		else if (isAllGwasParamValid == 1) {
			allInputs.add(allGen);
			allInputs.add(allSample);
			allFormat = "gwas";
		}
		else if (isAllHapsParamValid == 1) {
			allInputs.add(allHaps);
			allInputs.add(allSample);
			allFormat = "haps";
		}
		
		if (isCanEigenStratParamValid == 1) {
			canInputs.add(canGeno);
			canInputs.add(canInd);
			canInputs.add(canSnp);
			canFormat = "eigenstrat";
		}
		else if (isCanGwasParamValid == 1) {
			canInputs.add(canGen);
			canInputs.add(canSample);
			canFormat = "gwas";
		}
		else if (isCanHapsParamValid == 1) {
			canInputs.add(canHaps);
			canInputs.add(canSample);
			canFormat = "haps";
			if (containsHaplotype) {
				canInputs.add(hapList);
			}
		}
	}
	
	public void execute() {
		if (log != null) {
			try {
				System.setOut(new PrintStream(new File(log)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
			Output.writeTitle(bw, containsHaplotype);
			
			GeneticData all = Input.create(allInputs, allFormat, allThreshold);
			GeneticData can = Input.create(canInputs, canFormat, canThreshold);
			
			HashMap<String, String[]> admixedPops = new HashMap<String, String[]>(); 
			// a hash map stores the information of admixed populations
	        // key is the admixed population ID, value is a String array,
	        // the first two elements are the parental population IDs
	        // the third element is the admixed proportion from the first parental population
			
			Input.readAncAlleleFile(ancAlleleFileName, all, can);
			
			all.performCount(new CountSnp());
			if (containsHaplotype) {
				can = new PhasedHaplotype(can, canInputs.get(2));
				can.performCount(new CountHaplotype());
			}
			else {
				can.performCount(new CountSnp());
			}
			if (admixedPopulationFileName != null) {
				admixedPops = Input.estimateAdmixedProportion(all, admixedPopulationFileName);
				all.performCount(new CountAdmixedPopsAllele(admixedPops));
				can.performCount(new CountAdmixedPopsAllele(admixedPops));
			}
			
			HashMap<String, Double> divergenceTimes = Input.readDivergenceTimeFile(divergenceTimeFileName);
			for (String id:divergenceTimes.keySet()) {
				String[] pops = id.split("_");
				if (!can.containsPop(pops[0]))
					throw new IllegalArgumentException("Can't find population " + pops[0] 
							+ " in the candidate data, while specified in the divergence time file " + divergenceTimeFileName);
				else if (!can.containsPop(pops[1]))
					throw new IllegalArgumentException("Can't find population " + pops[1] 
							+ " in the candidate data, while specified in the divergence time file " + divergenceTimeFileName);
			}
			
			all.performCount(new EstimateVarLogOdds(admixedPops));
			Model.estimateDelta(all, can, admixedPops, divergenceTimes, bw);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static class ThresholdValidator implements IParameterValidator {

		@Override
		public void validate(String name, String value)
				throws ParameterException {
			double n = Double.parseDouble(value);
			if ((n > 1.0) || (n < 0.0))
				throw new ParameterException("Parameter " + name + " should be between 0 and 1 (found " + value + ")");
		}
		
	}
	
	public static class HapConfidenceValidator implements IParameterValidator {

		@Override
		public void validate(String name, String value)
				throws ParameterException {
			double n = Double.parseDouble(value);
			if ((n > 1.0) || (n < 0.5))
				throw new ParameterException("Parameter " + name + " should be between 0.5 and 1 (found " + value + ")");
		}
		
	}
	
	public static class FileValidator implements IParameterValidator {

		@Override
		public void validate(String name, String value)
				throws ParameterException {
			File f = new File(value);
			
			if (name.equals("--output")) {
				String path = f.getPath();
				if (path.lastIndexOf(File.separator) > 0) { // the output file is not in the current directory
					path = path.substring(0, path.lastIndexOf(File.separator));
					if (!new File(path).exists()) 
						throw new ParameterException("Parameter " + name + " " + value + " does not exist");
				}
			}
			else if (!f.exists()) throw new ParameterException("Parameter " + name + " " + value + " does not exist");
			else if (f.isDirectory()) throw new ParameterException("Parameter " + name + " " + value + " is a directory");
		}
		
	}
}
