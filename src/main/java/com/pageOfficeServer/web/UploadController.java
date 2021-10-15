package com.pageOfficeServer.web;

import com.alibaba.fastjson.JSONObject;
import com.pageOfficeServer.mapper.TemplateParamMapper;
import com.pageOfficeServer.model.*;
import com.pageOfficeServer.response.BaseResponse;
import com.pageOfficeServer.service.TemplateParamService;
import com.pageOfficeServer.util.FileUtil;
import com.pageOfficeServer.util.wordUtil.PDFUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
public class UploadController extends HttpServlet {

    @Resource
    HttpServletRequest request;
    @Resource
    private TemplateParamService templateParamService;

    @Autowired
    private TemplateParamMapper templateParamMapper;

    /**
     * @return 返回相对路径RelativePath
     */
    public String RelPath() {
        String path = request.getContextPath();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    }

    /**
     * @return 返回服务器目录的真实路径
     */
    public String RealPath() {
        return request.getSession().getServletContext().getRealPath("/");
    }

    /**
     * 单文件上传
     *
     * @param imageFile
     * @param request
     * @return
     */
    @RequestMapping(value = "/singleUpload", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String singleUpload(@RequestParam("imageFile") MultipartFile imageFile, HttpServletRequest request) {//,
        JSONObject result = new JSONObject();
        String filename = imageFile.getOriginalFilename();
        String templateNo = request.getParameter("templateNo");

        String sysParams = request.getParameter("sysParams");

        if (StringUtils.isBlank(filename)) {
            result.put("success", "-1");
            result.put("errorMsg", "附件为空，不允许上传");
            return result.toString();
        }

        if (StringUtils.isBlank(templateNo)) {
            result.put("success", "-1");
            result.put("errorMsg", "模板编号为空，不允许上传");
            return result.toString();
        }

        if (StringUtils.isBlank(sysParams)) {
            result.put("success", "-1");
            result.put("errorMsg", "系统变量为空，不允许上传模板");
            return result.toString();
        }

//        JSONObject sys=JSONObject.parseObject(sysParams);
        List<SysParamInfoDto> sys = JSONObject.parseArray(sysParams, SysParamInfoDto.class);
        String filePat = request.getServletContext().getRealPath("") + "/template/" + templateNo;

        TemplateParamModel templateParamModel = templateParamMapper.getTemplateByTemplateNo(templateNo);
        if (templateParamModel != null) {
            templateParamService.deleteTemplateParam(templateParamModel.getFid());
            String fileName1 = FileUtil.getFileName(filePat);
            File targetFile = new File(filePat + "/" + fileName1);
            if (targetFile.exists()) {
                targetFile.delete();
            }
        }
        List<SysParamInfoDto> sysParamInfoDtos = JSONObject.parseArray(sysParams, SysParamInfoDto.class);


        //保存系统变量信息
        templateParamService.addTemplateAndSysParam(templateNo, sys);
        File dir = new File(filePat);//1.新建一个文件夹对象
        if (!dir.exists()) {              //2.检查路径下upload文件夹是否存在
            dir.mkdirs();
        }

        File targetFile = new File(filePat + "/" + filename);//3.在文件夹下新建一个filename文件的文件对象,此处新建文件应该新建在确切的物理路径下

        if (!targetFile.exists()) {//4.判断真实路径下是否存在filename文件
            try {
                targetFile.createNewFile();//5.在真实路径下创建filename空文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            imageFile.transferTo(targetFile);//6.复制文件到真实路径下
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("真实路径:" + RealPath() + "upload/");

        result.put("success", "0");
        String filePath = "fileName=" + templateNo + "/" + filename + "&type=template";
        String fileId = FileUtil.getCharAndNumr(20);
        templateParamService.addTemplateFile(fileId, filePath);
        result.put("path", RelPath() + "download.do?fileId=" + fileId);
        return result.toString();            //非安全目录下使用(可用)
    }

    /**
     * esign文件上传
     *
     * @param imageFiles
     * @param request
     * @return
     */
    @RequestMapping(value = "/esignFileUploads")
    public @ResponseBody
    BaseResponse uploadPdfs(@RequestParam("imageFile") MultipartFile[] imageFiles, HttpServletRequest request) {//,
        List<String> paths = new ArrayList<>();
        String contractNo = request.getParameter("contractNo");
        for (MultipartFile imageFile : imageFiles) {
            String filename = imageFile.getOriginalFilename();

            if (StringUtils.isBlank(filename)) {
                return BaseResponse.error("附件为空，不允许上传");
            }
            String filePat = request.getServletContext().getRealPath("") + "/esignPdf/" + contractNo;

            File dir = new File(filePat);//1.新建一个文件夹对象
            if (!dir.exists()) {              //2.检查路径下upload文件夹是否存在
                dir.mkdirs();
            }

            File targetFile = new File(filePat + "/" + filename);//3.在文件夹下新建一个filename文件的文件对象,此处新建文件应该新建在确切的物理路径下

            if (!targetFile.exists()) {//4.判断真实路径下是否存在filename文件
                try {
                    targetFile.createNewFile();//5.在真实路径下创建filename空文件
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                imageFile.transferTo(targetFile);//6.复制文件到真实路径下
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print("Begin======" + new Date());
            PDFUtil.doc2pdf(filePat + "/" + filename, filePat + "/" + FileUtil.getFileNameBYpath(filename) + ".pdf");
            System.out.print("End======" + new Date());

            String filePath = "fileName=" + contractNo + "/" + FileUtil.getFileNameBYpath(filename) + ".pdf&type=esignPdf";
            String fileId = FileUtil.getCharAndNumr(20);
            templateParamService.addTemplateFile(fileId, filePath);

            String path = RelPath() + "download.do?fileId=" + fileId;
            paths.add(path);
            targetFile.delete();
        }
        return BaseResponse.success((Object) paths);            //非安全目录下使用(可用)
    }

    /**
     * esign文件上传
     *
     * @param imageFile
     * @param request
     * @return
     */
    @RequestMapping(value = "/esignFileUpload")
    public @ResponseBody
    BaseResponse uploadPdf(@RequestParam("imageFile") MultipartFile imageFile, HttpServletRequest request) {//,
//        String filename = imageFile.getOriginalFilename();
        String filename = request.getParameter("fileName");

        if (StringUtils.isBlank(filename)) {
            return BaseResponse.error("附件为空，不允许上传");
        }

        String contractNo = request.getParameter("contractNo");
        if (StringUtils.isBlank(contractNo)) {
            return BaseResponse.error("合同编号不能为空");
        }
        String filePat = request.getServletContext().getRealPath("") + "/esignPdf/" + contractNo;

        File dir = new File(filePat);//1.新建一个文件夹对象
        if (!dir.exists()) {              //2.检查路径下upload文件夹是否存在
            dir.mkdirs();
        }

        File targetFile = new File(filePat + "/" + filename);//3.在文件夹下新建一个filename文件的文件对象,此处新建文件应该新建在确切的物理路径下

        if (!targetFile.exists()) {//4.判断真实路径下是否存在filename文件
            try {
                targetFile.createNewFile();//5.在真实路径下创建filename空文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            imageFile.transferTo(targetFile);//6.复制文件到真实路径下
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filePath = "fileName=" + contractNo + "/" + filename + "&type=esignPdf";
        String fileId = FileUtil.getCharAndNumr(20);
        templateParamService.addTemplateFile(fileId, filePath);

        String path = RelPath() + "download.do?fileId=" + fileId;
//        String path = RelPath()+"download.do?fileName="+ filename+"&type=esignPdf";
        return BaseResponse.success((Object) path);            //非安全目录下使用(可用)
    }

    @RequestMapping(value = "/quoteTemplate", produces = "application/json;charset=UTF-8")
    @Transactional
    public @ResponseBody
    BaseResponse quoteTemplate(HttpServletRequest request) {//,

        String quoteTemplateNo = request.getParameter("quoteTemplateNo");
        String templateNo = request.getParameter("templateNo");
        if (StringUtils.isBlank(quoteTemplateNo)) {
            return BaseResponse.error("被引用的模板编号为空，不允许引用模板");
        }
        if (StringUtils.isBlank(templateNo)) {
            return BaseResponse.error("新的模板编号为空，不允许引用模板");
        }
        TemplateParamModel quoteTemplateParamModel = templateParamMapper.getTemplateByTemplateNo(quoteTemplateNo);
        if (quoteTemplateParamModel == null) {
            return BaseResponse.error("被引用的模板为空，不允许引用模板");
        }

        //获取被引用文件路径
        String quoteRootPath = request.getServletContext().getRealPath("") + "/template/" + quoteTemplateNo;
        String fileName = FileUtil.getFileName(quoteRootPath);
        if (StringUtils.isBlank(fileName)) {
            return BaseResponse.error("被引用的模板为空，不允许引用模板");
        }

        //获取当前文件路径
        String rootPath = request.getServletContext().getRealPath("") + "/template/" + templateNo;
        File templatePath = new File(rootPath);
        if (!templatePath.exists()) {
            templatePath.mkdir();
            //如果新模板不存就复制过来
            FileUtil.fileCopy_channel(quoteRootPath + "/" + fileName, rootPath + "/" + fileName);


            TemplateParamModel templateParamModel = new TemplateParamModel();
            templateParamModel.setTemplateId(templateNo);
            //保存模板信息
            templateParamMapper.addTemplateInfo(templateParamModel);

            //获取变量列表
            List<TemplateParamEntryModel> templateParamEntryModels = templateParamMapper.getParamEntrys(quoteTemplateParamModel.getFid());
            List<TemplateParamEntryModel> newTemplateParamEntryModels = new ArrayList<>();
            for (TemplateParamEntryModel templateParamEntryModel : templateParamEntryModels) {
                //复制自定义变量和系统变量
                if ("1".equals(templateParamEntryModel.getParamType())) {
                    templateParamEntryModel.setFid(String.valueOf(templateParamModel.getFid()));
                    newTemplateParamEntryModels.add(templateParamEntryModel);
                }
                //复制下拉选项
                if ("3".equals(templateParamEntryModel.getParamType()) || "2".equals(templateParamEntryModel.getParamType())) {
                    List<TemplateParamOptionModel> templateParamOptionModels = templateParamMapper.getOptions(templateParamEntryModel.getFentryId());
                    for (TemplateParamOptionModel templateParamOptionModel : templateParamOptionModels) {
                        templateParamOptionModel.setFid(templateParamModel.getFid());
                    }
                    List<String> bookMarks = templateParamMapper.queryParamBookMarkBy("1", templateParamEntryModel.getFentryId());//获取被引用模板的自定义变量标签列表
                    templateParamService.addParam(String.valueOf(templateParamModel.getFid()), templateParamEntryModel, templateParamOptionModels, bookMarks);
                }
            }

            if (newTemplateParamEntryModels.size() != 0) {
                //保存新的系统变量
                templateParamMapper.saveParamEntrys(newTemplateParamEntryModels);
            }

            //获取条款信息
            List<ArticleInfoDto> articleInfoDtos = templateParamService.queryAtricleDtoByFid(String.valueOf(quoteTemplateParamModel.getFid()));
            //保存条款信息
            if (articleInfoDtos != null & articleInfoDtos.size() != 0) {
                templateParamService.addAtricleDtosByFid(String.valueOf(templateParamModel.getFid()), articleInfoDtos);
            }

            //复制一份新的标签信息
            List<BooKMarkModel> booKMarkModels = templateParamMapper.queryBkByFid(quoteTemplateParamModel.getFid());
            if (booKMarkModels != null & booKMarkModels.size() != 0) {
                for (BooKMarkModel booKMarkModel : booKMarkModels) {
                    booKMarkModel.setFid(String.valueOf(templateParamModel.getFid()));
                }
                templateParamMapper.addBookMarks(booKMarkModels);
            }
        } else {
            //引用模板之前已经引用过模板
            String oldTemplateFileName = FileUtil.getFileName(rootPath);
            File targetFile = new File(rootPath + "/" + oldTemplateFileName);
            if (targetFile.exists()) {
                targetFile.delete();
            }
            //如果新模板不存就复制过来
            FileUtil.fileCopy_channel(quoteRootPath + "/" + fileName, rootPath + "/" + fileName);
        }

        String filePath = "fileName=" + templateNo + "/" + fileName + "&type=template";
        String fileId = FileUtil.getCharAndNumr(20);
        templateParamService.addTemplateFile(fileId, filePath);
        String url = RelPath() + "download.do?fileId=" + fileId;
        return BaseResponse.success((Object) url);
    }

    /**
     * 多文件上传
     *
     * @param request
     * @return
     */
    @RequestMapping("/multiUpload")
    public String multiUpload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;//1、将请求进行转义
        Map<String, MultipartFile> files = multipartHttpServletRequest.getFileMap();//2、获取同一表单提交过来的所有文件

        //3、在真实路径创建文件
        File dir = new File(RealPath() + "upload/");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        List<String> fileList = new ArrayList<String>();//4、将上传的文件的相对地址保存在一个列表中(客户端只能请求服务器的相对地址)

        for (MultipartFile file : files.values()) {  //5、在服务器的绝对地址下新建文件,并将上传的文件复制过去,将相对路径保存进List列表中,服务器的相对路径和绝对路径是相互映射的，客户端只能请求相对路径
            File targetFile = new File(RealPath() + "upload/" + file.getOriginalFilename());
            if (!targetFile.exists()) {
                try {
                    targetFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    file.transferTo(targetFile);
                    fileList.add(RelPath() + "upload/" + file.getOriginalFilename());
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                fileList.add(RelPath() + "upload/" + file.getOriginalFilename());//文件如果存在直接访问
            }

        }
        System.out.println(fileList);
        request.setAttribute("files", fileList);

        return "/WEB-INF/jsp/multiUploadResult.jsp";
    }


    public static void main(String[] args) {
//        List<SysParamInfoDto> sysParamInfoDtos1=new ArrayList<>();
//        SysParamInfoDto sysParamInfoDtos=new SysParamInfoDto();
//        sysParamInfoDtos.setSysKey("1");
//        sysParamInfoDtos.setSysName("ceshi11");
//        sysParamInfoDtos1.add(sysParamInfoDtos);
//        SysParamInfoDto sysParamInfoDtoss=new SysParamInfoDto();
//        sysParamInfoDtoss.setSysKey("2");
//        sysParamInfoDtoss.setSysName("ceshi11");
//        sysParamInfoDtos1.add(sysParamInfoDtoss);
//        SysParamInfoDto sysParamInfoDtosss=new SysParamInfoDto();
//        sysParamInfoDtosss.setSysKey("3");
//        sysParamInfoDtosss.setSysName("ceshi11");
//        sysParamInfoDtos1.add(sysParamInfoDtosss);
//        SysParamInfoDto sysParamInfoDtossss=new SysParamInfoDto();
//        sysParamInfoDtossss.setSysKey("4");
//        sysParamInfoDtossss.setSysName("ceshi11");
//        sysParamInfoDtos1.add(sysParamInfoDtossss);
//        String sys=JSONObject.toJSONString(sysParamInfoDtos1);
//        List<SysParamInfoDto> sysParamInfoDtos2=JSONObject.parseArray(sys,SysParamInfoDto.class);
//        System.out.println(sys);
//        System.out.println(sysParamInfoDtos2.get(1).getSysKey());

        Integer s = 2;
        System.out.println(Long.valueOf(s));
    }
}
