<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.zhuozhengsoft.pageoffice.*, com.zhuozhengsoft.pageoffice.wordwriter.*,java.awt.*"%>
<%

    PDFCtrl poCtrl1=(PDFCtrl)request.getAttribute("poCtrl");
    poCtrl1.setJsFunction_AfterDocumentOpened("AfterDocumentOpened");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <!-- 强制Chromium内核，作用于360浏览器、QQ浏览器等国产双核浏览器 -->
    <meta name="renderer" content="webkit"/>
    <!-- 强制Chromium内核，作用于其他双核浏览器 -->
    <meta name="force-rendering" content="webkit"/>
    <!-- 如果有安装 Google Chrome Frame 插件则强制为Chromium内核，否则强制本机支持的最高版本IE内核，作用于IE浏览器 -->
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"/>
    <style>
    </style>
</head>
<body style="overflow:hidden" >


<script type="text/javascript" src="jquery.min.js"></script>
<script src="../js/json2.js"></script>
<script src="/js/selectivizr.js"></script>
<script type="text/javascript">
    function Close() {
        window.external.close();
    }

</script>
<!--**************   卓正 PageOffice 客户端代码开始    ************************-->
<script language="javascript" type="text/javascript">
    function AfterDocumentOpened() {
        //alert(document.getElementById("PDFCtrl1").Caption);
    }
    function SetBookmarks() {
        document.getElementById("PDFCtrl1").BookmarksVisible = !document.getElementById("PDFCtrl1").BookmarksVisible;
    }

    function PrintFile() {
        document.getElementById("PDFCtrl1").ShowDialog(4);
    }
    function SwitchFullScreen() {
        document.getElementById("PDFCtrl1").FullScreen = !document.getElementById("PDFCtrl1").FullScreen;
    }
    function SetPageReal() {
        document.getElementById("PDFCtrl1").SetPageFit(1);
    }
    function SetPageFit() {
        document.getElementById("PDFCtrl1").SetPageFit(2);
    }
    function SetPageWidth() {
        document.getElementById("PDFCtrl1").SetPageFit(3);
    }
    function ZoomIn() {
        document.getElementById("PDFCtrl1").ZoomIn();
    }
    function ZoomOut() {
        document.getElementById("PDFCtrl1").ZoomOut();
    }
    function FirstPage() {
        document.getElementById("PDFCtrl1").GoToFirstPage();
    }
    function PreviousPage() {
        document.getElementById("PDFCtrl1").GoToPreviousPage();
    }
    function NextPage() {
        document.getElementById("PDFCtrl1").GoToNextPage();
    }
    function LastPage() {
        document.getElementById("PDFCtrl1").GoToLastPage();
    }
    function RotateRight() {
        document.getElementById("PDFCtrl1").RotateRight();
    }
    function RotateLeft() {
        document.getElementById("PDFCtrl1").RotateLeft();
    }
    function getHeight() {
        var hi = $(document.body).height();
        $('.div_left').height(hi);
    }

    function AfterDocumentOpened() {
        document.getElementById("PageOfficeCtrl1").FullScreen = true;// 全屏
    }

    window.onload=function (ev) {
        getHeight();
    }
</script>

<form id="form3" >
    <div id="content" class="div_left">
        <%=poCtrl1.getHtmlCode("PDFCtrl1")%>
    </div>
</form>
</body>
</html>