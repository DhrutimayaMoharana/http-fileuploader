package com.https.fileuploader.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.https.fileuploader.dto.ApiResponse;

public interface FileService {

	public ApiResponse uploadFile(MultipartFile file, String filename, String timestamp, String sign,
			HttpServletResponse response);

	public ApiResponse uploadFileApiRegister(String contentType, Map<String, Object> bodyData,
			Map<String, Object> formData, MultipartFile file);

	public ApiResponse getRequestData(String filename);

}
