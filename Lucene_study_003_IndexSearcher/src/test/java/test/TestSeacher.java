package test;

import java.io.IOException;
import java.util.concurrent.Executors;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import cn.startcaft.www.lucene.Lucene_study_003_IndexReader.IndexSearcherUtil;

public class TestSeacher {
	
	@Test
	public void testSearcher() throws IOException{
		
		IndexSearcher searcher = IndexSearcherUtil.getIndexSearcher("index", Executors.newCachedThreadPool(), false);
		
		Query contentQuery = IndexSearcherUtil.getQuery("content", "string", "电话", false);
		TopDocs tds = IndexSearcherUtil.getScoreDocsByPerPageAndSortField(searcher, contentQuery, 0, 20, null);
		
		System.out.println("符合条件的数据总数：" + tds.totalHits);
		System.out.println("本次查询到的数目为："+tds.scoreDocs.length);
		
		ScoreDoc[] scoreDocs = tds.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			System.out.println(doc.get("path")+"    "+doc.get("content"));
		}
	}
}
