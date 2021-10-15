package com.pageOfficeServer.util.wordUtil;


import com.pageOfficeServer.util.tableProcessUtil.PoiWordUtils;
import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DocxUtil{

    /**
     * @Description: 将t中的字段转换成替换模板需要的数据${字段}-->字段值
     *      在word模板中变量为${valuename},为每个值建一个以‘${valuename}’为键，‘value’为值的Map集合，
     *      利用键去和Word模板中寻找相等的变量
     */
    public <T> Map<String, String> copyParamFromBean(T t, Map<String, String> params) {
        Field[] fields = t.getClass().getDeclaredFields();
        String key;
        String value;
        for (Field field : fields) {
            String fieldName = field.getName();
            key = "${" + fieldName + "}";
            String name = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                value = String.valueOf(t.getClass().getMethod(name).invoke(t));
                params.put(key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return params;
    }
    /***
     *替换Word模板中的对应变量。
     *两种情况：1-段落中的变量。 2-表格中的变量
     */
    public static void searchAndReplace(XWPFDocument document, Map<String, String> map) {
        try {
            // 替换段落中的指定文字
            Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();//获得word中段落
//            com((List)itPara);
            while (itPara.hasNext()) {       //遍历段落
                XWPFParagraph paragraph = (XWPFParagraph) itPara.next();
                List<XWPFRun> run=paragraph.getRuns();
                for(int i=0;i<run.size();i++)
                {
                    if(run.get(i).getCTR().getRPr().getShd()!=null){
                        run.get(i).getCTR().getRPr().getShd().setColor("auto");
                        run.get(i).getCTR().getRPr().getShd().setFill("auto");
                    }
                    run.get(i).setTextHighlightColor("none");

                    if(run!=null&run.get(i)!=null&&run.get(i).getText(run.get(i).getTextPosition())!=null&&run.get(i).getText(run.get(i).getTextPosition()).trim()!=null)
                    {
                        String text0 = run.get(i).getText(run.get(i).getTextPosition());
                        if(text0!=null && text0.trim().startsWith("{")){
                            //记录分隔符中间跨越的runs数量，用于字符串拼接和替换
                            int num=0;
                            int j = i;
                            for(; j < run.size(); j++){
                                String text1 = run.get(j).getText(run.get(j).getTextPosition());
                                if(text1!=null && text1.trim().endsWith("}")){
                                    num=j-i;
                                    break;
                                }
                            }
                            if(num!=0) {
                                //num!=0说明找到了[]配对，需要替换
                                StringBuilder newText = new StringBuilder();
                                for (int s = i; s <= i+num; s++) {
                                    String text2 = run.get(s).getText(run.get(s).getTextPosition());
                                    newText.append(text2);
                                    run.get(s).setText(null, 0);
                                }
                                run.get(i).setText(newText.toString(),0);
                                //重新定义遍历位置，跳过设置为null的位置
                                i=j+1;
                            }
                        }
                    }
                }
            }


            Iterator<XWPFParagraph> itParaa = document.getParagraphsIterator();//获得word中段落
            while (itParaa.hasNext()) {       //遍历段落
                XWPFParagraph paragraph = (XWPFParagraph) itParaa.next();
                Set<String> set = map.keySet();
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    List<XWPFRun> run=paragraph.getRuns();
                    for(int i=0;i<run.size();i++)
                    {
//                        System.out.print("第一层判断---"+run.get(i).getText(run.get(i).getTextPosition()));
//                        System.out.print("第一层判断---"+run.get(i).getText(run.get(i).getTextPosition()));
//                        System.out.print("第一层判断---"+run.get(i).getText(run.get(i).getTextPosition()).trim());
                        if(run!=null&run.get(i)!=null&&run.get(i).getText(run.get(i).getTextPosition())!=null&&run.get(i).getText(run.get(i).getTextPosition()).trim()!=null)
                        {
                            if(run.get(i).getText(run.get(i).getTextPosition()).trim().equals(key.trim())){
                                /**
                                 * 参数0表示生成的文字是要从哪一个地方开始放置,设置文字从位置0开始
                                 * 就可以把原变量替换掉
                                 */
                                run.get(i).setText(map.get(key),0);
                                setTypeface(run.get(i),run.get(i));
                            }
                        }
                    }
                }
            }

            // 替换页眉中的指定文字
            List<XWPFHeader> headerListt = document.getHeaderList();
            for(int k=0;k<headerListt.size();k++){
                List<XWPFParagraph> headerParas = headerListt.get(k).getParagraphs();
                com(headerParas);
            }

            // 替换页眉中的指定文字
            List<XWPFHeader> headerList = document.getHeaderList();
            for(int k=0;k<headerList.size();k++){
                List<XWPFParagraph> headerPara = headerList.get(k).getParagraphs();
                for(XWPFParagraph xwpfParagraph:headerPara) {       //遍历段落
                    XWPFParagraph paragraph =  xwpfParagraph;
                    Set<String> set = map.keySet();
                    Iterator<String> iterator = set.iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        List<XWPFRun> run=paragraph.getRuns();
                        for(int i=0;i<run.size();i++)
                        {
                            if(run.get(i).getText(run.get(i).getTextPosition())!=null&&run.get(i).getText(run.get(i).getTextPosition()).trim()!=null)

                            {
                                if(run.get(i).getText(run.get(i).getTextPosition()).trim().equals(key.trim())){
                                    /**
                                    * 参数0表示生成的文字是要从哪一个地方开始放置,设置文字从位置0开始
                                    * 就可以把原变量替换掉
                                    */
                                    run.get(i).setText(map.get(key),0);
                                    setTypeface(run.get(i),run.get(i));
                                }

                                if(run.get(i).getText(run.get(i).getTextPosition()).trim().contains(key.trim())){
                                    run.get(i).setText(run.get(i).getText(run.get(i).getTextPosition()).replace(key.trim(),(String) map.get(key)),0);
                                }
                            }
                        }
                    }
                }
            }

            // 替换表格中的指定文字
            Iterator<XWPFTable> itTable = document.getTablesIterator();//获得Word的表格
            while (itTable.hasNext()) { //遍历表格
                XWPFTable table = (XWPFTable) itTable.next();
                List<XWPFTableRow> rows = table.getRows();
                //遍历表格,并替换模板
                eachTable(rows, map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 遍历表格
     * @param rows 表格行对象
     * @param textMap 需要替换的信息集合
     */
    public static void eachTable(List<XWPFTableRow> rows ,Map<String, String> textMap){
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                //判断单元格是否需要替换
//                if(checkText(cell.getText())){
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    com(paragraphs);
                    for (XWPFParagraph paragraph : paragraphs) {
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) {
                            run.setTextHighlightColor("none");
                            if(!"null".equals(run.text())){
                                run.setText(changeValue(run.toString(), textMap),0);
                                setTypeface(run,run);
                            }
                        }
                    }
//                }
            }
        }
    }


    public static void com(List<XWPFParagraph> paragraphs){
        for(XWPFParagraph headerPara:paragraphs) {       //遍历段落
            List<XWPFRun> run=headerPara.getRuns();
            for(int i=0;i<run.size();i++)
            {
                if(run!=null&run.get(i)!=null&&run.get(i).getText(run.get(i).getTextPosition())!=null&&run.get(i).getText(run.get(i).getTextPosition()).trim()!=null)
                {

                    String text0 = run.get(i).getText(run.get(i).getTextPosition());
                    if(text0!=null && text0.trim().startsWith("{")){
                        //记录分隔符中间跨越的runs数量，用于字符串拼接和替换
                        int num=0;
                        int j = i;
                        for(; j < run.size(); j++){
                            String text1 = run.get(j).getText(run.get(j).getTextPosition());
                            if(text1!=null && text1.trim().endsWith("}")){
                                num=j-i;
                                break;
                            }
                        }
                        if(num!=0) {
                            //num!=0说明找到了[]配对，需要替换
                            StringBuilder newText = new StringBuilder();
                            for (int s = i; s <= i+num; s++) {
                                String text2 = run.get(s).getText(run.get(s).getTextPosition());
                                newText.append(text2);
                                run.get(s).setText(null, 0);
                            }
                            run.get(i).setText(newText.toString(),0);
                            //重新定义遍历位置，跳过设置为null的位置
                            i=j+1;
                        }
                    }
                }
            }
        }
    }

    /**
     * 匹配传入信息集合与模板
     * @param value 模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static String changeValue(String value, Map<String, String> textMap){
        Set<Map.Entry<String, String>> textSets = textMap.entrySet();
        for (Map.Entry<String, String> textSet : textSets) {
            //匹配模板与替换值 格式${key}
            if(value.indexOf(textSet.getKey())!= -1){
                value = textSet.getValue();
            }
        }
        //模板未匹配到区域替换为空
//        if(checkText(value)){
//            value = "";
//        }
        return value;
    }

    /**
     * 判断文本中时候包含$
     * @param text 文本
     * @return 包含返回true,不包含返回false
     */
    public static boolean checkText(String text){
        boolean check  =  false;
        if(text.indexOf("{")!= -1){
            check = true;
        }
        return check;
    }

    /**
     * 关闭输出流
     *
     * @param os
     */
    public void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制字体
     */
    private static void setTypeface(XWPFRun sourceRun, XWPFRun targetRun) {
        String fontFamily = sourceRun.getFontFamily();
        int fontSize = sourceRun.getFontSize();
        boolean bold = sourceRun.isBold();
        boolean italic = sourceRun.isItalic();
        int kerning = sourceRun.getKerning();
        UnderlinePatterns underline = sourceRun.getUnderline();

        targetRun.setFontFamily(fontFamily);
        targetRun.setBold(bold);
        targetRun.setTextHighlightColor("none");
        targetRun.setItalic(italic);
        targetRun.setKerning(kerning);
        targetRun.setUnderline(underline);
        targetRun.setFontSize(fontSize == -1 ? 10 : fontSize);
    }
}
