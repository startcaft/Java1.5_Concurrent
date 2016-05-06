package cn.startcaft.com.lucene.Thread_008_JDK5_Lock_Condition;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {

	public static void main(String[] args) {
		
		final Queue3 q3 = new Queue3();
		
		//产生三个线程用于读取数据
		for (int i = 0; i < 3; i++) {
			
			new Thread(new Runnable() {
				
				public void run() {
					
					while(true){
						q3.get();
					}
				}
			}).start();
		}
		
		//产生三个线程用于修改数据
		for (int i = 0; i < 3; i++) {
			
			new Thread(new Runnable() {
				
				public void run() {
					
					int randomInt = new Random().nextInt(10000);
					q3.put(randomInt);
				}
			}).start();
		}
	}

}

class Queue3{
	
	private Object data = null;//共享数据，只有一个线程能写该数据，但可以有多个线程同时读取该数据
	private ReadWriteLock rwLock = new ReentrantReadWriteLock();
	
	public void get(){
		rwLock.readLock().lock();
		try {
			System.out.println(Thread.currentThread().getName() + " be ready to read data!");
			Thread.sleep((long) (Math.random() * 1000));
			System.out.println(Thread.currentThread().getName() + " have read data:" + this.data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			rwLock.readLock().unlock();
		}
	}
	
	public void put(Object data){
		rwLock.writeLock().lock();
		try {
			System.out.println(Thread.currentThread().getName() + " be ready to write data!");
			Thread.sleep((long) (Math.random() * 1000));
			this.data = data;
			System.out.println(Thread.currentThread().getName() + " have write data:" + this.data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			rwLock.writeLock().unlock();
		}
	}
}
