package cn.startcaft.com.lucene.Thread_003_ThreaderSynchronized;

public class ThreadInterview {

	public static void main(String[] args) {
		
		/*
		 * 一道java多线程的面试题：
		 * 子线程循环10次，接着主线程循环100次，接着又回到子线程循环10，接着又再回到主线程循环100次，如果喜欢50次。
		 * 【子线程和主线程肯定是互斥的】
		 */
		final Business bi = new Business();
		
		new Thread(new Runnable(){

			public void run() {
				//循环50次
				for (int i = 1; i <= 50; i++) {
					bi.subThread(i);
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
	
	private boolean bShoudSub = true;
	
	public synchronized void subThread(int loopCount){
		
		//如果为false，就是不让子线程运行，则线程挂起
		while (!bShoudSub) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 1; i <= 10; i++) {
			System.out.println("sub thread sequece of " + i + ",loop of " + loopCount);
		}
		bShoudSub = false;
		//唤醒mainThread
		this.notify();
	}
	
	public synchronized void mainThread(int loopCount){
		
		while (bShoudSub) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 1; i <= 100; i++) {
			System.out.println("main thread sequece of " + i + ",loop of " + loopCount);
		}
		bShoudSub = true;
		//唤醒subThread
		this.notify();
	}
}
