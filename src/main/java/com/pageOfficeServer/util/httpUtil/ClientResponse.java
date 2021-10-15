package com.pageOfficeServer.util.httpUtil;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

/**
 * 客户端响应对象
 * 
 * @author wangcheng
 *
 */
public class ClientResponse implements Serializable {

	private static final long serialVersionUID = 4734932165532976515L;

	/**
	 * 返回状态
	 */
	private boolean success;

	/**
	 * 返回代码
	 */
	private int code;

	/**
	 * 返回信息
	 */
	private String message;

	/**
	 * 返回堆栈
	 */
	private String stack;

	/**
	 * 返回结果
	 */
	private Object data;

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStack() {
		return stack;
	}

	public void setStack(String stack) {
		this.stack = stack;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String toString() {
		return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
	}

}
