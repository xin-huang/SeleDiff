package xin.bio.popgen;

import java.util.Arrays;

import org.ejml.simple.SimpleEVD;
import org.ejml.simple.SimpleMatrix;

import edu.princeton.cs.algs4.Stopwatch;

/**
 * 
 * @author Xin Huang
 *
 */
public class EigenTest {
	
	private double eval;        // the largest eigenvalue
	private SimpleMatrix evec;  // the corresponding eigenvector to the largest eigenvalue
	private double tw;          // a statistic approximately follows TW-distribution
	private SimpleMatrix genotypeMatrix;    // a matrix contains genotype information for individuals
								// rows represent individuals
								// columns represent SNPs
	private boolean isSignificant;
	
	/**
	 * 
	 * @param geno
	 */
	public EigenTest(SimpleMatrix genotypeMatrix) {
		this.genotypeMatrix = genotypeMatrix;
	}
	
	/**
	 * 
	 * @return TRUE if significant; otherwise FALSE
	 */
	public void run() {
		int indNum = genotypeMatrix.numRows();
		int snpNum = genotypeMatrix.numCols();
		
		SimpleMatrix matrix = normalize(genotypeMatrix);
		SimpleMatrix covMatrix = matrix.mult(matrix.transpose()).scale(1.0/snpNum);
		
		SimpleEVD eigs = covMatrix.eig();
		int maxEvalIndex = eigs.getIndexMax();
		double[] eigenvals = new double[eigs.getNumberOfEigenvalues()];
		
		double lambda = 0.0;
		double lambda2 = 0.0;
		
		for (int i = 0; i < eigenvals.length; i++) {
			eigenvals[i] = eigs.getEigenvalue(i).getReal();
		}
		
		Arrays.sort(eigenvals);
		
		for (int i = eigenvals.length - 1; i >= 0; i--) {
			lambda += eigenvals[i];
			lambda2 += eigenvals[i] * eigenvals[i];
		}
		
		double n = (indNum + 1) * lambda * lambda / ((indNum - 1) * lambda2 - lambda * lambda); // $n^\prime$
		double mu = Math.pow(Math.sqrt(n-1)+Math.sqrt(indNum), 2) / n;
		double sigma = (Math.sqrt(n-1)+Math.sqrt(indNum)) * Math.pow(1/Math.sqrt(n-1)+1/Math.sqrt(indNum), 1.0/3.0) / snpNum;
		
		eval = eigs.getEigenvalue(maxEvalIndex).getReal();
		eval = (indNum - 1) * eval / lambda;
		evec = eigs.getEigenVector(maxEvalIndex);
		tw = (eval - mu) / sigma;
		if (tw > 2) 
			isSignificant = true;
		else
			isSignificant = false;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getEval() {
		return eval;
	}
	
	/**
	 * 
	 * @return
	 */
	public SimpleMatrix getEvec() {
		return evec;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getTw() {
		return tw;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isSignificant() {
		return isSignificant;
	}
	
	/**
	 * 
	 * @param matrix
	 * @return
	 */
	private SimpleMatrix normalize(SimpleMatrix matrix) {
		int row = matrix.numRows();
		int col = matrix.numCols();
		double[][] normalizedMatrix = new double[row][col];
		for (int j = 0; j < col; j++) {
			double mu = 0.0;
			for (int i = 0; i < row; i++) {
				mu += matrix.get(i, j);
			}
			mu = mu / row;
			double p = mu / 2.0;
			for (int i = 0; i < row; i++) {
				normalizedMatrix[i][j] = (matrix.get(i, j) - mu) / (Math.sqrt(p * (1 - p)));
			}
		}
		return new SimpleMatrix(normalizedMatrix);
	}
	
	/**
	 * Unit tests the <tt>EigenTest<tt> data type
	 */
	public static void main(String[] args) {
		String genoFileName = args[0];
		String indFileName = args[1];
		String snpFileName = args[2];
		Stopwatch elapse = new Stopwatch();
		EigenSoft eigen = new EigenSoft(genoFileName, indFileName, snpFileName);
		System.out.println("Load data: " + elapse.elapsedTime() );
		
/*		elapse = new Stopwatch();
		double[][] genoMatrix = eigen.getGenoMatrix();
		System.out.println("Construct genotype matrix: " + elapse.elapsedTime());
		
		elapse = new Stopwatch();
		EigenTest eigentest = new EigenTest(new SimpleMatrix(genoMatrix));
		eigentest.run();
		System.out.println("EigenTest: " + elapse.elapsedTime());
		System.out.println(genoMatrix[0].length);
		if (eigentest.isSignificant()) {
			System.out.println(eigentest.getTw());
			System.out.println(eigentest.getEval());
			System.out.println(eigentest.getEvec());
		}
		
		SimpleMatrix evec = eigentest.getEvec();
		System.out.println(evec.numRows());
		System.out.println(evec.numCols());
		int j = 0;
		int k = 0;
		LinkedQueue<double[]> indQueue = new LinkedQueue<double[]>();
		for (int i = 0; i < evec.numRows(); i++) {
			if (evec.get(i, 0) > 0) {
				System.out.println(eigen.getIndId(i));
				indQueue.enqueue(genoMatrix[i]);
				j++;
			}
			else {
				k++;
			}
		}
		System.out.println(j);
		System.out.println(k);
		
		double[][] submatrix = new double[j][genoMatrix[0].length];
		int i = 0;
		for (double[] ind:indQueue) {
			submatrix[i++] = ind;
		}*/
		
		/*for (int p = 0; p < submatrix.length; p++) {
			for (int q = 0; q < submatrix[0].length; q++) {
				System.out.print(submatrix[p][q] + " ");
			}
			System.out.println();
		}*/
		
		/*System.out.println(submatrix[1][0]);
		EigenTest et = new EigenTest(new SimpleMatrix(submatrix));
		et.run();
		if (et.isSignificant) {
			System.out.println(et.getTw());
			System.out.println(et.getEval());
			System.out.println(et.getEvec());
		}
		else {
			System.out.println("Not significant!");
		}*/
		
	}

}
