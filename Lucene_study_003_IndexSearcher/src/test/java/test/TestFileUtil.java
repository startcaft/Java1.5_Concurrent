package test;

import java.util.List;

import org.junit.Test;

import com.startcaft.lucene.bean.FileBean;
import com.startcaft.lucene.util.FileUtil;

public class TestFileUtil {
	
	@Test
	public void testGetFileBeans() throws Exception{
		
		String folder = "testdir";
		
		List<FileBean> fbs = FileUtil.getFolderFiles(folder);
		
		if (fbs != null && fbs.size() > 0) {
			for (FileBean fileBean : fbs) {
				System.out.println("file:" + fileBean.getPath());
			}
		}
	}
}
