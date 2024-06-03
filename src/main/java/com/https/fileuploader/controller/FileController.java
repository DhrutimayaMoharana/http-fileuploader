package com.https.fileuploader.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.https.fileuploader.dto.ApiResponse;
import com.https.fileuploader.service.FileService;

@RestController
public class FileController {

	@Autowired
	private FileService fileService;

	@PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("filename") String filename, @RequestParam("timestamp") String timestamp,
			@RequestParam("sign") String sign, HttpServletResponse httpServletResponse) {

		ApiResponse response = fileService.uploadFile(file, filename, timestamp, sign, httpServletResponse);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));

	}

	@GetMapping(value = "/upload")
	public ResponseEntity<?> uploadFileApiRegister(
			@RequestHeader(value = "Content-Type", required = false) String contentType,
			@RequestPart(required = false) Map<String, Object> bodyData,
			@RequestParam(required = false, defaultValue = "") Map<String, Object> formData,
			@RequestPart(required = false) MultipartFile file) {

		ApiResponse response = fileService.uploadFileApiRegister(contentType, bodyData, formData, file);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));

	}

	@GetMapping("/get")
	public ResponseEntity<?> getRequestData(@RequestParam(required = false, name = "file") String filename) {

		ApiResponse resource = fileService.getRequestData(filename);
		if (resource.getData() != null) {
			// Build the response entity
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource.getData());
		}
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

}
