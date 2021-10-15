package com.pageOfficeServer.util;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class DocParamProcessUtil {

    public static void main(String[] args) throws Exception {

    }

    public static void replaceDocFile(String inputFilePath,Map<String,String> map,String outputFilePath)throws IOException {
        HWPFDocument doc = new HWPFDocument(new POIFSFileSystem(new FileInputStream(inputFilePath)));
        // getHeaderStoryRange 获取到的内容包括：页眉、页脚
        Range range= doc.getHeaderStoryRange();
        int length = range.numCharacterRuns();
        for(int i=0;i<length;i++){
            Byte b=new Byte("0");
            range.getCharacterRun(i).setHighlighted(b);
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            range.replaceText(entry.getKey(), entry.getValue());
        }

        //替换文本中的变量
        Range rangee= doc.getRange();
        int lengthh = rangee.numCharacterRuns();
//        DocumentProperties documentProperties=doc.getDocProperties();
        for(int i=0;i<lengthh;i++){
            Byte b=new Byte("0");
            rangee.getCharacterRun(i).setHighlighted(b);
//            rangee.getCharacterRun(i).getSymbolFont().
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            rangee.replaceText(entry.getKey(), entry.getValue());
        }

        //？ 1. 测试中发现只要替换的值和文件中标签长度不一致就会导致页眉页脚错乱（比如页脚跑到页眉了，或者页眉有部分跑到页脚了）
        //？ 2. 用于获取页脚的代码 doc.getFootNoteRange() 取不到内容

        OutputStream os = new FileOutputStream(outputFilePath);
        doc.write(os);
        os.close();
    }

    public static void replaceDrugDocFile(String inputFilePath,Map<String,Object> map,String outputFilePath)throws IOException {
        HWPFDocument doc = new HWPFDocument(new POIFSFileSystem(new FileInputStream(inputFilePath)));
        // getHeaderStoryRange 获取到的内容包括：页眉、页脚
//        Range range= doc.getHeaderStoryRange();
//        int length = range.numCharacterRuns();
//        for(int i=0;i<length;i++){
//            Byte b=new Byte("0");
//            range.getCharacterRun(i).setHighlighted(b);
//        }
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            range.replaceText(entry.getKey(), (String) entry.getValue());
//        }
//
//        //替换文本中的变量
//        Range rangee= doc.getRange();
//        int lengthh = rangee.numCharacterRuns();
//        for(int i=0;i<lengthh;i++){
//            Byte b=new Byte("0");
//            rangee.getCharacterRun(i).setHighlighted(b);
////            rangee.getCharacterRun(i).getSymbolFont().
//        }
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            rangee.replaceText(entry.getKey(), (String)entry.getValue());
//        }

        //？ 1. 测试中发现只要替换的值和文件中标签长度不一致就会导致页眉页脚错乱（比如页脚跑到页眉了，或者页眉有部分跑到页脚了）
        //？ 2. 用于获取页脚的代码 doc.getFootNoteRange() 取不到内容

        OutputStream os = new FileOutputStream(outputFilePath);
        doc.write(os);
        os.close();
    }
}
