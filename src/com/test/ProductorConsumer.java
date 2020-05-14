package com.test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

public class ProductorConsumer {

	public static void main(String[] args) {

		LinkedList linkedList = new LinkedList();
		ExecutorService service = Executors.newFixedThreadPool(15);
		for (int i = 0; i < 5; i++) {
			service.submit(new Productor(linkedList, 8));
		}

		for (int i = 0; i < 10; i++) {
			service.submit(new Consumer(linkedList));
		}

	}

	static class Productor implements Runnable {

		private List<Integer> list;
		private int maxLength;

		public Productor(List list, int maxLength) {
			this.list = list;
			this.maxLength = maxLength;
		}

		@Override
		public void run() {
			while (true) {
				synchronized (list) {
					try {
						while (list.size() == maxLength) {
							System.out.println("生产者" + Thread.currentThread().getName() + "  list以达到最大容量，进行wait");
							list.wait();
							System.out.println("生产者" + Thread.currentThread().getName() + "  退出wait");
						}
						Random random = new Random();
						int i = random.nextInt();
						System.out.println("生产者" + Thread.currentThread().getName() + " 生产数据" + i);
						list.add(i);
						list.notifyAll();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	static class Consumer implements Runnable {

		private List<Integer> list;

		public Consumer(List list) {
			this.list = list;
		}

		@Override
		public void run() {
			while (true) {
				synchronized (list) {
					try {
						while (list.isEmpty()) {
							System.out.println("消费者" + Thread.currentThread().getName() + "  list为空，进行wait");
							list.wait();
							System.out.println("消费者" + Thread.currentThread().getName() + "  退出wait");
						}
						Integer element = list.remove(0);
						System.out.println("消费者" + Thread.currentThread().getName() + "  消费数据：" + element);
						list.notifyAll();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private static LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

	@Test
	public void blockingQueueTest() {
		ExecutorService service = Executors.newFixedThreadPool(15);
		for (int i = 0; i < 5; i++) {
			service.submit(new BlockingQueueProductor(queue));
		}
		for (int i = 0; i < 10; i++) {
			service.submit(new BlockingQueueConsumer(queue));
		}
	}

	static class BlockingQueueProductor implements Runnable {

		private BlockingQueue queue;

		public BlockingQueueProductor(BlockingQueue queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				while (true) {
					Random random = new Random();
					int i = random.nextInt();
					System.out.println("生产者" + Thread.currentThread().getName() + "生产数据" + i);
					queue.put(i);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static class BlockingQueueConsumer implements Runnable {
		private BlockingQueue queue;

		public BlockingQueueConsumer(BlockingQueue queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				while (true) {
					Integer element = (Integer) queue.take();
					System.out.println("消费者" + Thread.currentThread().getName() + "正在消费数据" + element);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
