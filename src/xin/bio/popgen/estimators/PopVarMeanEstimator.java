package xin.bio.popgen.estimators;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringJoiner;

import xin.bio.popgen.infos.SampleInfo;

public final class PopVarMeanEstimator extends Estimator {
	
	private double[] popPairVarMeans;
	
	 /**
     * Constructor of class {@code PopVarMeanEstimator}.
     *
     * @param sampleInfo a SampleInfo instance containing sample information
     */
    public PopVarMeanEstimator(SampleInfo sampleInfo) {
    	super(sampleInfo, null);
    }

	@Override
	public void estimate() {
	}

	@Override
	void writeLine(BufferedWriter bw) throws IOException {
		for (int i = 0; i < popPairNum; i++) {
            String[] popPair = sampleInfo.getPopPair(i);
            StringJoiner sj = new StringJoiner("\t");
            sj.add(popPair[0]).add(popPair[1]).add(String.valueOf(Model.round(popPairVarMeans[i])));
            bw.write(sj.toString());
            bw.newLine();
        }
	}

	@Override
	void writeHeader(BufferedWriter bw) throws IOException {}

	@Override
	public void parseSnpInfo(String line) {}

}
