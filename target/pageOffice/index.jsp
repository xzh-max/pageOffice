<%@ page pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String templateNo= request.getParameter("templateNo");
    String contractNo= request.getParameter("contractNo");
    String quoteTemplateNo = request.getParameter("quoteTemplateNo");

    String fromTemplateNo= request.getParameter("fromTemplateNo");
    String toTemplateNo= request.getParameter("toTemplateNo");
    String operationType = request.getParameter("operationType");
    String fileName=request.getParameter("fileName");
    String fileId=request.getParameter("fileId");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	 <!--PageOffice.js和jquery.min.js文件一定要引用-->
      <script type="text/javascript" src="js/jquery.min.js"></script>
      <script type="text/javascript" src="pageoffice.js" id="po_js_main"></script>
      <script type="text/javascript">
          document.onreadystatechange = function () {
              var templateNo=document.getElementById("templateNo").value;
              var toTemplateNo=document.getElementById("toTemplateNo").value;
              var fromTemplateNo=document.getElementById("fromTemplateNo").value;
              var quoteTemplateNo=document.getElementById("quoteTemplateNo").value;
              var operationType=document.getElementById("operationType").value;
              var contractNo=document.getElementById("contractNo").value;
              var fileName=document.getElementById("fileName").value;
              var fileId=document.getElementById("fileId").value;
              setTimeout(function () {
                  if (document.readyState == "complete") {
                      //编辑模板
                      if (operationType == "templateEdit") {
                          POBrowser.openWindowModeless("templateEdit.do?templateNo=" + templateNo, "frame=yes;resizable=yes;fullscreen=yes");
                      }
                      if (operationType == "templateRead") {
                          POBrowser.openWindowModeless("readOnly.do?templateNo=" + templateNo, "frame=yes;resizable=yes;fullscreen=yes");
                          // POBrowser.openWindowModeless("readOnly.do?templateNo="+templateNo,"width=auto;height=auto;position=relative");
                      }
                      if (operationType == "quoteTemplateEdit") {
                          POBrowser.openWindowModeless('quotetemplateEdit.do?quoteTemplateNo=' + quoteTemplateNo + '&templateNo=' + templateNo, "resizable=yes;fullscreen=yes");
                      }
                      if (operationType == "pageOfficeGenerateCrt") {
                          POBrowser.openWindowModeless("pageOfficeGenerateCrt.do?templateNo=" + templateNo + "&contractNo=" + contractNo, "width=auto;height=auto;position=relative");
                      }
                      if (operationType == "compareTemplate") {
                          POBrowser.openWindowModeless("compareTemplate.do?toTemplateNo=" + toTemplateNo + "&fromTemplateNo=" + fromTemplateNo, "width=auto;height=auto;position=relative");
                      }
                      if (operationType == "contractPdfRead") {
                          POBrowser.openWindowModeless("contractPdfRead.do?contractNo=" + contractNo, "width=auto;height=auto;position=relative");
                      }
                      if (operationType == "readContractDoc") {
                          POBrowser.openWindowModeless("readContractDoc.do?fileId=" + fileId, "width=auto;height=auto;position=relative");
                      }
                      if (operationType == "docRevisionOnly") {
                          POBrowser.openWindowModeless("docRevisionOnly.do?templateNo=" + templateNo, "frame=yes;resizable=yes;fullscreen=yes");
                      }
                  }
              },1000)
          }


          function closeWP() {
              var Browser = navigator.appName;
              var indexB = Browser.indexOf('Explorer');

              if (indexB > 0) {
                  var indexV = navigator.userAgent.indexOf('MSIE') + 5;
                  var Version = navigator.userAgent.substring(indexV, indexV + 1);

                  if (Version >= 7) {
                      window.open('', '_self', '');
                      window.close();
                  }
                  else if (Version == 6) {
                      window.opener = null;
                      window.close();
                  }
                  else {
                      window.opener = '';
                      window.close();
                  }

              }
              else {
                  window.close();
              }
          }

      </script>
  </head>
  
  <body>
      <input id="templateNo"  value="<%=templateNo%>" type="hidden">
      <input id="toTemplateNo"  value="<%=toTemplateNo%>" type="hidden">
      <input id="fromTemplateNo"  value="<%=fromTemplateNo%>" type="hidden">
      <input id="quoteTemplateNo"  value="<%=quoteTemplateNo%>" type="hidden">
      <input id="operationType" value="<%=operationType%>" type="hidden">
      <input id="contractNo" value="<%=contractNo%>" type="hidden">
      <input id="fileName" value="<%=fileName%>" type="hidden">
      <input id="fileId" value="<%=fileId%>" type="hidden">
  </body>

</html>
