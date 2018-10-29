package com.ambow;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 两个线程交替打印奇偶数
 * 
 * @author pengjieran
 *
 */
public class ThreadPrintDemo {

	static AtomicInteger cxsNum = new AtomicInteger(0);
	static volatile boolean flag = false;

	/**
	 * 算法实现打印,消除了使用 synchronized 导致的上下文切换带来的损耗，性能更好。相信，如果你面试的时候，这么写，面试官肯定很满意。
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Thread t1 = new Thread(() -> {
			for (; 100 > cxsNum.get();) {
				if (!flag && (cxsNum.get() == 0 || cxsNum.incrementAndGet() % 2 == 0)) {
					try {
						Thread.sleep(100);// 防止打印速度过快导致混乱
					} catch (InterruptedException e) {
						// NO
					}

					System.out.println(cxsNum.get());
					flag = true;
				}
			}
		});

		Thread t2 = new Thread(() -> {
			for (; 100 > cxsNum.get();) {
				if (flag && (cxsNum.incrementAndGet() % 2 != 0)) {
					try {
						Thread.sleep(100);// 防止打印速度过快导致混乱
					} catch (InterruptedException e) {
						// NO
					}

					System.out.println(cxsNum.get());
					flag = false;
				}
			}
		});

		t1.start();
		t2.start();
	}
}

/**
 * synchronized方式实现，比较平常的做法
 * 
 * @author pengjieran
 *
 */
class ThreadPrintDemo2 {
	public static void main(String[] args) {
		final ThreadPrintDemo2 demo2 = new ThreadPrintDemo2();
		Thread t1 = new Thread(demo2::print1);
		Thread t2 = new Thread(demo2::print2);

		t1.start();
		t2.start();
	}

	public synchronized void print2() {
		for (int i = 1; i <= 100; i += 2) {
			System.out.println(i);
			this.notify();
			try {
				this.wait();
				Thread.sleep(100);// 防止打印速度过快导致混乱
			} catch (InterruptedException e) {
				// NO
			}
		}
	}

	public synchronized void print1() {
		for (int i = 0; i <= 100; i += 2) {
			System.out.println(i);
			this.notify();
			try {
				this.wait();
				Thread.sleep(100);// 防止打印速度过快导致混乱
			} catch (InterruptedException e) {
				// NO
			}
		}
	}
}

class ThreadPrintDemo3 {

	static volatile int num = 0;
	static volatile boolean flag = false;

	/**
	 * 使用 volatile 变量代替 CAS 变量，减轻使用 CAS 的消耗，注意，这里 ++num 不是原子的，但不妨碍，因为有 flag 变量控制。而
	 * num 必须是 volatile 的，如果不是，会导致可见性问题。
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Thread t1 = new Thread(() -> {
			for (; 100 > num;) {
				if (!flag && (num == 0 || ++num % 2 == 0)) {

					try {
						Thread.sleep(100);// 防止打印速度过快导致混乱
					} catch (InterruptedException e) {
						// NO
					}

					System.out.println(num);
					flag = true;
				}
			}
		});

		Thread t2 = new Thread(() -> {
			for (; 100 > num;) {
				if (flag && (++num % 2 != 0)) {

					try {
						Thread.sleep(100);// 防止打印速度过快导致混乱
					} catch (InterruptedException e) {
						// NO
					}

					System.out.println(num);
					flag = false;
				}
			}
		});

		t1.start();
		t2.start();

	}
}