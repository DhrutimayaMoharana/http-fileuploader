package com.https.fileuploader.dto;

import org.springframework.util.MultiValueMap;

public class RequestEntityDto {

	private String url;
	private String body;
	private String mediaType;
	private String httpMethodType;
//	private MultiValueMap<String, String> formData;

	public RequestEntityDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RequestEntityDto(String url, String body, String mediaType, String httpMethodType,
			MultiValueMap<String, String> formData) {
		super();
		this.url = url;
		this.body = body;
		this.mediaType = mediaType;
		this.httpMethodType = httpMethodType;
//		this.formData = formData;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getHttpMethodType() {
		return httpMethodType;
	}

	public void setHttpMethodType(String httpMethodType) {
		this.httpMethodType = httpMethodType;
	}

//	public MultiValueMap<String, String> getFormData() {
//		return formData;
//	}
//
//	public void setFormData(MultiValueMap<String, String> formData) {
//		this.formData = formData;
//	}

}
