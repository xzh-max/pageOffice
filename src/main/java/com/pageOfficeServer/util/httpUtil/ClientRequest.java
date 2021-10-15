package com.pageOfficeServer.util.httpUtil;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

/**
 * 客户端请求对象
 * 
 * @author wangcheng
 *
 */
public class ClientRequest implements Serializable {

	private static final long serialVersionUID = 4352906592186010143L;

	/**
	 * 请求方式
	 */
	private String httpMethod ="POST";

	/**
	 * 请求数据
	 */
	private String data;

	/**
	 * 请求地址
	 */
	private String url;

	/**
	 * 请求接口
	 */
	private String method;

	/**
	 * 会话凭证
	 */
	private String token;

	public ClientRequest() {
		super();
	}

	public ClientRequest(String method) {
		super();
		this.setMethod(method);
	}

	public ClientRequest(String method, Object data) {
		super();
		this.setMethod(method);
		this.setData(data);
	}

	public ClientRequest(String httpMethod, String method, Object data) {
		super();
		this.setHttpMethod(httpMethod);
		this.setMethod(method);
		this.setData(data);
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public JSONObject getData() {
		return JSONObject.parseObject(data);
	}

	public void setData(Object data) {
		JSONObject obj = JSONObject.parseObject(JSONObject.toJSONString(data, SerializerFeature.DisableCircularReferenceDetect));
		if (obj.containsKey("token")) {
			String token = obj.getString("token");
			this.token = token;
		}
		this.data = obj.toString();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String toString() {
		return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
	}

}
