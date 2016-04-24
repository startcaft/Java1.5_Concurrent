package cn.startcaft.com.lucene.Thread_003_ThreaderSynchronized;

public class ThreadSynchronized {

	public static void main(String[] args) {
		
		new ThreadSynchronized().init();
	}
	
	private void init(){
		
		final Outputer outer = new Outputer();
		
		//线程1
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
		
		//线程2
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
		
		public void outPut(String name){
			
			//同步代码块，同一时间只能有一个线程访问，参事this为同步锁，必须确保多个线程的锁都是同一个对象
			synchronized (this) {
				for(int i=0;i<name.length();i++){
					System.out.print(name.charAt(i));
				}
				System.out.println();
			}
		}
		
		//或者使用synchronized关键字来修饰方法，切记一个方法最好只能有一个synchronized(避免死锁的情况)
		public synchronized void outPut2(String name){
			
			for(int i=0;i<name.length();i++){
				System.out.print(name.charAt(i));
			}
			System.out.println();
		}
	}
}
