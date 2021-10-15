<%@ page language="java"
         import="java.util.*,com.zhuozhengsoft.pageoffice.*"
         pageEncoding="utf-8"%>
<%@ page import="com.pageOfficeServer.util.FileUtil" %>
<%@ page import="com.pageOfficeServer.util.SysUtil" %>
<%
    PageOfficeCtrl poCtrl = (PageOfficeCtrl)request.getAttribute("poCtrl");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>痕迹编辑</title>
    <!-- 强制Chromium内核，作用于360浏览器、QQ浏览器等国产双核浏览器 -->
    <meta name="renderer" content="webkit"/>
    <!-- 强制Chromium内核，作用于其他双核浏览器 -->
    <meta name="force-rendering" content="webkit"/>
    <!-- 如果有安装 Google Chrome Frame 插件则强制为Chromium内核，否则强制本机支持的最高版本IE内核，作用于IE浏览器 -->
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"/>
</head>

<body>
<script type="text/javascript" src="jquery.min.js"></script>
<script src="../js/json2.js"></script>
<script src="/js/selectivizr.js"></script>
<script type="text/javascript">

    function SaveAndExit() {
        document.getElementById("PageOfficeCtrl1").WebSave();
        window.external.close();
    }

    //痕迹显隐
    function Show_HidRevisions() {
        document.getElementById("PageOfficeCtrl1").ShowRevisions = !document.getElementById("PageOfficeCtrl1").ShowRevisions;
    }

    function AfterDocumentOpened() {
        refreshList();
    }

    //获取当前痕迹列表
    function refreshList() {
        var i;
        document.getElementById("ul_Comments").innerHTML = "";
        for (i = 1; i <= document.getElementById("PageOfficeCtrl1").Document.Revisions.Count; i++) {
            var str = "";
            str = str + document.getElementById("PageOfficeCtrl1").Document.Revisions.Item(i).Author;
            var  revisionDate=document.getElementById("PageOfficeCtrl1").Document.Revisions.Item(i).Date;
            //转换为标准时间
            str=str+" "+dateFormat(revisionDate,"yyyy-MM-dd HH:mm:ss");

            if (document.getElementById("PageOfficeCtrl1").Document.Revisions.Item(i).Type == "1") {
                str = str + ' 插入：' + document.getElementById("PageOfficeCtrl1").Document.Revisions.Item(i).Range.Text;
            }
            else if (document.getElementById("PageOfficeCtrl1").Document.Revisions.Item(i).Type == "2") {
                str = str + ' 删除：' + document.getElementById("PageOfficeCtrl1").Document.Revisions.Item(i).Range.Text;
            }
            else {
                str = str + ' 调整格式或样式。';
            }
            document.getElementById("ul_Comments").innerHTML += "<li><a href='#' onclick='goToRevision(" + i + ")'>" + str + "</a></li>"
        }

    }
    //GMT时间格式转换为CST
    dateFormat = function (date, format) {
        date = new Date(date);
        var o = {
            'M+' : date.getMonth() + 1, //month
            'd+' : date.getDate(), //day
            'H+' : date.getHours(), //hour
            'm+' : date.getMinutes(), //minute
            's+' : date.getSeconds(), //second
            'q+' : Math.floor((date.getMonth() + 3) / 3), //quarter
            'S' : date.getMilliseconds() //millisecond
        };

        if (/(y+)/.test(format))
            format = format.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));

        for (var k in o)
            if (new RegExp('(' + k + ')').test(format))
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length));

        return format;
    }

    //定位到当前痕迹
    function goToRevision(index)
    {
        document.getElementById("PageOfficeCtrl1").Document.Revisions.Item(index).Range.Select();
    }

    //刷新列表
    function refresh_click()
    {
        refreshList();
    }

    //接受所有修订
    function AcceptAllRevisions() {
        document.getElementById("PageOfficeCtrl1").AcceptAllRevisions();
        refreshList()
    }

</script>
<div  style=" width:1300px; height:700px;">
    <div style=" width:1050px; height:700px; float:left;">
        <%=poCtrl.getHtmlCode("PageOfficeCtrl1")%>
    </div>
    <div id="Div_Comments" style=" float:right; width:200px; height:700px; border:solid 1px red;">
        <h3>痕迹列表</h3>
        <input type="button" name="refresh" value="刷新"onclick=" return refresh_click()"/>
        <ul id="ul_Comments">

        </ul>
    </div>
</div>
</body>
</html>

