package test;

import java.nio.file.Paths;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*
 * 除了TermQuery之外，还有其他的查询方式
 */
public class OtherSearchTest {
	
	private Directory dir = null;
	private IndexReader reader = null;
	private IndexSearcher searcher = null;
	
	
	
	@Before
	public void setUp() throws Exception{
		
		dir = FSDirectory.open(Paths.get("D:\\LuceneIndexFiles\\index005", new String[0]));
		reader = DirectoryReader.open(dir);
		searcher = new IndexSearcher(reader);
	}
	
	@After
	public void shutDown() throws Exception{
		
		reader.close();
	}
	
	/*
	 * 指定文档域的范围查询 TermRangeQuery
	 */
	@Test
	public void testTermRangeQuery() throws Exception{
		
		TermRangeQuery tRangeQuery = new TermRangeQuery("desc", new BytesRef("a".getBytes()), new BytesRef("c".getBytes()), true, true);
		
		TopDocs hits = searcher.search(tRangeQuery, 10);
		
		for (ScoreDoc sd : hits.scoreDocs) {
			
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("id"));
			System.out.println(doc.get("city"));
			System.out.println(doc.get("desc"));//这里是空，因为索引时，没有存储
		}
	}
	
	/*
	 * 指定数字的范围查询 NumericRangeQuery
	 */
	@Test
	public void testNumericRangeQuery() throws Exception{
		
		NumericRangeQuery<Integer> query = NumericRangeQuery.newIntRange("id", 1, 2, true, true);
		
		TopDocs hits = searcher.search(query, 10);
		
		for (ScoreDoc sd : hits.scoreDocs) {
			
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("id"));
			System.out.println(doc.get("city"));
			System.out.println(doc.get("desc"));
		}
	}
	
	/*
	 * 指定字符串开头搜索 PrefixQuery
	 */
	@Test
	public void testPrefixQuery() throws Exception{
		
		PrefixQuery query = new PrefixQuery(new Term("city", "s"));//city字段以s开头的
		
		TopDocs hits = searcher.search(query, 10);
		
		for (ScoreDoc sd : hits.scoreDocs) {
			
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("id"));
			System.out.println(doc.get("city"));
			System.out.println(doc.get("desc"));
		}
	}
	
	/*
	 * 组合查询，也叫多条件查询 BooleanQuery【这个比较重要】
	 */
	@Test
	public void testBooleanQuery() throws Exception{
		
		//第一个条件，id必须是1,2之间
		NumericRangeQuery<Integer> query1 = NumericRangeQuery.newIntRange("id", 1, 2, true, true);
		//第二个条件，city必须以q开头
		PrefixQuery query2 = new PrefixQuery(new Term("city", "q"));
		
		//在组合查询的时候，多个条件之间可以是and，也可以是or，由Occur枚举来控制
		//MUST就是并且
		//SHOULD就是或者
		//MUST_NOT就是不能包含
		BooleanQuery bQuery = new BooleanQuery();
		bQuery.add(query1, Occur.MUST);
		bQuery.add(query2, Occur.MUST);
		
		TopDocs hits = searcher.search(bQuery, 10);
		
		for (ScoreDoc sd : hits.scoreDocs) {
			
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("id"));
			System.out.println(doc.get("city"));
			System.out.println(doc.get("desc"));
		}
	}
}
