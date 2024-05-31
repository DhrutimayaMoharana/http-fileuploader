package com.https.fileuploader.service;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.https.fileuploader.dto.ApiResponse;
import com.https.fileuploader.entity.RequestResponseEntity;

public interface RequestResponseService {

	public RequestResponseEntity saveRequestData(byte[] bs);

	void storeFileInDir(MultipartFile file, String filename, InputStream inputStream);

	ApiResponse getRequestData(String filename);

}
