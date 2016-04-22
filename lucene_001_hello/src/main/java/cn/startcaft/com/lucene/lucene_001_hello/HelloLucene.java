package cn.startcaft.com.lucene.lucene_001_hello;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class HelloLucene {
	
	/**
	 * 为了快速搜索大量的文本，必须首先建立针对文本的索引，
	 * 将文本额你容转换成能够进行快速搜索的格式，从而消除慢速顺序扫描处理所带来的影响。
	 * @throws IOException 
	 */
	public void createIndex() throws IOException{
		
		//1,创建Directory目录对象，指定索引文件存储的位置(内存，硬盘等)
		Directory directory = FSDirectory.open(Paths.get("D:", "LuceneIndexFiles/Index001"));
		
		//2,创建IndexWriter对象，
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		
		//3,创建Document对象
		Document document = null;
		File fileDir = new File("D:\\LuceneTestFiles");//指定需要搜索的文件存放的配置
		for(File file : fileDir.listFiles()){
			//4,为Document添加各种域Field对象
			document = new Document();
			document.add(new TextField("content", new BufferedReader(new FileReader(file))));//添加content字段
			document.add(new StringField("filename", file.getName(), Store.YES));//添加filename字段，并保存到索引
			document.add(new StringField("path", file.getAbsolutePath(), Store.YES));//添加path字段，并保存到索引
			
			//5,通过IndexWriter对象将Document对象写入到索引中
			indexWriter.addDocument(document);
		}
		
		//6,用完之后必须关闭IndexWriter对象
		indexWriter.close();
	}
	
	
	/**
	 * 通过索引查询关键字，
	 * 注意：索引是增量式索引，每一次创建索引都会新的索引文件，这样的话，查询出来的结果也是增量式的。
	 */
	public void searcher() throws IOException, ParseException{
		
		//1,创建Directory目录对象，指定索引文件存储的位置(内存，硬盘等)
		Directory directory = FSDirectory.open(Paths.get("D:", "LuceneIndexFiles/Index001"));
		
		//2,创建IndexReader对象
		IndexReader indexReader = DirectoryReader.open(directory);
		
		//3,根据IndexReader对象创建IndexSearcher对象
		IndexSearcher searcher = new IndexSearcher(indexReader);
		
		//4,创建搜索的Query对象
		//创建QueryParser对象来确定要搜索文件的内容，第一个参数表示要搜索的域(字段)
		QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		//通过QueryParser对象创建Query对象，表示搜索字段中包含的"java"的文档
		Query query = parser.parse("java");
		
		//5,根据IndexSearcher对象搜索并返回TopDocs
		TopDocs tds = searcher.search(query, 10);//第二个参数表示搜索的条目数
		
		//6,根据TopDocs对象获取ScoreDocs对象
		ScoreDoc[] sds = tds.scoreDocs;
		for(ScoreDoc sd : sds){
			//7,根据IndexSearcher对象和ScoreDoc对象获取 具体的Document对象
			Document document = searcher.doc(sd.doc);
			
			//8,根据Document对象获取需要的字段值
			System.out.println(document.get("filename") + "["+document.get("path")+"]");
		}
		
		//9,查询完毕，必须关闭IndexReader对象
		indexReader.close();
	}
}
