package cn.startcaft.com.lucene.Thread_008_JDK5_Lock_Condition;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
	
public static void main(String[] args) {
		
		new LockTest().init();
	}
	
	private void init(){
		
		final Outputer outer = new Outputer();
		
		//线程1---完全输出"pikai"
		new Thread(new Runnable() {
			
			public void run() {
				while(true){
					try {
						Thread.sleep(30);
						outer.outPut("pikai");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		//线程2---完全输出"startcaft"
		new Thread(new Runnable() {
			
			public void run() {
				while(true){
					try {
						Thread.sleep(30);
						outer.outPut2("startcaft");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	
	//内部类，output方式实现参数name的逐个字符的打印，完毕换行
	class Outputer{
		
		//定义一个Lock锁对象
		Lock lock = new ReentrantLock();
		
		public void outPut(String name){
			
			//上锁
			lock.lock();
			
			try {
				for(int i=0;i<name.length();i++){
					System.out.print(name.charAt(i));
				}
				System.out.println();
			} finally {
				//解锁
				lock.unlock();
			}
		}
		
		public void outPut2(String name){
			
			//上锁
			lock.lock();
			try {
				for(int i=0;i<name.length();i++){
					System.out.print(name.charAt(i));
				}
				System.out.println();
			} finally {
				//解锁
				lock.unlock();
			}
		}
	}
}
