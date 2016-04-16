package xin.bio.popgen.selediff;

import com.beust.jcommander.JCommander;

/**
 * 
 * @author Xin Huang
 *
 * 22 Mar 2016
 */
public class SeleDiff {
	
	/**
	 * Main program 
	 * 22 Mar 2016
	 */
	public static void main(String[] args) {
		CommandLineArgs cm = new CommandLineArgs();
		JCommander jc = new JCommander(cm);
		jc.setProgramName("SeleDiff");
		
		if (args.length == 0) {
			jc.usage();
			System.exit(-1);
		}
		else {
			jc.parse(args);
			if (cm.help) {
				jc.usage();
				System.exit(-1);
			}
			else {
				cm.checkParameters();
				cm.execute();
			}
		}
		
	}
	
}
