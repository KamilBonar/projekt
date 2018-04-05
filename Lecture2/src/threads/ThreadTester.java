/**
 * 
 */
package threads;

class SimpleThread extends Thread {
	private int counter = 3;
	private static int threadCount = 0;

	public SimpleThread() {
		super("" + ++threadCount); // Store the thread name
		start();
	}

	public String toString() {
		return "#" + getName() + ": " + counter;
	}

	public void run() {
		while (true) {
			System.out.println(this);
			if (--counter == 0)
				return;
			try {
				sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}

/**
 * Following is the excerpt from "The Complete Reference, Java, by Herbert Shieldt":
 * As an absolute value, a priority is meaningless; a higher-priority thread doesn’t run any faster
 * than a lower-priority thread if it is the only thread running. 
 * Instead, a thread’s priority is used to decide when to switch from one running thread to the next. 
 * This is called a context switch. 
 * The rules that determine when a context switch takes place are simple:
 * 1. A thread can voluntarily relinquish control. This is done by explicitly yielding, sleeping, 
 *    or blocking on pending I/O. In this scenario, all other threads are examined, and
 *    the highest-priority thread that is ready to run is given the CPU.
 * 2. A thread can be preempted by a higher-priority thread. In this case, a lower-priority thread that 
 *    does not yield the processor is simply preempted — no matter what it is doing — by a higher-priority
 *    thread. Basically, as soon as a higher-priority thread wants to run, it does. 
 *    This is called preemptive multitasking.
 * 
 * @author lm
 *
 */
class PriorityThread extends Thread {
	private int counter = 5;
	private volatile double f = 0;

	public PriorityThread(int priority) {
		setPriority(priority);
		start();
	}

	public void run() {
		while (true) {
			// An expensive, interruptible operation:
			for (int i = 1; i < 100000000; i++)
				f = f + (Math.PI + Math.E) / (double) i;
			System.out.println(this);
			if (--counter == 0) {
				System.out.println("End of " + this + " : f=" + f);
				return;
			}
		}
	}
}

/** Thread testing class
 * @author lm
 */
public class ThreadTester {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ThreadTester threads = new ThreadTester();
//		 threads.testSimple();
//		 threads.testJoin();
//		 threads.testPriority();
		threads.testDeposits();
	}

	private void testDeposits() {
		Account account = new Account("LM", 0.d);
		double[] deposits = { 10.d, 20.d, 30.d };
		// here we create the Depositor threads and run them
		Depositor[] depositors = { 
				new Depositor(account, deposits[0]), 
				new Depositor(account, deposits[1]),
				new Depositor(account, deposits[2]) };
		try {
			for (Depositor t : depositors)
				t.join(); // and here we wait for completion
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Account FINAL balance=" + account.toString());
	}

	// testing simple threads
	void testSimple() {
		for (int i = 0; i < 3; i++)
			new SimpleThread();
	}

	// testing simple threads with join()
	void testJoin() throws InterruptedException {
		for (int i = 0; i < 3; i++)
			new SimpleThread().join();
	}

	// testing threads with priorities
	void testPriority() {
		new PriorityThread(Thread.MAX_PRIORITY);
		for(int i = 0; i < 2; i++)	
			new PriorityThread(Thread.MIN_PRIORITY);
	}
}
