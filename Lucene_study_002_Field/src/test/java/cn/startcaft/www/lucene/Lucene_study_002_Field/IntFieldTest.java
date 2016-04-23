package cn.startcaft.www.lucene.Lucene_study_002_Field;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopFieldDocs;
import org.junit.Test;

public class IntFieldTest {
	
	/*
	 * 保存一个intField字段 
	 */
	@Test
	public void testIntFieldStored(){
		Document doc = new Document();
		doc.add(new IntField("intValue", 30, Store.YES));
		//要排序必须加同名的field，并且类型为NumericDocValueField
		doc.add(new NumericDocValuesField("intValue", 30));
		
		Document doc1 = new Document();
		doc1.add(new IntField("intValue", 40, Store.YES));
		doc1.add(new NumericDocValuesField("intValue", 40));
		
		IndexWriter writer = null;
		
		try {
			writer = IndexWriterUtil.getIndexWriter("intFieldPath", false);
			
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
	 * 测试IntField排序
	 */
	@Test
	public void testIntFieldSort(){
		
		try {
			IndexSearcher searcher = IndexSearcherUtil.getIndexSearcher("intFieldPath", null);
			//构建排序字段
			SortField[] sf = new SortField[1];
			sf[0] = new SortField("intValue", SortField.Type.INT, true);
			Sort sort = new Sort(sf);
			//查询所有结果
			Query query = new MatchAllDocsQuery();
			TopFieldDocs docs = searcher.search(query, 2, sort);
			ScoreDoc[] sds = docs.scoreDocs;
			//便利结果
			for(ScoreDoc sd : sds){
				Document doc = searcher.doc(sd.doc);
				System.out.println(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
