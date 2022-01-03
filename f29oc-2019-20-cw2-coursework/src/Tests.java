import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
import java.sql.ResultSet;

import junit.framework.Test;

/**
 * @author mikec
 *
 */
class Tests {
	int waitTime = 1500;
	//@org.junit.jupiter.api.Test
	//Example Test
	void test() {
		JobDispatcher dispatcher = new JobDispatcher();
		//Specify job for 3 Compute threads and 0 Storage threads
		dispatcher.specifyJob(1, 0);

		//But start only one Compute thread:
		Thread computeThread = new Thread() {			
			public void run () {
			}
		};	

		computeThread.start();

		//Wait for set time and assume that execution has finished:
		try {Thread.sleep(waitTime);} catch (InterruptedException e) {e.printStackTrace(); };
		String name = "Test1";
		ThreadHelper.printThread(name);

		//System.out.println("\nName: "+computeThread.getName()+", State: "+computeThread.getState());

		//The single Compute thread should be blocked waiting: 
		assertEquals(Thread.State.WAITING, computeThread.getState()); 
	}


	@org.junit.jupiter.api.Test
	void UR1a() {
		//the desired number of compute and storage threads
		int nComputeThreads = 4, nStorageThreads = 0, 
				extraComputeThreads = 2, extraStorageThreads = 4;

		//the count of threads in state waiting 
		int numComputeWaiting=0, numStorageWaiting=0, 
				numComputeTerminated=0, numStorageTerminated=0;
		JobDispatcher dispatcher = new JobDispatcher();

		dispatcher.specifyJob(nComputeThreads, nStorageThreads);

		Thread[][] arr = DefineJobs.SingleJob(nComputeThreads, extraComputeThreads, 
				nStorageThreads, extraStorageThreads, dispatcher);
		Thread[] computeThreads = arr[0];
		Thread[] storageThreads = arr[1];

		//Wait for set time and assume that execution has finished:
		try {Thread.sleep(waitTime);} catch (InterruptedException e) {e.printStackTrace(); };

		//iterates through array of threads
		for (int k=0;k<computeThreads.length;k++) {
			//add one to count of numComputeTerminated if a thread in the array is terminated, i.e. has completed it job 
			if(Thread.State.TERMINATED == computeThreads[k].getState()) {
				numComputeTerminated++;}
			//add one to count of numComputeWaiting if a thread in the array is waiting, i.e. wasn't needed for the job 
			if(Thread.State.WAITING == computeThreads[k].getState()) {
				numComputeWaiting++;	}
		}		
		for (int l=0;l<storageThreads.length;l++) {
			//add one to count of storageThreads if a thread in the array is waiting 
			if(Thread.State.TERMINATED == storageThreads[l].getState()) {
				numStorageTerminated++;}
			if(Thread.State.WAITING == storageThreads[l].getState()) {
				numStorageWaiting++;}
		}

		//prints the name and state of each thread
		String name = "UR1b";
		ThreadHelper.printThread(name);

		ThreadHelper.printResults(computeThreads.length,numComputeWaiting, extraComputeThreads, numComputeTerminated, nComputeThreads, "compute");

		ThreadHelper.printResults(storageThreads.length,numStorageWaiting, extraStorageThreads, numStorageTerminated, nStorageThreads, "storage");


		//test passes if the number of waiting threads == the number of desired threads
		assertEquals(numComputeTerminated, nComputeThreads);
		assertEquals(numStorageTerminated, nStorageThreads);
		assertEquals(numComputeWaiting, extraComputeThreads);
		assertEquals(numStorageWaiting, extraStorageThreads);

	}

