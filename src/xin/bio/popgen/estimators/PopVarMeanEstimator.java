package xin.bio.popgen.estimators;

import static xin.bio.popgen.estimators.Model.calDriftVar;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.StringJoiner;

import xin.bio.popgen.infos.InfoReader;

public final class PopVarMeanEstimator extends Estimator {
	
	private final float[] popPairVarMeans;
	
	 /**
     * Constructor of class {@code PopVarMeanEstimator}.
     *
     * @param sampleInfo a SampleInfo instance containing sample information
     */
    public PopVarMeanEstimator(String indFileName, String snpFileName) {
    	super(indFileName, snpFileName);
    	popPairVarMeans = new float[popPairNum];
    }
    
	@Override
	public void analyze(List<String> genoFileNames) {
		readFile(new InfoReader(genoFileNames.get(0)).getBufferedReader());
	}
	
	@Override
	protected void parseLine(char[] cbuf) {
		int[][] alleleCounts = countAlleles(cbuf);
		for (int m = 0; m < alleleCounts.length; m++) {
			for (int n = m + 1; n < alleleCounts.length; n++) {
				int popPairIndex = sampleInfo.getPopPairIndex(m,n);
				// Assume no missing SNP in any population
                popPairVarMeans[popPairIndex] = (float) ((snpIndex * popPairVarMeans[popPairIndex] 
                		+ calDriftVar(alleleCounts[m][0],alleleCounts[m][1], 
                				alleleCounts[n][0],alleleCounts[n][1]))
                		/ (snpIndex + 1));
			}
		}
		snpIndex++;
	}

	@Override
	protected void writeLine(Writer bw) throws IOException {
		for (int i = 0; i < popPairNum; i++) {
            String[] popPair = sampleInfo.getPopPair(i);
            StringJoiner sj = new StringJoiner("\t");
            sj.add(popPair[0]).add(popPair[1])
            	.add(String.valueOf(Model.round(popPairVarMeans[i])))
            	.add("\n");
            bw.write(sj.toString());
        }
	}

	@Override
	protected void writeHeader(Writer bw) throws IOException {}

}
