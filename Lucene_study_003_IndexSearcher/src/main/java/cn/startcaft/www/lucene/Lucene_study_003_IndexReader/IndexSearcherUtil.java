package cn.startcaft.www.lucene.Lucene_study_003_IndexReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.FSDirectory;

/**
 * 根据IndexSearcher相关的构建过程及其特性编写的一个搜索的工具类
 * @author startcaft
 */
public class IndexSearcherUtil {
	
	public static final Analyzer analyzer = new SmartChineseAnalyzer();
	
	/**
	 * 获取IndexSearcher对象(适合单索引目录查询使用)
	 * @param indexPath		索引目录
	 * @param service		多线程查询
	 * @param realtime
	 * @return
	 * @throws IOException
	 */
	public static IndexSearcher getIndexSearcher(String indexPath,
												ExecutorService service,
												boolean realtime) throws IOException{
		
		DirectoryReader reader = DirectoryReader.open(IndexWriterUtil.getIndexWriter(indexPath, true), realtime);
		IndexSearcher searcher = new IndexSearcher(reader, service);
		
		if (service != null) {
			service.shutdown();
		}
		
		return searcher;
	}
	
	
	/**
	 * 适用于多级目录的索引目录查询
	 * @param parentPath	父级索引目录
	 * @param service		多线程查询
	 * @param realtime
	 * @return
	 * @throws IOException
	 */
	public static IndexSearcher getMultiSearcher(String parentPath,
												ExecutorService service,
												boolean realtime) throws IOException{
		
		MultiReader mReader;
		File file = new File(parentPath);
		File[] files = file.listFiles();
		
		IndexReader[] readers = new IndexReader[files.length];
		if (!realtime) {
			for (int i = 0; i < files.length; i++) {
				readers[i] = DirectoryReader.open(FSDirectory.open(Paths.get(files[i].getPath(), new String[0])));
			}
		}
		else {
			for (int i = 0; i < files.length; i++) {
				readers[i] = DirectoryReader.open(IndexWriterUtil.getIndexWriter(files[i].getPath(), true), true);
			}
		}
		
		mReader = new MultiReader(readers);
		IndexSearcher searcher = new IndexSearcher(mReader, service);
		
		if (service != null) {
			service.shutdown();
		}
		return searcher;
	}
	
	
	/**
	 * 从指定的配置项中查询
	 * @param fieldName		字段名称
	 * @param fieldType		字段的值类型
	 * @param queryStr		查询条件
	 * @param range			是否区间查询
	 * @return
	 */
	public static Query getQuery(String fieldName,String fieldType,String queryStr,boolean range){
		
		Query query = null;
		if (queryStr != null && !"".equals(queryStr)) {
			//范围查询
			if (range) {
				String[] strs = queryStr.split("\\|");
				if ("int".equals(fieldType)) {
					int min = new Integer(strs[0]);
					int max = new Integer(strs[1]);
					query = NumericRangeQuery.newIntRange(fieldName, min, max, true, true);  
				}
				else if ("double".equals(fieldType)) {
					Double min = new Double(strs[0]);  
                    Double max = new Double(strs[1]);  
                    query = NumericRangeQuery.newDoubleRange(fieldName, min, max, true, true); 
				}
				else if("float".equals(fieldType)){  
                    Float min = new Float(strs[0]);  
                    Float max = new Float(strs[1]);  
                    query = NumericRangeQuery.newFloatRange(fieldName, min, max, true, true);  
                }
				else if("long".equals(fieldType)){  
                    Long min = new Long(strs[0]);  
                    Long max = new Long(strs[1]);  
                    query = NumericRangeQuery.newLongRange(fieldName, min, max, true, true);  
                }  
			}
			//简单查询
			else {  
                if("int".equals(fieldType)){  
                	query = NumericRangeQuery.newIntRange(fieldName, new Integer(queryStr), new Integer(queryStr), true, true);  
                }else if("double".equals(fieldType)){  
                	query = NumericRangeQuery.newDoubleRange(fieldName, new Double(queryStr), new Double(queryStr), true, true);  
                }else if("float".equals(fieldType)){  
                	query = NumericRangeQuery.newFloatRange(fieldName, new Float(queryStr), new Float(queryStr), true, true);  
                }else{  
                    Term term = new Term(fieldName, queryStr);  
                    query = new TermQuery(term);  
                }  
            }  
		}
		else {
			query = new MatchAllDocsQuery(); 
		}
		
		System.out.println(query); 
		return query;
	}
	
	
	/**
	 * 多条查询，类似于sql的in语句查询
	 * @param querys
	 * @return
	 */
	public static Query getMultiQueryLikeSqlIn(Query ...querys){
		
		BooleanQuery query = new BooleanQuery();
		for (Query subQuery : querys) {
			query.add(subQuery,Occur.SHOULD);
		}
		
		return query;
	}
	
	
	/**
	 * 多条件查询，类似于sql的and语句
	 * @param querys
	 * @return
	 */
	public static Query getMultiQueryLikeSqlAnd(Query ... querys){  
		
        BooleanQuery query = new BooleanQuery();  
        for (Query subQuery : querys) {  
            query.add(subQuery,Occur.MUST);  
        }  
        return query;  
    }  
	
	
	/**
	 * 对多个条件进行排序构建排序条件
	 * @param fields	字段数组
	 * @param types		类型数组
	 * @param reverses
	 * @return
	 */
	public static Sort getSortInfo(String[] fields,Type[] types,boolean[] reverses){
		
		SortField[] sfs = null;
		int fieldLength = fields.length;
		int typeLength = types.length;
		int reverLength = reverses.length;
		
		//判断字段个数和类型个数是否匹配
		if (!(fieldLength == typeLength) || !(fieldLength == reverLength)) {
			return null;
		}
		else {
			sfs = new SortField[fields.length];
			for (int i = 0; i < sfs.length; i++) {
				sfs[i] = new SortField(fields[i], types[i], reverses[i]);
			}
		}
		
		return new Sort(sfs);
	}
	
