package com.pageOfficeServer.util.tableProcessUtil;

import com.pageOfficeServer.util.wordUtil.DocxUtil;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Create by IntelliJ Idea 2018.2
 *
 * @author: qyp
 * Date: 2019-10-25 14:48
 */
public class DynWordUtils {

    private final Logger logger = LoggerFactory.getLogger(DynWordUtils.class);

    /**
     * 被list替换的段落 被替换的都是oldParagraph
     */
    private XWPFParagraph oldParagraph;

    /**
     * 参数
     */
    private Map<String, Object> paramMap;

    /**
     * 当前元素的位置
     */
    int n = 0;

    /**
     * 判断当前是否是遍历的表格
     */
    boolean isTable = false;

    /**
     * 模板对象
     */
    XWPFDocument templateDoc;

    /**
     * 默认字体的大小
     */
    final int DEFAULT_FONT_SIZE = 10;

    /**
     * 重复模式的占位符所在的行索引
     */
    private int currentRowIndex;

    /**
     * 入口
     *
     * @param paramMap     模板中使用的参数
     * @param templatePaht 模板全路径
     * @param outPath      生成的文件存放的本地全路径
     */
    public static void process(Map<String, Object> paramMap, String templatePaht, String outPath) {
        DynWordUtils dynWordUtils = new DynWordUtils();
        dynWordUtils.setParamMap(paramMap);
        dynWordUtils.createWord(templatePaht, outPath);
    }

    /**
     * 生成动态的word
     * @param templatePath
     * @param outPath
     */
    public void createWord(String templatePath, String outPath) {
        try (FileOutputStream outStream = new FileOutputStream(outPath)) {
            InputStream inputStream = new FileInputStream(ResourceUtils.getFile(templatePath));
            templateDoc = new XWPFDocument(OPCPackage.open(inputStream));
            parseTemplateWord();
            templateDoc.write(outStream);
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceElement[] stackTrace = e.getStackTrace();

            String className = stackTrace[0].getClassName();
            String methodName = stackTrace[0].getMethodName();
            int lineNumber = stackTrace[0].getLineNumber();

            logger.error("错误：第:{}行, 类名:{}, 方法名:{}", lineNumber, className, methodName);
//            throw new RuntimeException(e.getCause().getMessage());
        }
    }

