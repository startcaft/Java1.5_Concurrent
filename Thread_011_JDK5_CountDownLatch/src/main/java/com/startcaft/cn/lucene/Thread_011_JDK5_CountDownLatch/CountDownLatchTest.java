package com.startcaft.cn.lucene.Thread_011_JDK5_CountDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchTest {

	public static void main(String[] args) {
		
		final CountDownLatch cbOrder = new CountDownLatch(1);
		final CountDownLatch cbAnswer = new CountDownLatch(3);
		
		ExecutorService pool = Executors.newCachedThreadPool();
		
		for (int i = 0; i < 3; i++) {
			Runnable runnable = new Runnable() {
				
				public void run() {
					try {
						System.out.println("线程[" + Thread.currentThread() + "]"
								+ "正准备接受命令");
						
						cbOrder.await();
						
						System.out.println("线程[" + Thread.currentThread() + "]"
								+ "已接受命令");
						
						Thread.sleep((long) (Math.random() * 10000));
						
						System.out.println("线程[" + Thread.currentThread() + "]"
								+ "回应命令处理结果");
						
						cbAnswer.countDown();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			};
			pool.execute(runnable);
		}
		
		//该线程用于减少计数器
		try {
			Thread.sleep((long) (Math.random() * 10000));
			
			System.out.println("线程[" + Thread.currentThread().getName() + "]"
					+ "即将发布命令");
			
			cbOrder.countDown();//减去1
			
			System.out.println("线程[" + Thread.currentThread().getName() + "]"
					+ "已经发送命令，正在等待结果...");
			
			cbAnswer.await();
			
			System.out.println("线程[" + Thread.currentThread().getName() + "]"
					+ "已经收到所有响应结果");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
