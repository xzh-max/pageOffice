package com.pageOfficeServer.util;

import com.aspose.words.*;
//import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
//import org.apache.poi.xwpf.usermodel.*;
import com.aspose.words.Shape;
//import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr;

import java.awt.*;
import java.io.*;

public class WaterMarkUtil {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        loadLicense();
        //去除水印
        Document doc = null;
        try {
            doc = new Document("D:/work/wens.docx");
            removeWatermark(doc);
            doc.save("D:/work/wens1.docx");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //添加水印
        // Address是将要被转化的word文档
//        Document docc = null;
//        try {
//            docc = new Document("D:/work/sc222.docx");
//            insertWatermarkText(docc,"WENS合同");
//            docc.save("D:/work/wens.docx");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //先添加水印
        //insertWatermarkText(doc,"测试使用水印");

    }

    public static void addWM(String path,String text){
        Document doc = null;
        try {
            doc = new Document(path);
            insertWatermarkText(doc,text);
            doc.save(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeWM(String path){
        Document doc = null;
        try {
            doc = new Document(path);
            removeWatermark(doc);
            doc.save(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除全部水印
     * @param doc
     * @throws Exception
     */
    private static void removeWatermark(Document doc) throws Exception {
        for (Section sect : doc.getSections()) {
            // There could be up to three different headers in each section, since we want
            // the watermark to appear on all pages, insert into all headers.
            removeWatermarkFromHeader(sect, HeaderFooterType.HEADER_PRIMARY);
            removeWatermarkFromHeader(sect, HeaderFooterType.HEADER_FIRST);
            removeWatermarkFromHeader(sect, HeaderFooterType.HEADER_EVEN);
        }
    }

    /**
     * 移除指定Section的水印
     * @param sect
     * @param headerType
     * @throws Exception
     */
    private static void removeWatermarkFromHeader(Section sect, int headerType) throws Exception {
        HeaderFooter header = sect.getHeadersFooters().getByHeaderFooterType(headerType);
        if (header != null) {
            header.removeAllChildren();
        }
    }


    /**
     * 从Classpath（jar文件中）中读取License
     */
    private static void loadLicense() {
        //返回读取指定资源的输入流
        License license = new License();
        InputStream is = null;
        try {
            is = WaterMarkUtil.class.getClassLoader().getResourceAsStream("license.xml");
            if(is==null)
                throw new RuntimeException("Cannot find licenses file. Please contact wdmsyf@yahoo.com or visit http://sheng.javaeye.com for get more information.");
            license.setLicense(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
            if(is!=null){
                try{ is.close(); }catch(IOException ex){ };
                is = null;
            }
        }
    }

    /**
     *
     * @Title: insertWatermarkText
     * @Description: PDF生成水印
     * @author zz
     * @param doc 需要添加的pdf文档
     * @param watermarkText 水印内容
     * @throws Exception
     */
    private static void insertWatermarkText(Document doc, String watermarkText) throws Exception {

        Shape watermark = new Shape(doc, ShapeType.TEXT_PLAIN_TEXT);


        //水印内容
        watermark.getTextPath().setText(watermarkText);
        //水印字体
        watermark.getTextPath().setFontFamily("宋体");
        //水印宽度
        watermark.setWidth(500);
        //水印高度
        watermark.setHeight(200);
        //旋转水印
        watermark.setRotation(-40);
        //水印颜色
        watermark.getFill().setColor(Color.lightGray);
        watermark.setStrokeColor(Color.lightGray);

        watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
        watermark.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
        watermark.setWrapType(WrapType.NONE);
        watermark.setVerticalAlignment(VerticalAlignment.CENTER);
        watermark.setHorizontalAlignment(HorizontalAlignment.CENTER);

        Paragraph watermarkPara = new Paragraph(doc);
        watermarkPara.appendChild(watermark);

        for (Section sect : doc.getSections()){
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_PRIMARY);
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_FIRST);
            insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_EVEN);
        }
    }





    private static void insertWatermarkIntoHeader(Paragraph watermarkPara, Section sect, int headerType) throws Exception {
        HeaderFooter header = sect.getHeadersFooters().getByHeaderFooterType(headerType);

        if (header == null) {
            header = new HeaderFooter(sect.getDocument(), headerType);
            sect.getHeadersFooters().add(header);
        }

        header.appendChild(watermarkPara.deepClone(true));
    }

    }
