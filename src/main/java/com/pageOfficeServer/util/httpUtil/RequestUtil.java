package com.pageOfficeServer.util.httpUtil;

import com.alibaba.fastjson.JSONObject;
import com.pageOfficeServer.loginkingdee.CacheMap;
import com.pageOfficeServer.loginkingdee.DESUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求工具类
 *
 * @author wangcheng
 */
public class RequestUtil {


    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 可是如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取请求头部Map对象
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getHeaderMap(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();

//		// 获取请求头部
//		ApiRequestContext context = ApiRequestContext.get();
//		String deviceId = context.getHeaderValue(HeaderConstant.DEVICE_ID_KEY);
//		if (StringUtils.isNotBlank(deviceId)) {
//			result.put("deviceId", deviceId);
//		}
////		String apiCode = context.getHeaderValue("apiCode");
//		if (StringUtils.isNotBlank(deviceId)) {
//			result.put("deviceId", deviceId);
//		}
//		String deviceType = context.getHeaderValue(HeaderConstant.DEVICE_TYPE_KEY);
//		if (StringUtils.isNotBlank(deviceType)) {
//			result.put("deviceType", deviceType);
//		}
//		String deviceName = context.getHeaderValue(HeaderConstant.DEVICE_NAME_KEY);
//		if (StringUtils.isNotBlank(deviceName)) {
//			result.put("deviceName", deviceName);
//		}
//		String systemVersion = context.getHeaderValue(HeaderConstant.SYSTEM_VERSION_KEY);
//		if (StringUtils.isNotBlank(systemVersion)) {
//			result.put("systemVersion", systemVersion);
//		}
//		String appVersion = context.getHeaderValue(HeaderConstant.APP_VERSION_KEY);
//		if (StringUtils.isNotBlank(appVersion)) {
//			result.put("appVersion", appVersion);
//		}
//		String accessToken = context.getHeaderValue(HeaderConstant.ACCESS_TOKEN_KEY);
//		if (StringUtils.isNotBlank(accessToken)) {
//			try {
//				JSONObject accessTokenJson = JSONObject.parseObject(DESUtil.decrypt(accessToken)); // 解密登录认证信息
//				if (null != accessTokenJson) {
//					result.putAll(JSONObject.parseObject(accessTokenJson.toJSONString(), Map.class));
//				}
//			} catch (Exception e) {
//			}
//		} else {
        result.put("account", SystemServiceConstant.SRV_CON_ACCOUNT_ID);
//			result.put("token", AuthUtil.getCommonAccessToken()); // 获取通用access_token
        result.put("token", CacheMap.getUncheck("accessToken")); // 获取通用access_token
        result.put("timestamp", new Date().getTime());
//		}

        return result;
    }

    /**
     * 获取请求参数Map对象
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getParameterMap(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();

        // 获取请求参数
        String method = request.getMethod();
        if (method.equalsIgnoreCase("POST")) {
            String postData = null;
            try {
                postData = toString(request.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (StringUtils.isNotBlank(postData)) {
                JSONObject jsonData = JSONObject.parseObject(postData);
//				if (jsonData.containsKey("params")) {
                result.putAll(jsonData);
//				}
            }
        } else if (method.equalsIgnoreCase("GET")) {
            String getData = request.getQueryString();
            if (StringUtils.isNotBlank(getData)) {
                String[] dataArray = getData.split("&");
                for (String keyAndValue : dataArray) {
                    String[] tmpArray = keyAndValue.split("=");
                    if (2 == tmpArray.length) {
                        try {
                            result.put(tmpArray[0], URLDecoder.decode(tmpArray[1], "utf-8"));
                        } catch (UnsupportedEncodingException e) {
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * 获取请求头部和请求参数Map对象
     *
     * @param request
     * @return
     */
    public static Map<String, Object> getHeaderAndParameterMap(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.putAll(getHeaderMap(request));
        result.putAll(getParameterMap(request));
        return result;
    }

    public static String toString(InputStream is) {

        try {
            ByteArrayOutputStream boa = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];

            while ((len = is.read(buffer)) != -1) {
                boa.write(buffer, 0, len);
            }
            is.close();
            boa.close();
            byte[] result = boa.toByteArray();

            String temp = new String(result);

//识别编码
            if (temp.contains("utf-8")) {
                return new String(result, "utf-8");
            } else if (temp.contains("gb2312")) {
                return new String(result, "gb2312");
            } else {
                return new String(result, "utf-8");
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }


}
