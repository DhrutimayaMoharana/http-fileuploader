package com.https.fileuploader.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ApiResponse {

	private int code;
	private Object data;
	private String message;

	public ApiResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApiResponse(int code, Object data, String message) {
		super();
		this.code = code;
		this.data = data;
		this.message = message;
	}

	public ApiResponse(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public ApiResponse(int code) {
		super();
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
