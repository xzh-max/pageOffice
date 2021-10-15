<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.zhuozhengsoft.pageoffice.*, com.zhuozhengsoft.pageoffice.wordwriter.*,java.awt.*,java.net.*"%>
<%

PageOfficeCtrl poCtrl1 = (PageOfficeCtrl)request.getAttribute("poCtrl");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Word文档比较</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!-- 强制Chromium内核，作用于360浏览器、QQ浏览器等国产双核浏览器 -->
    <meta name="renderer" content="webkit"/>
    <!-- 强制Chromium内核，作用于其他双核浏览器 -->
    <meta name="force-rendering" content="webkit"/>
    <!-- 如果有安装 Google Chrome Frame 插件则强制为Chromium内核，否则强制本机支持的最高版本IE内核，作用于IE浏览器 -->
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"/>
  </head>
  
  <body>
  <script language="javascript" type="text/javascript">
	    function SaveDocument() {
	        document.getElementById("PageOfficeCtrl1").WebSave();
	    }
	    function ShowFile1View() {
	        document.getElementById("PageOfficeCtrl1").Document.ActiveWindow.View.ShowRevisionsAndComments = false;
	        document.getElementById("PageOfficeCtrl1").Document.ActiveWindow.View.RevisionsView = 1;
	    }
	    function ShowFile2View() {
	        document.getElementById("PageOfficeCtrl1").Document.ActiveWindow.View.ShowRevisionsAndComments = false;
	        document.getElementById("PageOfficeCtrl1").Document.ActiveWindow.View.RevisionsView = 0;
	    }
	    function ShowCompareView() {
	        document.getElementById("PageOfficeCtrl1").Document.ActiveWindow.View.ShowRevisionsAndComments = true;
	        document.getElementById("PageOfficeCtrl1").Document.ActiveWindow.View.RevisionsView = 0;
	    }
	    function SetFullScreen() {
	        document.getElementById("PageOfficeCtrl1").FullScreen = !document.getElementById("PageOfficeCtrl1").FullScreen;
	    }
	</script>
    <div style="width:1000px; height:800px;">
        <%=poCtrl1.getHtmlCode("PageOfficeCtrl1")%>
  	</div>
  </body>
</html>
