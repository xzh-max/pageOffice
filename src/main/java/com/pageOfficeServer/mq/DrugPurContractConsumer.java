package com.pageOfficeServer.mq;

import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.pageOfficeServer.mapper.TemplateParamMapper;
import com.pageOfficeServer.model.ConsumerFailModel;
import com.pageOfficeServer.model.TemplateParamEntryModel;
import com.pageOfficeServer.model.TemplateParamModel;
import com.pageOfficeServer.util.DocParamProcessUtil;
import com.pageOfficeServer.util.FileUtil;
import com.pageOfficeServer.util.WaterMarkUtil;
import com.pageOfficeServer.util.tableProcessUtil.DynWordUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 药物疫苗采购合同新增消费者
 */
@Component
public class DrugPurContractConsumer implements MessageListener {

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
            System.out.print("药物疫苗类合同收到消费成功-------");
            System.out.print("body"+msg);
            req=(JSONObject) JSONObject.parse(msg);

            String templateNo=req.getString("templateNo");
            String contractNo=req.getString("contractno");
            String subOrAudit=req.getString("subOrAudit");
            String mouth=req.getString("mouth");

            Map<String,Object> params=new HashMap<>();

            TemplateParamModel templateParamModel=templateParamMapper.getTemplateByTemplateNo(templateNo);

            List<TemplateParamEntryModel> templateParamEntryModels=templateParamMapper.getParamEntrys(templateParamModel.getFid());
            for(TemplateParamEntryModel templateParamEntryModel:templateParamEntryModels){
                for(String key:req.keySet()){
                    //如果数据库key和入参key相等，将name放入传入的值
                    if(templateParamEntryModel.getSyskey()!=null&&templateParamEntryModel.getSyskey().trim()!=null){
                        if(templateParamEntryModel.getSyskey().equals(key)){
                            params.put(templateParamEntryModel.getName(), req.get(key));
                        }
                    }
                }
            }

            String realPath= servletContext.getRealPath("");
            //  获取文件
            String path = FileUtil.getFilePath(realPath+"/template/"+templateNo);

            String[] fileType=path.split(".doc");
            System.out.print("生成合同开始时间"+new Date());
            String outPath;
            if(fileType.length==2) {

                File dir;
                if(StringUtils.isEmpty(mouth)){
                    dir = new File(realPath + "/contract/");
                }else{
                    dir = new File(realPath + "/contract/" + mouth +"/");
                }
                if (!dir.exists()) {// 判断文件目录是否存在
                    dir.mkdirs();
                }
                if(StringUtils.isEmpty(mouth)){
                    outPath = realPath + "/contract/" + contractNo + ".docx";
                }else {
                    outPath = realPath + "/contract/"+ mouth + "/" + contractNo + ".docx";
                }
                DynWordUtils.process(params, path, outPath);

                if("1".equals(subOrAudit)){
                    //审核就添加水印
                    WaterMarkUtil.addWM(outPath,"WENS");
                }

            }

            if (fileType.length == 1) {
                if(StringUtils.isEmpty(mouth)){
                    outPath = realPath + "/contract/" + contractNo;
                }else {
                    outPath = realPath + "/contract/" + mouth + "/" + contractNo;
                }
                //表示doc类型word文件
                DynWordUtils.process(params, path, outPath + ".docx");

                DocParamProcessUtil.replaceDrugDocFile(path, params, outPath + ".doc");
            }

            System.out.print("生成合同结束时间"+new Date());
            if (req.containsKey("consumerFailId")) {
                //如果是定时重试，成功之后删除
                templateParamMapper.removeConsumerFail(req.getInteger("consumerFailId"));
            }

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
