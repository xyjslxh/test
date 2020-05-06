package com.test;

public class DaemonDemo {
	public static void main(String[] args) {
		Thread daemonThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						System.out.println("i am alive");
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						System.out.println("finally block");
					}
				}
			}
		});
		daemonThread.setDaemon(true);
		daemonThread.start();
		// ȷ��main�߳̽���ǰ�ܸ�daemonThread�ܹ��ֵ�ʱ��Ƭ
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
