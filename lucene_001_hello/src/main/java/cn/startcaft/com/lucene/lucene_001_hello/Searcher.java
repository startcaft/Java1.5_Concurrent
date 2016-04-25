package cn.startcaft.com.lucene.lucene_001_hello;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher {
	
	private static Analyzer analyzer = new StandardAnalyzer();//标准分词器
	
	
	public static void main(String[] args) {
		
		String indexDir = "D:\\LuceneIndexFiles\\datIndex";
		
//		String q = "java";
		String q = "Java 7";
		
		try {
			Searcher.searcher(indexDir, q);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	public static void searcher(String indexPath,String queryStr) throws Exception{
		
		Directory dir = FSDirectory.open(Paths.get(indexPath));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		
		QueryParser parser = new QueryParser("content", analyzer);//查询指定的field
		Query query = parser.parse(queryStr);					//匹配指定内容
		
		long start = System.currentTimeMillis();
		TopDocs hits = searcher.search(query, 10);
		long end = System.currentTimeMillis();
		System.out.println("匹配[" + queryStr + "]，总共花费 " + (end-start) + "(ms)，查询到" + hits.totalHits + "条记录!");
		
		for(ScoreDoc sd : hits.scoreDocs){
			//通过文档内部编号获取对象
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("fullPath"));
		}
		
		reader.close();
	}
}
