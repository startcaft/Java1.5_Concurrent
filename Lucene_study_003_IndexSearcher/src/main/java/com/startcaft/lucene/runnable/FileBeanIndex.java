package com.startcaft.lucene.runnable;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;

import com.startcaft.lucene.bean.FileBean;

public class FileBeanIndex extends BaseIndex<FileBean> {

	public FileBeanIndex(IndexWriter writer, CountDownLatch countDownLatch1, CountDownLatch countDownLatch2,
			List<FileBean> list) {
		super(writer, countDownLatch1, countDownLatch2, list);
	}
	
	public FileBeanIndex(String parentIndexPath, int subIndex, CountDownLatch countDownLatch1,
			CountDownLatch countDownLatch2, List<FileBean> list) {
		super(parentIndexPath, subIndex, countDownLatch1, countDownLatch2, list);
	}

	@Override
	public void indexDoc(IndexWriter writer, FileBean t) throws Exception {
		
		Document doc = new Document();
		System.out.println("正在为文件[" + t.getPath() + "]创建索引...");
		doc.add(new StringField("path", t.getPath(), Field.Store.YES));
		doc.add(new LongField("modified", t.getModified(), Field.Store.YES));
		//为modified字段添加一个排序
		doc.add(new NumericDocValuesField("modified", t.getModified()));
		doc.add(new TextField("content", t.getContent(), Field.Store.YES));
		
		if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
			writer.addDocument(doc);
		}
		else {
			writer.updateDocument(new Term("path", t.getPath()), doc);
		}
	}
	
}
