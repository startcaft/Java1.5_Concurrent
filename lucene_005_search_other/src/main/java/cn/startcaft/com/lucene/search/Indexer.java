package cn.startcaft.com.lucene.search;

import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {
	
	
	private static Analyzer analyzer = new StandardAnalyzer();//标准分词器
	
	private int[] ids = {1,2,3};
	private String[] cites = {"qingdao","nanjing","shanghai"};
	private String[] descs = {
			"Qingdao is a beautiful city",
			"Nanjing is c city of culture",
			"Shanghai is a bustling city"
	};
	
	private Directory dir = null;
	
	
	public static void main(String[] args) {
		
		try {
			new Indexer().index("D:\\LuceneIndexFiles\\Index005");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成索引
	 * @param indexDir
	 * @throws Exception
	 */
	public void index(String indexDir) throws Exception{
		
		dir = FSDirectory.open(Paths.get(indexDir, new String[0]));
		IndexWriter writer = getWriter();
		
		for (int i = 0; i < ids.length; i++) {
			Document doc = new Document();
			doc.add(new IntField("id", ids[i], Store.YES));
			doc.add(new StringField("city", cites[i], Store.YES));
			doc.add(new TextField("desc", descs[i], Store.NO));
			
			writer.addDocument(doc);
		}
		
		System.out.println("生成索引完毕");
		
		writer.close();
	}
	
	
	/**
	 * 获取IndexWriter实例
	 * @return
	 * @throws Exception
	 */
	private IndexWriter getWriter() throws Exception{
		IndexWriterConfig iwc=new IndexWriterConfig(analyzer);
		IndexWriter writer=new IndexWriter(dir, iwc);
		return writer;
	}
}
