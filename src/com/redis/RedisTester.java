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
		System.out.println("redis每秒操作：" + i + "次");
	}

	static int TOTAL_SIZE = 100000;

	@Test
	public void test() {
		long start = System.currentTimeMillis();
		// 连接信息，从控制台可以获得
		Jedis jedis = new Jedis("localhost", 6379, 100000);
		try {
			// Key(键)
			String key = "vtron";
			// 清除可能的已有数据
			jedis.del(key);
			// 模拟生成若干个游戏玩家
//			List<String> playerList = new ArrayList<String>();
//			for (int i = 0; i < TOTAL_SIZE; ++i) {
//				// 随机生成每个玩家的ID
//				playerList.add(UUID.randomUUID().toString());
//			}
			System.out.println("输入所有玩家 ");
			// 记录每个玩家的得分
			for (int i = 0; i < TOTAL_SIZE; i++) {
				// 随机生成数字，模拟玩家的游戏得分
				int score = (int) (Math.random() * 5000);
				// String member = UUID.randomUUID().toString();
				// System.out.println("玩家ID：" + member + "， 玩家得分: " + score);
				// 将玩家的ID和得分，都加到对应key的SortedSet中去
				jedis.zadd(key, i, i + "");
			}
			// 输出打印全部玩家排行榜
			System.out.println();
			System.out.println(" " + key);
			System.out.println(" 全部玩家排行榜 ");
			// 从对应key的SortedSet中获取已经排好序的玩家列表
//			Set<Tuple> scoreList = jedis.zrevrangeWithScores(key, 0, -1);
//			for (Tuple item : scoreList) {
//				System.out
//						.println("玩家ID：" + item.getElement() + "， 玩家得分:" + Double.valueOf(item.getScore()).intValue());
//			}
			// 输出打印Top5玩家排行榜
			System.out.println();
			System.out.println(" " + key);
			System.out.println(" Top 玩家");
			Set<Tuple> scoreList = jedis.zrevrangeWithScores(key, 0, 10);
			for (Tuple item : scoreList) {
				System.out
						.println("玩家ID：" + item.getElement() + "， 玩家得分:" + Double.valueOf(item.getScore()).intValue());
			}
			// 输出打印特定玩家列表
			System.out.println();
			System.out.println(" " + key);
			System.out.println(" 积分在1000至2000的玩家");
			// 从对应key的SortedSet中获取已经积分在1000至2000的玩家列表
//			scoreList = jedis.zrangeByScoreWithScores(key, 1000, 2000);
//			for (Tuple item : scoreList) {
//				System.out
//						.println("玩家ID：" + item.getElement() + "， 玩家得分:" + Double.valueOf(item.getScore()).intValue());
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
