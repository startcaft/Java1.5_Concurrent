package cn.startcaft.www.lucene.Lucene_study_002_Field;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.SortedDocValuesField;
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

public class StringFieldTest {
	
	/*
	 * 保存一个StringField类型的字段，
	 * 它要排序的话需要添加相同名称的SortedDocValuesField字段
	 */
	@Test
	public void testIndexStringFileStored(){
		
		Document document = new Document();
		document.add(new StringField("stringValue", "12345", Store.YES));
		document.add(new SortedDocValuesField("stringValue", new BytesRef("12345".getBytes())));
		
		Document document1 = new Document();
		document1.add(new StringField("stringValue", "23456", Store.YES));
		document1.add(new SortedDocValuesField("stringValue", new BytesRef("23456".getBytes())));
		
		IndexWriter writer = null;  
        try {  
            writer = IndexWriterUtil.getIndexWriter("stringFieldPath", false);  
            writer.addDocument(document);  
            writer.addDocument(document1);  
              
        } catch (IOException e) {  
            e.printStackTrace();  
        }finally{  
            try {  
                writer.commit();  
                writer.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
	}
	
	/*
	 * 测试StringField字段的排序，
	 * 构建SortFiled时，需要设置SortField.Type.STRING而不是STRING_VAL
	 */
	@Test  
    public void testStringFieldSort(){  
        try {  
            IndexSearcher searcher = IndexSearcherUtil.getIndexSearcher("stringFieldPath", null); 
            //构建排序字段数组
            SortField[] sfs = new SortField[1];
            //注意，这里的SortField.Type.STRING
            sfs[0] = new SortField("stringValue", SortField.Type.STRING);
            //构建排序对象Sort
            Sort sort = new Sort(sfs);
            //构建Query查询对象
            Query query = new MatchAllDocsQuery();
            //使用IndexSearcher对象获取查询结果
            TopFieldDocs docs = searcher.search(query, 10, sort);
            ScoreDoc[] scores = docs.scoreDocs;  
            //遍历结果  
            for (ScoreDoc scoreDoc : scores) {  
                Document doc = searcher.doc(scoreDoc.doc);  
                System.out.println(doc);  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}
