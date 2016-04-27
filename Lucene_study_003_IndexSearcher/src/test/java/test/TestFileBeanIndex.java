package test;

import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.junit.Test;
import com.startcaft.lucene.bean.FileBean;
import com.startcaft.lucene.util.FileUtil;

import cn.startcaft.www.lucene.Lucene_study_003_IndexReader.IndexWriterUtil;

public class TestFileBeanIndex {
	
	@Test
	public void testIndex() throws Exception{
		
		List<FileBean> fbs = FileUtil.getFolderFiles("testdir");
		int totalFile = fbs.size();
		System.out.println("得到的文件总数是："+ totalFile);
		
		IndexWriter writer = IndexWriterUtil.getIndexWriter("index", true);
		
		for (FileBean t : fbs) {
			Document doc = new Document();
			System.out.println("正在为文件[" + t.getPath() + "]创建索引...");
			doc.add(new StringField("path", t.getPath(), Field.Store.YES));
			doc.add(new LongField("modified", t.getModified(), Field.Store.YES));
			//为modified字段添加一个排序
			doc.add(new NumericDocValuesField("modified", t.getModified()));
			doc.add(new TextField("content", t.getContent(), Field.Store.YES));
			
			if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
				writer.addDocument(doc);
			}
			else {
				writer.updateDocument(new Term("path", t.getPath()), doc);
			}
		}
		
		writer.commit();
		writer.close();
		
		
		
		/*
		//计算准备使用的线程数量
		int perThreadCount = 3000;
		int threadCount = totalFile/perThreadCount + (totalFile%perThreadCount == 0 ? 0 : 1);  
		
		ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
		CountDownLatch countDownLatch1 = new CountDownLatch(1); 
		CountDownLatch countDownLatch2 = new CountDownLatch(threadCount);  
		
		//向线程池中添加任务
		for (int i = 0; i < threadCount; i++) {
			
			int start = i * perThreadCount;
			int end = (i+1) * perThreadCount < totalFile ? (i+1) * perThreadCount : totalFile;
			List<FileBean> subList = fbs.subList(start, end);
			
			Runnable runnable = new FileBeanIndex("index", i, countDownLatch1, countDownLatch2, subList);
			threadPool.execute(runnable);
		}
		countDownLatch1.countDown();  
		System.out.println("开始创建索引");  
		//等待所有线程都完成  
		countDownLatch2.await();  
		 //线程全部完成工作  
		System.out.println("所有线程都创建索引完毕");  
		//释放线程池资源  
		threadPool.shutdown();  
		*/
	}
}
