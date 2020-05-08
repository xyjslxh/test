package com.test;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                        System.out.println("������" + Thread.currentThread().getName() + "  list�Դﵽ�������������wait");
                        list.wait();
                        System.out.println("������" + Thread.currentThread().getName() + "  �˳�wait");
                    }
                    Random random = new Random();
                    int i = random.nextInt();
                    System.out.println("������" + Thread.currentThread().getName() + " ��������" + i);
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
                        System.out.println("������" + Thread.currentThread().getName() + "  listΪ�գ�����wait");
                        list.wait();
                        System.out.println("������" + Thread.currentThread().getName() + "  �˳�wait");
                    }
                    Integer element = list.remove(0);
                    System.out.println("������" + Thread.currentThread().getName() + "  �������ݣ�" + element);
                    list.notifyAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

}

