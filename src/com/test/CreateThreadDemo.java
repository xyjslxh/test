package com.test;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import sun.misc.Unsafe;

public class CreateThreadDemo {

	public static void main(String[] args)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// 1.�̳�Thread
		Thread thread = new Thread() {
			@Override
			public void run() {
				System.out.println("�̳�Thread");
				super.run();
			}
		};
		thread.start();
		// 2.ʵ��runable�ӿ�
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("ʵ��runable�ӿ�");
			}
		});
		thread1.start();
		// 3.ʵ��callable�ӿ�
		ExecutorService service = Executors.newSingleThreadExecutor();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Future<String> future = service.submit(new Callable() {
			@Override
			public String call() throws Exception {
				return "ͨ��ʵ��Callable�ӿ�";
			}
		});
		try {
			String result = future.get();
			System.out.println(result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

}
