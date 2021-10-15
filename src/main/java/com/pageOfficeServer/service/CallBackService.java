package com.pageOfficeServer.service;

import com.pageOfficeServer.util.httpUtil.ClientResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CallBackService {
     ClientResponse callback(HttpServletRequest request, HttpServletResponse response, String method);
}
