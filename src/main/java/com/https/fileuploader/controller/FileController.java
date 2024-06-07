package com.https.fileuploader.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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

//	@PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("filename") String filename, @RequestParam("timestamp") String timestamp,
			@RequestParam("sign") String sign, HttpServletResponse httpServletResponse) {

		ApiResponse response = fileService.uploadFile(file, filename, timestamp, sign, httpServletResponse);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));

	}
	
	
	@PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadFilev1(@RequestParam("file") MultipartFile file,
                           @RequestParam("filename") String filename, 
                           @RequestParam("timestamp") String timestamp,
                           @RequestParam("sign") String sign, 
                           HttpServletResponse httpServletResponse) throws IOException {

        // Set headers for chunked transfer encoding
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setHeader("Transfer-Encoding", "chunked");

        // Create the response object
        ApiResponse response = fileService.uploadFile(file, filename, timestamp, sign,httpServletResponse);

        // Convert the response to JSON string
        String jsonResponse = String.format("{\"code\":%d,\"message\":\"%s\",\"data\":\"%s\"}",
                                            response.getCode(), response.getMessage(), response.getData());

        // Convert to bytes
        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        int chunkSize = responseBytes.length;

        // Get the output stream
        ServletOutputStream out = httpServletResponse.getOutputStream();

        // Write the chunk size in hexadecimal followed by CRLF
        out.print(Integer.toHexString(chunkSize) + "\r\n");
        // Write the actual chunk data
        out.write(responseBytes);
        // Write CRLF to end the chunk
        out.print("\r\n");

        // Write the final chunk (0 length) to indicate the end of the response
        out.print("0\r\n\r\n");

        // Flush and close the output stream
        out.flush();
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
	
	@GetMapping("/check")
	public ResponseEntity<?> chekFileExistOrNot(@RequestParam(required = false, name = "file") String filename) {

		ApiResponse resource = fileService.chekFileExistOrNot(filename);
		
		return new ResponseEntity<>(resource, HttpStatus.valueOf(resource.getCode()));
	}
	
	

}
