package com.startcfat.cn.lucene.Thread_009_JDK_Semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {

	public static void main(String[] args) {
		
		//创建一个线程池
		ExecutorService pool = Executors.newCachedThreadPool();
		final Semaphore sp = new Semaphore(3);
		
		//创建10个线程对象
		for (int i = 0; i < 10; i++) {
			Runnable runnable = new Runnable() {
				
				public void run() {
					try {
						sp.acquire();
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("线程[" + Thread.currentThread().getName() + "]"
							+ "进入，当前已有" + (3 - sp.availablePermits()) + "盏信号灯");
					
					try {
						Thread.sleep((long) (Math.random() * 1000));
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("线程[" + Thread.currentThread().getName() + "]"
							+ "即将离开");
					sp.release();
					System.out.println("线程[" + Thread.currentThread().getName() + "]"
							+ "已离开，当前已有" + (3 - sp.availablePermits()) + "盏信号灯");
				}
			};
			
			//向线程池中添加线程
			pool.execute(runnable);
		}
	}
}