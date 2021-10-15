package com.pageOfficeServer.util.httpUtil;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

/**
 * HTTP请求工具类
 *
 * @author wangcheng
 */
public class HttpClientUtil {

    private static final String CHARSET = "UTF-8";


    private static final HttpClientUtil instance = new HttpClientUtil();

    private DefaultHttpClient httpClient;

    public static HttpClientUtil getInstance() {
        return instance;
    }

    private HttpClientUtil() {
        if (httpClient == null) {
            httpClient = HttpClientTool.getHttpClient();
        }
    }

    public DefaultHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * 中台接口
     *
     * @param request
     * @return
     */
    public ClientResponse send(ClientRequest request) {
        ClientResponse response = null;

        // 中台接口请求地址
        StringBuilder requestURL = new StringBuilder();
        requestURL.append(SystemServiceConstant.SRV_CON_PROTOCOL).append("://");
        requestURL.append(SystemServiceConstant.SRV_CON_HOST);
        requestURL.append(SystemServiceConstant.SRV_ESIGN_SIGN_CON_PREFIX);
        if (ESignInvokeMethod.E_SIGN_SIGN.equalsIgnoreCase(request.getMethod())) {
            requestURL.append(SystemServiceConstant.SRV_ESIGN_SIGN_CON_SUFFIX);
        } else if (ESignInvokeMethod.E_SIGN_COMPANY.equals(request.getMethod())) {
            requestURL.append(SystemServiceConstant.SRV_ESIGN_COMPANY_CON_SUFFIX);
        } else if (ESignInvokeMethod.E_SIGN_PERSONAL.equals(request.getMethod())) {
            requestURL.append(SystemServiceConstant.SRV_ESIGN_PERSONAL_CON_SUFFIX);
        }else if (ESignInvokeMethod.E_SIGN_REARER_INFO.equals(request.getMethod())) {
            requestURL.append(SystemServiceConstant.SRV_ESIGN_REARER_INFO_SUFFIX);
        } else if (ESignInvokeMethod.E_SIGN_REDIRECT_COMPANY.equals(request.getMethod())) {
            requestURL.append(SystemServiceConstant.SRV_ESIGN_CER_COMPANY_CON_SUFFIX);
        } else {
            requestURL.append(SystemServiceConstant.SRV_ESIGN_CER_PERSON_CON_SUFFIX);
        }
        requestURL.append("?").append("access_token").append("=").append(request.getToken());
        request.setUrl(requestURL.toString());

        // 根据中台接口需要格式封装
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("method", request.getMethod());
        params.put("data", request.getData());
        System.out.println("此时封装的参数为" + params);
        request.setData(params);

        if ("POST".equalsIgnoreCase(request.getHttpMethod())) {
            response = this.sendPost(request);
        } else if ("GET".equalsIgnoreCase(request.getHttpMethod())) {
            response = this.sendGet(request);
        } else {
            response = new ClientResponse();

            response.setSuccess(false);
            response.setMessage("可能由于网络原因引起的错误，请您稍后重试。");
            response.setStack("不支持" + request.getHttpMethod() + "请求方式，请注册请求方式。");

        }
        System.out.println("此次发送结果为" + response);
        return response;
    }

    /**
     * 简单的get请求，只是普通的http get请求
     *
     * @param url 请求URL
     * @return http请求结果
     * edit by 刘桂平 20190429
     */
    public String simpleGet(String url) {
        String result = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = getInstance().getHttpClient().execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                if (null != httpResponse.getEntity()) {
                    result = EntityUtils.toString(httpResponse.getEntity(), CHARSET);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 简单的post请求，只是普通的http post请求
     *
     * @param url 请求URL
     * @param map 参数
     * @return http请求结果
     * edit by 刘桂平 20190411
     */
    public String simplePost(String url, Map<String, Object> map) {
        String result = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            // 设置参数
            List<NameValuePair> list = null;
            if (null != map) {
                list = new ArrayList<>();
                Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, Object> elem = iterator.next();
                    list.add(new BasicNameValuePair(elem.getKey(), elem.getValue() + ""));
                }
            }
            if (null != list && list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, CHARSET);
                httpPost.setEntity(entity);
            }
            HttpResponse httpResponse = getInstance().getHttpClient().execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                if (null != httpResponse.getEntity()) {
                    result = EntityUtils.toString(httpResponse.getEntity(), CHARSET);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String simplePost(String url) {
        return simplePost(url, null);
    }

    /**
     * POST请求
     *
     * @param request
     * @return
     */
    public ClientResponse sendPost(ClientRequest request) {
        ClientResponse response = null;

        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(request.getUrl());
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");

            // 封装post请求查询参数
            if (null != request.getData()) {
                StringEntity entity = new StringEntity(request.getData().toString(), Charset.forName(CHARSET));
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);

            }

            // 发送post请求
            HttpResponse result = httpClient.execute(httpPost);

            // 解析返回请求
            response = this.analysisResponse(request, result);
        } catch (Exception e) {
            response = new ClientResponse();

            response.setSuccess(false);
            response.setMessage("可能由于网络原因引起的错误，请您稍后重试。");
            response.setStack(e.toString());

        } finally {
            if (null != httpPost) {
                httpPost.releaseConnection();
            }
        }

        return response;
    }

