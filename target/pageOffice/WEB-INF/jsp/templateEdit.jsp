<%@ page language="java"
         import="java.util.*,com.zhuozhengsoft.pageoffice.*,com.zhuozhengsoft.pageoffice.wordwriter.*"
         pageEncoding="utf-8"%>
<%
    PageOfficeCtrl poCtrl =(PageOfficeCtrl)request.getAttribute("poCtrl");
    Integer fid=(Integer) request.getAttribute("fid");
    poCtrl.setCaption("欣农合同模板编辑");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>欣农合同模板编辑</title>
    <!-- 强制Chromium内核，作用于360浏览器、QQ浏览器等国产双核浏览器 -->
    <meta name="renderer" content="webkit"/>
    <!-- 强制Chromium内核，作用于其他双核浏览器 -->
    <meta name="force-rendering" content="webkit"/>
    <!-- 如果有安装 Google Chrome Frame 插件则强制为Chromium内核，否则强制本机支持的最高版本IE内核，作用于IE浏览器 -->
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"/>
    <link  type="text/css" rel="stylesheet" href="./css/bodyStyle.css"/>
    <script type="text/javascript" src="/js/selectivizr.js"></script>
    <script src="/js/jquery.min.js"></script>
    <!--[if (gte IE 6)&(lte IE 8)]>
      <script type="text/javascript" src="/js/selectivizr.js"></script>
      <noscript><link rel="stylesheet" href="./css/bodyStyle.css" /></noscript>
    <![endif]-->
    <script type="text/javascript">
        function add() {
            document.getElementById("PageOfficeCtrl1").DataRegionList.Add( "PO_test1", "" );
        }

        //定位Tag
        function locateTag(tagName) {
            var appSlt = document.getElementById("PageOfficeCtrl1").Document.Application.Selection;
            var bFind = false;
            //appSlt.HomeKey(6);
            appSlt.Find.ClearFormatting();
            appSlt.Find.Replacement.ClearFormatting();
            bFind = appSlt.Find.Execute(tagName);
            if (!bFind) {
                document.getElementById("PageOfficeCtrl1").Alert("已搜索到文档末尾。");
                // appSlt.HomeKey(6);
            }
            window.focus();

        }
        //添加Tag
        function addTag(tagName) {
            try {
                var tmpRange = document.getElementById("PageOfficeCtrl1").Document.Application.Selection.Range;
                tmpRange.Text = tagName;
                tmpRange.Select();

                var mysub= "Function myfun()" + " \r\n"
                    +"Options.DefaultHighlightColorIndex = wdYellow" + " \r\n"
                    +"Selection.Range.HighlightColorIndex = wdYellow" + " \r\n"
                    +"End Function" + " \r\n"
                document.getElementById("PageOfficeCtrl1").RunMacro("myfun", mysub);

                return "true";
            } catch (e) {
                return "false";
            }
        }

        function insertEditRegion(){
            var date=new Date();//201911191101
            var year = date.getFullYear();       //年
            var month = date.getMonth() + 1;     //月
            var day = date.getDate();            //日
            var dateformat  =year+"-"+month+"-"+day;
            var datenum = changeTime(dateformat);
            //生成随机数3位+随机大小写字母3位
            var abc = randomString(6);
            //拼接 PO_date+
            //添加书签 param= "PO_hsjg="""
            var bkName = "PO_"+datenum+abc;
            var mac = "Function myfunc()" + " \r\n"
                + " If Application.Selection.Bookmarks.Count >= 1 Then " + " \r\n"
                + '   MsgBox  "该区域已经包含自定义变量或者可编辑区域，不能重复插入!" ' +"\r\n"
                + "   Else" +"\r\n"
                + "Dim r As Range " + " \r\n"
                + "Set r = Application.Selection.Range " + " \r\n"
                + "Application.ActiveDocument.Bookmarks.Add Name:=\"" + bkName + "\", Range:=r " + " \r\n"
                + "Application.ActiveDocument.Bookmarks(\"" + bkName + "\").Select " + " \r\n"
                + "With Selection.Font" + " \r\n"
                + "With .Shading" + " \r\n"
                + "End With" + " \r\n"
                + ".Borders(1).LineStyle = wdLineStyleNone" + " \r\n"
                + ".Borders.Shadow = False" + " \r\n"
                + "End With" + " \r\n"

                + "With Selection.Font" + " \r\n"
                + "With .Shading" + " \r\n"
                + ".Texture = wdTextureNone" + " \r\n"
                + ".ForegroundPatternColor = wdColorAutomatic" + " \r\n"
                + ".BackgroundPatternColor = wdColorLightYellow" + " \r\n"
                + "End With" + " \r\n"
                + ".Borders(1).LineStyle = wdLineStyleNone" + " \r\n"
                + ".Borders.Shadow = False" + " \r\n"
                + "End With" + " \r\n"
                + "With Options" + " \r\n"
                + ".DefaultBorderLineStyle = wdLineStyleSingle" + " \r\n"
                + ".DefaultBorderLineWidth = wdLineWidth050pt" + " \r\n"
                + ".DefaultBorderColor = wdColorAutomatic" + " \r\n"
                + "End With" + " \r\n"
                + "   End If "+ "\r\n"

                + "End Function " + " \r\n";
            document.getElementById("PageOfficeCtrl1").RunMacro("myfunc", mac);
        }

        //删除选中文本的内容中的书签
        function deleteEditRegion() {
            //判断当前是否选择了文本内容
            if (document.getElementById("PageOfficeCtrl1").Document.Application.Selection.Range.Text != ""){
                var mac = "Function myfunc()" + " \r\n"
                    + " If Application.Selection.Bookmarks.Count >= 1 Then " + " \r\n"
                    + "   ReDim aMarks(Application.Selection.Bookmarks.Count - 1)" + " \r\n"
                    + "   i = 0"+"\r\n"
                    + "   For Each aBookmark In Application.Selection.Bookmarks " + " \r\n"
                    + "   aMarks(i) = aBookmark.Name " + " \r\n"
                    + "   i = i + 1"+"\r\n"
                    + "   Next aBookmark " + " \r\n"
                    + "   For Each myElement In aMarks"+"\r\n"
                    + "   If Selection.Font.Shading.BackgroundPatternColor <> wdColorLightYellow Then"+"\r\n"
                    + '   MsgBox  "选择文本中没有可编辑子模板区域!" ' +"\r\n"
                    + "   exit for "+ "\r\n"
                    + "   Exit Function"+ "\r\n"
                    + "   End If "+ "\r\n"

                    + "   ActiveDocument.Bookmarks(myElement).Select" + " \r\n"
                    + "   ActiveDocument.Bookmarks(myElement).Delete"+" \r\n"
                    + "With Selection.Font" + " \r\n"
                    + "With .Shading" + " \r\n"
                    + ".Texture = wdTextureNone" + " \r\n"
                    + ".ForegroundPatternColor = wdColorAutomatic" + " \r\n"
                    + ".BackgroundPatternColor = wdColorAutomatic" + " \r\n"
                    + "End With" + " \r\n"
                    + ".Borders(1).LineStyle = wdLineStyleNone" + " \r\n"
                    + ".Borders.Shadow = False" + " \r\n"
                    + "End With" + " \r\n"
                    + "With Options" + " \r\n"
                    + ".DefaultBorderLineStyle = wdLineStyleSingle" + " \r\n"
                    + ".DefaultBorderLineWidth = wdLineWidth050pt" + " \r\n"
                    + ".DefaultBorderColor = wdColorAutomatic" + " \r\n"
                    + "End With" + " \r\n"
                    + "   Next" +"\r\n"
                    //判断选择文本中是否有书签
                    + "   Else" +"\r\n"
                    + '   MsgBox  "选择文本中没有可编辑子模板区域" ' +"\r\n"
                    + "   End If "+ "\r\n"



                    + "   End Function " + " \r\n";
                document.getElementById("PageOfficeCtrl1").RunMacro("myfunc", mac);

                // var mysub= "Function deleteColor()" + " \r\n"
                //     +"Options.DefaultHighlightColorIndex = wdNoHighlight" + " \r\n"
                //     +"Selection.Range.HighlightColorIndex = wdNoHighlight" + " \r\n"
                //     +"End Function" + " \r\n"

                // document.getElementById("PageOfficeCtrl1").RunMacro("deleteColor", mysub);
            }else{
                document.getElementById("PageOfficeCtrl1").Alert("您没有选择任何文本");
            }
        }


        function changeTime(option) {
            // currentTime当前时间，假设是 2019-7-2 19:03
            var currentTime = option;
            var reg = new RegExp("-","g");//去掉时间里面的-
            var a = currentTime.replace(reg,"");
            var regs = new RegExp(" ","g");//去掉时间里面的空格
            var b = a.replace(regs,"");
            var regss = new RegExp(":","g");//去掉时间里面的:冒号
            var c = b.replace(regss,"");
            return c
        }

        //生成随机数3位+随机大小写字母3位
        function randomString(len) {
            var charSet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
            var randomString = '';
            for (var i = 0; i < len; i++) {
                var randomPoz = Math.floor(Math.random() * charSet.length);
                randomString += charSet.substring(randomPoz,randomPoz+1);
            }
            return randomString;
        }

        //删除Tag
        function delTag(tagName) {
            var tmpRange = document.getElementById("PageOfficeCtrl1").Document.Application.Selection.Range;

            if (tagName == tmpRange.Text) {
                tmpRange.Text = "";
                return "true";
            }
            else
                return "false";

        }

    </script>

    <script type="text/javascript">
        function Save() {
            var status;
            $.ajax({
                url: "queryTemplateStatus.do?fid=" + fid,
                type: "POST",
                contentType: "application/json",
                data: {},
                success:function (data) {
                    status=data;
                    if(status=="A"){
                        //已发布
                        alert("该模板已经发布，不允许修改模板内容")
                        return;
                    }
                    var drlist = document.getElementById("PageOfficeCtrl1").DataRegionList;
                    drlist.refresh();
                    var count = document.getElementById("PageOfficeCtrl1").DataRegionList.Count;
                    var bookMarks=new Array(count);
                    for(j = 0; j < count; j++){
                        var name = document.getElementById("PageOfficeCtrl1").DataRegionList.Item(j).Name;
                        bookMarks[j]=name;
                    }
                    if(bookMarks.length!=0){
                        $.ajax({
                            url: "addBookMark.do?fid=" + fid + "&bookMarks=" + bookMarks,
                            type: "POST",
                            contentType: "application/json",
                            data: {},
                        })
                    }

                    document.getElementById("PageOfficeCtrl1").WebSave();
                    // window.external.close();//关闭POBrowser窗口
                }
            })
        }

        function SaveAndExit() {
            var status;
            $.ajax({
                url: "queryTemplateStatus.do?fid=" + fid,
                type: "POST",
                contentType: "application/json",
                data: {},
                success:function (data) {
                    status=data;
                    if(status=="A"){
                        //已发布
                        window.external.close();//关闭POBrowser窗口
                        return;
                    }

                    var drlist = document.getElementById("PageOfficeCtrl1").DataRegionList;
                    drlist.refresh();
                    var count = document.getElementById("PageOfficeCtrl1").DataRegionList.Count;
                    var bookMarks=new Array(count);
                    for(j = 0; j < count; j++){
                        var name = document.getElementById("PageOfficeCtrl1").DataRegionList.Item(j).Name;
                        bookMarks[j]=name;
                    }
                    if(bookMarks.length!=0){
                        if(addBookMark!=""&&addBookMark!=null&&bookMarks.length!=0){
                            $.ajax({
                                url: "addBookMark.do?fid=" + fid + "&bookMarks=" + bookMarks,
                                type: "POST",
                                contentType: "application/json",
                                data: {},
                            })
                        }
                    }
                    document.getElementById("PageOfficeCtrl1").WebSave();
                    window.external.close();//关闭POBrowser窗口
                }
            })

        }
    </script>

    <script type="text/javascript">
        //获取后台添加的书签名称字符串
        function getBkNames() {
            var bkNames = document.getElementById("PageOfficeCtrl1").DataRegionList.DefineNames;
            return bkNames;
        }

        //获取后台添加的书签文本字符串
        function getBkConts() {
            var bkConts = document.getElementById("PageOfficeCtrl1").DataRegionList.DefineCaptions;
            return bkConts;
        }


        var paramBkcount=0;
        //定位书签
        function locateBK(param) {
            var tmpArr = param.split("=");
            var fparamId=tmpArr[0];
            var type=tmpArr[1];
            var drlist = document.getElementById("PageOfficeCtrl1").DataRegionList;
            drlist.refresh();
            var datas = new Array();
            $.ajax({
                url: "searchParam.do?fparamId=" + fparamId+"&type="+type,
                type: "POST",
                contentType: "application/json",
                data: {},
                success:function (data) {
                    datas=data;
                    if(paramBkcount==data.length){
                        paramBkcount=0;
                    }
                    if(paramBkcount<datas.length){
                        var parambkName=datas[paramBkcount];
                        if(document.getElementById("PageOfficeCtrl1").DataRegionList.DataRegionByNameExists(parambkName)){
                            drlist.refresh();
                            drlist.GetDataRegionByName(parambkName).Locate();
                            document.getElementById("PageOfficeCtrl1").Activate();
                            window.focus();
                        }else {
                            document.getElementById("PageOfficeCtrl1").Alert("已搜索到文档末尾。");

                        }
                        paramBkcount++;
                    }else {
                        paramBkcount=0;
                    }
                }
            });
        }


        //查询书签
        function decideBk(PONames){
            for (var i = 0; i <PONames.length ; i++) {
                var bool=document.getElementById("PageOfficeCtrl1").DataRegionList.DataRegionByNameExists(PONames[i]);
                if(bool==true){
                    return bool
                }
            }
        }


        //添加书签 param= "PO_hsjg=回收价格"
        function addBookMark(param) {
            var tmpArr = param.split("=");
            var content=tmpArr[0];
            var type = tmpArr[1];
            var fparamId=tmpArr[2];
            var date=new Date();//201911191101
            var year = date.getFullYear();       //年
            var month = date.getMonth() + 1;     //月
            var day = date.getDate();            //日
            var dateformat  =year+"-"+month+"-"+day;
            var datenum = changeTime(dateformat);
            //生成随机数3位+随机大小写字母3位
            var abc = randomString(6);
            //拼接 PO_date+
            //添加书签 param= "PO_hsjg="""
            var bkName = "PO_"+datenum+abc;

            // var bkName = "PO_hsjg";
            // var content = "回收价格";
            var drlist = document.getElementById("PageOfficeCtrl1").DataRegionList;
            drlist.Refresh();
            var count = document.getElementById("PageOfficeCtrl1").DataRegionList.Count;
            for(j = 0; j < count; j++){
                var name = document.getElementById("PageOfficeCtrl1").DataRegionList.Item(j).Name;
                if(name==bkName){
                    alert("该变量已经插入过文本，请重新制作变量");
                    return ;
                }
            }

            try {
                var mac = "Function myfunc()" + " \r\n"
                    + "Dim r As Range " + " \r\n"
                    + " If Application.Selection.Bookmarks.Count >= 1 Then " + " \r\n"
                    + '   MsgBox  "该区域已经包含自定义变量或者可编辑区域，不能重复插入!" ' +"\r\n"
                    + "myfunc = 1" + " \r\n"
                    + "   End If "+ "\r\n"
                    + " If Application.selection.Bookmarks.Count < 1 Then " + " \r\n"
                    + "Set r = Application.Selection.Range " + " \r\n"
                    + "r.Text = \"" + content + "\"" + " \r\n"
                    + "Application.ActiveDocument.Bookmarks.Add Name:=\"" + bkName + "\", Range:=r " + " \r\n"
                    + "Application.ActiveDocument.Bookmarks(\"" + bkName + "\").Select " + " \r\n"
                    + "   End If "+ "\r\n"
                    + "End Function " + " \r\n";
                var strValue=document.getElementById("PageOfficeCtrl1").RunMacro("myfunc", mac);
                if(strValue==1){
                    return ;
                }
                drlist.Refresh();

                $.ajax({
                    url: "addParamBookMarks.do?type=" + type + "&bookMark=" + bkName+"&fparamId="+fparamId+"&fid="+fid,
                    type: "POST",
                    contentType: "application/json",
                    data: {},
                })
                var mac = "Function myfunc()" + " \r\n"
                    + "Dim r As Range " + " \r\n"
                    + "Set r = Application.Selection.Range " + " \r\n"
                    + "With Selection.Font" + " \r\n"
                    + "With .Shading" + " \r\n"
                    + ".Texture = wdTextureNone" + " \r\n"
                    + ".ForegroundPatternColor = wdColorAutomatic" + " \r\n"
                    + ".BackgroundPatternColor = wdColorYellow" + " \r\n"
                    + "End With" + " \r\n"
                    + ".Borders(1).LineStyle = wdLineStyleNone" + " \r\n"
                    + ".Borders.Shadow = False" + " \r\n"
                    + "End With" + " \r\n"
                    + "With Options" + " \r\n"
                    + ".DefaultBorderLineStyle = wdLineStyleSingle" + " \r\n"
                    + ".DefaultBorderLineWidth = wdLineWidth050pt" + " \r\n"
                    + ".DefaultBorderColor = wdColorAutomatic" + " \r\n"
                    + "End With" + " \r\n"

                    + "End Function " + " \r\n";
                document.getElementById("PageOfficeCtrl1").RunMacro("myfunc", mac);
                return "true";
            } catch (e) {
                return "false";
            }
        }


        //删除书签
        function delBookMark(bkName) {
            var drlist = document.getElementById("PageOfficeCtrl1").DataRegionList;
            try {
                drlist.Delete(bkName);
                return "true";
            } catch (e) {
                return "false";
            }
        }
    </script>


    <!--PageOffice.js和jquery.min.js文件一定要引用-->
    <script type="text/javascript" src="jquery.min.js"></script>
    <script src="../js/json2.js"></script>
    <%--    <script type="text/javascript" src="pageoffice.js" id="po_js_main"></script>外网访问注释--%>
    <script type="text/javascript">
        var fid = "";
        var datas ;
        var dataAs ;
        var colorName=new Array();
        var deleteBKList=new Array();
        //设值宽高
        function getHeight() {
            var hi = $(document.body).height();
            $('.div_left').height(hi);
        }

        window.onload =function () {
            var hi = window.screen.height/1.3;
            var wi = window.screen.width/2.8;
            // paramValue.close()
            getHeight();
            //关闭自定义变量命令提示框
            $('#closebox').click(function () {
                $('#box').css('display', 'none')
            });
            //关闭自定义条款命令提示框
            $('#closeboxes').click(function () {
                $('#boxes').css('display', 'none')
            });

            //新增变量按钮
            $('#nowly').click(function () {  // 自定义变量新增按钮ShowHtmlModeDialog
                document.getElementById("PageOfficeCtrl1").ShowHtmlModelessDialog("html/CustomVariableText-againsx.html", "parameter=xy", "width="+wi+";height="+hi+";frame:no;Caption=新增自定义变量;");
                // return (strnowly);

            });

            //新增条款按钮
            $('#openclause').click(function () {  // 自定义条款新增按钮
                var stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModelessDialog("html/AdditionalClause-againsx-pro.html", "parameter=xopen", "width="+(wi*1.3)+";height="+hi+";frame:no;Caption=新增条款;");
                return (stropenclause);
            });


            //自定义删除指定变量并提示
            $('#getdelete').click(function () {
                $(".input-u").each(function(i) {
                    if($(this).is(':checked')){
                        var fentryId = $(this).val();
                        for (var j = 0; j < dataAs.templateParMarkDtos.length; j++) {
                            if (dataAs.templateParMarkDtos[j].fentryId == fentryId) {
                                var temname = dataAs.templateParMarkDtos[j].name;
                            }
                        }
                        $.ajax({
                            url: "searchParam.do?fparamId=" + fentryId+"&type="+1,
                            type: "POST",
                            contentType: "application/json",
                            data: {},
                            success:function (data) {
                                if (decideBk(data)) {
                                    $('#box').css('display', 'block')//提示：该变量已经被引用，请在文本中将变量删除后，再重试
                                    $('#box p').html("提示：该变量已经被引用，请在文本中将变量删除后，再重试");
                                    setTimeout(function () {
                                        $('#box').css('display', 'none')
                                    }, 4000);
                                    return;
                                }else {
                                    if (confirm("确认要删除"+temname+"？")) {
                                        $.ajax({
                                            url: "removeParam.do?fentryId=" + fentryId,
                                            type: "POST",
                                            contentType: "application/json",
                                            data: {
                                                "fentryId": fentryId
                                            },
                                            success: function (data) {
                                                After();
                                            },
                                            error: function (data, i, j) {
                                                // alert(data.status);
                                            }
                                        })
                                    }
                                }
                            }
                        });
                    }
                })
            });

            //自定义条款删除提示
            $('#choses').click(function () {
                $(".input-v").each(function(i) {
                    if($(this).is(':checked')){
                        var farticleId = $(this).val();
                        for (var j = 0; j < dataAs.articleInfoDtos.length; j++) {
                            if (dataAs.articleInfoDtos[j].farticleId == farticleId) {
                                var Artname = dataAs.articleInfoDtos[j].name;
                            }
                        }
                        $.ajax({
                            url: "searchParam.do?fparamId=" + farticleId + "&type=" + 2,
                            type: "POST",
                            contentType: "application/json",
                            data: {},
                            success: function (data) {
                                if(decideBk(data)) {
                                    $('#boxes').css('display', 'block')//提示：该变量已经被引用，请在文本中将变量删除后，再重试
                                    $('#boxes p').html("提示：该变量已经被引用，请在文本中将变量删除后，再重试");
                                    setTimeout(function () {
                                        $('#boxes').css('display', 'none')
                                    }, 4000);
                                    return;
                                }else {
                                    if (confirm("确认要删除"+Artname+"？")) {
                                        $.ajax({
                                            url:"removeArticle.do?farticleId="+farticleId,
                                            type:"POST",
                                            contentType: "application/json",
                                            data:{
                                                "farticleId":farticleId
                                            },
                                            success:function (data) {
                                                After();
                                            },
                                            error:function (data,i,j) {
                                                // alert(data.status);
                                            }
                                        })
                                    }
                                }
                            }
                        })
                    }
                });
            });
        };


        function AfterDocumentOpened() {
            fid = $("#fids").val();
            localStorage.setItem("fid",fid);
            $.ajax({
                url: "templateGet.do?fid=" + fid,
                type: "POST",
                contentType: "application/json",
                success: function (data) {
                    var Amtname = new Array("金额","重量","百分比");
                    var Textname = new Array("文本","时间");
                    var Datename = new Array("日期");
                    if(data!=null){
                        if(data.templateParMarkDtos==""&&data.articleInfoDtos==""){
                            for (var j = 0; j < Textname.length ; j++) {
                                var type = "文字"
                                var XZData=JSON.stringify({
                                    "fid":fid,
                                    "entry":{
                                        "name":Textname[j],
                                        "type":type,
                                        "length":"",
                                        "paramType":2
                                    },
                                    "options":""
                                });
                                $.ajax({
                                    url:"addParam.do",
                                    type:"POST",
                                    contentType: "application/json;charset=UTF-8",
                                    data:XZData,
                                    success : function(data) {
                                    },
                                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                                        // alert(XMLHttpRequest.status)
                                    }
                                });
                            }
                            for (var k = 0; k < Datename.length; k++) {
                                var type = "日期"
                                var XZData=JSON.stringify({
                                    "fid":fid,
                                    "entry":{
                                        "name":Datename[k],
                                        "type":type,
                                        "length":"",
                                        "paramType":2
                                    },
                                    "options":""
                                })
                                $.ajax({
                                    url:"addParam.do",
                                    type:"POST",
                                    contentType: "application/json;charset=UTF-8",
                                    data:XZData,
                                    success : function(data) {
                                    },
                                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                                        // alert(XMLHttpRequest.status)
                                    }
                                });
                            }
                            for (var h = 0; h < Amtname.length; h++){
                                var type = "数字"
                                var XZData=JSON.stringify({
                                    "fid":fid,
                                    "entry":{
                                        "name":Amtname[h],
                                        "type":type,
                                        "length":"",
                                        "paramType":2
                                    },
                                    "options":""
                                })
                                $.ajax({
                                    url:"addParam.do",
                                    type:"POST",
                                    contentType: "application/json;charset=UTF-8",
                                    data:XZData,
                                    success : function(data) {
                                        // After()
                                    },
                                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                                        // alert(XMLHttpRequest.status)
                                    }
                                });
                            }
                        }
                    }
                }
            });
            After()
        }

        function After() {
            // 打开文件的时候执行
            colorName.length=0;
            deleteBKList.length=0;
            var dataRegionList = document.getElementById("PageOfficeCtrl1").DataRegionList;
            dataRegionList.Refresh();
            var count = dataRegionList.Count;
            for (i = 0; i < count; i++) {
                colorName.push(dataRegionList.Item(i).Name);
            }
            $.ajax({
                url:"searchParamByFid.do?fid="+fid,
                type:"POST",
                contentType: "application/json",
                success: function (data) {
                    for (var i = 0; i < data.length; i++) {
                        var boolean=$.inArray(data[i],colorName);
                        if(boolean==-1){
                            deleteBKList.push(data[i]);
                        }
                    }
                    $.ajax({
                        url:"deleteBkByFid.do?fid="+fid+"&bookMarks="+deleteBKList,
                        type:"POST",
                        contentType: "application/json",
                        success: function (data) {
                        }
                    });
                }
            });

            //更新模板为打开状态
            updateOpenStatus("openning");
            document.getElementById("PageOfficeCtrl1").JsFunction_BeforeDocumentClosed = "closeDocument()";

            ready();
        }
        //加载页面数据
        function ready() {
            console.log(colorName)
            var count = 0;
            $.ajax({
                url: "templateGet.do?fid=" + fid,
                type: "POST",
                contentType: "application/json",
                success: function (data) {
                    datas = "";
                    datas = data;
                    $("#winnum").html("");
                    $(".big-custom").html("");
                    $(".big-terms-bottom").html("");
                    dataAs = data;
                    var dataAll = JSON.stringify(dataAs);
                    localStorage.setItem("dataAll", dataAll);
                    //系统编号回显
                    $.each(data.tags, function (i, item) {
                        $("#winnum").append("<div class=\"Contract\" >\n" +
                            "<div style='width: 30%;height: 100%;float: left;'>\n" +
                            "                        <h2 style=\"float:left;padding-left:0.625rem;line-height: 2.1875rem;font-family: 'Microsoft YaHei UI';color: #5E6173;font-size: 0.9em;\n" +
                            "                margin-right: 0.75rem;overflow: hidden;text-overflow: ellipsis;white-space: nowrap\n" +
                            "                \">" + item + "</h2>\n" +
                            "</div>\n" +
                            "                       <div style='width: 100%;height: 2.5rem;margin-top: 0.1875rem'><h3 onclick=\"locateTag('" + item + "')\" class='variable-query'>定位</h3>\n" +
                            "                        <h4 class='add-to' onclick= \"addTag('" + item + "')\">添加</h4>\n" +
                            "                    </div></div>"
                        );
                    });
                    //自定义变量回显
                    $.each(data.templateParMarkDtos, function (i, item) {
                        count++;
                        $(".big-custom").append(
                            "<div class=\"custom-bottom\">\n" +
                            "  <div id=\"custom" + count + "\" class=\"custom-bottom-top\" style=\"width: 100%;height: 2.5rem;display: flex;align-items: center;justify-items: center;\">\n" +
                            "                    <div class=\"custom-input\" style=\"width: 10%;height: 100%;float: left;margin-top: 0.3125rem;\">\n" +
                            "                        <input type=\"checkbox\" value=" + item.fentryId + " class=\"input-u\" style=\"width: 0.9375rem;height: 0.9375rem;float: left;\n" +
                            "                        margin-left: 0.9375rem;margin-top: 0.7rem;\">\n" +
                            "                    </div>\n" +
                            "                    <div class=\"custom-content\" style=\"width: 40%;height: 100%;text-align: left;\n" +
                            "                    font-family: 'Microsoft YaHei UI';font-size: 0.875rem;color: #5E6173;line-height: 2.5rem;overflow: hidden;white-space: nowrap;\n" +
                            "                    text-overflow: ellipsis;padding-left: 0.9375rem;\"onmouseover=\"this.title =this.innerHTML\">" + item.name + "</div>\n" +
                            "<div class=\"custom-bottom-list\" style=\"width: 100%;height: 2.5rem;\">\n" +
                            "                    <h4 id=\"toseemove\" onclick='toseemoveNews(" + item.fentryId + ")' class='see-move'>查看</h4>\n" +
                            "                    <h5 onclick=\"locateBK('" + item.fentryId + "=" + 1 + "')\" class='variable-icon'>定位</h5>\n" +
                            "                    <h6 onclick=\"editHtml('" + item.fentryId + "')\" class='see-move'>编辑</h6>\n" +
                            "                    <h6 onclick=\"addBookMark('" + item.name + "=" + 1 + "=" + item.fentryId + "')\" class='add-ion'>添加</h6>\n" +
                            "\t\t\t\t\t</div>\n" +
                            "\t\t\t\t</div>\n" +
                            "      </div>"
                        )
                        console.log(colorName.toString())
                        for(var j=0;j<item.bookMarks.length;j++){
                            for (var k = 0; k <colorName.length; k++) {
                                if(colorName[k]==item.bookMarks[j]){
                                    $("#custom"+count).css({'background-color': '#ebeef2'})
                                }
                            }
                        }
                    });
                    //条款回显
                    $.each(data.articleInfoDtos, function (i, item) {
                        count++;
                        $(".big-terms-bottom").append(
                            "<div id=\"terms-bottom" + count + "\" class=\"terms-bottom\" style=\"width: 100%;height: 2.5rem;display: flex;align-items: center;justify-items: center;\">\n" +
                            "                <div class=\"terms-bottom-left\" style=\"width: 10%;height: 100%;float: left;margin-top: 0.3125rem;\">\n" +
                            "                    <input type=\"checkbox\" value=" + item.farticleId + " title=\"\"  class=\"input-v\" style=\"width: 0.9375rem;height: 0.9375rem;float: left;\n" +
                            "                    margin-left: 0.9375rem;margin-top: 0.75rem;\"  name="+item.name+">\n" +
                            "                </div>\n" +
                            "                <div class=\"terms-bottom-content\" style=\"width: 40%;height: 100%;text-align: left;\n" +
                            "font-family: 'Microsoft YaHei UI';font-size: 0.875rem;color: #5E6173;line-height: 2.5rem;overflow: hidden;white-space: nowrap;\n" +
                            "                text-overflow: ellipsis;padding-left: 0.9375rem;\"onmouseover=\"this.title=this.innerHTML\">" + item.name + "</div>\n" +
                            "                <div class=\"terms-bottom-right\" style=\"width: 100%;height: 2.5rem;\">\n" +
                            "                    <h4 class=\"see\" style=\"\" onclick='toseemoveTK(" + item.farticleId + ")'>查看</h4>\n" +
                            "                    <h5 class=\"see\" style=\"\" onclick=\"locateBK('" + item.farticleId + "=" + 2 + "')\">定位</h5>\n" +
                            "                    <h6 class=\"see-add\" onclick=\"addBookMark('" + item.name + "=" + 2 + "=" + item.farticleId + "')\">添加</h6>\n" +
                            "               </div>\n" +
                            "               </div>\n" +
                            "</div>"
                        )
                        console.log(colorName.toString())
                        for(var j=0;j<item.bookMarks.length;j++){
                            for (var k = 0; k <colorName.length; k++) {
                                if(colorName[k]==item.bookMarks[j]){
                                    $("#terms-bottom"+count).css({'background-color': '#ebeef2'})
                                }
                            }
                        }
                    });
                    //变量点击加色
                    $(".add-ion").click(function () {
                        var id = $(this).parent().parent().attr('id');
                        $("#" + id).css({'background-color': '#ebeef2'})
                    });
                    //条款点击加色
                    $(".see-add").click(function () {
                        var id = $(this).parent().parent().attr('id');
                        $("#" + id).css({'background-color': '#ebeef2'})
                    });
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        }


        //查看条款信息
        function toseemoveTK(countBL){
            $.ajax({
                url: "queryByArticleId.do?articleId=" + countBL,
                type: "POST",
                contentType: "application/json",
                success: function (data) {
                    var hi = window.screen.height/1.3;
                    var wi = window.screen.width/2.8;
                    localStorage.setItem("TKname", data.name);
                    var contents = JSON.stringify(data.contents);
                    localStorage.setItem("contents", contents);
                    var stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModalDialog("html/ViewTerms-agains.html", "parameter=", "width="+wi+";height="+hi+";frame:no;Caption=条款;")
                    return (stropenclause);
                },
                error: function (data) {
                }
            })
        }
        //查看新增信息
        function toseemoveNews(countBL){
            $.ajax({
                url: "paramEntry.do?fentryId="+countBL,
                type: "POST",
                contentType: "application/json",
                data:{
                    "fentryId":countBL
                },
                success: function (data) {
                    var hi = window.screen.height/1.3;
                    var wi = window.screen.width/2.8;
                    if(data.templateParamEntryModel.paramType=="2"){
                        var str_jsonData = JSON.stringify(data);
                        localStorage.setItem("datatext",str_jsonData);
                        var stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModalDialog("html/ViewText-agains.html", "parameter=", "width="+wi+";height="+hi+";frame:no;Caption=文本;");
                        return (stropenclause);
                    }else if(data.templateParamEntryModel.paramType=="3"){
                        $.ajax({
                            url: "paramEntryOption.do?fentryId="+countBL,
                            type: "POST",
                            contentType: "application/json",
                            data:{
                                "fentryId":countBL
                            },
                            success: function (dataoption) {
                                var hi = window.screen.height/1.45;
                                var wi = window.screen.width/2.2;
                                var str_jsonData = JSON.stringify(dataoption);
                                // alert(str_jsonData)
                                localStorage.setItem("dataoption",str_jsonData);
                                var stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModalDialog("html/ViewOptions-agains.html", "parameter=", "width="+wi+";height="+hi+";frame:no;Caption=下拉选项;");
                                return (stropenclause);
                            },
                            error:function (i,e,m) {
                                // alert(i.status)
                            }
                        });
                    }
                },
                error:function (i,e,m) {
                    // alert(i.status)
                }
            })
        }


        //新增条框变量确认
        function TKbottom(name) {
            var values = localStorage.getItem("values");
            var value = JSON.parse(values);
            var data=JSON.stringify({
                "fid":fid,
                "entry": {
                    "name":name,
                    "articles": value
                }
            });
            $.ajax({
                url: "addArticle.do",
                type: "POST",
                contentType: "application/json;charset=UTF-8",
                data: data,
                success: function (data) {
                    ready();
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    // alert(XMLHttpRequest.status)
                }
            })
        }


        //文档关闭前先提示用户是否保存
        function BeforeBrowserClosed(){

            if (document.getElementById("PageOfficeCtrl1").IsDirty){
                if(confirm("提示：文档已被修改，是否继续关闭放弃保存 ？"))
                {
                    return  true;
                }else{
                    return  false;
                }
            }

        }

        function updateOpenStatus(status) {
            $.ajax({
                url: "updateOpenStatus.do?fid="+fid+"&openStatus="+status,
                type: "POST",
                contentType: "application/json;charset=UTF-8"
            })
        }


        function closeDocument(){
            updateOpenStatus("closed");
        }


        function editHtml(id){
            $.ajax({
                url: "searchParam.do?fparamId=" + id+"&type="+1,
                type: "POST",
                contentType: "application/json",
                data: {},
                success:function (data) {
                    if (decideBk(data)) {
                        $('#box').css('display', 'block')//提示：该变量已经被引用，请在文本中将变量删除后，再重试
                        $('#box p').html("提示：该变量已经被引用，请在文本中将变量删除后，再重试");
                        setTimeout(function () {
                            $('#box').css('display', 'none')
                        }, 4000);
                        return;
                    }else {
                        if(dataAs.articleInfoDtos ==""){
                            $.ajax({
                                url: "paramEntry.do?fentryId=" + id,
                                type: "POST",
                                contentType: "application/json",
                                data: {
                                    "fentryId": id
                                },
                                success: function (data) {
                                    var hi = window.screen.height / 1.3;
                                    var wi = window.screen.width / 2.8;
                                    if (data.templateParamEntryModel.paramType == "2") {
                                        var str_jsonData = JSON.stringify(data);
                                        localStorage.setItem("datatext", str_jsonData);
                                        localStorage.removeItem("dataoption");
                                        var stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModelessDialog("html/EditText-agains.html", "parameter=", "width=" + wi + ";height=" + hi + ";frame:no;Caption=文本;");
                                        return (stropenclause);
                                    } else if (data.templateParamEntryModel.paramType == "3") {
                                        $.ajax({
                                            url: "paramEntryOption.do?fentryId=" + id,
                                            type: "POST",
                                            contentType: "application/json",
                                            data: {
                                                "fentryId": id
                                            },
                                            success: function (dataoption) {
                                                var hi = window.screen.height / 1.3;
                                                var wi = window.screen.width / 2.8;
                                                var str_jsonData = JSON.stringify(dataoption);
                                                localStorage.setItem("dataoption", str_jsonData);
                                                localStorage.removeItem("datatext");
                                                var stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModelessDialog("html/EditText-agains.html", "parameter=", "width=" + wi + ";height=" + hi + ";frame:no;Caption=下拉选项;");
                                                return (stropenclause);
                                            },
                                            error: function (i, e, m) {
                                                // alert(i.status)
                                            }
                                        });
                                    }
                                },
                                error: function (i, e, m) {
                                }
                            })
                        }else {
                            $.ajax({
                                url: "editOrNot.do?fentryId=" + id,
                                type: "POST",
                                contentType: "application/json",
                                success: function (data) {
                                    if(!data){
                                        $('#box').css('display', 'block')//提示：该变量已经被引用，请在文本中将变量删除后，再重试
                                        $('#box p').html("提示：该变量已经被条款引用，请在文本中将变量删除后，再重试");
                                        setTimeout(function () {
                                            $('#box').css('display', 'none')
                                        }, 4000);
                                        return;
                                    }else {
                                        $.ajax({
                                            url: "paramEntry.do?fentryId=" + id,
                                            type: "POST",
                                            contentType: "application/json",
                                            data: {
                                                "fentryId": id
                                            },
                                            success: function (data) {
                                                var hi = window.screen.height / 1.3;
                                                var wi = window.screen.width / 2.8;
                                                if (data.templateParamEntryModel.paramType == "2") {
                                                    var str_jsonData = JSON.stringify(data);
                                                    localStorage.setItem("datatext", str_jsonData);
                                                    localStorage.removeItem("dataoption");
                                                    var stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModelessDialog("html/EditText-agains.html", "parameter=", "width=" + wi + ";height=" + hi + ";frame:no;Caption=文本;");
                                                    return (stropenclause);
                                                } else if (data.templateParamEntryModel.paramType == "3") {
                                                    $.ajax({
                                                        url: "paramEntryOption.do?fentryId=" + id,
                                                        type: "POST",
                                                        contentType: "application/json",
                                                        data: {
                                                            "fentryId": id
                                                        },
                                                        success: function (dataoption) {
                                                            var hi = window.screen.height / 1.3;
                                                            var wi = window.screen.width / 2.8;
                                                            var str_jsonData = JSON.stringify(dataoption);
                                                            localStorage.setItem("dataoption", str_jsonData);
                                                            localStorage.removeItem("datatext");
                                                            var stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModelessDialog("html/EditText-agains.html", "parameter=", "width=" + wi + ";height=" + hi + ";frame:no;Caption=下拉选项;");
                                                            return (stropenclause);
                                                        },
                                                        error: function (i, e, m) {
                                                            // alert(i.status)
                                                        }
                                                    });
                                                }
                                            },
                                            error: function (i, e, m) {
                                            }
                                        })
                                    }
                                }
                            })
                        }
                    }
                }
            });
        }

    </script>


</head>
<body style="height: 100vh;">
<form id="form1">
    <%--    <div  style=" width: 100%; height: 100%;">--%>
    <div style=" float: left; width: 70%; height: 100%;" class="div_left">
        <%--<input id="Button1" type="button" value="插入批注" onclick="return Button1_onclick()" />--%>
        <%--<input id="Text1" type="text" />--%>
        <%=poCtrl.getHtmlCode("PageOfficeCtrl1")%>
    </div>
    <div  style=" width: 30%; height: 100%;" id="div_left">
        <%--        <div href="../../html/Contract.html" />--%>
        <div class="wrap">
            <div class="left"></div>
            <div class="right"  id="ha" style="height: 100%;">
                <div class="right-top"><p>合同变量</p></div>
                <div class="right-list" id="winnum">
                    <div class="variables">系统编号</div>
                </div>
                <div class="right-content">
                    <div class="custom">
                        <div class="custom-top" style="position: relative">
                            <div id="box" class="prompt-box" style="width: 100%;height: 2.5rem;background:#EEF4FF;z-index:999;display: none;position: absolute;">
                                <div class="prompt-box-content"><p style="font-family: 'Microsoft YaHei UI';
font-size: 0.7em;color: #48484A;line-height: 2.5rem;float: left;"></p><span style="float: right;width: 0.9375rem;height: 0.9375rem;display: inline-block;
background: url(/image/delete.png) no-repeat center center;background-size: 1.875rem 1.875rem;margin-top: 0.75rem;
margin-right: 0.5rem;cursor: pointer;"id="closebox"></span></div>
                            </div><!--命令提示框-->
                            <h2 style="float: left;font-family: 'Microsoft YaHei UI';font-weight: bold;
font-size: 0.9375rem;color: #48484A;margin-left: 0.9375rem;line-height: 2.5rem;">自定义变量</h2>
                            <h3 id="getdelete" class="del-icon">删除</h3>
                            <h4 id="nowly" class="add-icon">新增</h4>
                            <h5 class="add-icon" onclick='After()'>刷新</h5>

                        </div>
                        <div class="custom-list">
                            <div class="custom-list-left"></div>
                            <div class="custom-list-right">
                                <p>名称</p>
                            </div>
                        </div>
                    </div>
                    <div class="big-custom">

                    </div>
                </div>
                <div class="right-bottom">
                    <div class="terms"style="position: relative;">
                        <div id="boxes" class="prompt-boxes" style="width: 100%;height: 2.5rem;background:#EEF4FF;z-index:999;display: none;position: absolute;">
                            <div class="prompt-boxes-content"><p style="font-family: 'Microsoft YaHei UI';
font-size: 0.7em;color: #48484A;line-height: 2.5rem;float: left;"></p><span style="float: right;width: 0.9375rem;height: 0.9375rem;display: inline-block;
background: url(/image/delete.png) no-repeat center center;background-size: 1.875rem 1.875rem;margin-top: 0.75rem;
margin-right: 0.5rem;cursor: pointer;"id="closeboxes"></span></div>
                        </div><!--条款命令提示框-->
                        <div class="terms-title" style="width: 100%;height: 2.5rem;">
                            <p style="float: left;font-family: 'Microsoft YaHei UI';font-weight: bold;
font-size: 0.9375rem;color: #48484A;margin-left: 0.9375rem;line-height: 2.5rem;">自定义条款</p>
                            <span id="choses" class="delete-icon">删除</span>
                            <h6 id="openclause" class="add-query">新增</h6>
                            <h5 class="add-icon" onclick="After()">刷新</h5>
                        </div>
                        <div class="terms-top" style="width: 100%;height: 2.5rem;">
                            <div class="terms-top-left" style="width: 10%;height: 100%;float: left;"></div>
                            <div class="terms-top-content" style="width: 22.5%;height: 100%;float: left;font-size: 0.875rem;
font-family: 'Microsoft YaHei UI';color: #48484A;line-height: 2.5rem;text-align: left;font-weight: bold;padding-left: 0.3125rem;">条款</div>
                            <%--<div class="terms-top-right" style="width: 22.5%;height: 100%;float: right;font-size: 14px;
font-family: 'Microsoft YaHei UI';color: #48484A;line-height: 40px;text-align: center;">详情</div>--%>
                        </div>
                    </div>
                    <div class="big-terms-bottom" style="width: 100%;"></div>
                </div>
            </div>
        </div>
        <input type="hidden" id="fids" value="<%=fid%>"></input>
    </div>
    <%--    </div>--%>
</form>
</body>
</html>