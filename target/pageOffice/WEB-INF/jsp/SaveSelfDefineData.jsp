<%--保存自定义变量--%>

<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@ page import="com.zhuozhengsoft.pageoffice.*,com.zhuozhengsoft.pageoffice.wordreader.*,java.awt.*,javax.servlet.*,javax.servlet.http.*,java.sql.*,java.text.SimpleDateFormat,java.util.Date"%>
<%@ page import="com.pageOfficeServer.model.TemplateParamMarkInfoModel" %>

<%
	Class.forName("org.sqlite.JDBC");
	    				String strUrl = "jdbc:sqlite:"
								+ this.getServletContext().getRealPath("demodata/") + "\\demo_poword.db";
	Connection conn = DriverManager.getConnection(strUrl);
	Statement stmt = conn.createStatement();
	String id = request.getParameter("ID");
	String ErrorMsg = "";
	String BaseUrl = "";
	//-----------  PageOffice 服务器端编程开始  -------------------//
	WordDocument doc = new WordDocument(request, response);
	String sName = doc.openDataRegion("PO_name").getValue();
	String sDept = doc.openDataRegion("PO_dept").getValue();
	String sCause = doc.openDataRegion("PO_cause").getValue();
	String sNum = doc.openDataRegion("PO_num").getValue();
	String sDate = doc.openDataRegion("PO_date").getValue();

	if (sName.equals("")) {
		ErrorMsg = ErrorMsg + "<li>申请人</li>";
	}
	if (sDept.equals("")) {
		ErrorMsg = ErrorMsg + "<li>部门名称</li>";
	}
	if (sCause.equals("")) {
		ErrorMsg = ErrorMsg + "<li>请假原因</li>";
	}
	if (sDate.equals("")) {
		ErrorMsg = ErrorMsg + "<li>日期</li>";
	}
	try {
		if (sNum != "") {
			if (Integer.parseInt(sNum) < 0) {
				ErrorMsg = ErrorMsg + "<li>请假天数不能是负数</li>";
			}
		} else {
			ErrorMsg = ErrorMsg + "<li>请假天数</li>";
		}
	} catch (Exception Ex) {
		ErrorMsg = ErrorMsg	+ "<li><font color=red>注意：</font>请假天数必须是数字</li>";
	}

	if (ErrorMsg == "") {
		String strsql = "update leaveRecord set Name='" + sName
				+ "', Dept='" + sDept + "', Cause='" + sCause
				+ "', Num=" + sNum + ", SubmitTime='" + sDate
				+ "' where  ID=" + id;
		stmt.executeUpdate(strsql);
	} else {
		doc.showPage(578, 380);
	}
	doc.close();
	stmt.close();
	conn.close();
	//-----------  PageOffice 服务器端编程结束  -------------------//
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
	<HEAD>
		<title>SaveData</title>
		<meta content="Microsoft Visual Studio .NET 7.1" name="GENERATOR">
		<meta content="C#" name="CODE_LANGUAGE">
		<meta content="JavaScript" name="vs_defaultClientScript">
		<meta content="http://schemas.microsoft.com/intellisense/ie5" name="vs_targetSchema">

	</HEAD>
	<body>
		<div class="errMainArea" id="error163"><div class="errTopArea" style="TEXT-ALIGN:left">[提示标题：这是一个开发人员可自定义的对话框]</div>
			<div class="errTxtArea" style="HEIGHT:150px; TEXT-ALIGN:left">
				<b class="txt_title">
					<div style="color:#FF0000;">请填写以下信息：</div>
					<ul>
					<%=ErrorMsg%>
					</ul>
					
				</b>
				
			</div>
			<div class="errBtmArea"><input type="button" class="btnFn" value=" 关闭 " onClick="window.opener=null;window.close();"></div>
		</div>
	</body>
</HTML>

