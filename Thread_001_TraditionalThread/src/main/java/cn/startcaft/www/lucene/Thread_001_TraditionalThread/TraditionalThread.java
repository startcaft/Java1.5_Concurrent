package cn.startcaft.www.lucene.Thread_001_TraditionalThread;

public class TraditionalThread {

	public static void main(String[] args) {
		
		/*
		 * 通过Thread子类的run方法来创建线程
		 */
		Thread thread = new Thread(){
			
			public void run() {
				while(true){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					System.out.println("1:" + Thread.currentThread().getName());
					System.out.println("2:" + this.getName());
				}
			};
		};
		thread.start();
		
		
		/*
		 * 通过Runable接口来创建线程，
		 * 两种方式并没有什么大不同，只是这种方式更能体现面向对象编程的方式
		 */
		Thread thread2 = new Thread(new Runnable() {
			
			public void run() {
				while(true){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					System.out.println("1:" + Thread.currentThread().getName());
					//这里的this就不行哦，因为这里的this指向Runable接口的对象(线程要运行的代码的宿主而不是Thread对象)
					//System.out.println("4:" + this.getName());
					System.out.println("2:" + Thread.currentThread().getName());
				}
			}
		});
		thread2.start();
		
		
		/*
		 * Thread子类复写了run方法，所以线程在运行中只会执行子类的run方法中的代码，
		 * 而不执行Runnable对象中的run方法中的代码。
		 */
		new Thread(new Runnable() {
			
			public void run() {
				
				while(true){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("runnable:" + Thread.currentThread().getName());
				}
			}
		}){
			
			public void run() {
				while(true){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("thread:" + Thread.currentThread().getName());
				}
			};
		}.start();
	}
}