    /**
     * GET请求
     *
     * @param request
     * @return
     */
    public ClientResponse sendGet(ClientRequest request) {
        ClientResponse response = null;

        HttpGet httpGet = null;
        try {
            StringBuilder url = new StringBuilder(request.getUrl());

            // 封装get请求查询参数
            if (null != request.getData()) {
                StringBuilder params = new StringBuilder();

                Set<Entry<String, Object>> entrySet = request.getData().entrySet();
                for (Entry<String, Object> entry : entrySet) {
                    params.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                if (url.indexOf("?") != -1) {
                    url.append("&").append(params);
                } else {
                    url.append("?").append(params);
                }

            }

            // 发送get请求
            httpGet = new HttpGet(url.toString());
            HttpResponse result = httpClient.execute(httpGet);

            // 解析返回请求
            response = this.analysisResponse(request, result);
        } catch (Exception e) {
            response = new ClientResponse();

            response.setSuccess(false);
            response.setMessage("可能由于网络原因引起的错误，请您稍后重试。");
            response.setStack(e.toString());

        } finally {
            if (null != httpGet) {
                httpGet.releaseConnection();
            }
        }

        return response;
    }

    /**
     * 解析返回请求
     *
     * @param request
     * @param result
     * @return
     */
    private ClientResponse analysisResponse(ClientRequest request, HttpResponse result) {
        ClientResponse response = new ClientResponse();

        // 判断状态码
        int statusCode = result.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_OK) {
            try {
                // 读取服务器返回过来的json字符串数据
                if (null != result.getEntity()) {
                    String strResult = EntityUtils.toString(result.getEntity(), CHARSET);

                    // 返回数据（已排除null值）
                    JSONObject jsonResult = JSONObject.parseObject(strResult);

                    // 是否成功
                    if (jsonResult.containsKey("success")) {
                        response.setSuccess(jsonResult.getBoolean("success"));
                    } else if (jsonResult.containsKey("state")) { // 平台登录接口与业务接口返回数据格式不一样所以增加:jsonResult.containsKey("state")
                        String state = jsonResult.getString("state");
                        if ("success".equalsIgnoreCase(state)) {
                            response.setSuccess(true);
                        } else {
                            response.setSuccess(false);
                        }
                    } else {
                        response.setSuccess(false);
                    }

                    // 返回代码，当值为1时表示token过期
                    if (jsonResult.containsKey("error_code")) {
                        response.setCode(jsonResult.getInteger("error_code"));
                    }

                    // 返回信息，当服务调用出现异常时才有内容
                    if (jsonResult.containsKey("message")) {
                        response.setMessage(jsonResult.getString("message"));
                    }

                    // 返回信息，当服务调用出现异常时才有内容
                    if (jsonResult.containsKey("error_desc")) {
                        response.setMessage(jsonResult.getString("error_desc"));
                    }

                    // 返回信息，当服务调用出现异常时才有内容
                    if (jsonResult.containsKey("stack")) {
                        response.setStack(jsonResult.getString("stack"));
                    }

                    // 返回结果，根据方法返回值不同，可以是对象、字符串、基本类型、NULL
                    if (jsonResult.containsKey("data")) {
                        response.setData(jsonResult.get("data"));
                    }
                }
            } catch (Exception e) {
                // 默认错误提示信息
                response.setSuccess(false);
                response.setMessage("可能由于网络原因引起的错误，请您稍后重试。");
                response.setStack("HTTP请求提交失败 => " + request.getUrl() + "，状态码 => " + statusCode);

            }
        } else {
            // 默认错误提示信息
            response.setSuccess(false);
            response.setMessage("可能由于网络原因引起的错误，请您稍后重试。");
            response.setStack("HTTP请求提交失败 => " + request.getUrl() + "，状态码 => " + statusCode);

        }

        return response;
    }

    /**
     * 平台登录专用接口
     *
     * @param request
     * @return
     */
    public ClientResponse sendByPlatform(ClientRequest request) {
        ClientResponse response = null;

        // 平台接口请求地址
        StringBuilder requestURL = new StringBuilder();
        if ("80".equals(SystemServiceConstant.SRV_CON_PORT) || "443".equals(SystemServiceConstant.SRV_CON_PORT) || StringUtils.isEmpty(SystemServiceConstant.SRV_CON_PORT)) {
            requestURL.append(SystemServiceConstant.SRV_CON_PROTOCOL).append("://");
            requestURL.append(SystemServiceConstant.SRV_CON_HOST);
            requestURL.append(SystemServiceConstant.SRV_CON_LOGIN_URL_SUFFIX).append(request.getMethod());
        } else {
            requestURL.append(SystemServiceConstant.SRV_CON_PROTOCOL).append("://");
            requestURL.append(SystemServiceConstant.SRV_CON_HOST).append(":").append(SystemServiceConstant.SRV_CON_PORT);
            requestURL.append(SystemServiceConstant.SRV_CON_LOGIN_URL_SUFFIX).append(request.getMethod());
        }
        request.setUrl(requestURL.toString());

        if ("Post".equalsIgnoreCase(request.getHttpMethod())) {
            response = this.sendPost(request);
        } else if ("GET".equalsIgnoreCase(request.getHttpMethod())) {
            response = this.sendGet(request);
        } else {
            response = new ClientResponse();

            response.setSuccess(false);
            response.setMessage("可能由于网络原因引起的错误，请您稍后重试。");
            response.setStack("不支持" + request.getHttpMethod() + "请求方式，请注册请求方式。");

        }

        return response;
    }

}
