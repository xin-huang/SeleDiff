package xin.bio.popgen.estimators;

import static xin.bio.popgen.estimators.Model.calDriftVar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringJoiner;

import xin.bio.popgen.infos.IndInfo;

public final class PopVarMeanEstimator extends Estimator {
	
	private final float[] popPairVarMeans;
	
	 /**
     * Constructor of class {@code PopVarMeanEstimator}.
     *
     * @param sampleInfo a SampleInfo instance containing sample information
     */
    public PopVarMeanEstimator(IndInfo sampleInfo, int snpNum) {
    	super(sampleInfo, snpNum);
    	popPairVarMeans = new float[popPairNum];
    }
    
	@Override
	public void analyze(BufferedReader br) {
		readFile(br);
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
	protected void writeLine(BufferedWriter bw) throws IOException {
		for (int i = 0; i < popPairNum; i++) {
            String[] popPair = sampleInfo.getPopPair(i);
            StringJoiner sj = new StringJoiner("\t");
            sj.add(popPair[0]).add(popPair[1]).add(String.valueOf(Model.round(popPairVarMeans[i])));
            bw.write(sj.toString());
            bw.newLine();
        }
	}

	@Override
	protected void writeHeader(BufferedWriter bw) throws IOException {}

}
