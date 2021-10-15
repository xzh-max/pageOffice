package com.pageOfficeServer.web;

import com.pageOfficeServer.mapper.TemplateParamMapper;
import com.pageOfficeServer.util.FileUtil;
import com.pageOfficeServer.util.SysUtil;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CompareWord extends HttpServlet {

    @Autowired
    private TemplateParamMapper templateParamMapper;

    private static final long serialVersionUID = -758686623642845302L;
    @RequestMapping("compareTemplate")
    public String compareWord(HttpServletRequest request, HttpServletResponse response){
        String toTemplateNo = request.getParameter("toTemplateNo");
        String fromTemplateNo = request.getParameter("fromTemplateNo");
        if(templateParamMapper.getTemplateByTemplateNo(toTemplateNo)==null||
                templateParamMapper.getTemplateByTemplateNo(fromTemplateNo)==null){
            return null;
        }

        String topath = request.getServletContext().getRealPath("")+"/template/"+toTemplateNo;
        String toFileName = FileUtil.getFileName(topath);

        String frompath = request.getServletContext().getRealPath("")+"/template/"+fromTemplateNo;
        String fromFileName = FileUtil.getFileName(frompath);

        if(StringUtils.isBlank(toFileName)||StringUtils.isBlank(fromFileName)){
            return null;
        }

        PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
        poCtrl1.setServerPage(request.getContextPath()+"/poserver.zz");

        // Create custom toolbar
//        poCtrl1.addCustomToolButton("保存", "SaveDocument()", 1);
        poCtrl1.addCustomToolButton("显示"+toTemplateNo+"模板", "ShowFile1View()", 0);
        poCtrl1.addCustomToolButton("显示"+fromTemplateNo+"模板", "ShowFile2View()", 0);
        poCtrl1.addCustomToolButton("显示比较结果", "ShowCompareView()", 0);

        if(SysUtil.IsLinux()){
            String rootPath = request.getServletContext().getRealPath("")+"/template/";
            String toPath="file://"+rootPath+toTemplateNo+"/"+toFileName;
            String fromPath="file://"+rootPath+fromTemplateNo+"/"+fromFileName;
            poCtrl1.wordCompare(toPath,fromPath,OpenModeType.docAdmin,"差异");
        }else {
            poCtrl1.wordCompare("template/"+toTemplateNo+"/"+toFileName,
                    "template/"+fromTemplateNo+"/"+fromFileName,
                    OpenModeType.docAdmin, "差异");
        }


        request.setAttribute("poCtrl",poCtrl1);
        return "Compare";
    }
}
