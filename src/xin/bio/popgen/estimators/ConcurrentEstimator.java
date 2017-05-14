package xin.bio.popgen.estimators;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import xin.bio.popgen.infos.IndInfo;

public abstract class ConcurrentEstimator extends Estimator {
	
	protected static final int BATCHSIZE = 1_000_000_000;
	
	// an integer stores the number of threads to be used
	protected final int thread;
	
	// an integer stores the number of threads to be used
	protected final List<Future<String>> results;
	
	protected final ReentrantLock l = new ReentrantLock();
	
	public ConcurrentEstimator(IndInfo sampleInfo, int snpNum, int thread) {
		super(sampleInfo, snpNum);
		this.thread = thread;
		this.results = new ArrayList<>();
	}
	
	protected void readFile(BufferedReader br) {
		int lineNum = BATCHSIZE / indNum; // limit the size of lines processed by each thread 
		int remainedLineNum = snpNum % lineNum;
		int chunkNum = snpNum / lineNum;
		int submittedChunkNum = 0;
		int startIndex = 0;
		ExecutorService executor = Executors.newFixedThreadPool(thread);
		Semaphore threadSemaphore = new Semaphore(thread);
    	CountDownLatch doneSignal = new CountDownLatch(chunkNum + 1);
    	try {
	    	while (submittedChunkNum != chunkNum) {
	    		// read new lines and submit new task if at least one thread is available
	    		if (threadSemaphore.availablePermits() > 0) {
		    		char[][] lines = new char[lineNum][indNum];
					for (int i = 0; i < lineNum; i++) {
						br.read(lines[i]);
						br.read();
					}
					executor.submit(creatCounter(lines, startIndex, 
							threadSemaphore, doneSignal));
					startIndex += lineNum;
					submittedChunkNum++; 
	    		}
	    	}
	    	if (remainedLineNum != 0) {
				char[][] lines = new char[remainedLineNum][indNum];
				for (int i = 0; i < remainedLineNum; i++) {
					br.read(lines[i]); 
					br.read();
				}
				executor.submit(creatCounter(lines, startIndex, 
						threadSemaphore, doneSignal));
	    	}
	    	else {
	    		doneSignal.countDown();
	    	}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
		// wait for all threads end their work
	    try {
			doneSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			
		} finally {
			executor.shutdown();
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void parseLine(char[] cbuf) {}
	
	protected abstract Runnable creatCounter(char[][] lines, int startIndex, 
			Semaphore threadSemaphore, CountDownLatch doneSignal);

}
