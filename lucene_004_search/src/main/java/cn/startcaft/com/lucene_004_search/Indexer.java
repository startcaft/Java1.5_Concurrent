package cn.startcaft.com.lucene_004_search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {
	
	private IndexWriter writer;//索引写入器，构建IndexWriter，需要Directory对象和IndexWriterConfig对象
	
	private static Analyzer analyzer = new StandardAnalyzer();//标准分词器
	
	
	public static void main(String[] args) {
		
		String indexDir = "D:\\LuceneIndexFiles\\index004";
		String dataDir = "D:\\LuceneTestFiles\\data";
		
		Indexer indexer = null;
		int count = 0;
		long start = System.currentTimeMillis();
		
		try {
			indexer = new Indexer(indexDir);
			count = indexer.createIndex(dataDir);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				indexer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		
		System.out.println("总共索引：" + count + " 个文件，花费了 " + (end - start) + "(ms)");
	}
	
	
	/**
	 * 构造方法 实例化IndexWriter对象
	 * @param indexDir
	 * @throws Exception
	 */
	public Indexer(String indexDir) throws Exception {
		
		Directory dir = FSDirectory.open(Paths.get(indexDir));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		this.writer = new IndexWriter(dir, config);
	}
	
	/**
	 * 关闭IndexWriter对象
	 * @throws Exception
	 */
	public void close() throws Exception{
		
		this.writer.close();
	}
	
	/**
	 * 索引指定目录的所有文件，返回索引文件总数
	 * @param dataDir
	 * @throws Exception
	 */
	public int createIndex(String dataDir) throws Exception{
		
		File[] files = new File(dataDir).listFiles();
		for (File f : files) {
			indexFile(f);
		}
		return this.writer.numDocs();
	}
	
	
	/**
	 * 索引指定的一个文件
	 * @param file
	 * @throws Exception
	 */
	private void indexFile(File file) throws Exception {
		System.out.println("正在为文件[" + file.getCanonicalFile() + "]创建索引文件...");
		Document doc = getDocument(file);
		
		this.writer.addDocument(doc);
	}
	
	
	/**
	 * 获取文档对象，文档里再设置每个字段
	 * @param file
	 * @throws Exception 
	 */
	private Document getDocument(File file) throws Exception {
		
		Document doc = new Document();
		doc.add(new TextField("content", new BufferedReader(new FileReader(file))));
		doc.add(new TextField("fileName", file.getName(), Store.YES));
		doc.add(new TextField("fullPath",file.getCanonicalPath(),Store.YES));
		
		return doc;
	}
}
