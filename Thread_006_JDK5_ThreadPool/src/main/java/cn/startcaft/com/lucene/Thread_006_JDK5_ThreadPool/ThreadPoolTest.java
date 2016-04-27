package cn.startcaft.com.lucene.Thread_006_JDK5_ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {

	public static void main(String[] args) {
		
		//1，创建线程池
		ExecutorService threadPool = Executors.newFixedThreadPool(3);
//		Executors.newCachedThreadPool();
//		Executors.newSingleThreadExecutor();
		//2，向线程池中添加任务
		for (int i = 1; i <= 10; i++) {
			
			final int task = i;
			threadPool.execute(new Runnable() {
				
				public void run() {
					
					for (int j = 1; j <= 10; j++) {
						try {
							Thread.sleep(30);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println(Thread.currentThread().getName() + ",loop of " + j + ",task for " + task);
					}
				}
			});
		}
		
		System.out.println("commit 10 task!");
		//3，线程池里面没有任务了，就可以shutdonw了
		threadPool.shutdown();
		
		
		Executors.newScheduledThreadPool(3).schedule(new Runnable() {
			
			public void run() {
				System.out.println("bombing!");
			}
		}, 10, TimeUnit.SECONDS);
		
		
		Executors.newScheduledThreadPool(3).scheduleAtFixedRate(new Runnable() {
			
			public void run() {
				System.out.println("startcfatcfatsfs bombing!");
			}
		}, 6, 2, TimeUnit.SECONDS);
	}
}
