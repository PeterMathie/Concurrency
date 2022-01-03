public class ThreadHelper {
	static int waitTime = 10;

	static Thread[] computeThreads;
	static Thread[] storageThreads;

	public static void printThread(String name) {
		String string = "+-------------------------------------------------------+";
		System.out.println("\n"+string);
		System.out.println("|FINAL THREAD STATES "+name+"				|");
		System.out.println(string);

		//iterates through list of threads
		for(Thread t : computeThreads) {
			//prints the name and state of thread
			System.out.println(String.format("|Name: %s, 	| State = %s	|", t.getName(), t.getState()));
		}
		for(Thread t : storageThreads) {
			//prints the name and state of thread
			System.out.println(String.format("|Name: %s, 	| State = %s	|", t.getName(), t.getState()));
		}
		System.out.println(string);

	}

	public static void printThread(Thread[] computeThreads, Thread[] storageThreads, String name) {
		String string = "+-------------------------------------------------------+";
		System.out.println("\n"+string);
		System.out.println("|FINAL THREAD STATES "+name+"				|");
		System.out.println(string);

		//iterates through list of threads
		for(Thread t : computeThreads) {
			//prints the name and state of thread
			System.out.println(String.format("|Name: %s, 	| State = %s	|", t.getName(), t.getState()));
		}
		for(Thread t : storageThreads) {
			//prints the name and state of thread
			System.out.println(String.format("|Name: %s, 	| State = %s	|", t.getName(), t.getState()));
		}
		System.out.println(string);

	}

	public static Thread[] getComputeThreads() {
		//gets the set of all threads
		return computeThreads;
	}

	public static  Thread[]  getStorageThreads() {
		return storageThreads;
	}

	//make nThread number of threads
	public static Thread[] makeThreads(int nThreads, boolean storage, JobDispatcher dispatcher ) {
		Thread[] threads;
		//if you storage bool is true make a store thread
		if(storage) { threads = storageThreads(nThreads, dispatcher);}
		//if storage bool is false make a compute thread
		else {threads = computeThreads(nThreads, dispatcher);
		}

		return threads;
	}

	private static Thread[] computeThreads(int nThreads, JobDispatcher dispatcher) {
		computeThreads = new Thread[nThreads];
		//makes nThread number of threads
		for (int i=0;i<nThreads;i++) {
			computeThreads[i] = new Thread() {
				public void run() {
					dispatcher.queueComputeThread();
				}
			}; 
			//sets name to be computeThread-number
			computeThreads[i].setName("computeThread-"+i);
			//calls the thread to start making it RUNNABLE
			computeThreads[i].start();
		}
		try {Thread.sleep(waitTime);} catch (InterruptedException e) {e.printStackTrace(); };

		dispatcher.signalComputeThreads();


		return computeThreads;		
	}
	//same as computeThreads but makes storageThreads
	private static Thread[]  storageThreads(int nThreads, JobDispatcher dispatcher) {
		storageThreads = new Thread[nThreads];

		for (int i=0;i<nThreads;i++) {
			storageThreads[i] = new Thread(){
				public void run() { 
					dispatcher.queueStorageThread();
				}
			};
			storageThreads[i].setName("storageThread-"+i);
			storageThreads[i].start();
		}
		try {Thread.sleep(waitTime);} catch (InterruptedException e) {e.printStackTrace(); };     
		                                                                                          
		dispatcher.signalStorageThreads();        
		
		return storageThreads;		
	}

	public static void printResults(int length, int numWaiting, int extraThreads, int numTerminated, int nThreads, String type) {
		System.out.println("\nNo. "+type+" threads = "
				+length
				+"\nNo. WAITING "+type+" threads = "
				+numWaiting+", Expected = "
				+extraThreads
				+"\nNo. TERMINATED "+type+" threads = "
				+numTerminated
				+" Expected = "
				+nThreads);

	}

}
