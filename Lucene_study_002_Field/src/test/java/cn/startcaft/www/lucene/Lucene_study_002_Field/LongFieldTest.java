package cn.startcaft.www.lucene.Lucene_study_002_Field;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.document.Field.Store;
import org.junit.Test;

public class LongFieldTest {
	
	/*
	 * 保存一个longField字段,
	 * LongField类型的字段一般都是用来存储时间戳
	 */
	@Test
	public void testIndexLongField() throws IOException{
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		IndexWriter writer = null;
		
		try {
			Document doc = new Document();
			doc.add(new LongField("longValue", format.parse("2015-02-03").getTime(), Store.YES));
			//添加排序
			doc.add(new NumericDocValuesField("longValue", format.parse("2015-02-03").getTime()));
			
			Document doc1 = new Document();
			doc1.add(new LongField("longValue", format.parse("2016-02-03").getTime(), Store.YES));
			//添加排序
			doc1.add(new NumericDocValuesField("longValue", format.parse("2016-02-03").getTime()));
			
			writer = IndexWriterUtil.getIndexWriter("longFieldPath", false);
			writer.addDocument(doc);
			writer.addDocument(doc1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{  
            try {  
            	writer.commit();  
            	writer.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
	}
	
	/*
	 * 测试LongField排序
	 */
	@Test
	public void testLongFieldSort(){
		try {
			IndexSearcher searcher = IndexSearcherUtil.getIndexSearcher("longFieldPath", null);
			//构建排序字段，确定Field的名称和Field的类型
			SortField[] sf = new SortField[1];
			sf[0] = new SortField("longValue", SortField.Type.LONG,true);
			Sort sort = new Sort(sf);
			//查询所有结果
			Query query = new MatchAllDocsQuery();
			TopFieldDocs tfds = searcher.search(query, 10, sort);
			ScoreDoc[] sds = tfds.scoreDocs;
			//遍历结果集
			for (ScoreDoc sd : sds) {
				Document doc = searcher.doc(sd.doc);
				long time = Long.parseLong(doc.get("longValue"));
				Date date = new Date(time);
				System.out.println(doc + ":" + date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
