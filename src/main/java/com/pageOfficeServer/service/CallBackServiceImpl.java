package com.pageOfficeServer.service;

import com.pageOfficeServer.util.httpUtil.ClientRequest;
import com.pageOfficeServer.util.httpUtil.ClientResponse;
import com.pageOfficeServer.util.httpUtil.HttpClientUtil;
import com.pageOfficeServer.util.httpUtil.RequestUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Service
public class CallBackServiceImpl implements CallBackService {
    @Override
    public ClientResponse callback(HttpServletRequest request, HttpServletResponse response, String method) {
//        ApiRequestContext.initThreadLocal(request,response);
        Map<String, Object> params = RequestUtil.getHeaderAndParameterMap(request);
        System.out.println("此时的参数为"+params);
        ClientRequest clientRequest = new ClientRequest(method, params);
        // edit end
       return HttpClientUtil.getInstance().send(clientRequest);
    }
}
