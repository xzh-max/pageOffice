<%@ page language="java"
	import="java.util.*,com.zhuozhengsoft.pageoffice.FileMakerCtrl,com.zhuozhengsoft.pageoffice.*,com.zhuozhengsoft.pageoffice.wordwriter.*"
	pageEncoding="utf-8"%>
<%
	FileMakerCtrl fmCtrl = new FileMakerCtrl(request);
	fmCtrl.setServerPage(request.getContextPath()+"/poserver.zz");
	WordDocument doc = new WordDocument();
	//禁用右击事件
	doc.setDisableWindowRightClick(true);
	//给数据区域赋值，即把数据填充到模板中相应的位置
	doc.openDataRegion("PO_company").setValue("北京卓正志远软件有限公司  ");
	doc.openDataRegion("PO_age").setValue("100");
	fmCtrl.setSaveFilePage("savepdf.do");
	fmCtrl.setWriter(doc);
	fmCtrl.setJsFunction_OnProgressComplete("OnProgressComplete()");
	fmCtrl.fillDocumentAsPDF("doc/aaa1.doc", DocumentOpenType.Word, "a.pdf");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>My JSP 'FileMaker.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		
	</script>
	</head>

	<body>
		<div>
			<!--**************   卓正 PageOffice 客户端代码结束    ************************-->
			<%=fmCtrl.getHtmlCode("FileMakerCtrl1")%>

		</div>

	</body>
</html>
