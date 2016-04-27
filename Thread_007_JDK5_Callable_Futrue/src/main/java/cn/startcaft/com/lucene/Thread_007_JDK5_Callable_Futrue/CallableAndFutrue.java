package cn.startcaft.com.lucene.Thread_007_JDK5_Callable_Futrue;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableAndFutrue {

	public static void main(String[] args) {
		
		
		ExecutorService threadPool = Executors.newSingleThreadExecutor();
		
		//不调用ExecutorService对象的execute(Runnable)方法
		//而调用ExecutorService的submit()系列方法，返回一个Future<T>对象，它就代表返回的结果，T就是结果的数据类型
		Future<String> result = threadPool.submit(new Callable<String>() {

			public String call() throws Exception {
				
				Thread.sleep(3000);
				return "thread done";
			}
		});
		
		try {
			System.out.println("等待线程结束时的结果......");
			//只要线程没有结束，Futrue.get()方就会永远处于等待状态
			//get()方法的重载，如何在指定的间隔后还等到不结果，会抛出异常
			System.out.println("拿到结果:" + result.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		System.out.println("//////////////使用CompletionService提交一组任务////////////");
		
		
		ExecutorService pool = Executors.newFixedThreadPool(10);
		CompletionService<Integer> completionService = 
					new ExecutorCompletionService<Integer>(pool);
		
		//提交10个任务
		for (int i = 1; i <= 10; i++) {
			
			final int sequence = i;
			completionService.submit(new Callable<Integer>() {

				public Integer call() throws Exception {
					
					Thread.sleep(new Random().nextInt(5000));
					return sequence;
				}
			});
		}
		
		for (int i = 1; i <= 10; i++){
			try {
				System.out.println("done:" + completionService.take().get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
}
