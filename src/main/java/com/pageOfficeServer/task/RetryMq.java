package com.pageOfficeServer.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pageOfficeServer.mapper.TemplateParamMapper;
import com.pageOfficeServer.model.ConsumerFailModel;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RetryMq {

    @Autowired
    private TemplateParamMapper templateParamMapper;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Scheduled(cron="0 */2 * * * ?") //每半个小时执行一次
    public void test(){
        System.out.println("开始执行重试重推mq生成合同");
        List<ConsumerFailModel> consumerFailModels=templateParamMapper.queryConsumerFail();
        for(ConsumerFailModel consumerFailModel:consumerFailModels){
            //将id放入消费消息
            JSONObject jsonObject=(JSONObject)JSONObject.parse(consumerFailModel.getMsg());
            jsonObject.put("consumerFailId",consumerFailModel.getId());
            rabbitTemplate.convertAndSend(consumerFailModel.getMqexchange(),consumerFailModel.getMqtagkey(), JSON.toJSONStringWithDateFormat(jsonObject,"yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat));
        }
        System.out.println("<<<---------结束执行HR数据同步任务--------->>>");
    }
}
