package cn.startcaft.www.lucene.Lucene_study_002_Field;

import java.io.IOException;
import java.text.SimpleDateFormat;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FloatDocValuesField;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopFieldDocs;
import org.junit.Test;

public class FloatFieldTest {
	
	/*
	 * 保存一个FloatField字段,
	 * 注意，FloatField类型字段的排序不再使用NumericDocValuesField来排序，而是用【FloatDocValuesField】来排序
	 */
	@Test
	public void testIndexFloatField() throws IOException{
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		IndexWriter writer = null;
		
		try {
			Document doc = new Document();
			doc.add(new FloatField("floatValue", 9.1f, Store.YES));
			//添加排序
			doc.add(new FloatDocValuesField("floatValue", format.parse("2015-02-03").getTime()));
			
			Document doc1 = new Document();
			doc1.add(new FloatField("floatValue", 8.2f,Store.YES));
			//添加排序
			doc1.add(new FloatDocValuesField("floatValue", 8.2f));
			
			writer = IndexWriterUtil.getIndexWriter("floatFieldPath", false);
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
	 * 测试FloatField排序
	 */
	@Test
	public void testFloatFieldSort(){
		try {
			IndexSearcher searcher = IndexSearcherUtil.getIndexSearcher("floatFieldPath", null);
			//构建排序字段，确定Field的名称和Field的类型
			SortField[] sf = new SortField[1];
			sf[0] = new SortField("floatValue", SortField.Type.FLOAT,true);
			Sort sort = new Sort(sf);
			//查询所有结果
			Query query = new MatchAllDocsQuery();
			TopFieldDocs tfds = searcher.search(query, 10, sort);
			ScoreDoc[] sds = tfds.scoreDocs;
			//遍历结果集
			for (ScoreDoc sd : sds) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
