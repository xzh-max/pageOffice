package com.pageOfficeServer.util.wordUtil;

import com.aspose.words.*;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PDFUtil {

    public static void main(String[] args) {
//        doc2pdf("D:\\pageOffice\\target\\pageOffice\\contract\\no1.docX","D:\\pageOffice\\target\\pageOffice\\contract\\no1.pdf");
       String downUrl = "http://10.11.50.11:80/pageOffice/download.do?fileName=WS-2019-XDD0-YZ-000118.docx&type=contract";

        String fileName = downUrl.substring(downUrl.indexOf("fileName=") + 9, downUrl.indexOf("&type"));

        System.out.print(fileName);

        String str="";
        System.out.print(StringUtils.isEmpty(str));
    }

    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = PDFUtil.class.getClassLoader().getResourceAsStream("license.xml"); // license.xml应放在..\WebRoot\WEB-INF\classes路径下
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void doc2pdf(String inPath, String outPath) {
        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return;
        }
        try {
            long old = System.currentTimeMillis();
            File file = new File(outPath); // 新建一个空白pdf文档
            FileOutputStream os = new FileOutputStream(file);
            Document doc = new Document(inPath); // Address是将要被转化的word文档
            String osName = System.getProperty("os.name");
            if(!osName.toLowerCase().startsWith("win")){
                FontSettings.getDefaultInstance().setFontsFolder(File.separator + "home"
                        + File.separator + "fonts" + File.separator + "windows-fonts" +File.separator, true);
            }
            doc.removeMacros();
            doc.stopTrackRevisions();
            doc.setTrackRevisions(false);
            RevisionCollection collection = doc.getRevisions();
            doc.save(os, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF,
            // EPUB, XPS, SWF 相互转换
            long now = System.currentTimeMillis();
            os.close();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
