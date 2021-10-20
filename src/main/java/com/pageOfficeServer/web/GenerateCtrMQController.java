package com.pageOfficeServer.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pageOfficeServer.mapper.TemplateParamMapper;
import com.pageOfficeServer.model.TemplateParamModel;
import com.pageOfficeServer.response.BaseResponse;
import com.pageOfficeServer.service.TemplateParamService;
import com.pageOfficeServer.util.FileUtil;
import com.pageOfficeServer.util.wordUtil.PDFUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Controller
public class GenerateCtrMQController {

    @Resource
    HttpServletRequest request;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Resource
    private TemplateParamMapper templateParamMapper;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private TemplateParamService templateParamService;

    @RequestMapping("/poiGenerateCrt")
    @ResponseBody
    public BaseResponse sendAmqbMsg(HttpServletRequest request, @RequestBody String reqParam){

        String templateNo=request.getParameter("templateNo");
        String contractNo=request.getParameter("contractNo");
        String subOrAudit=request.getParameter("subOrAudit");//'0'代表提交,'1'代表审核

        if(StringUtils.isBlank(contractNo)){
            return BaseResponse.error("合同编号不能为空");
        }

        TemplateParamModel templateParamModel= templateParamMapper.getTemplateByTemplateNo(templateNo);
        if(templateParamModel==null){
            return BaseResponse.error("模板编号查不到系统变量");
        }

        //获取文件路径
        String rootPath =request.getServletContext().getRealPath("")+"/template/";
        //  获取文件
        String path = FileUtil.getFilePath(rootPath+templateNo);
        if(StringUtils.isBlank(path)){
                return BaseResponse.error("模板编号查不到对word模板");
        }

        Calendar cal = Calendar.getInstance();
        JSONObject req=(JSONObject) JSONObject.parse(reqParam);
        req.put("templateNo",templateNo);
        req.put("contractNo", contractNo);
        req.put("subOrAudit",subOrAudit);
        req.put("mouth",cal.get(Calendar.MONTH+1));

        try {
//            rabbitTemplate.
            rabbitTemplate.convertAndSend("exchange","miaosha.tag.key", JSON.toJSONStringWithDateFormat(req,"yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat));
        }catch (Exception e){
            return BaseResponse.error("Mq发送生产者失败");
        }

        String[] fileType=path.split("\\."+"doc");
        String url="";
        if(fileType.length==2){
            //模板如果为docx，生成的合同为docx
//            url=RelPath()+"download.do?fileName="+contractNo+".docx&type=contract";
            String filePath="fileName="+contractNo+".docx&type=contract"+"&mouth="+cal.get(Calendar.MONTH+1);
            String fileId=FileUtil.getCharAndNumr(20);
            templateParamService.addTemplateFile(fileId,filePath);
            url=RelPath()+"download.do?fileId="+ fileId;
        }
        if(fileType.length==1){
            //模板如果为doc，生成的合同为doc
            String filePath="fileName="+contractNo+".doc&type=contract"+"&mouth="+cal.get(Calendar.MONTH+1);
            String fileId=FileUtil.getCharAndNumr(20);
            templateParamService.addTemplateFile(fileId,filePath);
            url=RelPath()+"download.do?fileId="+ fileId;
        }

        return BaseResponse.success((Object) url);
    }

    @RequestMapping(value="/poiGenerateCrtDrug", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public BaseResponse poiGenerateCrtDrug(HttpServletRequest request, @RequestBody String reqParam){

        String templateNo=request.getParameter("templateNo");
        String contractNo=request.getParameter("contractNo");
        String subOrAudit=request.getParameter("subOrAudit");//'0'代表提交,'1'代表审核

        if(StringUtils.isBlank(contractNo)){
            return BaseResponse.error("合同编号不能为空");
        }

        TemplateParamModel templateParamModel=templateParamMapper.getTemplateByTemplateNo(templateNo);
        if(templateParamModel==null){
            return BaseResponse.error("模板编号查不到系统变量");
        }

        //获取文件路径
        String rootPath =request.getServletContext().getRealPath("")+"/template/";
        //  获取文件
        String path = FileUtil.getFilePath(rootPath+templateNo);
        if(StringUtils.isBlank(path)){
            return BaseResponse.error("模板编号查不到对word模板");
        }

        System.out.print("请求参数"+reqParam);
        Calendar cal = Calendar.getInstance();
        JSONObject req=(JSONObject) JSONObject.parse(reqParam);
        req.put("templateNo",templateNo);
        req.put("contractno",contractNo);
        req.put("subOrAudit",subOrAudit);
        req.put("mouth",cal.get(Calendar.MONTH+1));

        try {
            rabbitTemplate.convertAndSend("exchange","drug.key", JSON.toJSONStringWithDateFormat(req,"yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat));
        }catch (Exception e){
            e.printStackTrace();
            return BaseResponse.error("Mq发送生产者失败");
        }

        String[] fileType=path.split("\\."+"doc");
        String url="";
        if(fileType.length==2){
            //模板如果为docx，生成的合同为docx
//            url=RelPath()+"download.do?fileName="+contractNo+".docx&type=contract";
            String filePath="fileName="+contractNo+".docx&type=contract"+"&mouth="+cal.get(Calendar.MONTH+1);
            String fileId=FileUtil.getCharAndNumr(20);
            templateParamService.addTemplateFile(fileId,filePath);
            url=RelPath()+"download.do?fileId="+ fileId;
        }
        if(fileType.length==1){
            //模板如果为doc，生成的合同为doc
//            url=RelPath()+"download.do?fileName="+contractNo+".doc&type=contract";
            String filePath="fileName="+contractNo+".doc&type=contract"+"&mouth="+cal.get(Calendar.MONTH+1);
            String fileId=FileUtil.getCharAndNumr(20);
            templateParamService.addTemplateFile(fileId,filePath);
            url=RelPath()+"download.do?fileId="+ fileId;
        }

        return BaseResponse.success((Object) url);
    }

    @RequestMapping("/deleteContractWord")
    @ResponseBody
    public BaseResponse deleteContractWord(HttpServletRequest request){
        //获取文件路径，防止下载任意文件
        String fileId=request.getParameter("fileId");
        String filePath=templateParamService.getTemplateFileById(fileId);
        String fileName=filePath.split("&type=")[0].split("=")[1];
        String mouth = null;
        if(filePath.split("=").length>2){
            mouth=filePath.split("&mouth=")[1];
        }
        //获取文件路径
        String rootPath;
        if(StringUtils.isNotEmpty(mouth)){
            rootPath =request.getServletContext().getRealPath("")+"/contract/"+mouth+"/";
        }else{
            rootPath =request.getServletContext().getRealPath("")+"/contract/";
        }
        //  获取文件
        if(FileUtil.deleteFileByNo(fileName,rootPath)){
            return BaseResponse.success("删除成功");
        }

        return BaseResponse.error("删除失败");
    }

    @RequestMapping("/wordToPdf")
    @ResponseBody
    public BaseResponse word2Pdf(HttpServletRequest request){
        String fileName=request.getParameter("fileName");
        String contractNo=request.getParameter("contractNo");
        String mouth=request.getParameter("mouth");
        //获取文件路径
        String rootPath;
        if(StringUtils.isNotEmpty(mouth)){
            rootPath =request.getServletContext().getRealPath("")+"/contract/"+mouth+"/";
        }else{
            rootPath =request.getServletContext().getRealPath("")+"/contract/";
        }
        //  获取文件
        if(!FileUtil.fileExit(fileName,rootPath)){
            return BaseResponse.error("文件不存在,转换失败");
        }

        System.out.print("Begin======"+new Date());
        PDFUtil.doc2pdf(rootPath+fileName,rootPath+contractNo+".pdf");
        System.out.print("End======"+new Date());
        Calendar cal = Calendar.getInstance();
        String filePath="fileName="+contractNo+".pdf&type=contract"+"&mouth="+cal.get(Calendar.MONTH+1);
        String fileId=FileUtil.getCharAndNumr(20);
        templateParamService.addTemplateFile(fileId,filePath);
        return BaseResponse.success("转换成功",RelPath()+"download.do?fileId="+fileId);
    }

    /**
     * @return  返回相对路径RelativePath
     */
    public String RelPath() {
        String path = request.getContextPath();
        return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    }


    public static String greater(String flag){
        Long time=System.currentTimeMillis();
        String result=flag+String.valueOf(time)+getUuid();
        return result;
    }
    public static String[] chars = new String[] { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    public static String getUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 36]);
        }
        return shortBuffer.toString();
    }

    public static void main(String[] args){
        System.out.print(greater("A"));
    }
}
