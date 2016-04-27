package com.startcaft.lucene.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;

import com.startcaft.lucene.bean.FileBean;

public class FileUtil {
	
	/**
	 * 递归获取一个指定路径下的所有文件信息
	 * @param folder
	 * @return
	 * @throws Exception
	 */
	public static List<FileBean> getFolderFiles(String folder) throws Exception{
		
		List<FileBean> fbs = new LinkedList<FileBean>();
		
		File file = new File(folder);
		//如果是一个文件夹
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File f : files) {
					//递归
					fbs.addAll(getFolderFiles(f.getAbsolutePath()));
				}
			}
		}
		//如果只是一个单纯的文件
		else{
			FileBean bean = new FileBean();
			String filePath = file.getAbsolutePath();
			bean.setPath(filePath);
			bean.setModified(file.lastModified());
			String content = "";
			//判断文件类型
			if (filePath.endsWith(".doc") || filePath.endsWith(".docx")) {
				content = readDoc(file);
			}
			else if (filePath.endsWith(".xls") || filePath.endsWith(".xlsx")) {
				content = readExcel(file);
			}
			else if(filePath.endsWith(".pdf")){
				content = readPdf(file);
			}
			else {
				content = new String(Files.readAllBytes(Paths.get(folder)));
			}
			bean.setContent(content);
			fbs.add(bean);
		}
		
		return fbs;
	}

	/**
	 * 读取PDF文档内容
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private static String readPdf(File file) throws Exception {
		
		PDDocument doc = PDDocument.load(file.getAbsolutePath());  
        PDFTextStripper stripper = new PDFTextStripper();  
        String content = stripper.getText(doc);  
        doc.close();  
        return content; 
	}
	
	/**
	 * 读文Excel文档内容
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private static String readExcel(File file) throws Exception {
		
		String filePath = file.getAbsolutePath();  
        StringBuffer content = new StringBuffer("");  
        if(filePath.endsWith(".xls")){  
            InputStream inp = new FileInputStream(filePath);  
            Workbook wb = WorkbookFactory.create(inp);     
            Sheet sheet = wb.getSheetAt(0);  
            for(int i = sheet.getFirstRowNum();i<= sheet.getPhysicalNumberOfRows();i++){    
                HSSFRow row = (HSSFRow) sheet.getRow(i);    
                if (row == null) {    
                      continue;    
                }  
                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {   
                    if(j < 0){  
                        continue;//增加下标判断  
                    }  
                    HSSFCell cell = row.getCell(j);    
                    if (cell == null) {    
                          continue;    
                    }  
                    content.append(cell.getStringCellValue());  
                      
                }  
            }  
            inp.close();  
        }else{  
            @SuppressWarnings("deprecation")
			XSSFWorkbook xwb = new XSSFWorkbook(file.getAbsolutePath());  
            XSSFSheet sheet = xwb.getSheetAt(0);    
            // 定义 row、cell    
            XSSFRow row;    
            String cell;    
            // 循环输出表格中的内容    
            for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {    
                row = sheet.getRow(i);    
                if(row == null){  
                    continue;  
                }  
                for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {    
                    // 通过 row.getCell(j).toString() 获取单元格内容，  
                    if(j<0){  
                        continue;  
                    }  
                    XSSFCell xfcell = row.getCell(j);  
                    if(xfcell == null){  
                        continue;  
                    }  
                    xfcell.setCellType(Cell.CELL_TYPE_STRING);//数值型的转成文本型  
                    cell = xfcell.getStringCellValue();  
                    content.append(cell+" ");  
                }    
            }    
        }  
        return content.toString();  
	}
	
	/**
	 * 读文word文档内容
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws XmlException
	 * @throws OpenXML4JException
	 */
	private static String readDoc(File file) throws IOException, XmlException, OpenXML4JException {
		
		String filePath = file.getAbsolutePath();  
        if(filePath.endsWith(".doc")){  
            InputStream is = new FileInputStream(file);  
            WordExtractor ex = new WordExtractor(is);    
            String text2003 = ex.getText();    
            is.close();  
            return text2003;  
        }else{  
            OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);    
            POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);    
            String text2007 = extractor.getText();    
            return text2007;  
        }  
	}
}
