package cn.startcaft.www.lucene.Lucene_study_002_Field;

import java.io.IOException;
import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;

public class BinaryDocValuesFieldTest {
	
	/*
	 * 保存一个BinaryDocValuesField类型的字段，二进制内容，
	 * 【BinaryDocValuesField的排序不需要添加预排序字段】
	 */
	@Test
	public void testIndexBinaryDocValuesFieldStored(){
		
		Document doc = new Document();
		doc.add(new BinaryDocValuesField("binaryValue", new BytesRef("1234".getBytes())));
		
		Document doc1 = new Document();
		doc1.add(new BinaryDocValuesField("binaryValue", new BytesRef("2345".getBytes())));
		
		IndexWriter writer = null;
		try {
			writer = IndexWriterUtil.getIndexWriter("binaryValueFieldPath", false);
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
	 * 测试BinaryDocValuesField类型字段的排序
	 */
	@Test
	public void testBinaryDocValuesFieldSort(){
		try {
			IndexSearcher searcher = IndexSearcherUtil.getIndexSearcher("binaryValueFieldPath", null);
			//构建排序字段
			SortField[] sfs = new SortField[1];
			sfs[0] = new SortField("binaryValue", SortField.Type.STRING_VAL,true);
			Sort sort = new Sort(sfs);
			//查询所有结果
			Query query = new MatchAllDocsQuery();
			TopFieldDocs docs = searcher.search(query, 10, sort);
			ScoreDoc[] sds = docs.scoreDocs;
			//遍历结果
			for (ScoreDoc sd : sds) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(doc);
				//这里不会输出BinaryDocValuesField字段的值，这是跟BinaryDocValuesField类型的特性决定的
				//【BinaryDocValuesField只索引不存储值】
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
