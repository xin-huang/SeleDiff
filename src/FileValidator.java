package xin.bio.popgen;

import java.io.File;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/** 
 * The <tt>FileValidator</tt> data type is for
 * JCommander to validate whether input files exist
 * 
 * @author Xin Huang
 *
 */
public class FileValidator implements IParameterValidator {

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
		else 
			if (!f.exists())  
				throw new ParameterException("Parameter " + name + " " + value + " does not exist");
	}
	
}
