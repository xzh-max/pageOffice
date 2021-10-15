<%@ page language="java"
	import="java.util.*,com.zhuozhengsoft.pageoffice.*,java.sql.*,java.io.*,javax.servlet.*,javax.servlet.http.*"
	pageEncoding="utf-8"%>
<%
    PageOfficeCtrl poCtrl =(PageOfficeCtrl)request.getAttribute("poCtrl");
    poCtrl.setJsFunction_AfterDocumentOpened("AfterDocumentOpened");
    int fid=(int) request.getAttribute("fid");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
    <title></title>
    <!-- 强制Chromium内核，作用于360浏览器、QQ浏览器等国产双核浏览器 -->
    <meta name="renderer" content="webkit"/>
    <!-- 强制Chromium内核，作用于其他双核浏览器 -->
    <meta name="force-rendering" content="webkit"/>
    <!-- 如果有安装 Google Chrome Frame 插件则强制为Chromium内核，否则强制本机支持的最高版本IE内核，作用于IE浏览器 -->
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"/>

    <link href="images/csstg.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="jquery.min.js"></script>
    <script src="../js/json2.js"></script>
    <script src="/js/selectivizr.js"></script>
    <script language="javascript">

        var datas;

        function ShowDialog1() {
            document.getElementById("PageOfficeCtrl1").ShowDialog(2);
        }
        function ShowDialog2() {
            document.getElementById("PageOfficeCtrl1").ShowDialog(5);
        }
        function ShowDialog3() {
            document.getElementById("PageOfficeCtrl1").ShowDialog(4);
        }

        //全屏/还原
        function IsFullScreen() {
            document.getElementById("PageOfficeCtrl1").FullScreen = !document.getElementById("PageOfficeCtrl1").FullScreen;
        }

        function getHeight() {
            var hi = $(document.body).height();
            $('.div_left').height(hi);
        }
        function AfterDocumentOpened() {
            document.getElementById("PageOfficeCtrl1").FullScreen = true;// 全屏
        }

        // window.onload=function (ev) {
        //     getHeight();
        // }

        window.onload =function () {
            var fid =document.getElementById("fid").value;
            getHeight();
            localStorage.setItem("parentFid",fid)
            $.ajax({
                url: "templateGet.do?fid=" + fid,
                type: "POST",
                contentType: "application/json",
                success: function (data) {
                    datas = data;
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            })
        };

        function OnWordDataRegionClick(Name,Value,Left,Bottom){
            var hi = $(document.body).height()/1.3;
            var wi = $(document.body).width()/2.8;
            var stropenclause = "";
            for (var i = 0; i <datas.templateParMarkDtos.length ; i++) {
                for(var j=0;j<datas.templateParMarkDtos[i].bookMarks.length;j++){
                    if(datas.templateParMarkDtos[i].bookMarks[j]==Name){
                        var MarkDtos = datas.templateParMarkDtos[i].bookMarks[j];
                        var name = datas.templateParMarkDtos[i].name;
                        localStorage.setItem("MarkDtos",MarkDtos);
                        localStorage.setItem("MarkName",name);
                        if(datas.templateParMarkDtos[i].type =="日期"){
                            stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModalDialog("html/datetimer-agains.htm", Value, "left=" + Left + "px;width="+wi+";height="+hi+"px;frame:no;Caption=请选择日期;");
                            if (stropenclause != "") {
                                return (stropenclause);
                            }
                            else {
                                if ((Value == undefined) || (Value == ""))
                                    return " ";
                                else
                                    return Value;
                            }
                        }else
                        if(datas.templateParMarkDtos[i].type =="下拉选项"){
                            var s = JSON.stringify(datas);
                            localStorage.setItem("data",s);
                            stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModalDialog("html/selectOption-agains.htm", Value, "left=" + Left + "px;width="+wi+";height="+hi+"px;frame:no;Caption=选择下拉项;");
                            if (stropenclause != "") {
                                return (stropenclause);
                            }
                            else {
                                if ((Value == undefined) || (Value == ""))
                                    return " ";
                                else
                                    return Value;
                            }
                        }else
                        if(datas.templateParMarkDtos[i].type =="文字") {
                            stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModalDialog("html/ParamInputPage-agains.htm", Value, "left=" + Left + "px;width="+wi+";height="+hi+"px;frame:no;Caption=输入文字;");
                            if (stropenclause != "") {
                                return (stropenclause);
                            } else {
                                if ((Value == undefined) || (Value == ""))
                                    return " ";
                                else
                                    return Value;
                            }
                        }else
                        if(datas.templateParMarkDtos[i].type == "数字"){
                            stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModalDialog("html/ParamInputPageNumber-agains.htm", Value, "left=" + Left + "px;width="+wi+";height="+hi+"px;frame:no;Caption=输入数字;");
                            if (stropenclause != "") {
                                return (stropenclause);
                            }
                            else {
                                if ((Value == undefined) || (Value == ""))
                                    return " ";
                                else
                                    return Value;
                            }
                        }
                    }
                }
            }
            for (var i = 0; i <datas.articleInfoDtos.length ; i++) {
                for (var j=0;j<datas.articleInfoDtos[i].bookMarks.length;j++) {
                    if (datas.articleInfoDtos[i].bookMarks[j] == Name) {
                        var Article = datas.articleInfoDtos[i].bookMarks[j];
                        localStorage.setItem("Article", Article);
                        localStorage.setItem("Datas",JSON.stringify(datas));
                        stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModalDialog("html/OptionClause-Againsx.html", Name, "left=" + Left + "px;width="+wi+";height="+hi+";frame:no;Caption=选择条款;");
                        if (stropenclause != "") {
                            return (stropenclause);
                        } else {
                            if ((Value == undefined) || (Value == ""))
                                return " ";
                            else
                                return Value;
                        }
                    }
                }
            }
        }
    </script>
</head>
<body style="height: 100vh;">
<form id="form2">
    <%--    <div id="header">--%>
    <%--    </div>--%>
    <div id="textcontent" class="div_left">
        <!--**************   卓正 PageOffice组件 ************************-->
        <%=poCtrl.getHtmlCode("PageOfficeCtrl1")%>
    </div>
</form>
<input type="hidden" id="fid" value="<%=fid%>">
</body>
</html>
