package cn.startcaft.com.lucene.lucene_002_index;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;

public class IndexTest {
	
	private static Analyzer analyzer = new StandardAnalyzer();//标准分词器
	
	private String[] ids = {"1","2","3"};
	private String[] cites = {"qingdao","nanjing","shanghai"};
	private String[] descs = {
			"Qingdao is a beautiful city",
			"Nanjing is c city of culture",
			"Shanghai is a bustling city"
	};
	
	private Directory dir;
	
	@Before
	public void setUp() throws Exception{
		
		dir = FSDirectory.open(Paths.get("D:\\LuceneIndexFiles\\Index002", new String[0]));
		IndexWriter writer = getIndexWirter();
		
		//先清空原先的索引
		writer.deleteAll();
		
		for (int i = 0; i < ids.length; i++) {
			Document doc = new Document();
			doc.add(new StringField("id", ids[i], Store.YES));
			doc.add(new StringField("city", cites[i], Store.YES));
			doc.add(new TextField("desc", descs[i], Store.NO));
			
			writer.addDocument(doc);
		}
		
		writer.close();
	}
	
	/*
	 * 测试写了几个文档
	 */
	@Test
	public void testIndexWriter() throws Exception{
		
		IndexWriter writer = getIndexWirter();
		System.out.println("写入了" + writer.numDocs() + "个文档");
		
		writer.close();
	}
	
	/*
	 * 测试读取文档
	 */
	@Test
	public void testIndexReader() throws Exception{
		
		IndexReader reader = DirectoryReader.open(dir);
		System.out.println("最大文档数：" + reader.maxDoc());
		System.out.println("实际文档数：" + reader.numDocs());
		
		reader.close();
	}
	
	/*
	 * 测试删除文档，在merge合并之前
	 */
	@Test
	public void testDeleteBeforeMerge() throws Exception{
		
		IndexWriter writer = getIndexWirter();
		System.out.println("删除前------");
		System.out.println("writer.numDocs()" + writer.numDocs());
		System.out.println("writer.numDocs()：" + writer.numDocs());
		
		//删除id为1的Document，在merge之前，只会把要删除的Docuemnt进行标记，而不进行真正的操作
		Term term = new Term("id", "1");
		writer.deleteDocuments(term);
		writer.commit();
		
		System.out.println("删除后------");
		System.out.println("writer.maxDoc()：" + writer.maxDoc());
		System.out.println("writer.numDocs()：" + writer.numDocs());
		
		writer.close();
	}
	
	/*
	 * 测试删除文档，在merge合并之后
	 */
	@Test
	public void testDeleteAfterMerge() throws Exception{
		
		IndexWriter writer = getIndexWirter();
		System.out.println("删除前------");
		System.out.println("writer.numDocs()" + writer.numDocs());
		System.out.println("writer.numDocs()：" + writer.numDocs());
		
		//删除id为1的Document
		Term term = new Term("id", "1");
		writer.deleteDocuments(term);
		//合并，强制删除标记为删除的Document，不要随便进行此操作，会消耗大量系统资源
		writer.forceMergeDeletes();
		writer.commit();
		
		System.out.println("删除后------");
		System.out.println("writer.maxDoc()：" + writer.maxDoc());
		System.out.println("writer.numDocs()：" + writer.numDocs());
		
		writer.close();
	}
	
	/*
	 * 测试更新索引中的Document，先删除指定的Document，再添加一个新的Document
	 */
	@Test
	public void testUpdate() throws Exception{
		
		IndexWriter writer = getIndexWirter();
		Document newDoc = new Document();
		newDoc.add(new StringField("id", "1", Store.YES));
		newDoc.add(new StringField("city", "qingdao", Store.YES));
		newDoc.add(new TextField("desc", "dsss is a city", Store.NO));
		
		writer.updateDocument(new Term("id","1"), newDoc);
		writer.commit();
		
		writer.close();
	}
	
	
	
	
	
	/**
	 * 获取IndexWriter对象
	 * @return
	 * @throws Exception
	 */
	private IndexWriter getIndexWirter() throws Exception{
		
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		return new IndexWriter(dir, config);
	}
	
}
