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
		// 1.继承Thread
		Thread thread = new Thread() {
			@Override
			public void run() {
				System.out.println("继承Thread");
				super.run();
			}
		};
		thread.start();
		// 2.实现runable接口
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("实现runable接口");
			}
		});
		thread1.start();
		// 3.实现callable接口
		ExecutorService service = Executors.newSingleThreadExecutor();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Future<String> future = service.submit(new Callable() {
			@Override
			public String call() throws Exception {
				return "通过实现Callable接口";
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
