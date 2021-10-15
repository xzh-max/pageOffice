package com.pageOfficeServer.service;

import com.pageOfficeServer.util.httpUtil.ClientRequest;
import com.pageOfficeServer.util.httpUtil.ClientResponse;
import com.pageOfficeServer.util.httpUtil.HttpClientUtil;
import org.springframework.stereotype.Service;
import com.pageOfficeServer.request.ReturnUrlRequest;

@Service("returnTemplateUrlService")
public class ReturnTemplateUrlService implements ReturnurlService{

    @Override
    public void returnUrl(ReturnUrlRequest returnUrlRequest) {
        ClientRequest clientRequest=new ClientRequest("templateUrl",returnUrlRequest);
        HttpClientUtil httpClientUtil=HttpClientUtil.getInstance();
        ClientResponse clientResponse = httpClientUtil.send(clientRequest);

    }
}
