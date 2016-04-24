package cn.startcaft.com.lucene.Thread_004_ThreadScopeShardData;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThreadShardData {
	
	private static int data = 0;
	
	//Map的Key就是当前运行线程的Thread对象
	private static Map<Thread, Integer> threadData = new HashMap<Thread, Integer>();
	
	public static void main(String[] args) {
		
		while(true){
			
			try {
				Thread.sleep(300);
				
				new Thread(new Runnable() {
					public void run() {
						int data = new Random().nextInt();
						System.out.println(Thread.currentThread().getName() + "has put data:" + data);
						
						threadData.put(Thread.currentThread(), data);
						new A().get();
						new B().get();
					}
				}).start();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	//B模块
	static class A{
		
		public void get(){
			
			int data = threadData.get(Thread.currentThread());
			System.out.println("A模块--->" 
							+ Thread.currentThread().getName()
							+ "get data:" + data);
		}
	}
	
	//A模块
	static class B{
		
		public void get(){
			
			int data = threadData.get(Thread.currentThread());
			System.out.println("B模块--->" 
					+ Thread.currentThread().getName()
					+ "get data:" + data);
		}
	}
}
