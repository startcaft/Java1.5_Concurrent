package cn.startcaft.www.lucene.Lucene_study_001_IndexWriter;

import java.io.IOException;
import org.apache.lucene.index.IndexWriter;
import org.junit.Test;

public class IndexWriterUtilTest {
	
	@Test
	public void testIndexWriter() throws IOException{
		
		IndexWriter iw = IndexWriterUtil.getIndexWriter("D:\\LuceneIndexFiles\\index001", true);
		
		System.out.println(iw);
	}
}
