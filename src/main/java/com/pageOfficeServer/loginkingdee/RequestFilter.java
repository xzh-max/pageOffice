package com.pageOfficeServer.loginkingdee;

//import kd.bos.api.ApiRequestContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        httpRequest.setCharacterEncoding("UTF-8");
//        ApiRequestContext.initThreadLocal(httpRequest,httpResponse);
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
