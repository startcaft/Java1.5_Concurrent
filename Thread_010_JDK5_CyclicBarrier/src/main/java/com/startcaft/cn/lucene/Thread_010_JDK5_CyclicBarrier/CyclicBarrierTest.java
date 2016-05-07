package com.startcaft.cn.lucene.Thread_010_JDK5_CyclicBarrier;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierTest {
	
	public static void main(String[] args) {
		
		ExecutorService pool = Executors.newCachedThreadPool();
		final CyclicBarrier cb = new CyclicBarrier(3);
		
		for (int i = 0; i < 3; i++) {
			Runnable runnable = new Runnable() {
				
				public void run() {
					try {
						Thread.sleep((long) (Math.random()*10000));
						System.out.println("线程[" + Thread.currentThread().getName() + "]"
								+ "即将到达集合地点1，当前已有[" + (cb.getNumberWaiting() + 1) + "个]线程在此集结");
						cb.await();
						
						Thread.sleep((long) (Math.random()*10000));
						System.out.println("线程[" + Thread.currentThread().getName() + "]"
								+ "即将到达集合地点2，当前已有[" + (cb.getNumberWaiting() + 1) + "个]线程在此集结");
						cb.await();
						
						Thread.sleep((long) (Math.random()*10000));
						System.out.println("线程[" + Thread.currentThread().getName() + "]"
								+ "即将到达集合地点3，当前已有[" + (cb.getNumberWaiting() + 1) + "个]线程在此集结");
						cb.await();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			pool.execute(runnable);
		}
	}
}