	@org.junit.jupiter.api.Test
	void UR1b() {
		//the desired number of compute and storage threads
		int nComputeThreads = 4, nStorageThreads = 0, 
				extraComputeThreads = 0, extraStorageThreads = 0;

		//the count of threads in state waiting 
		int numComputeWaiting=0, numStorageWaiting=0, 
				numComputeTerminated=0, numStorageTerminated=0;
		JobDispatcher dispatcher = new JobDispatcher();

		dispatcher.specifyJob(nComputeThreads, nStorageThreads);

		Thread[][] arr = DefineJobs.SingleJob(nComputeThreads, extraComputeThreads, 
				nStorageThreads, extraStorageThreads, dispatcher);
		Thread[] computeThreads = arr[0];
		Thread[] storageThreads = arr[1];

		//Wait for set time and assume that execution has finished:
		try {Thread.sleep(waitTime);} catch (InterruptedException e) {e.printStackTrace(); };

		//iterates through array of threads
		for (int k=0;k<computeThreads.length;k++) {
			//add one to count of numComputeTerminated if a thread in the array is terminated, i.e. has completed it job 
			if(Thread.State.TERMINATED == computeThreads[k].getState()) {
				numComputeTerminated++;}
			//add one to count of numComputeWaiting if a thread in the array is waiting, i.e. wasn't needed for the job 
			if(Thread.State.WAITING == computeThreads[k].getState()) {
				numComputeWaiting++;	}
		}		
		for (int l=0;l<storageThreads.length;l++) {
			//add one to count of storageThreads if a thread in the array is waiting 
			if(Thread.State.TERMINATED == storageThreads[l].getState()) {
				numStorageTerminated++;}
			if(Thread.State.WAITING == storageThreads[l].getState()) {
				numStorageWaiting++;}
		}

		//prints the name and state of each thread
		String name = "UR1b";
		ThreadHelper.printThread(name);

		ThreadHelper.printResults(computeThreads.length,numComputeWaiting, extraComputeThreads, numComputeTerminated, nComputeThreads, "compute");

		ThreadHelper.printResults(storageThreads.length,numStorageWaiting, extraStorageThreads, numStorageTerminated, nStorageThreads, "storage");


		//test passes if the number of waiting threads == the number of desired threads
		assertEquals(numComputeTerminated, nComputeThreads);
		assertEquals(numStorageTerminated, nStorageThreads);
		assertEquals(numComputeWaiting, extraComputeThreads);
		assertEquals(numStorageWaiting, extraStorageThreads);
	}


	@org.junit.jupiter.api.Test
	void UR1c() {
		//the desired number of compute and storage threads
		//test should fail as there are not enough compute threads
		int nComputeThreads = 4, nStorageThreads = 4, 
				extraComputeThreads = 2, extraStorageThreads = 6;

		//the count of threads in state waiting 
		int numComputeWaiting=0, numStorageWaiting=0, 
				numComputeTerminated=0, numStorageTerminated=0;
		JobDispatcher dispatcher = new JobDispatcher();

		dispatcher.specifyJob(nComputeThreads, nStorageThreads);

		Thread[][] arr = DefineJobs.SingleJob(nComputeThreads, extraComputeThreads, 
				nStorageThreads, extraStorageThreads, dispatcher);
		Thread[] computeThreads = arr[0];
		Thread[] storageThreads = arr[1];


		//Wait for set time and assume that execution has finished:
		try {Thread.sleep(waitTime);} catch (InterruptedException e) {e.printStackTrace(); };

		//iterates through array of threads
		for (int k=0;k<computeThreads.length;k++) {
			//add one to count of numComputeTerminated if a thread in the array is terminated, i.e. has completed it job 
			if(Thread.State.TERMINATED == computeThreads[k].getState()) {
				numComputeTerminated++;}
			//add one to count of numComputeWaiting if a thread in the array is waiting, i.e. wasn't needed for the job 
			if(Thread.State.WAITING == computeThreads[k].getState()) {
				numComputeWaiting++;	}
		}		
		for (int l=0;l<storageThreads.length;l++) {
			//add one to count of storageThreads if a thread in the array is waiting 
			if(Thread.State.TERMINATED == storageThreads[l].getState()) {
				numStorageTerminated++;}
			if(Thread.State.WAITING == storageThreads[l].getState()) {
				numStorageWaiting++;}
		}

		//prints the name and state of each thread
		String name = "UR1c";
		ThreadHelper.printThread(name);

		ThreadHelper.printResults(computeThreads.length,numComputeWaiting, extraComputeThreads, numComputeTerminated, nComputeThreads, "compute");

		ThreadHelper.printResults(storageThreads.length,numStorageWaiting, extraStorageThreads, numStorageTerminated, nStorageThreads, "storage");


		//test passes if the number of waiting threads == the number of desired threads
		assertEquals(numComputeTerminated, nComputeThreads);
		assertEquals(numStorageTerminated, nStorageThreads);
		assertEquals(numComputeWaiting, extraComputeThreads);
		assertEquals(numStorageWaiting, extraStorageThreads);
	}

