/** A demo of mutual exclusion with the 'synchronized' modifier
 * 
 */
package threads;

/**
 * The Account class represents a bank account 
 * @author lm
 *
 */
class Account {
	private String owner;
	private double balance = 0.d;

	public Account(String owner, double balance) {
		super();
		this.owner = owner;
		this.balance = balance;
	}

	public
//	synchronized
	void makeDeposit(double d) {
		synchronized (this) {		
		  double old = balance;
		  System.out.println("Deposit started, balance=" + old);
		  balance = old + d;
		  System.out.println("Deposit FINISHED, balance=" + balance);
		}
	}

	public
	synchronized
	void withdraw(double d) {
		System.out.println("Withdraw STARTED, balance=" + balance);
		if ((balance - d) > 0.d)
			balance -= d;
		System.out.println("Withdraw FINISHED, balance=" + balance);
	}

	@Override
	public
	synchronized
	String toString() {
		return "Account [owner=" + owner + ", balance=" + balance + "]";
	}
}

/** Depositor is a thread which concurrently makes deposits in the selected account
 * @author lm
 *
 */
public class Depositor extends Thread {
	Account account;
	double deposit;

	public Depositor(Account acc, double deposit) {
		super();
		account = acc;
		this.deposit = deposit;
		start();	// make the thread running
	}

	public void run() {
		account.makeDeposit(deposit);
	}
}