	/**根据查询器、查询条件、每页数、排序条件进行查询 
     * @param query 查询条件 
     * @param first 起始值 
     * @param max 最大值 
     * @param sort 排序条件 
     * @return 
     */  
    public static TopDocs getScoreDocsByPerPageAndSortField(IndexSearcher searcher,Query query, int first,int max, Sort sort){  
    	
        try {  
            if(query == null){  
                System.out.println(" Query is null return null ");  
                return null;  
            }  
            TopFieldCollector collector = null;  
            if(sort != null){  
                collector = TopFieldCollector.create(sort, first+max, false, false, false);  
            }else{  
                sort = new Sort(new SortField[]{new SortField("modified", SortField.Type.LONG)});  
                collector = TopFieldCollector.create(sort, first+max, false, false, false);  
            }  
            searcher.search(query, collector);  
            return collector.topDocs(first, max);  
        } catch (IOException e) {  
        	e.printStackTrace();
        }  
        return null;  
    } 
    
    
    /**获取上次索引的id,增量更新使用 
     * @return 
     */  
    public static Integer getLastIndexBeanID(IndexReader multiReader){  
    	
    	IndexSearcher searcher = null;  
    	searcher = new IndexSearcher(multiReader);  
    	
        Query query = new MatchAllDocsQuery();  
        SortField sortField = new SortField("id", SortField.Type.INT,true);  
        Sort sort = new Sort(new SortField[]{sortField}); 
        
        TopDocs docs = getScoreDocsByPerPageAndSortField(searcher,query, 0, 1, sort);  
        ScoreDoc[] scoreDocs = docs.scoreDocs;  
        int total = scoreDocs.length;  
        if(total > 0){  
            ScoreDoc scoreDoc = scoreDocs[0];  
            Document doc = null;  
            try {  
                doc = searcher.doc(scoreDoc.doc);  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
            return new Integer(doc.get("id"));  
        }  
        return 0;  
    }  
}
