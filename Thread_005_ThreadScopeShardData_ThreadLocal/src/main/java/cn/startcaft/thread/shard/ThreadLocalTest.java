package cn.startcaft.thread.shard;

import java.util.Random;

public class ThreadLocalTest {
	
	//ThreadLocal<T> 线程绑定数据类型
	private static ThreadLocal<Integer> x = new ThreadLocal<Integer>();
//	private static ThreadLocal<MyThreadScopeData> threadData = new ThreadLocal<MyThreadScopeData>();
	
	public static void main(String[] args) {
		
		new Thread(new Runnable() {
			
			public void run() {
				
				while(true){
					try {
						Thread.sleep(300);
						
						int data = new Random().nextInt();
						System.out.println(Thread.currentThread().getName() + "has put data:" + data);
						//绑定线程与变量
						x.set(data);
						
//						MyThreadScopeData myData = new MyThreadScopeData();
//						myData.setName("startcaft_name" + data);
//						myData.setAge(data);
//						threadData.set(myData);
						
						//获取与当前线程有关的一个MyThreadScopeData实例
						MyThreadScopeData.getThreadInstance().setName("startcaft_name" + data);
						MyThreadScopeData.getThreadInstance().setAge(data);
						
						new A().get();
						new B().get();
						
					} catch (Exception e) {
					}
				}
			}
		}).start();
	}
	
	
	//B模块
	static class A{
		
		public void get(){
			
			//获取与当前线程绑定的变量
			int data = x.get();
			System.out.println("A模块--->" 
							+ Thread.currentThread().getName()
							+ "get data:" + data);
			
//			MyThreadScopeData myData = threadData.get();
			MyThreadScopeData myData = MyThreadScopeData.getThreadInstance();
			System.out.println("A模块--->" + myData.toString());
		}
	}
	
	//A模块
	static class B{
		
		public void get(){
			
			//获取与当前线程绑定的变量
			int data = x.get();
			System.out.println("B模块--->" 
					+ Thread.currentThread().getName()
					+ "get data:" + data);
			
//			MyThreadScopeData myData = threadData.get();
			MyThreadScopeData myData = MyThreadScopeData.getThreadInstance();
			System.out.println("A模块--->" + myData.toString());
		}
	}
}

/**
 * 由内存内单例模式 进化到 线程内单例模式
 */
class MyThreadScopeData{
	
	//private static MyThreadScopeData instance = null; //new MyThreadScopeData();
	
	private static ThreadLocal<MyThreadScopeData> map = new ThreadLocal<MyThreadScopeData>();
	
	private MyThreadScopeData(){
	}
	
	
	/**
	 * 获取一个与当前线程相关的MyThreadScopeData实例对象
	 * @return
	 */
	public static /*synchronized*/ MyThreadScopeData getThreadInstance(){
		
		MyThreadScopeData instance = map.get();
		
		if (instance == null) {
			instance = new MyThreadScopeData();
			map.set(instance);
		}
		
		return instance; 
	}
	
	private String name;
	private Integer age;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	@Override
	public String toString() {
		return "MyThreadScopeData [name=" + name + ", age=" + age + "]";
	}
}


