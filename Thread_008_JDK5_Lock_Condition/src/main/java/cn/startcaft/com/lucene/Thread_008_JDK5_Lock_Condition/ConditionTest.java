package cn.startcaft.com.lucene.Thread_008_JDK5_Lock_Condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {

	public static void main(String[] args) {
		
		final Business bi = new Business();
		
		new Thread(new Runnable(){
			public void run() {
				//循环50次
				for (int i = 1; i <= 50; i++) {
					bi.subThread(i);
				}
			}
		}).start();
		
		new Thread(new Runnable(){
			public void run() {
				//循环50次
				for (int i = 1; i <= 50; i++) {
					bi.subThread2(i);
				}
			}
		}).start();
		
		//循环50次
		for (int i = 1; i <= 50; i++) {
			bi.mainThread(i);
		}
	}

}


class Business{
	
	private Integer shoudSub = 1;
	
	private Lock lock = new ReentrantLock();
	
	private Condition condition1 = lock.newCondition(); 
	private Condition condition2 = lock.newCondition(); 
	private Condition condition3 = lock.newCondition(); 
	
	//老三
	public void subThread2(int loopCount){
		//上锁
		lock.lock();
		try{
			while (shoudSub != 3) {
				try {
					//this.wait();
					condition3.await();//如果不为3,3号Condition等起
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (int i = 1; i <= 20; i++) {
				System.out.println("sub3 thread sequece of " + i + ",loop of " + loopCount);
			}
			shoudSub = 2;
			//唤醒mainThread
			//this.notify();
			condition2.signal();//如果是3，则给2号Condition发信号
		}
		//解锁
		finally{
			lock.unlock();
		}
	}
	
	//老二
	public void subThread(int loopCount){
		//上锁
		lock.lock();
		try{
			while (shoudSub != 2) {
				try {
					//this.wait();
					condition2.await();//如果不为2,2号Condition等起
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (int i = 1; i <= 10; i++) {
				System.out.println("sub2 thread sequece of " + i + ",loop of " + loopCount);
			}
			shoudSub = 1;
			//唤醒mainThread
			//this.notify();
			condition1.signal();//如果为2，则给1号Condition发信号
		}
		//解锁
		finally{
			lock.unlock();
		}
	}
	
	//老大
	public void mainThread(int loopCount){
		//上锁
		lock.lock();
		try{
			while (shoudSub != 1) {
				try {
					//this.wait();
					condition1.await();//如果不为1,1号Condition等起
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (int i = 1; i <= 100; i++) {
				System.out.println("main thread sequece of " + i + ",loop of " + loopCount);
			}
			shoudSub = 3;
			//唤醒subThread
			//this.notify();
			condition3.signal();//如果为1，则给3号Condition发信号
		}
		//解锁
		finally {
			lock.unlock();
		}
	}
}
