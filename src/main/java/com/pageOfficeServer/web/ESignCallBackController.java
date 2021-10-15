package com.pageOfficeServer.web;

import com.pageOfficeServer.service.CallBackService;
import com.pageOfficeServer.util.httpUtil.ClientResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ESignCallBackController {
    @Resource
    private CallBackService callBackService;
    @RequestMapping(value="/{method}/callback", method= RequestMethod.POST, produces="application/json;charset=utf-8;")
    @ResponseBody
    public ClientResponse eSignCallBack(HttpServletRequest request, HttpServletResponse response, @PathVariable String method) throws IOException {
        System.out.println("e签宝回调成功");
        request.setCharacterEncoding("utf-8");

        return callBackService.callback(request,response, method);
    }

    @RequestMapping(value="/{method}/redirect", method= RequestMethod.GET)
    public String eSignRedirect(HttpServletRequest request, HttpServletResponse response, @PathVariable String method) throws IOException {
        System.out.println("e签宝回调成功");
        request.setCharacterEncoding("utf-8");
        ClientResponse result =  eSignCallBack(request,response, method);
        return "redirect:"+result.getData();
    }

}
