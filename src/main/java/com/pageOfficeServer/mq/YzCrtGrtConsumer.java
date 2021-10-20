package com.pageOfficeServer.mq;

import com.alibaba.fastjson.JSONObject;
import com.pageOfficeServer.mapper.TemplateParamMapper;
import com.pageOfficeServer.model.ConsumerFailModel;
import com.pageOfficeServer.model.TemplateParamEntryModel;
import com.pageOfficeServer.model.TemplateParamModel;
import com.pageOfficeServer.util.DocParamProcessUtil;
import com.pageOfficeServer.util.FileUtil;
import com.pageOfficeServer.util.WaterMarkUtil;
import com.pageOfficeServer.util.wordUtil.DocxUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 委托养殖类合同生成消费者
 */
@Component
public class YzCrtGrtConsumer implements MessageListener{

    @Autowired
    private TemplateParamMapper templateParamMapper;

    @Autowired
    private ServletContext servletContext;

    @Override
    public void onMessage(Message message) {
        String msg="";
        JSONObject req=null;
        try {
            msg = new String(message.getBody(), "utf-8");
            System.out.print("委托养殖类合同收到消费成功-------");
            System.out.print("body"+msg);
            req=(JSONObject) JSONObject.parse(msg);

            String templateNo=req.getString("templateNo");
            String contractNo=req.getString("contractNo");
            String subOrAudit=req.getString("subOrAudit");
            String mouth=req.getString("mouth");

            FileOutputStream fileOutputStream=null;

            Map<String,String> params=new HashMap<>();

            TemplateParamModel templateParamModel=templateParamMapper.getTemplateByTemplateNo(templateNo);

            List<TemplateParamEntryModel> templateParamEntryModels=templateParamMapper.getParamEntrys(templateParamModel.getFid());
            for(TemplateParamEntryModel templateParamEntryModel:templateParamEntryModels){
                for(String key:req.keySet()){
                    //如果数据库key和入参key相等，将name放入传入的值
                    if(templateParamEntryModel.getSyskey()!=null&&templateParamEntryModel.getSyskey().trim()!=null){
                        if(templateParamEntryModel.getSyskey().equals(key)){
                            params.put(templateParamEntryModel.getName(), req.get(key).toString());
                        }
                    }
                }
            }

            String realPath= servletContext.getRealPath("");
//            String realPath = servletContext.getRealPath("");
            //获取文件路径
            String rootPath =realPath+"/template/";
            DocxUtil docxUtil = new DocxUtil();
            //  获取文件
            String path = FileUtil.getFilePath(rootPath+templateNo);

            XWPFDocument document = null;//读取Word模板
            String[] fileType=path.split("\\."+"doc");
            System.out.print("生成合同开始时间"+new Date());
            String fileName;
            if(fileType.length==2) {
                //表示docx类型的word文件
                document = new XWPFDocument(POIXMLDocument.openPackage(path));

                docxUtil.searchAndReplace(document, params);//替换模板中的对应变量。
                File dir;
                if(StringUtils.isEmpty(mouth)){
                    dir = new File(realPath + "/contract/");
                    fileName=servletContext.getRealPath("")+"/contract/"+contractNo;
                }else{
                    dir = new File(realPath + "/contract/"+mouth+"/");
                    fileName=servletContext.getRealPath("")+"/contract/"+mouth+"/"+contractNo;
                }
                if (!dir.exists()) {// 判断文件目录是否存在
                    dir.mkdirs();
                }
                FileOutputStream os = new FileOutputStream(fileName + ".docx");
                document.write(os);
                os.close();

                if("1".equals(subOrAudit)){
                    //审核添加水印
                    WaterMarkUtil.addWM(fileName + ".docx","WENS");
                }

                if (null != docxUtil) {
                    docxUtil.close(fileOutputStream);//关闭流
                }
            }

                if (fileType.length == 1) {
                    if(StringUtils.isEmpty(mouth)){
                        fileName = servletContext.getRealPath("") + "/contract/" + contractNo;
                    }else{
                        fileName = servletContext.getRealPath("") + "/contract/" + mouth + "/" + contractNo;
                    }
                    //表示doc类型word文件
                    DocParamProcessUtil.replaceDocFile(path, params, fileName + ".doc");

                    if("1".equals(subOrAudit)){
                        WaterMarkUtil.addWM(fileName + ".doc","WENS");
                    }

                }


                if (req.containsKey("consumerFailId")) {
                    //如果是定时重试，成功之后删除
                    templateParamMapper.removeConsumerFail(req.getInteger("consumerFailId"));
                }

            System.out.print("生成合同结束时间"+new Date());
        } catch (Exception e) {
            e.printStackTrace();
            if(!req.containsKey("consumerFailId")){
                System.out.print("消费失败");
                ConsumerFailModel consumerFailModel=new ConsumerFailModel();
                consumerFailModel.setMqexchange("miaosha.exchange");
                consumerFailModel.setMqtagkey("miaosha.tag.key");
                consumerFailModel.setMsg(msg);
                consumerFailModel.setExceptionInfo(e.toString());
                templateParamMapper.addConsumerFail(consumerFailModel);
            }
        }

    }

}