	@org.junit.jupiter.api.Test
	void UR1d() {
		//the desired number of compute and storage threads
		//test should fail as there are not enough compute threads
		int nComputeThreads = 4, nStorageThreads = 4, 
				extraComputeThreads = 2, extraStorageThreads = 6;

		//the count of threads in state waiting 
		int numComputeWaiting=0, numStorageWaiting=0, 
				numComputeTerminated=0, numStorageTerminated=0;
		JobDispatcher dispatcher = new JobDispatcher();

		dispatcher.specifyJob(3, 4);
		dispatcher.specifyJob(2, 1);
		
		Thread[][] arr = DefineJobs.SingleJob(nComputeThreads, extraComputeThreads, 
				nStorageThreads, extraStorageThreads, dispatcher);
		Thread[] computeThreads = arr[0];
		Thread[] storageThreads = arr[1];


		//Wait for set time and assume that execution has finished:
		try {Thread.sleep(waitTime);} catch (InterruptedException e) {e.printStackTrace(); };

		//iterates through array of threads
		for (int k=0;k<computeThreads.length;k++) {
			//add one to count of numComputeTerminated if a thread in the array is terminated, i.e. has completed it job 
			if(Thread.State.TERMINATED == computeThreads[k].getState()) {
				numComputeTerminated++;}
			//add one to count of numComputeWaiting if a thread in the array is waiting, i.e. wasn't needed for the job 
			if(Thread.State.WAITING == computeThreads[k].getState()) {
				numComputeWaiting++;	}
		}		
		for (int l=0;l<storageThreads.length;l++) {
			//add one to count of storageThreads if a thread in the array is waiting 
			if(Thread.State.TERMINATED == storageThreads[l].getState()) {
				numStorageTerminated++;}
			if(Thread.State.WAITING == storageThreads[l].getState()) {
				numStorageWaiting++;}
		}

		//prints the name and state of each thread
		String name = "UR1d";
		ThreadHelper.printThread(name);

		ThreadHelper.printResults(computeThreads.length,numComputeWaiting, extraComputeThreads, numComputeTerminated, nComputeThreads, "compute");

		ThreadHelper.printResults(storageThreads.length,numStorageWaiting, extraStorageThreads, numStorageTerminated, nStorageThreads, "storage");


		//test passes if the number of waiting threads == the number of desired threads
		assertEquals(numComputeTerminated, nComputeThreads);
		assertEquals(numStorageTerminated, nStorageThreads);
		assertEquals(numComputeWaiting, extraComputeThreads);
		assertEquals(numStorageWaiting, extraStorageThreads);
	}