    /**
     * 解析word模板
     */
    public void parseTemplateWord() throws Exception {

        List<IBodyElement> elements = templateDoc.getBodyElements();

        for (; n < elements.size(); n++) {
            IBodyElement element = elements.get(n);
            // 普通段落
            if (element instanceof XWPFParagraph) {

                XWPFParagraph paragraph = (XWPFParagraph) element;
                oldParagraph = paragraph;
                if (paragraph.getParagraphText().isEmpty()) {
                    continue;
                }

                delParagraph(paragraph);

            } else if (element instanceof XWPFTable) {
                // 表格
                isTable = true;
                XWPFTable table = (XWPFTable) element;

                delTable(table, paramMap);
                isTable = false;
            }
        }

        // 替换页眉中的指定文字
        List<XWPFHeader> headerListt = templateDoc.getHeaderList();
        for(int k=0;k<headerListt.size();k++){
            List<XWPFParagraph> headerParas = headerListt.get(k).getParagraphs();
            DocxUtil.com(headerParas);
        }

        // 替换页眉中的指定文字
        List<XWPFHeader> headerList = templateDoc.getHeaderList();
        for(int k=0;k<headerList.size();k++){
            List<XWPFParagraph> headerPara = headerList.get(k).getParagraphs();
            for(XWPFParagraph xwpfParagraph:headerPara) {       //遍历段落
                XWPFParagraph paragraph =  xwpfParagraph;
                Set<String> set = paramMap.keySet();
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    List<XWPFRun> run=paragraph.getRuns();
                    for(int i=0;i<run.size();i++)
                    {
                        if(run.get(i).getText(run.get(i).getTextPosition())!=null&&run.get(i).getText(run.get(i).getTextPosition()).trim()!=null)

                        {
                            run.get(i).setTextHighlightColor("none");
                            if(run.get(i).getText(run.get(i).getTextPosition()).trim().equals(key.trim())){
                                /**
                                 * 参数0表示生成的文字是要从哪一个地方开始放置,设置文字从位置0开始
                                 * 就可以把原变量替换掉
                                 */
                                run.get(i).setText((String) paramMap.get(key),0);
                                run.get(i).setTextHighlightColor("none");
                            }

                            if(run.get(i).getText(run.get(i).getTextPosition()).trim().contains(key.trim())){
                                run.get(i).setText(run.get(i).getText(run.get(i).getTextPosition()).replace(key.trim(),(String) paramMap.get(key)),0);
                                run.get(i).setTextHighlightColor("none");
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * 处理段落
     */
    private void delParagraph(XWPFParagraph paragraph) throws Exception {
        List<XWPFRun> runs = oldParagraph.getRuns();
        StringBuilder sb = new StringBuilder();
        for (XWPFRun run : runs) {
            if(run.getCTR()!=null&&run.getCTR().getRPr()!=null&&run.getCTR().getRPr().getShd()!=null){
                run.getCTR().getRPr().getShd().setColor("auto");
                run.getCTR().getRPr().getShd().setFill("auto");
            }

//            if(run.getUnderline()!=null){
                run.setUnderline(run.getUnderline());
//            }

            run.setTextHighlightColor("none");

            String text = run.getText(0);
            if (text == null) {
                continue;
            }
            sb.append(text);
            run.setText("", 0);
        }
        Placeholder(paragraph, runs, sb);
    }


    /**
     * 匹配传入信息集合与模板
     *
     * @param placeholder 模板需要替换的区域()
     * @param paramMap    传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public void changeValue(XWPFRun currRun, String placeholder, Map<String, Object> paramMap) throws Exception {

        String placeholderValue = placeholder;
        if (paramMap == null || paramMap.isEmpty()) {
            return;
        }

        Set<Map.Entry<String, Object>> textSets = paramMap.entrySet();
        for (Map.Entry<String, Object> textSet : textSets) {
            //匹配模板与替换值 格式${key}
            String mapKey = textSet.getKey();
            String docKey = PoiWordUtils.getDocKey(mapKey);

            if (placeholderValue.indexOf(mapKey) != -1) {
                Object obj = textSet.getValue();
                // 需要添加一个list
                if (obj instanceof List) {
                    placeholderValue = delDynList(placeholder, (List) obj);
                } else {
//                    placeholderValue = placeholderValue.replaceAll(
//                            PoiWordUtils.getPlaceholderReg(mapKey)
//                            , String.valueOf(obj));
                    placeholderValue = placeholderValue.replace(
                            mapKey
                            , String.valueOf(obj));
                }
            }
        }

        currRun.setText(placeholderValue, 0);
    }

    /**
     * 处理的动态的段落（参数为list）
     *
     * @param placeholder 段落占位符
     * @param obj
     * @return
     */
    private String delDynList(String placeholder, List obj) {
        String placeholderValue = placeholder;
        List dataList = obj;
        Collections.reverse(dataList);
        for (int i = 0, size = dataList.size(); i < size; i++) {
            Object text = dataList.get(i);
            // 占位符的那行, 不用重新创建新的行
            if (i == 0) {
                placeholderValue = String.valueOf(text);
            } else {
                XWPFParagraph paragraph = createParagraph(String.valueOf(text));
                if (paragraph != null) {
                    oldParagraph = paragraph;
                }
                // 增加段落后doc文档的element的size会随着增加（在当前行的上面添加
                // 这里减操作是回退并解析新增的行（因为可能新增的带有占位符，这里为了支持图片和表格）
                if (!isTable) {
                    n--;
                }
            }
        }
        return placeholderValue;
    }

    /**
     * 创建段落 <p></p>
     *
     * @param texts
     */
    public XWPFParagraph createParagraph(String... texts) {

        // 使用游标创建一个新行
        XmlCursor cursor = oldParagraph.getCTP().newCursor();
        XWPFParagraph newPar = templateDoc.insertNewParagraph(cursor);
        // 设置段落样式
        newPar.getCTP().setPPr(oldParagraph.getCTP().getPPr());

        copyParagraph(oldParagraph, newPar, texts);

        return newPar;
    }

    /**
     * 处理表格（遍历）
     *
     * @param table    表格
     * @param paramMap 需要替换的信息集合
     */
    public void delTable(XWPFTable table, Map<String, Object> paramMap) throws Exception {
        List<XWPFTableRow> rows = table.getRows();
        for (int i = 0, size = rows.size(); i < size; i++) {
            XWPFTableRow row = rows.get(i);
            currentRowIndex = i;
            // 如果是动态添加行 直接处理后终止
            delAndJudgeRow(table, paramMap, row);
        }

        List<XWPFTableRow> rowss= table.getRows();
        for (int i = 0, size = rowss.size(); i < size; i++) {
            XWPFTableRow row = rowss.get(i);
            // 如果是动态添加行 直接处理后终止
            proCurrentRow(table, paramMap, row);
        }

    }

    /**
     * 判断并且是否是动态行，并且处理表格占位符
     * @param table 表格对象
     * @param paramMap 参数map
     * @param row 当前行
     * @return
     * @throws Exception
     */
    private boolean delAndJudgeRow(XWPFTable table, Map<String, Object> paramMap, XWPFTableRow row) throws Exception {
        // 当前行是动态行标志
        if (PoiWordUtils.isAddRow(row)) {
            List<XWPFTableRow> xwpfTableRows = addAndGetRows(table, row, paramMap);
            // 回溯添加的行，这里是试图处理动态添加的图片
//            for (XWPFTableRow tbRow : xwpfTableRows) {
//                delAndJudgeRow(table, paramMap, tbRow);
//            }
            return true;
        }

        // 如果是重复添加的行
        if (PoiWordUtils.isAddRowRepeat(row)) {
            List<XWPFTableRow> xwpfTableRows = addAndGetRepeatRows(table, row, paramMap);
            /*// 回溯添加的行，这里是试图处理动态添加的图片
            for (XWPFTableRow tbRow : xwpfTableRows) {
                delAndJudgeRow(table, paramMap, tbRow);
            }*/
            return true;
        }
        // 当前行非动态行标签
        List<XWPFTableCell> cells = row.getTableCells();
        for (XWPFTableCell cell : cells) {

            //判断单元格是否需要替换
            if (PoiWordUtils.checkText(cell.getText())) {
                List<XWPFParagraph> paragraphs = cell.getParagraphs();
                for (XWPFParagraph paragraph : paragraphs) {
                    List<XWPFRun> runs = paragraph.getRuns();
                    StringBuilder sb = new StringBuilder();
                    for (XWPFRun run : runs) {
                        if(run.getCTR()!=null&&run.getCTR().getRPr()!=null&&run.getCTR().getRPr().getShd()!=null){
                            run.getCTR().getRPr().getShd().setColor("auto");
                            run.getCTR().getRPr().getShd().setFill("auto");
                        }
                        sb.append(run.toString());
                        run.setText("", 0);
                        run.setTextHighlightColor("none");
                    }
                    Placeholder(paragraph, runs, sb);
                }
            }
        }
        for (XWPFTableCell cell : cells) {
            List<XWPFParagraph> paragraphs = cell.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    if(run.getCTR()!=null&&run.getCTR().getRPr()!=null&&run.getCTR().getRPr().getShd()!=null){
                        run.getCTR().getRPr().getShd().setColor("auto");
                        run.getCTR().getRPr().getShd().setFill("auto");
                        run.setTextHighlightColor("none");
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断并且是否是动态行，并且处理表格占位符
     * @param table 表格对象
     * @param paramMap 参数map
     * @param row 当前行
     * @return
     * @throws Exception
     */
    private boolean proCurrentRow(XWPFTable table, Map<String, Object> paramMap, XWPFTableRow row) throws Exception {
        // 当前行非动态行标签
        List<XWPFTableCell> cells = row.getTableCells();
        for (XWPFTableCell cell : cells) {
            //判断单元格是否需要替换
            if (PoiWordUtils.checkText(cell.getText())) {
                List<XWPFParagraph> paragraphs = cell.getParagraphs();
                for (XWPFParagraph paragraph : paragraphs) {
                    List<XWPFRun> runs = paragraph.getRuns();
                    StringBuilder sb = new StringBuilder();
                    for (XWPFRun run : runs) {
                        sb.append(run.toString());
                        run.setText("", 0);
                        run.setTextHighlightColor("none");
                    }
                    Placeholder(paragraph, runs, sb);
                }
            }
        }
        return false;
    }


    /**
     * 处理占位符
     * @param runs 当前段的runs
     * @param sb 当前段的内容
     * @throws Exception
     */
    private void Placeholder(XWPFParagraph currentPar, List<XWPFRun> runs, StringBuilder sb) throws Exception {
        if (runs.size() > 0) {
            String text = sb.toString();
            XWPFRun currRun = runs.get(0);
            if (PoiWordUtils.isPicture(text)) {
                // 该段落是图片占位符
                int indentationFirstLine = currentPar.getIndentationFirstLine();
                // 清除段落的格式，否则图片的缩进有问题
                currentPar.getCTP().setPPr(null);
                //设置缩进
                currentPar.setIndentationFirstLine(indentationFirstLine);
//                addPicture(currRun, imageEntity);
            } else {
//                oldParagraph = currentPar;
                changeValue(currRun, text, paramMap);
            }
        }
    }

//    /**
//     * 添加图片
//     * @param currRun 当前run
//     * @param imageEntity 图片对象
//     * @throws InvalidFormatException
//     * @throws FileNotFoundException
//     */
//    private void addPicture(XWPFRun currRun, ImageEntity imageEntity) throws InvalidFormatException, FileNotFoundException {
//        Integer typeId = imageEntity.getTypeId().getTypeId();
//        String picId = currRun.getDocument().addPictureData(new FileInputStream(imageEntity.getUrl()), typeId);
//        ImageUtils.createPicture(currRun, picId, templateDoc.getNextPicNameNumber(typeId),
//                imageEntity.getWidth(), imageEntity.getHeight());
//    }

    /**
     * 添加行  标签行不是新创建的
     *
     * @param table
     * @param flagRow  flagRow 表有标签的行
     * @param paramMap 参数
     */
    private List<XWPFTableRow> addAndGetRows(XWPFTable table, XWPFTableRow flagRow, Map<String, Object> paramMap) throws Exception {
        List<XWPFTableCell> flagRowCells = flagRow.getTableCells();
        XWPFTableCell flagCell = flagRowCells.get(0);

        String text = flagCell.getText();
        List<List<String>> dataList = (List<List<String>>) PoiWordUtils.getValueByPlaceholder(paramMap, text);

        // 新添加的行
        List<XWPFTableRow> newRows = new ArrayList<>(dataList.size());
        if (dataList == null || dataList.size() <= 0) {
            return newRows;
        }

        XWPFTableRow currentRow = flagRow;
        int cellSize = flagRow.getTableCells().size();
        for (int i = 0, size = dataList.size(); i < size; i++) {
            if (i != 0) {
//                currentRow = table.createRow();
                currentRow = table.insertNewTableRow(currentRowIndex+i);
                // 复制样式
                if (flagRow.getCtRow() != null) {
                    currentRow.getCtRow().setTrPr(flagRow.getCtRow().getTrPr());
                }
            }
            addRow(flagCell, currentRow, cellSize, dataList.get(i));
            newRows.add(currentRow);
        }
        return newRows;
    }

    /**
     * 添加重复多行 动态行  每一行都是新创建的
     * @param table
     * @param flagRow
     * @param paramMap
     * @return
     * @throws Exception
     */
    private List<XWPFTableRow> addAndGetRepeatRows(XWPFTable table, XWPFTableRow flagRow, Map<String, Object> paramMap) throws Exception {
        List<XWPFTableCell> flagRowCells = flagRow.getTableCells();
        XWPFTableCell flagCell = flagRowCells.get(0);
        String text = flagCell.getText();
        List<List<String>> dataList = (List<List<String>>) PoiWordUtils.getValueByPlaceholder(paramMap, text);
        String tbRepeatMatrix = PoiWordUtils.getTbRepeatMatrix(text);
        Assert.notNull(tbRepeatMatrix, "模板矩阵不能为空");

        // 新添加的行
        List<XWPFTableRow> newRows = new ArrayList<>(dataList.size());
        if (dataList == null || dataList.size() <= 0) {
            return newRows;
        }

        String[] split = tbRepeatMatrix.split(PoiWordUtils.tbRepeatMatrixSeparator);
        int startRow = Integer.parseInt(split[0]);
        int endRow = Integer.parseInt(split[1]);
        int startCell = Integer.parseInt(split[2]);
        int endCell = Integer.parseInt(split[3]);

        XWPFTableRow currentRow;
        for (int i = 0, size = dataList.size(); i < size; i++) {
            int flagRowIndex = i % (endRow - startRow + 1);
            XWPFTableRow repeatFlagRow = table.getRow(flagRowIndex);
            // 清除占位符那行
            if (i == 0) {
                table.removeRow(currentRowIndex);
            }
            currentRow = table.createRow();
            // 复制样式
            if (repeatFlagRow.getCtRow() != null) {
                currentRow.getCtRow().setTrPr(repeatFlagRow.getCtRow().getTrPr());
            }
            addRowRepeat(startCell, endCell, currentRow, repeatFlagRow, dataList.get(i));

            // 处理新生成的那行是否有占位符需要处理
            delAndJudgeRow(table, paramMap, currentRow);

            newRows.add(currentRow);
        }
        deleteTemplateRow(startRow, endRow, table);
        return newRows;
    }

    /**
     * 删除模板行
     */
    private void deleteTemplateRow(int startRowIdx, int endRowIdx, XWPFTable table) {
        for (;startRowIdx <= endRowIdx; startRowIdx++) {
            table.removeRow(0);
        }
    }

    /**
     * 根据模板cell添加新行
     *
     * @param flagCell    模板列(标记占位符的那个cell)
     * @param row         新增的行
     * @param cellSize    每行的列数量（用来补列补足的情况）
     * @param rowDataList 每行的数据
     */
    private void addRow(XWPFTableCell flagCell, XWPFTableRow row, int cellSize, List<String> rowDataList) {
        for (int i = 0; i < cellSize; i++) {
            XWPFTableCell cell = row.getCell(i);
            cell = cell == null ? row.createCell() : row.getCell(i);
            if (i < rowDataList.size()) {
                PoiWordUtils.copyCellAndSetValue(flagCell, cell, rowDataList.get(i));
            } else {
                // 数据不满整行时，添加空列
                PoiWordUtils.copyCellAndSetValue(flagCell, cell, "");
            }
        }
    }

    /**
     * 根据模板cell  添加重复行
     * @param startCell 模板列的开始位置
     * @param endCell 模板列的结束位置
     * @param currentRow 创建的新行
     * @param repeatFlagRow 模板列所在的行
     * @param rowDataList 每行的数据
     */
    private void addRowRepeat(int startCell, int endCell, XWPFTableRow currentRow, XWPFTableRow repeatFlagRow, List<String> rowDataList) {
        int cellSize = repeatFlagRow.getTableCells().size();
        for (int i = 0; i < cellSize; i++) {
            XWPFTableCell cell = currentRow.getCell(i);
            cell = cell == null ? currentRow.createCell() : currentRow.getCell(i);
            int flagCellIndex = i % (endCell - startCell + 1);
            XWPFTableCell repeatFlagCell = repeatFlagRow.getCell(flagCellIndex);
            if (i < rowDataList.size()) {
                PoiWordUtils.copyCellAndSetValue(repeatFlagCell, cell, rowDataList.get(i));
            } else {
                // 数据不满整行时，添加空列
                PoiWordUtils.copyCellAndSetValue(repeatFlagCell, cell, "");
            }
        }
    }

    /**
     * 复制段落
     *
     * @param sourcePar 原段落
     * @param targetPar
     * @param texts
     */
    private void copyParagraph(XWPFParagraph sourcePar, XWPFParagraph targetPar, String... texts) {

        targetPar.setAlignment(sourcePar.getAlignment());
        targetPar.setVerticalAlignment(sourcePar.getVerticalAlignment());

        // 设置布局
        targetPar.setAlignment(sourcePar.getAlignment());
        targetPar.setVerticalAlignment(sourcePar.getVerticalAlignment());

        if (texts != null && texts.length > 0) {
            String[] arr = texts;
            XWPFRun xwpfRun = sourcePar.getRuns().size() > 0 ? sourcePar.getRuns().get(0) : null;

            for (int i = 0, len = texts.length; i < len; i++) {
                String text = arr[i];
                XWPFRun run = targetPar.createRun();

                run.setText(text);

                run.setFontFamily(xwpfRun.getFontFamily());
                int fontSize = xwpfRun.getFontSize();
                run.setFontSize((fontSize == -1) ? DEFAULT_FONT_SIZE : fontSize);
                run.setBold(xwpfRun.isBold());
                run.setItalic(xwpfRun.isItalic());
            }
        }
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
}