package cn.startcaft.www.lucene.Thread_002_TraditionalTime;

import java.util.Timer;
import java.util.TimerTask;

public class TraditionalTimer {

	public static void main(String[] args) {
		
		//10秒之后，执行一次TimerTask中run方法的代码
		/*
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("boom!");
			}
		}, 10000);
		*/
		
		//3秒之后，执行一次TimerTask中run方法的代码，然后每隔2秒执行一次TimerTask中run方法的代码
		/*
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("boom!");
			}
		}, 3000, 2000);
		*/
		
		//每2秒执行一次TimerTask子类中的run方法中的代码
		class MyTask extends TimerTask{

			@Override
			public void run() {
				System.out.println("boom!!");
				//再new一个任务对象
				new Timer().schedule(/*new TimerTask() {
					
					@Override
					public void run() {
						System.out.println("boom!!");
					}
				}*/new MyTask(), 2000);
			}
		}
		
		new Timer().schedule(new MyTask(), 2000);
	}

}
