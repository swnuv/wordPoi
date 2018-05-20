package test1;

  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  

import java.util.HashMap;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.Map.Entry;  

import org.apache.poi.POIXMLDocument;  
import org.apache.poi.hwpf.HWPFDocument;  
import org.apache.poi.hwpf.usermodel.Range;  
import org.apache.poi.xwpf.usermodel.XWPFDocument;  
import org.apache.poi.xwpf.usermodel.XWPFParagraph;  
import org.apache.poi.xwpf.usermodel.XWPFRun;  
import org.apache.poi.xwpf.usermodel.XWPFTable;  
import org.apache.poi.xwpf.usermodel.XWPFTableCell;  
import org.apache.poi.xwpf.usermodel.XWPFTableRow;  
  
public class WordPoi {  
  
    
    // 替换word中需要替换的特殊字符  
    public static boolean replaceAndGenerateWord(String srcPath,  
            String destPath, Map<String, String> map) {  
        String[] sp = srcPath.split("\\.");  
        String[] dp = destPath.split("\\.");  
        if ((sp.length > 0) && (dp.length > 0)) {// 判断文件有无扩展名  
            // 比较文件扩展名  
            if (sp[sp.length - 1].equalsIgnoreCase("docx")) {  
                try {  
                    XWPFDocument document = new XWPFDocument(  
                            POIXMLDocument.openPackage(srcPath));  
                    // 替换段落中的指定文字  
                    Iterator<XWPFParagraph> itPara = document  
                            .getParagraphsIterator();  
                    while (itPara.hasNext()) {  
                        XWPFParagraph paragraph = (XWPFParagraph) itPara.next();  
                        List<XWPFRun> runs = paragraph.getRuns();  
                        for (int i = 0; i < runs.size(); i++) {  
                            String oneparaString = runs.get(i).getText(  
                                    runs.get(i).getTextPosition());  
                            for (Map.Entry<String, String> entry : map  
                                    .entrySet()) {  
                                oneparaString = oneparaString.replace(  
                                        entry.getKey(), entry.getValue());  
                            }  
                            runs.get(i).setText(oneparaString, 0);  
                        }  
                    }  
  
                    // 替换表格中的指定文字  
                    Iterator<XWPFTable> itTable = document.getTablesIterator();  
                    while (itTable.hasNext()) {  
                        XWPFTable table = (XWPFTable) itTable.next();  
                        int rcount = table.getNumberOfRows();  
                        for (int i = 0; i < rcount; i++) {  
                            XWPFTableRow row = table.getRow(i);  
                            List<XWPFTableCell> cells = row.getTableCells();  
                            for (XWPFTableCell cell : cells) {  
                                String cellTextString = cell.getText();  
                                for (Entry<String, String> e : map.entrySet()) {  
                                    if (cellTextString.contains(e.getKey()))  
                                        cellTextString = cellTextString  
                                                .replace(e.getKey(),  
                                                        e.getValue());  
                                }  
                                cell.removeParagraph(0);  
                                cell.setText(cellTextString);  
                            }  
                        }  
                    }  
                    FileOutputStream outStream = null;  
                    outStream = new FileOutputStream(destPath);  
                    document.write(outStream);  
                    outStream.close();  
                    return true;  
                } catch (Exception e) {  
                    e.printStackTrace();  
                    return false;  
                }  
  
            } else  
            // doc只能生成doc，如果生成docx会出错  
            if ((sp[sp.length - 1].equalsIgnoreCase("doc"))  
                    && (dp[dp.length - 1].equalsIgnoreCase("doc"))) {  
                HWPFDocument document = null;  
                try {  
                    document = new HWPFDocument(new FileInputStream(srcPath));  
                    Range range = document.getRange();  
                    for (Map.Entry<String, String> entry : map.entrySet()) {  
                        range.replaceText(entry.getKey(), entry.getValue());  
                    }  
                    FileOutputStream outStream = null;  
                    outStream = new FileOutputStream(destPath);  
                    document.write(outStream);  
                    outStream.close();  
                    return true;  
                } catch (FileNotFoundException e) {  
                    e.printStackTrace();  
                    return false;  
                } catch (IOException e) {  
                    e.printStackTrace();  
                    return false;  
                }  
            } else {  
                return false;  
            }  
        } else {  
            return false;  
        }  
    }  
  
    public static void main(String[] args) {  
        // TODO Auto-generated method stub  
        String filepathString = "C:/Users/suiwei/Desktop/合同生成.doc";  
        String destpathString = "C:/Users/suiwei/Desktop/合同生成1.doc";  
        Map<String, String> map = new HashMap<String, String>();  
        map.put("${NAME}", "12345000");  
        map.put("${NsAME}", "出租方测试");  
        map.put("${NAMaE}", "承租方测试");  
        map.put("${NArME}", "王五王五啊柯乐义的辣味回答东拉网");  
        map.put("${NwAME}", "王五王五啊王的辣味回答侯何问起网");  
        map.put("${NA4ME}", "王五王五啊王力侯何问起网");  
        map.put("${N5AME}", "王五王五辣味回答侯何问起网");  
        map.put("${NAadwME}", "王五力宏的辣味回答侯何问起网");  
        System.out.println(replaceAndGenerateWord(filepathString,  
                destpathString, map));  
    }  
}