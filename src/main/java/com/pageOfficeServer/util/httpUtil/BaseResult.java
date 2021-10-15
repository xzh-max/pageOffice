package com.pageOfficeServer.util.httpUtil;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

/**
 * 基础结果集
 * 
 * @author wangcheng
 *
 * @param <T>
 */
public class BaseResult<T> implements Serializable {

	private static final long serialVersionUID = 3624091113024618354L;

	/**
	 * 响应代码
	 */
	private Integer code = CodeConstant.FAILURE_CODE;

	/**
	 * 返回信息,中台返回的信息
	 */
	private String message;

	/**
	 * 前端后端错误的返回
	 */
	private BaseMessageEnum errorResponseMessage;

	/**
	 * 前端后端成功的返回
	 */
	private BaseMessageEnum successResponseMessage;

	/**
	 * 响应数据
	 */
	private T data;

	/**
	 * 构造函数
	 */
	public BaseResult() {

	}

	/**
	 * 构造函数
	 * 
	 * @param clientResponse
	 */
	public BaseResult(ClientResponse clientResponse) {
		if (clientResponse.getSuccess()) {
			this.setCode(CodeConstant.SUCCESS_CODE);
		} else {
			if (CodeConstant.EXPIRED_TOKEN_CODE == clientResponse.getCode()) {
				this.setCode(CodeConstant.EXPIRED_TOKEN_CODE);
			} else {
				this.setCode(CodeConstant.FAILURE_CODE);
			}
			this.setMessage(clientResponse.getMessage());
		}
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public BaseMessageEnum getErrorResponseMessage() {
		return errorResponseMessage;
	}

	public void setErrorResponseMessage(BaseMessageEnum errorResponseMessage) {
		this.errorResponseMessage = errorResponseMessage;
	}

	public BaseMessageEnum getSuccessResponseMessage() {
		return successResponseMessage;
	}

	public void setSuccessResponseMessage(BaseMessageEnum successResponseMessage) {
		this.successResponseMessage = successResponseMessage;
	}

	public String toString() {
		return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
	}

}
