package com.test;

import java.text.NumberFormat;
import java.util.concurrent.ConcurrentHashMap;

public class VolatileExample {
//	private static volatile int counter = 0;
	private static  int counter = 0;

	public static void main(String[] args) {
		ReentrantLock
		for (int i = 0; i < 10; i++) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 10000; i++) {
						synchronized (VolatileExample.class) {
							counter++;
						}

					}
				}
			});
			thread.start();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(counter);
	}
}