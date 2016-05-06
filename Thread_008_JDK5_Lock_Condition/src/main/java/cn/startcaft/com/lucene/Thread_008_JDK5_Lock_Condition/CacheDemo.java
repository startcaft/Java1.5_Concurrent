package cn.startcaft.com.lucene.Thread_008_JDK5_Lock_Condition;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheDemo {
	
	private Map<String, Object> cache = new HashMap<String, Object>();
	private ReadWriteLock rwLock = new ReentrantReadWriteLock();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
//	public synchronized Object getDate(String key){
//		
//		Object obj = cache.get(key);
//		if (obj == null) {
//			obj = new Random().nextInt(10000);
//		}
//		return obj;
//	}
	
	public Object getDate(String key){
		
		//1，首先一进来,上一把读取锁
		rwLock.readLock().lock();
		Object obj = null;
		obj = cache.get(key);
		try{
			if (obj == null) {
				//如果数据为空，则松开读取锁，上一把写锁，进行数据的修改。
				rwLock.readLock().unlock();
				rwLock.writeLock().lock();
				try{
					obj = new Random().nextInt(10000);
				}
				finally{
					if (obj == null) {
						//写完数据，松开写锁。
						rwLock.writeLock().unlock();
					}
				}
				//这里记得要恢复读取锁，不然最外层的finally不知道unlock谁
				rwLock.readLock().lock();
			}
		}
		finally{
			//最后，松开读取锁
			rwLock.readLock().unlock();
		}
		return obj;
	}
}
