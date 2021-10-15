package com.pageOfficeServer.loginkingdee;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pageOfficeServer.util.httpUtil.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录认证工具类
 * 
 * @author LiangMingHui
 *
 */
public class AuthUtil {

	private static final Map<String,String> tokenMap = new HashMap<>();
	/**
	 * 登录认证
	 * 
	 * @param tenantId
	 * @param account
	 * @param password
	 * @return
	 */
	public static BaseResult<Object> auth(String tenantId, String account, String password) {

		// 发送请求
		ClientRequest clientRequest = new ClientRequest();
		clientRequest.setMethod("login.do");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("tenantid", tenantId);
		jsonObject.put("accountId", SystemServiceConstant.SRV_CON_ACCOUNT_ID);
		jsonObject.put("user", account);
		jsonObject.put("password", password);
		jsonObject.put("logintype", "2");
		clientRequest.setData(jsonObject);

		// 请求平台接口
		ClientResponse response = HttpClientUtil.getInstance().sendByPlatform(clientRequest);
		BaseResult<Object> result = new BaseResult<Object>(response);

		if (response.getSuccess()) {
			result.setData(response.getData());
		} else {
		}

		return result;
	}

	/**
	 * 缓存通用access_token
	 * 
	 * @param accessToken
	 */
	public static void setCommonAccessToken(String accessToken) {
//		DistributeSessionlessCache distributeSessionlessCache = CacheFactory.getCommonCacheFactory().getDistributeSessionlessCache();
//		distributeSessionlessCache.put(SystemServiceConstant.COMMON_ACCESS_TOKEN_KEY, accessToken);
		tokenMap.put(SystemServiceConstant.COMMON_ACCESS_TOKEN_KEY, accessToken);
	}

	/**
	 * 获取通用access_token
	 */
	public static String getCommonAccessToken() {
//		DistributeSessionlessCache distributeSessionlessCache = CacheFactory.getCommonCacheFactory().getDistributeSessionlessCache();
//		return distributeSessionlessCache.get(SystemServiceConstant.COMMON_ACCESS_TOKEN_KEY);
		return tokenMap.get(SystemServiceConstant.COMMON_ACCESS_TOKEN_KEY);
	}

	/**
	 * 刷新公共账户的AccessToken
	 */
	public static void refreshCommonAccessToken(){
		String tenantId = SystemServiceConstant.SRV_CON_TENANT_ID;
		String account = SystemServiceConstant.SRV_CON_YOUKE_NAME;
		String password = SystemServiceConstant.SRV_CON_YOUKE_PASSWORD;
		// 登录认证
		BaseResult<Object> result = auth(tenantId, account, password);
		if (CodeConstant.SUCCESS_CODE == result.getCode()) {
			JSONObject resultData = JSONObject.parseObject(JSONObject.toJSONString(result.getData(),
                    SerializerFeature.DisableCircularReferenceDetect));
			setCommonAccessToken(resultData.getString("access_token")); // 缓存通用access_token
			System.out.println("通用access_token => " + resultData.getString("access_token"));
		} else {
		}
	}
}
