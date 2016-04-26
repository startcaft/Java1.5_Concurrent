package lucene_004_search;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchTest {
	
	private Directory dir;
	private IndexReader reader;
	private IndexSearcher searcher;
	
	
	@Before
	public void setUp() throws Exception{
		
		dir = FSDirectory.open(Paths.get("D:\\LuceneIndexFiles\\index004", new String[0]));
		reader = DirectoryReader.open(dir);
		searcher = new IndexSearcher(reader);
	}
	
	@After
	public void shutDown() throws Exception{
		
		reader.close();
	}
	
	/*
	 * 分页查询
	 */
	@Test
	public void testQueryPage() throws Exception{
		
		int pageSize = 5;
		int pageIndex = 1;
		
		Analyzer analyzer = new StandardAnalyzer();
		//指定要搜索的文档域Field
		String searchField = "content";
		//指定要搜索的的具体分词内容
		String q = "abc~";//查询相近的单词
		
		QueryParser parser = new QueryParser(searchField, analyzer);
		Query query = parser.parse(q);
		
		TopDocs hits = searcher.search(query, 100);
		ScoreDoc[] sds = hits.scoreDocs;
		//计算总的页数
		int pageTotal = (sds.length + pageSize - 1) / pageSize;
		System.out.println("匹配 '" + q + "'，总共查询到" + hits.totalHits + "个文档，" + "每页显示[" + pageSize + "]条记录，总共[" + pageTotal +  "]页");
		
		//查询起始记录的位置
		int begin = pageSize * (pageIndex - 1);
		//查询终止记录的未知
		int end = Math.min(begin + pageSize,sds.length);
		
		for (int i = begin; i < end; i++) {
			
			int docId = sds[i].doc;
			Document doc = searcher.doc(docId);
			System.out.println(doc.get("fullPath"));
		}
	}
	
	/*
	 * 解析查询表达式，需要指定一个Analyzer类型(分词器)，想要开发高级的搜索功能，必须深入研究分词器了。
	 */
	@Test
	public void testQueryParser() throws Exception{
		
		//指定分词器
		Analyzer analyzer = new StandardAnalyzer();
		//指定要搜索的文档域Field
		String searchField = "content";
		//指定要搜索的的具体分词内容
//		String q = "particular or unicode";//这里就体现了分词器的强大，or 默认可以用空格代替
//		String q = "particular AND jre";//这里就体现了分词器的强大，这里的介词  AND 必须大写【有点坑】
		String q = "abc~";//查询相近的单词
		
		QueryParser parser = new QueryParser(searchField, analyzer);
		Query query = parser.parse(q);
		
		TopDocs hits = searcher.search(query, 10);
		System.out.println("匹配 '" + q + "'，总共查询到" + hits.totalHits + "个文档");
		
		for (ScoreDoc sd : hits.scoreDocs) {
			
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("fullPath"));
		}
		
	}
	
	/*
	 * 针对指定项进行精确搜索
	 */
	@Test
	public void testTermQuery() throws Exception{
		
		//指定要搜索的文档域Field
		String searchField = "content";
		//指定要搜索的的具体分词内容
//		String q = "java";
//		String q = "particular";
		String q = "particula";
		
		Query query = new TermQuery(new Term(searchField, q));
		TopDocs hits = searcher.search(query, 10);
		System.out.println("匹配 '" + q + "'，总共查询到" + hits.totalHits + "个文档");
		
		for (ScoreDoc sd : hits.scoreDocs) {
			
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("fullPath"));
		}
	}
}
