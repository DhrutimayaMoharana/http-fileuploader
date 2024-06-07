package com.https.fileuploader.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.https.fileuploader.dto.ApiResponse;
import com.https.fileuploader.service.RequestResponseService;
import com.https.fileuploader.util.ConvertionUtility;

@RestController
@RequestMapping("/")
public class RequestResponseController {

	private static final String JSON_BODY_ATTRIBUTE = "JSON_REQUEST_BODY";

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseController.class);

	@Autowired
	private RequestResponseService requestResponseService;

	private byte[] getBytesFromObject(Object object) {
		// Implement conversion logic based on your requirements
		// Example: Convert MultipartFile to byte array
		if (object instanceof MultipartFile) {
			try {
				return ((MultipartFile) object).getBytes();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Handle other types as needed
		return null;
	}

	private byte[] convertMapToByteArray(Map<String, Object> map) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsBytes(map);
	}

//	@PostMapping("/upload")
	private ResponseEntity<?> getRequestBody(NativeWebRequest webRequest) {
		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

		// Get the content type of the request
		String contentType = servletRequest.getHeader("Content-Type");

		// Initialize the request body string
		String requestBody = null;

		// Check if the content type is JSON
		if (contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
			// Handle JSON body
			requestBody = handleJsonBody(webRequest, servletRequest);
		} else if (contentType != null && contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
			// Handle form data
			requestBody = handleFormData(webRequest);
		}

		requestResponseService.saveRequestData(requestBody.getBytes());

		return new ResponseEntity<>(requestBody, HttpStatus.OK);
	}

//	@GetMapping("/")
	private ResponseEntity<?> getRequestBody1(NativeWebRequest webRequest) {
		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

		// Get the content type of the request
		String contentType = servletRequest.getHeader("Content-Type");

		// Initialize the request body string
		String requestBody = null;

		// Check if the content type is JSON
		if (contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
			// Handle JSON body
			requestBody = handleJsonBody(webRequest, servletRequest);
		} else if (contentType != null && contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
			// Handle form data
			requestBody = handleFormData(webRequest);
		}

		requestResponseService.saveRequestData(requestBody.getBytes());

		return new ResponseEntity<>(requestBody, HttpStatus.OK);
	}

	@GetMapping("{path}")
	public ResponseEntity<?> handleGenericRequest(@PathVariable String path,
			@RequestParam(required = false) String type, HttpServletRequest request) throws IOException {
		if (StringUtils.isEmpty(path)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Determine the content type and the corresponding resource to serve
		Resource resource;
		HttpHeaders headers = new HttpHeaders();

		if (path.equals("favicon.ico")) {
			resource = new ClassPathResource("/static/favicon.ico");
			headers.add(HttpHeaders.CONTENT_TYPE, "image/x-icon");
		} else if (path.equals("robots.txt")) {
			resource = new ClassPathResource("/static/robots.txt");
			headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");
		} else if (path.equals("error")) {
			return new ResponseEntity<>("An error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			// Handle other paths
			resource = new ClassPathResource("/static/" + path);
			if (!resource.exists()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			headers.add(HttpHeaders.CONTENT_TYPE, request.getServletContext().getMimeType(resource.getFilename()));
		}

		if (!resource.exists()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}

//	@GetMapping("/upload")
	private ResponseEntity<?> getRequestBody2(
			@RequestHeader(value = "Content-Type", required = false) String contentType,
			@RequestPart(required = false) Map<String, Object> bodyData,
			@RequestParam(required = false, defaultValue = "") Map<String, Object> formData,
			@RequestPart(required = false) MultipartFile file) {
		try {

			Map<String, Object> requestBody = new HashMap<>();
			if (file != null) {
//		Map<String, String[]> paramMap = multipartRequest.getParameterMap();
//		Map<String, Object> params = new HashMap<>();
//		for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
//			params.put(entry.getKey(), String.join(",", entry.getValue()));
//		}
				formData.put("file", file.toString());

				requestResponseService.saveRequestData(convertMapToByteArray(formData));

				requestBody.put("status", "success");
				requestBody.put("message", "Multipart request data saved successfully");

			}

			requestResponseService.saveRequestData(convertMapToByteArray(formData));

			return new ResponseEntity<>(requestBody, HttpStatus.OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

//	@PostMapping("/upload")
	private ResponseEntity<?> getRequestBody3(
			@RequestHeader(value = "Content-Type", required = false) String contentType,
			@RequestPart(required = false) Map<String, Object> bodyData,
			@RequestParam(required = false, defaultValue = "") Map<String, Object> formData,
			@RequestPart(required = false) MultipartFile file) {
		try {

			Map<String, Object> requestBody = new HashMap<>();
			if (file != null) {
//		Map<String, String[]> paramMap = multipartRequest.getParameterMap();
//		Map<String, Object> params = new HashMap<>();
//		for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
//			params.put(entry.getKey(), String.join(",", entry.getValue()));
//		}

				LOGGER.info("File Size " + file.getSize());
				requestResponseService.storeFileInDir(file, file.getOriginalFilename(), null);

				formData.put("file", ConvertionUtility.encodeToBase64(file.getBytes()));

				requestResponseService.saveRequestData(convertMapToByteArray(formData));

				requestBody.put("status", "success");
				requestBody.put("message", "Multipart request data saved successfully");

			} else {

				requestResponseService.saveRequestData(convertMapToByteArray(formData));
			}
			return new ResponseEntity<>(requestBody, HttpStatus.OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private String handleJsonBody(NativeWebRequest webRequest, HttpServletRequest servletRequest) {
		String jsonBody = (String) webRequest.getAttribute(JSON_BODY_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);
		if (jsonBody == null) {
			try (BufferedReader reader = servletRequest.getReader()) {
				// Read JSON body from input stream
				jsonBody = reader.lines().collect(Collectors.joining(System.lineSeparator()));
				// Set JSON body attribute
				webRequest.setAttribute(JSON_BODY_ATTRIBUTE, jsonBody, NativeWebRequest.SCOPE_REQUEST);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return jsonBody;
	}

	private String handleFormData(NativeWebRequest webRequest) {
		StringBuilder formDataBuilder = new StringBuilder();

		try {
			// Iterate over the parts of the multipart request
			Collection<Part> parts = webRequest.getNativeRequest(HttpServletRequest.class).getParts();
			for (Part part : parts) {
				String name = part.getName();
				if (part.getContentType() == null) {
					// It's a text parameter
					String value = webRequest.getParameter(name);
					if (value != null && !value.isEmpty()) {
						formDataBuilder.append(name).append(": ").append(value).append(", ");
					}
				} else {
					// It's a file parameter
					String fileName = part.getSubmittedFileName();
					requestResponseService.storeFileInDir(null, fileName, part.getInputStream());
					long fileSize = part.getSize();

					if (fileName != null && !fileName.isEmpty()) {
						formDataBuilder.append(name).append(": ").append(fileName).append(" (").append(fileSize)
								.append(" bytes), ");
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error processing multipart request", e);
		}

		// Remove the trailing comma and space
		String formData = formDataBuilder.length() > 0 ? formDataBuilder.substring(0, formDataBuilder.length() - 2)
				: "";

		return formData;
	}

//	@GetMapping("/get")
	public ResponseEntity<?> getRequestData(@RequestParam(required = false) String filename) {

		ApiResponse resource = requestResponseService.getRequestData(filename);
		if (resource.getData() != null) {
			// Build the response entity
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource.getData());
		}
		return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), filename), HttpStatus.OK);
	}

}
