package com.pageOfficeServer.web;

import com.pageOfficeServer.mapper.TemplateParamMapper;
import com.pageOfficeServer.model.*;
import com.pageOfficeServer.service.TemplateParamService;
import com.pageOfficeServer.util.FileUtil;
import com.pageOfficeServer.util.SysUtil;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import com.zhuozhengsoft.pageoffice.wordwriter.DataRegion;
import com.zhuozhengsoft.pageoffice.wordwriter.WordDocument;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ReadOnlyController {

    @Autowired
    private TemplateParamMapper templateParamMapper;

    @Autowired
    private TemplateParamService templateParamService;

    @RequestMapping("readOnly")
    public ModelAndView readOnly(HttpServletRequest request, HttpServletResponse response){
        String templateNo=request.getParameter("templateNo");

        TemplateParamModel templateParamModel=templateParamMapper.getTemplateByTemplateNo(templateNo);
        if(templateParamModel==null){
            return null;
        }
        String rootPath = request.getServletContext().getRealPath("")+"/template/";
        String fileName1 = FileUtil.getFileName(rootPath+templateNo);
        if(StringUtils.isBlank(fileName1)){
            return null;
        }

        //系统变量
        List<String> tags=new ArrayList<>();
        //自定义变量
        List<TemplateParamEntryModel> templateParamEntryModels=templateParamMapper.getParamEntrys(templateParamModel.getFid());

        //自定义变量
        List<TemplateParMarkDto> templateParMarkDtos=new ArrayList<>();
        for(TemplateParamEntryModel templateParamEntryModel:templateParamEntryModels){
            p:
            switch (templateParamEntryModel.getParamType()){
                case "2":
                    //用户还未使用的自定义变量
                    TemplateParMarkDto templateParMarkDto=new TemplateParMarkDto();
                    templateParMarkDto.setName(templateParamEntryModel.getName());
                    templateParMarkDto.setFentryId(templateParamEntryModel.getFentryId());
                    templateParMarkDtos.add(templateParMarkDto);
                    break p;
                //下拉选项
                case "3":
                    //获取下拉选项信息
                    TemplateParMarkDto templateParOptionDto=new TemplateParMarkDto();
                    templateParOptionDto.setName(templateParamEntryModel.getName());
                    templateParOptionDto.setFentryId(templateParamEntryModel.getFentryId());
                    templateParMarkDtos.add(templateParOptionDto);
                    break p;
            }
        }
        List<ArticleInfoDto> articleInfoDtos = templateParamService.queryAtricleDtoByFid(String.valueOf(templateParamModel.getFid()));
        ModelAndView modelAndView = new ModelAndView("readOnly");

        //定义系统变量，使用dataTag
        WordDocument doc = new WordDocument();

        //定义自定义变量数据区域
        if(templateParMarkDtos!=null && templateParMarkDtos.size()!=0){
            for(TemplateParMarkDto templateParMarkDto:templateParMarkDtos){
                List<String> bookMarks=templateParamMapper.queryParamBookMarkBy("1",templateParMarkDto.getFentryId());
                for(String bookMark:bookMarks){
//					doc.getTemplate().defineDataRegion(bookMark, templateParMarkDto.getName());
                    DataRegion dataRegion =doc.openDataRegion(bookMark);
                    dataRegion.setEditing(true);
                    dataRegion.getShading().setBackgroundPatternColor(Color.yellow);
                }
                templateParMarkDto.setBookMarks(bookMarks);
            }
        }

        if(articleInfoDtos!=null && articleInfoDtos.size()!=0 ){
            for(ArticleInfoDto articleInfoDto:articleInfoDtos){
                List<String> bookMarks=templateParamMapper.queryParamBookMarkBy("2",articleInfoDto.getFarticleId());
                for(String bookMark:bookMarks){
                    DataRegion dataRegion =doc.openDataRegion(bookMark);
                    dataRegion.setEditing(true);
                    dataRegion.getShading().setBackgroundPatternColor(Color.YELLOW);
                }
                articleInfoDto.setBookMarks(bookMarks);
            }
        }

        //设置表格可编辑
        List<BooKMarkModel> booKMarkModels=templateParamMapper.queryBkByFid(templateParamModel.getFid());
        for(BooKMarkModel booKMarkModel:booKMarkModels){
            DataRegion dataRegion =doc.openDataRegion(booKMarkModel.getBookMark());
            dataRegion.setEditing(true);
            dataRegion.getShading().setBackgroundPatternColor(Color.YELLOW);
        }


        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);

        poCtrl.setServerPage(request.getContextPath()+"/poserver.zz");
        if(SysUtil.IsLinux()){
            String linuxFilePath="file://"+rootPath+templateNo+"/"+fileName1;
            System.out.print("readOnly.do,编辑模板，模板路径为-------："+linuxFilePath);
            poCtrl.webOpen(linuxFilePath, OpenModeType.docSubmitForm, "zhangsan");
        }else{
            poCtrl.webOpen("template/"+templateNo+"/"+fileName1, OpenModeType.docSubmitForm, "zhangsan");
        }
        poCtrl.setMenubar(false); //隐藏菜单栏
        poCtrl.setOfficeToolbars(false);//隐藏Office工具条
        poCtrl.setCustomToolbar(false);//隐藏自定义工具栏
        poCtrl.setWriter(doc);
        poCtrl.setJsFunction_OnWordDataRegionClick("OnWordDataRegionClick()");
        modelAndView.addObject("fid",templateParamModel.getFid());
        modelAndView.addObject("poCtrl",poCtrl);
        return modelAndView;
    }

    @RequestMapping("readContractDoc")
    public ModelAndView readContractDoc(HttpServletRequest request, HttpServletResponse response){

        //获取文件路径，防止下载任意文件
        String fileId=request.getParameter("fileId");
        String filePath=templateParamService.getTemplateFileById(fileId);
        if(filePath==null){
            return null;
        }
        String fileName=filePath.split("&type=")[0].split("=")[1];
        String mouth = null;
        if(filePath.split("&").length>2){
            mouth=filePath.split("&mouth=")[1];
        }

        ModelAndView modelAndView = new ModelAndView("readContract");

        String rootPath;
        if(StringUtils.isNotEmpty(mouth)){
            rootPath = request.getServletContext().getRealPath("")+"/contract/"+mouth;
        }else{
            rootPath = request.getServletContext().getRealPath("")+"/contract/";
        }
        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);

        poCtrl.setServerPage(request.getContextPath()+"/poserver.zz");
        if(SysUtil.IsLinux()){
            String linuxFilePath="file://"+rootPath+"/"+fileName;
            System.out.print("readOnly.do,编辑模板，模板路径为-------："+linuxFilePath);
            poCtrl.webOpen(linuxFilePath, OpenModeType.docReadOnly, "zhangsan");
        }else{
            poCtrl.webOpen("contract/"+fileName, OpenModeType.docReadOnly, "zhangsan");
        }
        request.setAttribute("poCtrl",poCtrl);
        return modelAndView;
    }
}
