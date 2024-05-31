package com.https.fileuploader.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.https.fileuploader.entity.RequestResponseEntity;
import com.https.fileuploader.service.RequestResponseService;
import com.https.fileuploader.util.ConvertionUtility;

@JsonInclude(Include.NON_NULL)
public class RequestResponseDto {

	private Long id;

	private Map<String, Object> requestPayload;

	private LocalDateTime createdAt;

	public RequestResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RequestResponseDto(Long id, Map<String, Object> requestPayload, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.requestPayload = requestPayload;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, Object> getRequestPayload() {
		return requestPayload;
	}

	public void setRequestPayload(Map<String, Object> requestPayload) {
		this.requestPayload = requestPayload;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public static RequestResponseDto convertFromRequestResponseEntity(RequestResponseEntity requestResponseEntity) {
		return new RequestResponseDto(requestResponseEntity.getId(),
				ConvertionUtility.convertByteArrayToMap(requestResponseEntity.getRequestPayload()),
				requestResponseEntity.getCreatedAt());
	}

}
