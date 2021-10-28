package com.pageOfficeServer.web;

import com.pageOfficeServer.response.BaseResponse;
import com.pageOfficeServer.service.TemplateParamService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Controller
public class DownloadController  extends HttpServlet {

    @Resource
    HttpServletRequest request;

    @Resource
    HttpServletResponse response;

    @Autowired
    private TemplateParamService templateParamService;

//    /**
//     *
//     * @param fileName
//     * @param type template 下载模板； contract 下载合同
//     * @return
//     */
//    @RequestMapping(value = "/download")
//    public String download(@RequestParam String fileName,@RequestParam String type){
//
//        response.setContentType("text/html;charset=utf-8");//1.设置响应的文件类型和文件编码
//
//        try {
//            request.setCharacterEncoding("UTF-8");//2.确保请求的编码类型为UTF-8,不然文件下载后有可能因为类型不一致出现乱码
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        if(!type.equals("contract")&&!type.equals("template")&&!type.equals("esignPdf")){
//            return null;
//        }
//
//        BufferedInputStream bis = null;
//        BufferedOutputStream bos = null;
//
//        String ctxPath = request.getSession().getServletContext().getRealPath("/")+type+"/";
//        String downLoadPath = ctxPath + fileName;
//
//        try{
//            long fileLength = new File(downLoadPath).length();
//            String fileRealName = fileName.substring(fileName.lastIndexOf("/")+1);
//            //3.设置响应头文件内容,文件类型、弹出下载对话框、文件大小
//            response.setContentType("application/x-msdownload");
//
//            //文件名乱码问题解决
//            String filename;
//            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
//                filename = URLEncoder.encode(fileRealName, "UTF-8");
//            } else {
//                filename = new String(fileRealName.getBytes("UTF-8"), "ISO8859-1");
//            }
//
//            response.setHeader("Content-disposition", "attachment; filename=" + filename);
//            response.setHeader("Content-Length", String.valueOf(fileLength));
//
//            bis = new BufferedInputStream(new FileInputStream(downLoadPath));//4.新建一个输入流缓存对象,并将文件输入流对象传递进去,将文件路径传递进文件输入流对象中，这是一个逐步处理的过程
//            bos = new BufferedOutputStream(response.getOutputStream());//5.新建一个输出流缓存对象,将服务器响应输出流对象至于其中
//            byte[] buff = new byte[2048];//6.新建一个缓存
//            int bytesRead;              //7.内容字节总数
//            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {//8.输入到到buff缓存中,当文件为空是read()会return -1,否则返回读取的字节总数
//                bos.write(buff, 0, bytesRead);//9.将buff的字节写到响应体的输出流中，输出流持续输出到客户端
//            }
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (bis != null)
//                try {
//                    bis.close();//10.关闭缓存输入流对象
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            if (bos != null)
//                try {
//                    bos.close();//10.关闭缓存输出流对象
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//        }
//        return null;
//    }

    /**
     *
     * @param fileId
     * @return
     */
    @RequestMapping(value = "/download")
    public String download(@RequestParam String fileId){

        response.setContentType("text/html;charset=utf-8");//1.设置响应的文件类型和文件编码

        try {
            request.setCharacterEncoding("UTF-8");//2.确保请求的编码类型为UTF-8,不然文件下载后有可能因为类型不一致出现乱码
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String filePath= templateParamService.getTemplateFileById(fileId);
        String[] fs=filePath.split("&type");
        String fileName=fs[0].split("=")[1];
        String type=fs[1].split("=")[1];
        String mouth = null;
        if(filePath.split("&").length>2){
            mouth=filePath.split("&mouth=")[1];
            type=fs[1].split("&")[0].split("=")[1];
        }
        if(!type.equals("contract")&&!type.equals("template")&&!type.equals("esignPdf")){
            return null;
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        String ctxPath;
        if((!"esignPdf".equals(type) || !"contract".equals(type)) && StringUtils.isEmpty(mouth)){
            ctxPath = request.getSession().getServletContext().getRealPath("/")+type+"/";
        }else{
            ctxPath = request.getSession().getServletContext().getRealPath("/")+type+"/"+mouth+"/";
        }
        String downLoadPath = ctxPath + fileName;

        try{
            long fileLength = new File(downLoadPath).length();
            String fileRealName = fileName.substring(fileName.lastIndexOf("/")+1);
            //3.设置响应头文件内容,文件类型、弹出下载对话框、文件大小
            response.setContentType("application/x-msdownload");

            //文件名乱码问题解决
            String filename;
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                filename = URLEncoder.encode(fileRealName, "UTF-8");
            } else {
                filename = new String(fileRealName.getBytes("UTF-8"), "ISO8859-1");
            }

            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            response.setHeader("Content-Length", String.valueOf(fileLength));

            bis = new BufferedInputStream(new FileInputStream(downLoadPath));//4.新建一个输入流缓存对象,并将文件输入流对象传递进去,将文件路径传递进文件输入流对象中，这是一个逐步处理的过程
            bos = new BufferedOutputStream(response.getOutputStream());//5.新建一个输出流缓存对象,将服务器响应输出流对象至于其中
            byte[] buff = new byte[2048];//6.新建一个缓存
            int bytesRead;              //7.内容字节总数
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {//8.输入到到buff缓存中,当文件为空是read()会return -1,否则返回读取的字节总数
                bos.write(buff, 0, bytesRead);//9.将buff的字节写到响应体的输出流中，输出流持续输出到客户端
            }

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();//10.关闭缓存输入流对象
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();//10.关闭缓存输出流对象
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     *
     * @param fileId
     * @return
     */
    @RequestMapping(value = "/queryFilePathById")
    public  @ResponseBody BaseResponse queryFilePathById(@RequestParam String fileId){
        if(StringUtils.isNotBlank(templateParamService.getTemplateFileById(fileId))){
            return BaseResponse.success((Object) templateParamService.getTemplateFileById(fileId));
        }
        return BaseResponse.error("不存在path");
    }
}
