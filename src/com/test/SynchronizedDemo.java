package com.test;

public class SynchronizedDemo {
	public static void main(String[] args) {
		MonitorDemo monitorDemo = new MonitorDemo();
		System.out.println(monitorDemo.reader());
	}

	private static void method() {
	}
}

class MonitorDemo {
	private int a = 0;

	public synchronized int writer() { // 1
		a++; // 2
		return a;
	} // 3

	public synchronized int reader() { // 4
		int i = a; // 5
		return i;
	} // 6
}
