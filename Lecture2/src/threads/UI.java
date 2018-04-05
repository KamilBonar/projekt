package threads;

import java.io.IOException;

class Unresponsive {
	static volatile double d = 1.d;

	public Unresponsive() {
		while (true)
			d = d + (Math.PI + Math.E) / d;
	}
}

class Responsive extends Thread {
	static volatile double d = 1.d;

	public Responsive() {
		setDaemon(true);
		start();
	}

	public void run() {
		while (true) {
			d = d + (Math.PI + Math.E) / d;
		}
	}
}

public class UI {

	public static void main(String[] args) {
		new Unresponsive(); // Must kill this process
//		new Responsive();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			System.out.println("Ready for input ... ");
			System.in.read();
			System.out.println("Responsive accumulated value " + Responsive.d); // Shows progress
			System.out.println("Waiting ... ");
			Thread.sleep(3000);
		  System.out.println("Responsive accumulated value " + Responsive.d); // Shows progress
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
