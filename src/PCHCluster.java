package xin.bio.popgen;

import org.ejml.simple.SimpleMatrix;

import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stopwatch;

public class PCHCluster {

	private SimpleMatrix genotypeMatrix; // a matrix stores genotyp for each individual
                                         // rows represent individuals
	                                     // columns represent SNPs
	private TreeNode root;               // a binary tree stores population tree
	
	private class TreeNode {
		private TreeNode parent;
		private TreeNode lchild;
		private TreeNode rchild;
		private LinkedQueue<Integer> indIds;
	}
	
	private class EigenInd implements Comparable<EigenInd> {
		private int id;
		private double evec;
		
		@Override
		public int compareTo(EigenInd that) {
			if (this.evec < that.evec) return -1;
			if (this.evec > that.evec) return +1;
			return 0;
		}
	}
	
	private class PopVar implements Comparable<PopVar> {
		private double var;
		private LinkedQueue<LinkedQueue<Integer>> ids;
		
		@Override
		public int compareTo(PopVar that) {
			if (this.var < that.var) return -1;
			if (this.var > that.var) return +1;
			return 0;
		}
		
	}
	
	public PCHCluster(String[] geno) {
		int indNum = geno[0].length();
		int snpNum = geno.length;
		double[][] data = new double[indNum][snpNum];
		for (int j = 0; j < snpNum; j++) {
			for (int i = 0; i < indNum; i++) {
				data[i][j] = Double.parseDouble(geno[j].substring(i, i+1));
			}
		}
		
		LinkedQueue<Integer> indIds = new LinkedQueue<Integer>();
		for (int i = 0; i < indNum; i++) {
			indIds.enqueue(i);
		}
		
		genotypeMatrix = new SimpleMatrix(data);
		
		// initialize the population tree with an empty root
		root = new TreeNode();
		root.parent = null;
		root.lchild = null;
		root.rchild = null;
		root.indIds = indIds;
	}
	
	public void run() {
		run(root);
	}
	
	public String getPopTree() {
		return root.toString();
	}
	
	private void run(TreeNode node) {
		LinkedQueue<LinkedQueue<Integer>> pops = runEigenTest(node.indIds);
		if (pops.size() > 0) {
			TreeNode lchild = new TreeNode();
			TreeNode rchild = new TreeNode();
			lchild.parent = node;
			rchild.parent = node;
			lchild.indIds = pops.dequeue();
			rchild.indIds = pops.dequeue();
			node.lchild = lchild;
			node.rchild = rchild;
			run(lchild);
			run(rchild);
		}
	}
	
	private LinkedQueue<LinkedQueue<Integer>> runEigenTest(LinkedQueue<Integer> indIds) {
		SimpleMatrix matrix = constructMatrix(indIds);
		LinkedQueue<MaxPQ<EigenInd>> pops = new LinkedQueue<MaxPQ<EigenInd>>();
		LinkedQueue<LinkedQueue<Integer>> correctedPops = new LinkedQueue<LinkedQueue<Integer>>();
		EigenTest eigentest = new EigenTest(matrix);
		eigentest.run();
		if (eigentest.isSignificant()) {
			SimpleMatrix evec = eigentest.getEvec();
			MaxPQ<EigenInd> pop0 = new MaxPQ<EigenInd>();
			MaxPQ<EigenInd> pop1 = new MaxPQ<EigenInd>();
			for (int i = 0; i < evec.numRows(); i++) {
				EigenInd ind = new EigenInd();
				ind.id = i;
				ind.evec = evec.get(i, 0);
				if (evec.get(i, 0) > 0) {
					pop0.insert(ind);
				}
				else {
					pop1.insert(ind);
				}
			}
			if (pop0.size() < pop1.size()) {
				pops.enqueue(pop0);
				pops.enqueue(pop1);
			}
			else {
				pops.enqueue(pop1);
				pops.enqueue(pop0);
			}
			double shift = correct(pops);
			LinkedQueue<Integer> correctedPop0 = new LinkedQueue<Integer>();
			LinkedQueue<Integer> correctedPop1 = new LinkedQueue<Integer>();
			for (int i = 0; i < evec.numRows(); i++) {
				if (evec.get(i, 0) > shift) {
					correctedPop0.enqueue(i);
				}
				else {
					correctedPop1.enqueue(i);
				}
			}
			correctedPops.enqueue(correctedPop0);
			correctedPops.enqueue(correctedPop1);
		}
		return correctedPops;
	}
	
