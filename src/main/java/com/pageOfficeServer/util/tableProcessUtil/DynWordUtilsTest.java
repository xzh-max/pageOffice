package com.pageOfficeServer.util.tableProcessUtil;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class DynWordUtilsTest {

    /**
     * 说明 普通占位符位${field}格式
     * 表格中的占位符为${tbAddRow:tb1}  tb1为唯一标识符
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) {

        // 模板全的路径
        String templatePaht = "E:\\ht.docx";
        // 输出位置
        String outPath = "e:\\221.docx";

        Map<String, Object> paramMap = new HashMap<>(16);
        // 普通的占位符示例 参数数据结构 {str,str}
        paramMap.put("title", "德玛西亚");
        paramMap.put("startYear", "2010");
        paramMap.put("endYear", "2020");
        paramMap.put("currentYear", "2019");
        paramMap.put("currentMonth", "10");
        paramMap.put("currentDate", "26");
        paramMap.put("name", "黑色玫瑰");


        // 表格中的参数示例 参数数据结构 [[str]]
        List<List<String>> tbRow1 = new ArrayList<>();
        List<String> tbRow1_row1 = new ArrayList<>(Arrays.asList("1、模块一", "分类1"));
        List<String> tbRow1_row2 = new ArrayList<>(Arrays.asList("2、模块二", "分类2"));
        tbRow1.add(tbRow1_row1);
        tbRow1.add(tbRow1_row2);
        paramMap.put(PoiWordUtils.addRowText + "tb1", tbRow1);

        List<List<String>> tbRow2 = new ArrayList<>();
        List<String> tbRow2_row1 = new ArrayList<>(Arrays.asList("指标c", "指标c的意见"));
        List<String> tbRow2_row2 = new ArrayList<>(Arrays.asList("指标d", "指标d的意见"));
        tbRow2.add(tbRow2_row1);
        tbRow2.add(tbRow2_row2);
        paramMap.put(PoiWordUtils.addRowText + "tb2", tbRow2);

        List<List<String>> tbRow3 = new ArrayList<>();
        List<String> tbRow3_row1 = new ArrayList<>(Arrays.asList("3", "耕地估值"));
        List<String> tbRow3_row2 = new ArrayList<>(Arrays.asList("4", "耕地归属", "平方公里"));
        tbRow3.add(tbRow3_row1);
        tbRow3.add(tbRow3_row2);
        paramMap.put(PoiWordUtils.addRowText + "tb3", tbRow3);

        // 支持在表格中动态添加图片
        List<List<String>> tbRow4 = new ArrayList<>();
        List<String> tbRow4_row1 = new ArrayList<>(Arrays.asList("03", "旅游用地", "18.8m2"));
        List<String> tbRow4_row2 = new ArrayList<>(Arrays.asList("04", "建筑用地"));
        tbRow4.add(tbRow4_row1);
        tbRow4.add(tbRow4_row2);

        paramMap.put(PoiWordUtils.addRowText + "tb4", tbRow4);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("A",tbRow4);
        System.out.print(jsonObject.toJSONString());

        // 支持在表格中添加重复模板的行


        List<List<String>> tb5List = new ArrayList<>();
        List<String> tb5Row1 = new ArrayList<>(Arrays.asList("岳云鹏", "孙悦"));
        List<String> tb5Row2 = new ArrayList<>(Arrays.asList("小沈阳", "宋小宝"));
        List<String> tb5Row3 = new ArrayList<>(Arrays.asList("张云雷", "严鹤翔"));
        tb5List.add(tb5Row1);
        tb5List.add(tb5Row2);
        tb5List.add(tb5Row3);

        paramMap.put("tbAddRow:tb5", tb5List);

        List<String> tbRow5_row4 = new ArrayList<>(Arrays.asList("诺克萨斯"));
        List<String> tbRow5_row5 = new ArrayList<>(Arrays.asList("德莱文", "诺手"));
        List<String> tbRow5_row6 = new ArrayList<>(Arrays.asList("男枪", "卡特琳娜"));

        List<List<String>> tbRow5 = new ArrayList<>();
        List<String> tbRow5_row1 = new ArrayList<>(Arrays.asList("欢乐喜剧人"));
        List<String> tbRow5_row2 = new ArrayList<>(Arrays.asList("常远", "艾伦"));
        tbRow5.add(tbRow5_row1);
        tbRow5.add(tbRow5_row2);
        tbRow5.add(tbRow5_row4);
        tbRow5.add(tbRow5_row5);
        tbRow5.add(tbRow5_row6);
        System.out.print(tbRow5.toString());
        paramMap.put("tbAddRowRepeat:tb5:0,2,0,1", tbRow5);


        // 图片占位符示例 ${image:imageid} 比如 ${image:image1}, ImageEntity中的值就为image:image1
        // 段落中的图片
        // 表格中的图片

        DynWordUtils.process(paramMap, templatePaht, outPath);
    }

}