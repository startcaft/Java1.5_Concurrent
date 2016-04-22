package cn.startcaft.com.lucene.lucene_002_index;

import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexUtil {
	
	private String[] ids = {"1","2","3","4","5","6"};
	private String[] emails = {
			"aa@163.com",
			"bb@163.com",
			"cc@163.com",
			"dd@163.com",
			"ee@163.com",
			"ff@163.com"
	};
	private String[] contents = {
			"welcome to visited the space",
			"hello boy",
			"my name is cc",
			"i like footbacl",
			"i like footbal and i like basketball too",
			"i like movie and swim"
	};
	private int[] attachs = {2,3,1,4,5};
	private String[] names = {"zhangsan","lisi","john","jetty","mike","jake"};
	
	private Directory directory = null;
	
	//1,指定索引所在的目录，构建一个Directory对象
	public IndexUtil() {
		try {
			this.directory = FSDirectory.open(Paths.get("D:", "LuceneIndexFiles/Index002"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void index(){
		
		IndexWriter indexWriter = null;
		
		try {
			//2,根据IndexWriterConfig(依赖于分词器对象Analyzer)对象创建 IndexWriter对象
			indexWriter = new IndexWriter(this.directory, new IndexWriterConfig(new StandardAnalyzer()));
			
			//3,创建Document
			Document doc = null;
			for (int i = 0; i < ids.length; i++) {
				doc = new Document();
				doc.add(new StringField("id", ids[i], Store.YES));
				doc.add(new StringField("email", emails[i], Store.YES));
				doc.add(new StringField("content", contents[i], Store.NO));
				doc.add(new StringField("name",names[i],Store.YES));
				
				//4，将Document添加到IndexWriter对象中
				indexWriter.addDocument(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (indexWriter != null) {
				try {
					indexWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void query(){
		
		//创建IndexReader
		IndexReader indexReader = null;
		
		try {
			//通过IndexReader可以有效的获取文档的数量
			indexReader = DirectoryReader.open(this.directory);
			System.out.println("存储的文档数:" + indexReader.numDocs());
			System.out.println("文档的总数:" + indexReader.maxDoc());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