	@org.junit.jupiter.api.Test
	void UR2a() {

		int nComputeThreads = 4, nStorageThreads = 4;

		//the count of threads in state waiting 
		int numComputeWaiting=0,numStorageWaiting =0, 
				numComputeTerminated=0, numStorageTerminated=0;

		int numJobs = 2;


		Thread[][] arr = DefineJobs.multipleJobs(nComputeThreads,nStorageThreads,numJobs);
		//array of compute threads
		Thread[] computeThreads = arr[0];
		//array of storae threads
		Thread[] storageThreads = arr[1];


		//prints the name and state of each thread
		String name = "UR2a";
		ThreadHelper.printThread(computeThreads, storageThreads, name);


		//iterates through array of threads
		for (int k=0;k<computeThreads.length;k++) {
			//add one to count of numComputeTerminated if a thread in the array is terminated, i.e. has completed it job 
			if(Thread.State.TERMINATED == computeThreads[k].getState()) {
				numComputeTerminated++;}
			//add one to count of numComputeWaiting if a thread in the array is waiting, i.e. wasn't needed for the job 
			if(Thread.State.WAITING == computeThreads[k].getState()) {
				numComputeWaiting++;	}
		}		
		for (int l=0;l<storageThreads.length;l++) {
			//add one to count of storageThreads if a thread in the array is waiting 
			if(Thread.State.TERMINATED == storageThreads[l].getState()) {
				numStorageTerminated++;}
			if(Thread.State.WAITING == storageThreads[l].getState()) {
				numStorageWaiting++;}
		}
		nComputeThreads *= numJobs;
		nStorageThreads *= numJobs; 

		ThreadHelper.printResults(computeThreads.length, numComputeWaiting, 0, numComputeTerminated, nComputeThreads, "compute");

		ThreadHelper.printResults(storageThreads.length, numStorageWaiting, 0, numStorageTerminated, nStorageThreads, "storage");


		//test passes if the number of waiting threads == the number of desired threads
		assertEquals(numComputeTerminated, nComputeThreads);
		assertEquals(numStorageTerminated, nStorageThreads);
		assertEquals(numComputeWaiting, 0);
		assertEquals(numStorageWaiting, 0);
	}

	@org.junit.jupiter.api.Test
	void UR2b() {

		int nComputeThreads = 4, nStorageThreads = 4, 
				extraComputeThreads = 2, extraStorageThreads = 2;

		//the count of threads in state waiting 
		int numComputeWaiting=0,numStorageWaiting =0, 
				numComputeTerminated=0, numStorageTerminated=0;

		int numJobs = 2;


		Thread[][] arr = DefineJobs.multipleJobs(nComputeThreads, extraComputeThreads, 
				nStorageThreads, extraStorageThreads, numJobs);
		//array of compute threads
		Thread[] computeThreads = arr[0];
		//array of storae threads
		Thread[] storageThreads = arr[1];


		//prints the name and state of each thread
		String name = "UR2b";
		ThreadHelper.printThread(computeThreads, storageThreads, name);


		//iterates through array of threads
		for (int k=0;k<computeThreads.length;k++) {
			//add one to count of numComputeTerminated if a thread in the array is terminated, i.e. has completed it job 
			if(Thread.State.TERMINATED == computeThreads[k].getState()) {
				numComputeTerminated++;}
			//add one to count of numComputeWaiting if a thread in the array is waiting, i.e. wasn't needed for the job 
			if(Thread.State.WAITING == computeThreads[k].getState()) {
				numComputeWaiting++;	}
		}		
		for (int l=0;l<storageThreads.length;l++) {
			//add one to count of storageThreads if a thread in the array is waiting 
			if(Thread.State.TERMINATED == storageThreads[l].getState()) {
				numStorageTerminated++;}
			if(Thread.State.WAITING == storageThreads[l].getState()) {
				numStorageWaiting++;}
		}
		nComputeThreads *= numJobs;
		nStorageThreads *= numJobs; 
		extraComputeThreads *= numJobs;
		extraStorageThreads *= numJobs;

		ThreadHelper.printResults(computeThreads.length, numComputeWaiting, extraComputeThreads, numComputeTerminated, nComputeThreads, "compute");

		ThreadHelper.printResults(storageThreads.length, numStorageWaiting, extraStorageThreads, numStorageTerminated, nStorageThreads, "storage");


		//test passes if the number of waiting threads == the number of desired threads
		assertEquals(numComputeTerminated, nComputeThreads);
		assertEquals(numStorageTerminated, nStorageThreads);
		assertEquals(numComputeWaiting, extraStorageThreads);
		assertEquals(numStorageWaiting, extraStorageThreads);
	}
}
