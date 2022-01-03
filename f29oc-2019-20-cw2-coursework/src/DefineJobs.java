import java.lang.reflect.Array;

public class DefineJobs {
	public static Thread[][] SingleJob(int nComputeThreads, int nStorageThreads, JobDispatcher dispatcher) {
		int waitTime = 1000;
		System.out.println("----NEW JOB----");

		//makes the correct amount of computeThreads and storageThreads -> TEST SHOULD PASS
		Thread[] computeThreads = ThreadHelper.makeThreads(nComputeThreads, false, dispatcher);
		Thread[] storageThreads = ThreadHelper.makeThreads(nStorageThreads, true, dispatcher);

		Thread[][] arr = {computeThreads, storageThreads};
		//Wait for set time and assume that execution has finished:
		try {Thread.sleep(waitTime);} catch (InterruptedException e) {e.printStackTrace(); };
 
		return arr;
	}
	
	public static Thread[][] SingleJob(int nComputeThreads, int extraComputeThreads, int nStorageThreads, int extraStorageThreads, JobDispatcher dispatcher) {
		int waitTime = 100;
		System.out.println("----NEW JOB----");

		//makes threads but can make more or less depending on the extra
		Thread[] computeThreads = ThreadHelper.makeThreads((nComputeThreads+extraComputeThreads), false, dispatcher);
		Thread[] storageThreads = ThreadHelper.makeThreads((nStorageThreads+extraStorageThreads), true, dispatcher);

		Thread[][] arr = {computeThreads, storageThreads};
		
		//Wait for set time and assume that execution has finished:
		try {Thread.sleep(waitTime);} catch (InterruptedException e) {e.printStackTrace(); };

		return arr;
	}
	
	//joins two arrays of threads together
	public static Thread[] concatenate(Thread[] a, Thread[] b) {
		int aLen = a.length;
		int bLen = b.length; 

		Thread[] c = (Thread[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		for(int i=0;i<c.length;i++) {
			// System.out.println(c[i].getName());

		}
		return c;
	}
	public static Thread[][] multipleJobs(int nComputeThreads, int extraComputeThreads, int nStorageThreads, int extraStorageThreads, int numJobs) {
		
		int waitTime = 100;
		
		//init array which store the threads
		Thread[] computeThreads = new Thread[0];
		Thread[] storageThreads = new Thread[0];

		for(int i=0;i<numJobs;i++) {
			System.out.println("----NEW JOB----");
			JobDispatcher dispatcher = new JobDispatcher();
			dispatcher.specifyJob(nComputeThreads, nStorageThreads);
			
			int nc = nComputeThreads + extraComputeThreads;
			int ns =nStorageThreads + extraStorageThreads;
			
			computeThreads = concatenate(computeThreads, ThreadHelper.makeThreads(nc, false, dispatcher));
			storageThreads = concatenate(storageThreads, ThreadHelper.makeThreads(ns, true, dispatcher));

			try {Thread.sleep(waitTime);} catch (InterruptedException e) {e.printStackTrace(); };

		}
		//Wait for set time and assume that execution has finished:
		try {Thread.sleep(waitTime);} catch (InterruptedException e) {e.printStackTrace(); };
		
		Thread[][] arr = new Thread[2][];	
		arr[0] = computeThreads; 
		arr[1] = storageThreads;
		
		return arr;
		
	}
	
	public static Thread[][] multipleJobs(int nComputeThreads, int nStorageThreads, int numJobs) {
		int waitTime = 100;
		
		//init array which store the threads
		Thread[] computeThreads = new Thread[0];
		Thread[] storageThreads = new Thread[0];

		for(int i=0;i<numJobs;i++) {
			System.out.println("----NEW JOB----");
			JobDispatcher dispatcher = new JobDispatcher();
			dispatcher.specifyJob(nComputeThreads, nStorageThreads);
			
			storageThreads = concatenate(storageThreads, ThreadHelper.makeThreads((nStorageThreads), true, dispatcher));

			computeThreads = concatenate(computeThreads, ThreadHelper.makeThreads((nComputeThreads), false, dispatcher));

			try {Thread.sleep(waitTime);} catch (InterruptedException e) {e.printStackTrace(); };

		}
		//Wait for set time and assume that execution has finished:
		try {Thread.sleep(waitTime);} catch (InterruptedException e) {e.printStackTrace(); };
		
		Thread[][] arr = new Thread[2][];	
		arr[0] = computeThreads; 
		arr[1] = storageThreads;
		
		return arr;
	}

}
