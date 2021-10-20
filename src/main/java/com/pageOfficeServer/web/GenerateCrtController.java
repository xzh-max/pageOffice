package com.pageOfficeServer.web;

import com.alibaba.fastjson.JSONObject;
import com.pageOfficeServer.mapper.TemplateParamMapper;
import com.pageOfficeServer.model.*;
import com.pageOfficeServer.response.BaseResponse;
import com.pageOfficeServer.service.TemplateParamService;
import com.pageOfficeServer.util.FileUtil;
import com.pageOfficeServer.util.PingYingUtils;
import com.pageOfficeServer.util.SysUtil;
import com.pageOfficeServer.util.wordUtil.DocxUtil;
import com.pageOfficeServer.util.wordUtil.PDFUtil;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PDFCtrl;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import com.zhuozhengsoft.pageoffice.wordwriter.DataRegion;
import com.zhuozhengsoft.pageoffice.wordwriter.DataTag;
import com.zhuozhengsoft.pageoffice.wordwriter.WordDocument;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GenerateCrtController  extends HttpServlet {


    @Resource
    HttpServletRequest request;

    @Resource
    private TemplateParamMapper templateParamMapper;

    @Autowired
    private TemplateParamService templateParamService;

    @RequestMapping("poiGenerateCrtOld")
    @ResponseBody
    public BaseResponse poiGenerateCrt(HttpServletRequest request, HttpServletResponse httpServletResponse,
                                @RequestBody String reqParam){
        String templateNo=request.getParameter("templateNo");
        String contractNo=request.getParameter("contractNo");
        String mouth=request.getParameter("mouth");

        FileOutputStream fileOutputStream=null;

        Map<String,String> params=new HashMap<>();

        TemplateParamModel templateParamModel=templateParamMapper.getTemplateByTemplateNo(templateNo);
        if(templateParamModel==null){
            return BaseResponse.error(("模板编号查不到系统变量"));
        }
        JSONObject req=(JSONObject) JSONObject.parse(reqParam);

        List<TemplateParamEntryModel> templateParamEntryModels=templateParamMapper.getParamEntrys(templateParamModel.getFid());
        for(TemplateParamEntryModel templateParamEntryModel:templateParamEntryModels){
            for(String key:req.keySet()){
                //如果数据库key和入参key相等，将name放入传入的值
                if(templateParamEntryModel.getSyskey()!=null&&templateParamEntryModel.getSyskey().trim()!=null){
                    if(templateParamEntryModel.getSyskey().equals(key)){
                        params.put(templateParamEntryModel.getName(),(String) req.get(key));
                    }
                }
            }
        }

        //获取文件路径
        String rootPath = request.getServletContext().getRealPath("")+"/template/";
        File file=new File(rootPath);

        DocxUtil docxUtil = new DocxUtil();
        //  获取文件
        String path = FileUtil.getFilePath(rootPath+templateNo);
        XWPFDocument document = null;//读取Word模板

        File dir;
        String fileName;
        if(StringUtils.isNotEmpty(mouth)){
            dir= new File(request.getServletContext().getRealPath("")+"/contract/"+mouth+"/");
            fileName=request.getServletContext().getRealPath("")+"/contract/"+mouth+"/"+contractNo;
        }else{
            dir= new File(request.getServletContext().getRealPath("")+"/contract/");
            fileName=request.getServletContext().getRealPath("")+"/contract/"+contractNo;
        }
        if (!dir.exists()) {// 判断文件目录是否存在
            dir.mkdirs();
        }
        try {
            document = new XWPFDocument(POIXMLDocument.openPackage(path));
            docxUtil.searchAndReplace(document, params);//替换模板中的对应变量。
            FileOutputStream os = new FileOutputStream(fileName + ".docx");
            document.write(os);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           if (null != docxUtil) {
                docxUtil.close(fileOutputStream);//关闭流
            }
        }
        PDFUtil.doc2pdf(fileName+".docx",fileName+".pdf");
        String url=RelPath()+"download.do?fileName="+contractNo+".pdf&type=contract";
        return BaseResponse.success((Object) url);
    }

    @RequestMapping("pageOfficeGenerateCrt")
    public ModelAndView pageOfficeGenerateCrt(HttpServletRequest request, HttpServletResponse httpServletResponse){
        String templateNo=request.getParameter("templateNo");
        String contractNo=request.getParameter("contractNo");

        Map<String,String> params=new HashMap<>();

        TemplateParamModel templateParamModel=templateParamMapper.getTemplateByTemplateNo(templateNo);
        if(templateParamModel==null){
            return null;
        }

        List<HandSysInfoModel> handsSysInfos=templateParamMapper.queryHandsSysInfo(contractNo);

        List<TemplateParamEntryModel> templateParamEntryModels=templateParamMapper.getParamEntrys(templateParamModel.getFid());
        for(TemplateParamEntryModel templateParamEntryModel:templateParamEntryModels){
            for(HandSysInfoModel handSysInfoModel:handsSysInfos){
                //如果数据库key和入参key相等，将name放入传入的值
                if("1".equals(templateParamEntryModel.getParamType())
                        &&templateParamEntryModel.getSyskey().equals(handSysInfoModel.getSysKey())){
                    params.put(templateParamEntryModel.getName(),handSysInfoModel.getSysValue());
                }
            }
        }

        //系统变量
        List<String> tags=new ArrayList<>();
        //自定义变量
        List<TemplateParMarkDto> templateParMarkDtos=new ArrayList<>();
        for(TemplateParamEntryModel templateParamEntryModel:templateParamEntryModels){
            p:
            switch (templateParamEntryModel.getParamType()){
                //系统变量
                case "1":
                    tags.add(templateParamEntryModel.getName());
                    //自定义变量
                    break p;
                case "2":
                    //用户还未使用的自定义变量
                    TemplateParMarkDto templateParMarkDto=new TemplateParMarkDto();
                    templateParMarkDto.setName(templateParamEntryModel.getName());
                    templateParMarkDto.setLength(templateParamEntryModel.getLength());
                    templateParMarkDto.setType(templateParamEntryModel.getType());

                    templateParMarkDtos.add(templateParMarkDto);
                    break p;
                //下拉选项
                case "3":
                    //获取下拉选项信息
                    TemplateParMarkDto templateParOptDto=new TemplateParMarkDto();
                    templateParOptDto.setName(templateParamEntryModel.getName());

                    //获取值信息
                    List<TemplateParamOptionModel> templateParamOptionModels=templateParamMapper.getOptions(templateParamEntryModel.getFentryId());
                    if(templateParamOptionModels!=null && templateParamOptionModels.size()!=0){

                        List<String> options=new ArrayList<>();
                        for(TemplateParamOptionModel templateParamOptionModel:templateParamOptionModels){
                            options.add(templateParamOptionModel.getFoptionValue());//添加选项值
                        }
                        templateParOptDto.setOptionValues(options);
                    }
                    templateParMarkDtos.add(templateParOptDto);
                    break p;
            }
        }

        List<ArticleInfoDto> articleInfoDtos = templateParamService.queryAtricleDtoByFid(String.valueOf(templateParamModel.getFid()));

        //定义系统变量，使用dataTag
        WordDocument doc = new WordDocument();
        //定义系统变量，使用dataTag
        if(tags!=null){
            for(String pa:tags){
                for(String key:params.keySet()){
                    if(pa.equals(key)){
                        DataTag tag = doc.openDataTag(pa);
                        tag.setValue(params.get(key));//替换系统变量
                    }
                }
            }
        }

        //定义自定义变量数据区域
        if(templateParMarkDtos!=null && templateParMarkDtos.size()!=0){
            for(TemplateParMarkDto templateParMarkDto:templateParMarkDtos){
                String poName="PO_"+ PingYingUtils.getPinYinHeadChar(templateParMarkDto.getName());
                //添加到model view
                //定义数据区域
                doc.getTemplate().defineDataRegion(poName, templateParMarkDto.getName());
                //打开数据区域
                DataRegion dataRegion =doc.openDataRegion(poName);
                dataRegion.setEditing(true);
                dataRegion.getShading().setBackgroundPatternColor(Color.blue);
            }
        }

        if(articleInfoDtos!=null && articleInfoDtos.size()!=0 ){
            for(ArticleInfoDto articleInfoDto:articleInfoDtos){
                String poName="PO_"+ PingYingUtils.getPinYinHeadChar(articleInfoDto.getName());
                //添加到model view
                articleInfoDto.setMarkName(poName);
                doc.getTemplate().defineDataRegion(poName, articleInfoDto.getName());
                DataRegion dataRegion =doc.openDataRegion(poName);
                dataRegion.setEditing(true);
                dataRegion.getShading().setBackgroundPatternColor(Color.blue);
            }
        }

        //设置表格可编辑
        List<BooKMarkModel> booKMarkModels=templateParamMapper.queryBkByFid(templateParamModel.getFid());
        for(BooKMarkModel booKMarkModel:booKMarkModels){
            DataRegion dataRegion =doc.openDataRegion(booKMarkModel.getBookMark());
            dataRegion.setEditing(true);
        }

        //获取文件路径
        String rootPath = request.getServletContext().getRealPath("")+"/template/";
        String fileName1 = FileUtil.getFileName(rootPath+templateNo);
        if(StringUtils.isBlank(fileName1)){
            return null;
        }

        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);

        poCtrl.setJsFunction_OnWordDataRegionClick("OnWordDataRegionClick()");
        poCtrl.setServerPage(request.getContextPath()+"/poserver.zz");
        poCtrl.setSaveFilePage("saveContractfile.do?contractNo="+contractNo);
        poCtrl.addCustomToolButton("另存为PDF文件", "SaveAsPDF()", 1);
        if(SysUtil.IsLinux()){
            String linuxFilePath="file://"+rootPath+templateNo+"/"+fileName1;
            System.out.print("pageOfficeGenerateCrt.do,编辑模板，模板路径为-------："+linuxFilePath);
            poCtrl.webOpen(linuxFilePath, OpenModeType.docNormalEdit, "zhangsan");
        }else{
            poCtrl.webOpen("template/"+templateNo+"/"+fileName1, OpenModeType.docSubmitForm, "zhangsan");
        }
        poCtrl.setWriter(doc);

        request.setAttribute("poCtrl",poCtrl);

        ModelAndView modelAndView = new ModelAndView("ContractHandGreate");
        modelAndView.addObject("tags",tags);
        modelAndView.addObject("fid",templateParamModel.getFid());
        modelAndView.addObject("templateParMarkDtos",templateParMarkDtos);//自定义变量
        modelAndView.addObject("articleInfoDtos",articleInfoDtos);//条款信息
        return modelAndView;
    }

    @RequestMapping("uploadSysValueInfo")
    @ResponseBody
    @Transactional
    public BaseResponse uploadSysValueInfo(HttpServletRequest request, HttpServletResponse httpServletResponse,
                                           @RequestBody String reqParam){
        String templateNo=request.getParameter("templateNo");
        String contractNo=request.getParameter("contractNo");

        List<HandSysInfoModel> handsSysInfos=templateParamMapper.queryHandsSysInfo(contractNo);

        //如果合同对应已经存在了系统变量key value，则先删除，再保存，保证下次编辑模板取到最新的key，value
        if(handsSysInfos!=null&&handsSysInfos.size()!=0){
            templateParamMapper.deleteHandsSysInfo(handsSysInfos.get(0).getContractNo());
        }

        JSONObject req=(JSONObject) JSONObject.parse(reqParam);
        List<HandSysInfoModel> handSysInfoModels=new ArrayList<>();
        if(req!=null){
            for(String key:req.keySet()){
                HandSysInfoModel handSysInfoModel=new HandSysInfoModel();
                handSysInfoModel.setSysKey(key);
                handSysInfoModel.setSysValue(req.getString(key));
                handSysInfoModel.setContractNo(contractNo);
                handSysInfoModels.add(handSysInfoModel);
            }
        }
        templateParamMapper.saveHandsSysInfo(handSysInfoModels);

        String url=RelPath()+"download.do?fileName="+contractNo+".pdf&type=contract";
        return BaseResponse.success((Object) url);
    }

    /**
     * @return  返回相对路径RelativePath
     */
    public String RelPath() {
        String path = request.getContextPath();
        return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    }

    @RequestMapping("contractPdfRead")
    public ModelAndView contractPdfRead(HttpServletRequest request){
        String contractNo=request.getParameter("contractNo");
        PDFCtrl poCtrl1 = new PDFCtrl(request);
        poCtrl1.setServerPage(request.getContextPath()+"/poserver.zz"); //此行必须
        poCtrl1.addCustomToolButton("打印", "Print()", 6);
        poCtrl1.addCustomToolButton("隐藏/显示书签", "SetBookmarks()", 0);
        poCtrl1.addCustomToolButton("-", "", 0);
        poCtrl1.addCustomToolButton("实际大小", "SetPageReal()", 16);
        poCtrl1.addCustomToolButton("适合页面", "SetPageFit()", 17);
        poCtrl1.addCustomToolButton("适合宽度", "SetPageWidth()", 18);
        poCtrl1.addCustomToolButton("-", "", 0);
        poCtrl1.addCustomToolButton("首页", "FirstPage()", 8);
        poCtrl1.addCustomToolButton("上一页", "PreviousPage()", 9);
        poCtrl1.addCustomToolButton("下一页", "NextPage()", 10);
        poCtrl1.addCustomToolButton("尾页", "LastPage()", 11);
        poCtrl1.addCustomToolButton("-", "", 0);

        String ctxPath = request.getSession().getServletContext().getRealPath("/contract");
        if(SysUtil.IsLinux()){
            String linuxFilePath="file://"+ctxPath+"/"+contractNo+".pdf";
            poCtrl1.webOpen(linuxFilePath);
        }else {
            poCtrl1.webOpen(ctxPath+"/"+contractNo+".pdf");
        }
        ModelAndView modelAndView=new ModelAndView("PdfRead");
        request.setAttribute("poCtrl",poCtrl1);
        return modelAndView;
    }
}
