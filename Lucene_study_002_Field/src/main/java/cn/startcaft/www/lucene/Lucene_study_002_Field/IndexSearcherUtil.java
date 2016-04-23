package cn.startcaft.www.lucene.Lucene_study_002_Field;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

/**
 * 创建索引的查看器，单例的，因为IndexReader在打开和关闭时很耗时
 */
public class IndexSearcherUtil {
	
	
	private static IndexReader reader = null;
	
	public static IndexSearcher getIndexSearcher(String indexPath,ExecutorService service) throws IOException{
		
		//如果静态变量reader为空，则创建一个新的IndexReader对象
		if (reader == null) {
			reader =  DirectoryReader.open(FSDirectory.open(Paths.get(indexPath, new String[0])));
		} 
		else{
			//如果不为空，则判断reader是否已经改变。
			DirectoryReader oldReader = (DirectoryReader) reader;
			IndexReader newReader = DirectoryReader.openIfChanged(oldReader);
			//为null就是reader没有改变，那就无所谓
			//如果不为null，则关闭原始的reader对象，再将获取到的新IndexReader对象赋值给reader对象。
			if (newReader != null) {
				reader.close();
				reader = newReader;
			}
		}
		IndexSearcher searcher = new IndexSearcher(reader,service);
		return searcher;
	}
}
