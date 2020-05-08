package com.test;

import java.util.ArrayList;
import java.util.List;

public class ConditionChange {
	private static List<String> lockObject = new ArrayList<String>();

	public static void main(String[] args) {
		Consumer consumer1 = new Consumer(lockObject);
		Consumer consumer2 = new Consumer(lockObject);
		Productor productor = new Productor(lockObject);
		consumer1.start();
		consumer2.start();
		productor.start();
	}

	static class Consumer extends Thread {
		private List<String> lock;

		public Consumer(List lock) {
			this.lock = lock;
		}

		@Override
		public void run() {
			synchronized (lock) {
				try {
					// ����ʹ��if�Ļ����ͻ����wait�����仯��ɳ�����������
					while (lock.isEmpty()) {
						System.out.println(Thread.currentThread().getName() + " listΪ��");
						System.out.println(Thread.currentThread().getName() + " ����wait����");
						lock.wait();
						System.out.println(Thread.currentThread().getName() + "  wait��������");
					}
					String element = lock.remove(0);
					System.out.println(System.currentTimeMillis()+Thread.currentThread().getName() + " ȡ����һ��Ԫ��Ϊ��" + element);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	static class Productor extends Thread {
		private List<String> lock;

		public Productor(List lock) {
			this.lock = lock;
		}

		@Override
		public void run() {
			synchronized (lock) {
				System.out.println(Thread.currentThread().getName() + " ��ʼ���Ԫ��");
				for (int i = 0; i < 2; i++) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(System.currentTimeMillis());
					lock.add(i+"");
					lock.notifyAll();
				}
				
			}
		}

	}
}
