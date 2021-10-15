package com.pageOfficeServer.util.wordUtil;

import com.alibaba.fastjson.JSONArray;

public class PoiUtils {


//        public static void main(String[] args) throws Exception {
//
//            Map<String, String> tags = new HashMap<String, String>() {{
//                put("header", "page header");
//                put("footer", "page footer");
//            }};
//
//            HWPFDocument doc = new HWPFDocument(new POIFSFileSystem(new FileInputStream("e:\\test1.doc")));
//            // getHeaderStoryRange 获取到的内容包括：页眉、页脚
//            Range range= doc.getHeaderStoryRange();
//            for (Map.Entry<String, String> entry : tags.entrySet()) {
//                range.replaceText("{" + entry.getKey() + "}", entry.getValue());
//            }
//            //？ 1. 测试中发现只要替换的值和文件中标签长度不一致就会导致页眉页脚错乱（比如页脚跑到页眉了，或者页眉有部分跑到页脚了）
//            //？ 2. 用于获取页脚的代码 doc.getFootNoteRange() 取不到内容
//
//            OutputStream os = new FileOutputStream("e:\\write.doc");
//            doc.write(os);
//            os.close();
//        }

//    public static void main(String[] args) throws IOException {
//        File is = new File("D:\\pageOffice\\target\\pageOffice\\template\\no1.docX");//文件路径
//        FileInputStream fis = new FileInputStream(is);
//        XWPFDocument docx = new XWPFDocument(fis);
//        List<XWPFHeader> headerList = docx.getHeaderList();
//        for(int i=0;i<headerList.size();i++){
//            List<XWPFParagraph> headerPara = headerList.get(i).getParagraphs();
//            for (int j = 0; j < headerPara.size(); j++) {
//                headerPara.get(j).setStyle("page-header"); //前提是styles.xml 中有styleId为 page-header
//                List<XWPFRun> run=headerPara.get(j).getRuns();
//                for(int n=0;n<run.size();n++)
//                {
//                    System.out.print(run.get(n).getText(run.get(n).getTextPosition()));
//                }
//            }
//        }


//        for (XWPFHeader xwpfHeader: headerList){
//            xwpfHeader.;
//            System.out.println(xwpfHeader.getText());//页眉
//        }

//    }

    public static void main(String[] args){
        JSONArray jsonArray=new JSONArray();
        jsonArray.add("1");
        jsonArray.add("2");
    }
}
