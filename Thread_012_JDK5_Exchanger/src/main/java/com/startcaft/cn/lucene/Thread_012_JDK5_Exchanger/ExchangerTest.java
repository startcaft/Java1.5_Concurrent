package com.startcaft.cn.lucene.Thread_012_JDK5_Exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangerTest {
	
	public static void main(String[] args) {
		
		ExecutorService pool = Executors.newCachedThreadPool();
		final Exchanger<String> exchanger = new Exchanger<String>();
		
		//吸毒者带着钱来买毒品
		pool.execute(new Runnable() {
			
			public void run() {
				try {
					String data1 = "1000块钱";
					System.out.println("线程 [" + Thread.currentThread().getName() + "]"
							+ "正在准备把数据:" + data1 + " 交换出去");
					
					Thread.sleep((long) (Math.random() * 10000));
					
					String data2 = exchanger.exchange(data1);
					System.out.println("线程 [" + Thread.currentThread().getName() + "]"
							+ "换回的数据是:" + data2);
				} catch (Exception e) {
				}
			}
		});
		
		//毒贩子带着毒品来卖钱
		pool.execute(new Runnable() {
			
			public void run() {
				try {
					String data1 = "3克冰毒";
					System.out.println("线程 [" + Thread.currentThread().getName() + "]"
							+ "正在准备把数据:" + data1 + " 交换出去");
					
					Thread.sleep((long) (Math.random() * 10000));
					
					String data2 = exchanger.exchange(data1);
					System.out.println("线程 [" + Thread.currentThread().getName() + "]"
							+ "换回的数据是:" + data2);
				} catch (Exception e) {
				}
			}
		});
	}
}
