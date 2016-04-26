package cn.startcaft.com.lucene_006_analyzer_chinese;

import java.io.StringReader;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher {
	
	private static Analyzer analyzer = new SmartChineseAnalyzer();//中文分词器
	
	
	public static void main(String[] args) {
		
		String indexDir = "D:\\LuceneIndexFiles\\Index006";
		
		String q = "南京 文明";
		
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
		
		QueryParser parser = new QueryParser("desc", analyzer);//查询指定的field
		Query query = parser.parse(queryStr);					//匹配指定内容
		
		long start = System.currentTimeMillis();
		TopDocs hits = searcher.search(query, 10);
		long end = System.currentTimeMillis();
		System.out.println("匹配[" + queryStr + "]，总共花费 " + (end-start) + "(ms)，查询到" + hits.totalHits + "条记录!");
		
		
		//高亮显示
		QueryScorer scorer=new QueryScorer(query);
		Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);
		//一个HTML的格式化器
		SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
		Highlighter highlighter=new Highlighter(simpleHTMLFormatter, scorer);
		highlighter.setTextFragmenter(fragmenter);
		
		for(ScoreDoc sd : hits.scoreDocs){
			//通过文档内部编号获取对象
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("city"));
			System.out.println(doc.get("desc"));
			String desc=doc.get("desc");
			
			if(desc!=null){
				TokenStream tokenStream = analyzer.tokenStream("desc", new StringReader(desc));
				System.out.println(highlighter.getBestFragment(tokenStream, desc));
			}
		}
		
		reader.close();
	}
}
