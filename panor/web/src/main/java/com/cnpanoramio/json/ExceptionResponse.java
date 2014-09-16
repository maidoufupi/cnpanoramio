package com.cnpanoramio.json;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ExceptionResponse {
	
	public ExceptionResponse() {
		super();
	}
	
	public ExceptionResponse(Status status) {
		super();
		this.status = status;
	}
	
	public static enum Status {
		/**
		 * 一切正常
		 */
		OK,
		/**
		 * 图片ID格式错误
		 */
		ID_FORMAT_ERROR,
		/**
		 * 输入参数错误
		 */
		PARAM_ERROR,
		/**
		 * 找不到对象
		 */
		NO_ENTITY,
		/**
		 * 未授权
		 */
		NO_AUTHORIZE,
		/**
		 * 访问被拒绝
		 */
		ACCESS_DENIED,
		/**
		 * 出现异常
		 */
		EXCEPTION
	}
	
	private Status status;
	
	private String info;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void setStatus(String status) {
		this.status = Status.valueOf(status);
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
