package com.pageOfficeServer.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.HeaderFooter;
import com.aspose.words.HeaderFooterType;
import com.aspose.words.Section;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.pdf.PdfCopy;
//import com.itextpdf.text.pdf.PdfImportedPage;
//import com.itextpdf.text.pdf.PdfReader;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args){
////        String outpath="D:/dest.docx";
//        List<String> paths=new ArrayList<>();
//        paths.add("D:/1.docx");
//        paths.add("D:/2.docx");
//        paths.add("D:/3.docx");
//        paths.add("D:/4.docx");
//        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("A",paths);
//        System.out.println(jsonObject.toJSONString());
////        merger(paths,outpath);
        docToDocx("D:/5.doc","D:/x.docx");
    }

    /**
     * 合并多个word
     * @param paths 多个word路径
     * @param outputPath 合并之后得路径
     */
    private static void merger(List<String> paths,String outputPath){
        InputStream in1 = null;
        InputStream in2 = null;
        OPCPackage src1Package = null;
        OPCPackage src2Package = null;

        OutputStream dest = null;
        try {
            dest = new FileOutputStream(outputPath);
            XWPFDocument douc1=null;
            CTBody src1=null;
            XWPFParagraph p1=null;
            XWPFParagraph p2=null;

            for(String path:paths){
                if(paths.indexOf(path)==0){
                    in1=new FileInputStream(path);
                    src1Package = OPCPackage.open(in1);
                    douc1 = new XWPFDocument(src1Package);
                    src1 = douc1.getDocument().getBody();
                    p1 = douc1.createParagraph();
                    p1.setPageBreak(true);
                }else {
                    in2=new FileInputStream(path);
                    src2Package=OPCPackage.open(in2);
                    XWPFDocument douc2 = new XWPFDocument(src2Package);
                    CTBody src2 = douc2.getDocument().getBody();
                    p2 = douc2.createParagraph();
                    p2.setPageBreak(true);
                    appendBody(src1, src2);
                    if(paths.indexOf(path)==paths.size()-1){
                        douc1.write(dest);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private static void appendBody(CTBody src, CTBody append) throws Exception {
        XmlOptions optionsOuter = new XmlOptions();
        optionsOuter.setSaveOuter();
        String appendString = append.xmlText(optionsOuter);
        String srcString = src.xmlText();
        String prefix = srcString.substring(0,srcString.indexOf(">")+1);
        String mainPart = srcString.substring(srcString.indexOf(">")+1,srcString.lastIndexOf("<"));
        String sufix = srcString.substring( srcString.lastIndexOf("<") );
        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
        CTBody makeBody = CTBody.Factory.parse(prefix+mainPart+addPart+sufix);
        src.set(makeBody);
    }

    /**
     * doc转docx
     * @param input
     * @param output
     */
    public static void docToDocx(String input,String output){
        try {
            Document doc = new Document(input);
            doc.save(output);
            Document out=new Document(output);
            out.save(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    /**
//     * 移除全部水印
//     * @param doc
//     * @throws Exception
//     */
//    private static void removeWatermark(Document doc) throws Exception {
//        for (Section sect : doc.getSections()) {
//            // There could be up to three different headers in each section, since we want
//            // the watermark to appear on all pages, insert into all headers.
//            removeWatermarkFromHeader(sect, HeaderFooterType.HEADER_PRIMARY);
//            removeWatermarkFromHeader(sect, HeaderFooterType.HEADER_FIRST);
//            removeWatermarkFromHeader(sect, HeaderFooterType.HEADER_EVEN);
//        }
//    }
//
//    /**
//     * 移除指定Section的水印
//     * @param sect
//     * @param headerType
//     * @throws Exception
//     */
//    private static void removeWatermarkFromHeader(Section sect, int headerType) throws Exception {
//        HeaderFooter header = sect.getHeadersFooters().getByHeaderFooterType(headerType);
//        if (header != null) {
//            header.removeAllChildren();
//        }
//    }


//    public static void main(String[] args) throws Exception {
//      List<Integer> dataList = new ArrayList<Integer>();
//      for(int i=0;i<20000;i++)
//      dataList.add(i);
//
//      //分批处理
//      if(null!=dataList&&dataList.size()>0){
//        int pointsDataLimit = 500;//限制条数
//        Integer size = dataList.size();
//        //判断是否有必要分批
//        if(pointsDataLimit<size){
//          int part = size/pointsDataLimit;//分批数
//          System.out.println("共有 ： "+size+"条，！"+" 分为 ："+part+"批");
//          //
//          for (int i = 0; i < part; i++) {
//            //1000条
//            List<Integer> listPage = dataList.subList(0, pointsDataLimit);
//            System.out.println(listPage);
//            //剔除
//            dataList.subList(0, pointsDataLimit).clear();
//          }
//
//          if(!dataList.isEmpty()){
//            System.out.println(dataList);//表示最后剩下的数据
//          }
//        }else{
//          System.out.println(dataList);
//        }
//      }

//        Document pdfdoc1 = new Document("input.pdf");
//        Document pdfdoc2 = new Document("input.pdf");

//        ArrayList list=new ArrayList();
//        getAllFileName("D:/using",list);
//        String[] strings = new String[list.size()];
//        list.toArray(strings);
//        mergePdfFiles(strings,"D:/files.pdf");
//    }

//    /*
//     * 合并pdf文件
//     * @param files 要合并文件数组(绝对路径如{ "e:\\1.pdf", "e:\\2.pdf" ,
//     * "e:\\3.pdf"}),合并的顺序按照数组中的先后顺序，如2.pdf合并在1.pdf后。
//     * @param newfile 合并后新产生的文件绝对路径，如 e:\\temp\\tempNew.pdf,
//     * @return boolean 合并成功返回true；否则，返回false
//     *
//     */
//
//    public static boolean mergePdfFiles(String[] files, String newfile) {
//        boolean retValue = false;
//        Document document = null;
//        try {
//            document = new Document(new PdfReader(files[0]).getPageSize(1));
//            PdfCopy copy = new PdfCopy(document, new FileOutputStream(newfile));
//            document.open();
//            for (int i = 0; i < files.length; i++) {
//                PdfReader reader = new PdfReader(files[i]);
//                int n = reader.getNumberOfPages();
//                for (int j = 1; j <= n; j++) {
//                    document.newPage();
//                    PdfImportedPage page = copy.getImportedPage(reader, j);
//                    copy.addPage(page);
//                }
//            }
//            retValue = true;
//        } catch (Exception e) {
//            System.out.println(e);
//        } finally {
//            System.out.println("执行结束");
//            document.close();
//        }
//        return retValue;
//    }


    /**
     * 获取某个文件夹下的所有文件
     *
     * @param fileNameList 存放文件名称的list
     * @param path 文件夹的路径
     * @return
     */
    public static void getAllFileName(String path,ArrayList<String> fileNameList) {
        //ArrayList<String> files = new ArrayList<String>();
        boolean flag = false;
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
//              System.out.println("文     件：" + tempList[i]);
                //fileNameList.add(tempList[i].toString());
                fileNameList.add(tempList[i].getPath());
            }
            if (tempList[i].isDirectory()) {
//              System.out.println("文件夹：" + tempList[i]);
                getAllFileName(tempList[i].getAbsolutePath(),fileNameList);
            }
        }
        return;
    }
}
