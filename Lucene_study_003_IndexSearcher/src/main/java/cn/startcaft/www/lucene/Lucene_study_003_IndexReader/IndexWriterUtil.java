package cn.startcaft.www.lucene.Lucene_study_003_IndexReader;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * 创建索引的写入器
 */
public class IndexWriterUtil {
	
	/**创建索引写入器
	 * @param indexPath
	 * @param create
	 * @return
	 * @throws IOException
	 */
	public static IndexWriter getIndexWriter(String indexPath,boolean create) throws IOException{
		Directory dir = FSDirectory.open(Paths.get(indexPath, new String[0]));
		//中文分词器
	    Analyzer analyzer = new SmartChineseAnalyzer();
	    IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
	    LogMergePolicy mergePolicy = new LogByteSizeMergePolicy();
	    //设置segment添加文档(Document)时的合并频率          
	    //值较小,建立索引的速度就较慢          
	    //值较大,建立索引的速度就较快,>10适合批量建立索引        
	    mergePolicy.setMergeFactor(50);                     
	    //设置segment最大合并文档(Document)数         
	    //值较小有利于追加索引的速度         
	    //值较大,适合批量建立索引和更快的搜索         
	    mergePolicy.setMaxMergeDocs(5000);                     
	    if (create){
	        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
	    }else {
	        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
	    }
	    IndexWriter writer = new IndexWriter(dir, iwc);
	    return writer;
	}
}
