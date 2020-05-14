package com.redis;

import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class RedisTester {
	@Test
	public void say() {
		System.out.println("say");

	}

	@Test
	public void redisTester() {
		Jedis jedis = new Jedis("10.1.5.47", 6378, 100000);
		System.out.println(jedis.ping());
		int i = 0;
		try {
			long start = System.currentTimeMillis();
			while (true) {
				long end = System.currentTimeMillis();
				if (end - start >= 1000) {
					break;
				}
				i++;
				jedis.set("test" + i, i + "");
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			jedis.close();
		}
		System.out.println("redisÿ�������" + i + "��");
	}

	static int TOTAL_SIZE = 100000;

	@Test
	public void test() {
		long start = System.currentTimeMillis();
		// ������Ϣ���ӿ���̨���Ի��
		Jedis jedis = new Jedis("localhost", 6379, 100000);
		try {
			// Key(��)
			String key = "vtron";
			// ������ܵ���������
			jedis.del(key);
			// ģ���������ɸ���Ϸ���
//			List<String> playerList = new ArrayList<String>();
//			for (int i = 0; i < TOTAL_SIZE; ++i) {
//				// �������ÿ����ҵ�ID
//				playerList.add(UUID.randomUUID().toString());
//			}
			System.out.println("����������� ");
			// ��¼ÿ����ҵĵ÷�
			for (int i = 0; i < TOTAL_SIZE; i++) {
				// ����������֣�ģ����ҵ���Ϸ�÷�
				int score = (int) (Math.random() * 5000);
				// String member = UUID.randomUUID().toString();
				// System.out.println("���ID��" + member + "�� ��ҵ÷�: " + score);
				// ����ҵ�ID�͵÷֣����ӵ���Ӧkey��SortedSet��ȥ
				jedis.zadd(key, i, i + "");
			}
			// �����ӡȫ��������а�
			System.out.println();
			System.out.println(" " + key);
			System.out.println(" ȫ��������а� ");
			// �Ӷ�Ӧkey��SortedSet�л�ȡ�Ѿ��ź��������б�
//			Set<Tuple> scoreList = jedis.zrevrangeWithScores(key, 0, -1);
//			for (Tuple item : scoreList) {
//				System.out
//						.println("���ID��" + item.getElement() + "�� ��ҵ÷�:" + Double.valueOf(item.getScore()).intValue());
//			}
			// �����ӡTop5������а�
			System.out.println();
			System.out.println(" " + key);
			System.out.println(" Top ���");
			Set<Tuple> scoreList = jedis.zrevrangeWithScores(key, 0, 10);
			for (Tuple item : scoreList) {
				System.out
						.println("���ID��" + item.getElement() + "�� ��ҵ÷�:" + Double.valueOf(item.getScore()).intValue());
			}
			// �����ӡ�ض�����б�
			System.out.println();
			System.out.println(" " + key);
			System.out.println(" ������1000��2000�����");
			// �Ӷ�Ӧkey��SortedSet�л�ȡ�Ѿ�������1000��2000������б�
//			scoreList = jedis.zrangeByScoreWithScores(key, 1000, 2000);
//			for (Tuple item : scoreList) {
//				System.out
//						.println("���ID��" + item.getElement() + "�� ��ҵ÷�:" + Double.valueOf(item.getScore()).intValue());
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.quit();
			jedis.close();
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}

	private static String resource_a = "A";
	private static String resource_b = "B";

	@Test
	public static void deadLock() {
		Thread threadA = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (resource_a) {
					System.out.println("get resource a");
					try {
						Thread.sleep(3000);
						synchronized (resource_b) {
							System.out.println("get resource b");
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		Thread threadB = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (resource_b) {
					System.out.println("get resource b");
					synchronized (resource_a) {
						System.out.println("get resource a");
					}
				}
			}
		});
		threadA.start();
		threadB.start();

	}

}
