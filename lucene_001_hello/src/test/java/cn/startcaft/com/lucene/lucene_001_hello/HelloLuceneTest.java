package cn.startcaft.com.lucene.lucene_001_hello;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;

public class HelloLuceneTest {
	
	@Test
	public void testCreateIndex() throws IOException{
		
		HelloLucene hl = new HelloLucene();
		hl.createIndex();
	}
	
	@Test
	public void testSearcherIndex() throws IOException, ParseException{
		
		HelloLucene hl = new HelloLucene();
		hl.searcher();
	}
}
