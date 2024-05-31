package com.https.fileuploader.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "request_response")
public class RequestResponseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	private byte[] requestPayload;

	private LocalDateTime createdAt;

	public RequestResponseEntity() {
		super();

		// TODO Auto-generated constructor stub
	}

	public RequestResponseEntity(Long id, byte[] requestPayload, LocalDateTime createdAt) {
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

	public byte[] getRequestPayload() {
		return requestPayload;
	}

	public void setRequestPayload(byte[] requestPayload) {
		this.requestPayload = requestPayload;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
