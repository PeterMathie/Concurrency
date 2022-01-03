import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
// Note that you MUST not use any other `thread safe` classes than the two imported above

public class JobDispatcher implements Dispatcher {


	ReentrantLock lock;

	//stores a condition for each Thread
	ArrayList<Condition> compConditions;
	ArrayList<Condition> strConditions;


	//used to keep track of how many threads have already been created, could have used a counter but thought having a list of working threads would be useful
	ArrayList<Thread> jobComputeThreads = new ArrayList<>();
	ArrayList<Thread> jobStorageThreads = new ArrayList<>();

	//the number of threads required for each job
	int nCompute, nStorage;


	@Override
	public void specifyJob(int nComputeThreads, int nStorageThreads) {
		//lets the queue methods see how many threads we are wanting 
		nCompute = nComputeThreads;
		nStorage = nStorageThreads;

		compConditions = new ArrayList<>();
		strConditions = new ArrayList<>();
		
		lock = new ReentrantLock();

	}


	public void queueComputeThread() {

		//lock all threads entering the queue
		System.out.println(Thread.currentThread().getName()+"'s turn\n");
		lock.lock();

		//create new condition for every thread
		Condition compCondition = lock.newCondition();

		//add to ArrayList of conditions
		compConditions.add(compCondition);

		System.out.println(Thread.currentThread().getName()+" is HAS control of lock\n");

		//add threads to list of threads
		jobComputeThreads.add(Thread.currentThread());

		try {
			//waits until signalled 
			compCondition.await();
			System.out.println(Thread.currentThread().getName()+" is executing job\n");
		} 
		catch (InterruptedException e) {e.printStackTrace();}

		//to save my CPU
		finally {lock.unlock();}

	}

	@Override
	public void queueStorageThread() {

		//lock all threads entering the queue
		System.out.println(Thread.currentThread().getName()+"'s turn\n");
		lock.lock();

		//create new condition for every thread
		Condition strCondition = lock.newCondition();

		//add to ArrayList of conditions
		strConditions.add(strCondition);

		System.out.println(Thread.currentThread().getName()+" is HAS control of lock\n");

		//add threads to list of threads
		jobStorageThreads.add(Thread.currentThread());

		try {
			//waits until signalled 
			strCondition.await();
			System.out.println(Thread.currentThread().getName()+" is executing job\n");
		} 
		catch (InterruptedException e) {e.printStackTrace();}

		//to save my CPU
		finally {lock.unlock();}

	}

	public void signalComputeThreads() {
		lock.lock();
		try {
			//only iterates though list if there are at least enough threads for the job
			if(compConditions.size()>=nCompute) {
				//signal the last nCompute threads by going backwards through arraylist of conditions
				for(int i=(compConditions.size()-1);i>=(compConditions.size()-nCompute);i--) {
					compConditions.get(i).signal();
					System.out.println("comCondition index = "+ i);
				}
			} 
		}
		finally {lock.unlock();}
	}

	public void signalStorageThreads() {
		lock.lock();
		try {
			if(strConditions.size()>=nStorage) {
				//signal the last nCompute threads by going backwards thorugh arraylist of conditions
				for(int i=(strConditions.size()-1);i>=(strConditions.size()-nStorage);i--) {
					strConditions.get(i).signal();
					System.out.println("strCondition index = "+ i);
				}
			} 
		}
		finally {lock.unlock();}
	} 

	
}
