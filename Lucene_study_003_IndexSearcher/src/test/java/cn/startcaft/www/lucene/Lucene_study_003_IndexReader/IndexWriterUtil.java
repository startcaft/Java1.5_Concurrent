package cn.startcaft.www.lucene.Lucene_study_003_IndexReader;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * 创建索引的写入器
 */
public class IndexWriterUtil {
	
	/**
	 * 根据不同的OpenMode来创建IndexWriter对象
	 * @param indexPath		索引目录路径字符串
	 * @param create		覆盖/追加
	 * @return				IndexWriter对象
	 * @throws IOException
	 */
	public static IndexWriter getIndexWriter(String indexPath,boolean create) throws IOException{
		//1,创建Directory对象
		Directory dir = FSDirectory.open(Paths.get(indexPath, new String[0]));
		//2,创建Analyzer对象
		Analyzer analyzer = new StandardAnalyzer();
		//3,创建IndexWriterConfig对象
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		if (create) {
			iwc.setOpenMode(OpenMode.CREATE);
		} else{
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}
		
		IndexWriter iw = new IndexWriter(dir, iwc);
		return iw;
	}
}
