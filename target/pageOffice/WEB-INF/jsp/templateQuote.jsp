<%@ page
         import="com.zhuozhengsoft.pageoffice.*"
         pageEncoding="utf-8"%>
<%
    PageOfficeCtrl poCtrl = (PageOfficeCtrl)request.getAttribute("poCtrl");
    int parentFid=(int) request.getAttribute("parentFid");
    poCtrl.setJsFunction_AfterDocumentOpened("AfterDocumentOpened");
    System.out.print(parentFid);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title></title>
    <!-- 强制Chromium内核，作用于360浏览器、QQ浏览器等国产双核浏览器 -->
    <meta name="renderer" content="webkit"/>
    <!-- 强制Chromium内核，作用于其他双核浏览器 -->
    <meta name="force-rendering" content="webkit"/>
    <!-- 如果有安装 Google Chrome Frame 插件则强制为Chromium内核，否则强制本机支持的最高版本IE内核，作用于IE浏览器 -->
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"/>
    <script type="text/javascript" src="jquery.min.js"></script>
    <script src="../js/json2.js"></script>
    <script src="/js/selectivizr.js"></script>
    <script type="text/javascript">

        var datas;

        //设值宽高
        function getHeight() {
            var hi = $(document.body).height();
            $('.div_left').height(hi);
        }

        function AfterDocumentOpened() {
            document.getElementById("PageOfficeCtrl1").FullScreen = true;// 全屏
        }

        window.onload =function () {
            document.getElementById("PageOfficeCtrl1").SaveAsReadOnly = false;
            var parentFid =document.getElementById("parentFid").value;
            getHeight();
            localStorage.setItem("parentFid",parentFid)
            $.ajax({
                url: "templateGet.do?fid=" + parentFid,
                type: "POST",
                contentType: "application/json",
                success: function (data) {
                    datas = data;
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            })
        };

        function Save2() {
            document.getElementById("PageOfficeCtrl1").Alert("此效果只是演示编辑模式生成文件，没有做保存功能。");
        }
        function Save() {
            document.getElementById("PageOfficeCtrl1").WebSave();
        }

        function SaveAndExit() {
            document.getElementById("PageOfficeCtrl1").WebSave();
            window.external.close();//关闭POBrowser窗口
        }


        function revertBlack() {
            var mac = "sub myfunc() ActiveDocument.Range.HighlightColorIndex = wdNoHighlight end sub";
            document.getElementById("PageOfficeCtrl1").RunMacro("myfunc", mac);
        }

        //另存为PDF文件
        function SaveAsPDF() {
            document.getElementById("PageOfficeCtrl1").WebSaveAsPDF();
            window.external.close();//关闭当前POBrower弹出的窗口
        }

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
<body  style="height: 100vh;">
<form id="form2" >
    <div class="div_left" >
        <%=poCtrl.getHtmlCode("PageOfficeCtrl1")%>
    </div>
</form>
<input type="hidden" id="parentFid" value="<%=parentFid%>">
</body>
</html>