	private SimpleMatrix constructMatrix(LinkedQueue<Integer> indIds) {
		SimpleMatrix subMatrix = new SimpleMatrix(0,0);
		int snpNum = genotypeMatrix.numCols();
		
		// extract individuals for eigentest
		for (Integer index:indIds) {
			SimpleMatrix indVector = genotypeMatrix.extractVector(true, index);
			subMatrix = subMatrix.combine(SimpleMatrix.END, 0, indVector);
		}
		
		// validate each SNP is biallelic and not missing
		SimpleMatrix testMatrix = new SimpleMatrix(0,0);
		for (int i = 0; i < snpNum; i++) {
			SimpleMatrix snpVector = subMatrix.extractVector(false, i);
			if (validate(snpVector))
				testMatrix = testMatrix.combine(0, SimpleMatrix.END, snpVector);
		}
		return testMatrix;
	}
	
	private double correct(LinkedQueue<MaxPQ<EigenInd>> pops) {
		MinPQ<PopVar> vars = new MinPQ<PopVar>();
		PopVar v = new PopVar();
		v.ids = new LinkedQueue<LinkedQueue<Integer>>();
		for (MaxPQ<EigenInd> pop:pops) {
			v.var += var(pop);
			LinkedQueue<Integer> popIds = new LinkedQueue<Integer>();
			for (EigenInd ind:pop) {
				popIds.enqueue(ind.id);
			}
			v.ids.enqueue(popIds);
		}
		vars.insert(v);
		// System.out.println(v.var);
		
		while (pops.peek().size() > 0) {
			MaxPQ<EigenInd> pop0 = pops.dequeue();
			MaxPQ<EigenInd> pop1 = pops.dequeue();
			pop1.insert(pop0.delMax());
			v = new PopVar();
			v.var = var(pop0) + var(pop1);
			v.ids = new LinkedQueue<LinkedQueue<Integer>>();
			LinkedQueue<Integer> popIds = new LinkedQueue<Integer>();
			for (EigenInd ind:pop0) {
				popIds.enqueue(ind.id);
			}
			v.ids.enqueue(popIds);
			popIds = new LinkedQueue<Integer>();
			for (EigenInd ind:pop1) {
				popIds.enqueue(ind.id);
			}
			v.ids.enqueue(popIds);
			vars.insert(v);
			pops.enqueue(pop0);
			pops.enqueue(pop1);
			// System.out.println(v.var);
		}
		
		// System.out.println(vars.min().var);
		return vars.min().var;
	}
	
	private double var(MaxPQ<EigenInd> pop) {
		double mean = 0.0;
		double mean2 = 0.0;
		for (EigenInd ind:pop) {
			mean += ind.evec;
			mean2 += ind.evec * ind.evec;
		}
		mean = mean / pop.size();
		mean2 = mean2 / pop.size();
		return mean2 - mean * mean;
	}
	
	private boolean validate(SimpleMatrix vector) {
		boolean isBiallelic = false;
		boolean isMissing = false;
		int indNum = vector.numRows();
		for (int i = 0; i < indNum; i++) {
			if (vector.get(i,0) == 9.0) {
				isMissing = true;
				break;
			}
			if (vector.get(i,0) != vector.get(0, 0)) {
				isBiallelic = true;
			}
		}
		
		if (!isBiallelic && vector.get(0,0) == 1.0) {
			isBiallelic = true;
		}
		
		if (isBiallelic && !isMissing)
			return true;
		return false;
	}
	
	/**
	 * Unit tests the <tt>PCHCluster<tt> data type  
	 */
	public static void main(String[] args) {
		String genoFileName = args[0];
		String indFileName = args[1];
		String snpFileName = args[2];
		
		Stopwatch elaspe = new Stopwatch();
		EigenSoft eigensoft = new EigenSoft(genoFileName, indFileName, snpFileName);
		LinkedQueue<Integer> indIds = new LinkedQueue<Integer>();
		for (int i = 0; i < eigensoft.getIndSize(); i++) {
			indIds.enqueue(i);
		}
		System.out.println("Load Data Time: " + elaspe.elapsedTime() + "s");
		elaspe = new Stopwatch();
		PCHCluster pchc = new PCHCluster(eigensoft.getGeno());
		LinkedQueue<Integer> pop0 = pchc.runEigenTest(indIds).peek();
		System.out.println("Run EigenTest Time: " + elaspe.elapsedTime() + "s");
		/*int j = 0;
		for (Integer index:pop0) {
				System.out.println(eigensoft.getIndId(index));
				j++;
		};
		System.out.println(j);*/
		
		elaspe = new Stopwatch();
		pchc.run();
		System.out.println("Run PCHCluster Time: " + elaspe.elapsedTime() + "s");
		/*if ((pchc.root.lchild.lchild == null) && (pchc.root.lchild.rchild == null)) {
			System.out.println("True");
		}
		if ((pchc.root.rchild.lchild == null) && (pchc.root.rchild.rchild == null)) {
			System.out.println("True");
		}
		System.out.println(pchc.root.lchild.indIds.size());
		System.out.println(pchc.root.rchild.indIds.size());*/
	}

}
