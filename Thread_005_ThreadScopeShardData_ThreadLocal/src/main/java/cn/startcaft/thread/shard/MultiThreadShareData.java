package cn.startcaft.thread.shard;

public class MultiThreadShareData {

	public static void main(String[] args) {
		
		final ShareData shareData = new ShareData();
		
		new Thread(new Runnable() {
			
			public void run() {
				shareData.increment();
			}
		}).start();
		
		new Thread(new Runnable() {
			
			public void run() {
				shareData.decrement();
			}
		}).start();
		
		
		////////
		new Thread(new MyRunnable1(shareData)).start();
		new Thread(new MyRunnable2(shareData)).start();

	}

}

class MyRunnable1 implements Runnable{

	private ShareData data;

	public MyRunnable1(ShareData data) {
		this.data = data;
	}
	
	public void run() {
		while(true){
			this.data.decrement();
		}
	}
}

class MyRunnable2 implements Runnable{

	private ShareData data;

	public MyRunnable2(ShareData data) {
		this.data = data;
	}
	
	public void run() {
		while(true){
			this.data.increment();
		}
	}
}

class ShareData{
	
	@SuppressWarnings("unused")
	private int j = 0;
	
	public synchronized void increment(){
		j++;
	}
	
	public synchronized void decrement(){
		j--;
	}
}
