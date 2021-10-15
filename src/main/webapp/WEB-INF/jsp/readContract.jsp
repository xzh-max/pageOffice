<%@ page language="java"
         import="java.util.*,com.zhuozhengsoft.pageoffice.*,java.sql.*,java.io.*,javax.servlet.*,javax.servlet.http.*"
         pageEncoding="utf-8"%>
<%
    PageOfficeCtrl poCtrl =(PageOfficeCtrl)request.getAttribute("poCtrl");
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

        window.onload=function (ev) {
            getHeight();
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
</body>
</html>
