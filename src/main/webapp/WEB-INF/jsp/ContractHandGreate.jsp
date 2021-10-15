<%@ page language="java"
         import="java.util.*,com.zhuozhengsoft.pageoffice.*,com.zhuozhengsoft.pageoffice.wordwriter.*"
         pageEncoding="gb2312"%>
<%@ page import="java.sql.*" %>

<%
    PageOfficeCtrl poCtrl = (PageOfficeCtrl)request.getAttribute("poCtrl");
    poCtrl.setJsFunction_AfterDocumentOpened("AfterDocumentOpened");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title></title>
    <!-- ǿ��Chromium�ںˣ�������360�������QQ������ȹ���˫������� -->
    <meta name="renderer" content="webkit"/>
    <!-- ǿ��Chromium�ںˣ�����������˫������� -->
    <meta name="force-rendering" content="webkit"/>
    <!-- ����а�װ Google Chrome Frame �����ǿ��ΪChromium�ںˣ�����ǿ�Ʊ���֧�ֵ���߰汾IE�ںˣ�������IE����� -->
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"/>
    <script type="text/javascript" src="jquery.min.js"></script>
    <script src="../js/json2.js"></script>
    <script src="/js/selectivizr.js"></script>
    <script type="text/javascript">
        var fid = localStorage.getItem("fid");
        var datas;

        //��ֵ���
        function getHeight() {
            var hi = $(document.body).height();
            $('.div_left').height(hi);
        }

        function AfterDocumentOpened() {
            document.getElementById("PageOfficeCtrl1").FullScreen = true;// ȫ��
        }

        window.onload =function () {
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

        function Save2() {
            document.getElementById("PageOfficeCtrl1").Alert("��Ч��ֻ����ʾ�༭ģʽ�����ļ���û�������湦�ܡ�");
        }
        function Save() {
            document.getElementById("PageOfficeCtrl1").WebSave();
        }

        //���ΪPDF�ļ�
        function SaveAsPDF() {
            document.getElementById("PageOfficeCtrl1").WebSaveAsPDF();
            window.external.close();//�رյ�ǰPOBrower�����Ĵ���
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
                        if(datas.templateParMarkDtos[i].type =="����"){
                            stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModalDialog("html/datetimer-agains.htm", Value, "left=" + Left + "px;width="+wi+";height="+hi+"px;frame:no;Caption=��ѡ������;");
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
                        if(datas.templateParMarkDtos[i].type =="����ѡ��"){
                            var s = JSON.stringify(datas);
                            localStorage.setItem("data",s);
                            stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModalDialog("html/selectOption-agains.htm", Value, "left=" + Left + "px;width="+wi+";height="+hi+"px;frame:no;Caption=ѡ��������;");
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
                        if(datas.templateParMarkDtos[i].type =="����") {
                            stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModalDialog("html/ParamInputPage-agains.htm", Value, "left=" + Left + "px;width="+wi+";height="+hi+"px;frame:no;Caption=��������;");
                            if (stropenclause != "") {
                                return (stropenclause);
                            } else {
                                if ((Value == undefined) || (Value == ""))
                                    return " ";
                                else
                                    return Value;
                            }
                        }else
                        if(datas.templateParMarkDtos[i].type == "����"){
                            stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModalDialog("html/ParamInputPageNumber-agains.htm", Value, "left=" + Left + "px;width="+wi+";height="+hi+"px;frame:no;Caption=��������;");
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
                        stropenclause = document.getElementById("PageOfficeCtrl1").ShowHtmlModalDialog("html/OptionClause-Againsx.html", Name, "left=" + Left + "px;width="+wi+";height="+hi+";frame:no;Caption=ѡ������;");
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
<body>
<form action="">
    <div style="width: 1000px; height: 800px;">
        <%=poCtrl.getHtmlCode("PageOfficeCtrl1")%>
    </div>
</form>
</body>
</html>