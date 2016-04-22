package cn.startcaft.com.lucene.lucene_002_index;

import org.junit.Test;

public class TestIndexUtil {
	
	@Test
	public void testIndex(){
		IndexUtil iu = new IndexUtil();
		iu.index();
	}
	
	@Test
	public void testQuery(){
		IndexUtil iu = new IndexUtil();
		iu.query();
	}
}
