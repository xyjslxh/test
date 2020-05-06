package com.test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEW;

import junit.framework.Assert;
import sun.misc.Unsafe;

public class UnsafeDemo {
	static Unsafe unsafe;

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, InstantiationException {
		Field f = Unsafe.class.getDeclaredField("theUnsafe"); // Internal reference
		f.setAccessible(true);
		unsafe = (Unsafe) f.get(null);

		// This creates an instance of player class without any initialization
		Player p = (Player) unsafe.allocateInstance(Player.class);
		System.out.println(p.getAge()); // Print 0

		p.setAge(45); // Let's now set age 45 to un-initialized object
		System.out.println(p.getAge()); // Print 45

		Object object = new Object();
		Object object2 = shallowClone(object);
		Assert.assertEquals(object, object2);
		Assert.assertTrue(object != object2);
		String password = new String("l00k@myHor$e");
		String fake = new String(password.replaceAll(".", "?"));
		System.out.println(password); // l00k@myHor$e
		System.out.println(fake); // ????????????
		// password = "l00k@myHor$f";
		//unsafe.copyMemory(fake, 0L, null, getAddr(password), sizeOf(password));

		System.out.println(password); // ????????????
		System.out.println(fake); // ????????????
	}

	private static <T> T fromAddress(long addr, long size) {
		Object[] array = new Object[] { null };
		long baseOffset = unsafe.arrayBaseOffset(Object[].class);
		unsafe.putLong(array, baseOffset, addr);
		return (T) array[0];
	}

	public static <T> T shallowClone(T t) throws InstantiationException {
		Class clazz = t.getClass();
		if (clazz.isArray()) {
			Object[] os = (Object[]) t;
			return (T) Arrays.copyOf(os, os.length);
		}
		long srcAddr = getAddr(t);
		long size = sizeOf(clazz);
		long destAddr = unsafe.allocateMemory(size);
		unsafe.copyMemory(srcAddr, destAddr, size);
		return fromAddress(destAddr, size);
	}

	public static Unsafe getUnsafe() {
		Field f;
		try {
			f = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			Unsafe unsafe = (Unsafe) f.get(null);
			return unsafe;
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Internal reference
		catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private static long getAddr(Object obj) {
		Object[] array = new Object[] { obj };
		long baseOffset = getUnsafe().arrayBaseOffset(Object[].class);
		return getUnsafe().getLong(array, baseOffset);
	}

	public static long sizeOf(Class clazz) {
		long maximumOffset = 0;
		Class maxiNumFieldClass = null;
		do {
			for (Field f : clazz.getDeclaredFields()) {
				if (!Modifier.isStatic(f.getModifiers())) {
					long tmp = unsafe.objectFieldOffset(f);
					if (tmp > maximumOffset) {
						maximumOffset = unsafe.objectFieldOffset(f);
						maxiNumFieldClass = f.getType();
					}
				}
			}
		} while ((clazz = clazz.getSuperclass()) != null);
		long last = byte.class.equals(maxiNumFieldClass) ? 1
				: (short.class.equals(maxiNumFieldClass) || char.class.equals(maxiNumFieldClass)) ? 2
						: (long.class.equals(maxiNumFieldClass) || double.class.equals(maxiNumFieldClass)) ? 8 : 4;
		return maximumOffset + last;
	}

	@Test
	public void test() {
		int sum = 0;
		long SUPER_SIZE = (long) Integer.MAX_VALUE * 2;
		SuperArray array = new SuperArray(SUPER_SIZE);
		System.out.println("Array size:" + array.size()); // 4294967294
		for (int i = 0; i < 100; i++) {
			array.set((long) Integer.MAX_VALUE + i, (byte) 3);
			sum += array.get((long) Integer.MAX_VALUE + i);
		}
		System.out.println("Sum of 100 elements:" + sum); // 300
	}

	class SuperArray {
		private final static int BYTE = 1;
		private long size;
		private long address;

		public SuperArray(long size) {
			this.size = size;
			// 得到分配内存的起始地址
			address = getUnsafe().allocateMemory(size * BYTE);
		}

		public void set(long i, byte value) {
			getUnsafe().putByte(address + i * BYTE, value);
		}

		public int get(long idx) {
			return getUnsafe().getByte(address + idx * BYTE);
		}

		public long size() {
			return size;
		}
	}
}

class Player {
	private int age = 12;

	private Player() {
		this.age = 50;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
