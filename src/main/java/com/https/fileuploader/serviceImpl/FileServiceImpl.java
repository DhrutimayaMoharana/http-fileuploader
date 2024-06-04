package com.https.fileuploader.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.https.fileuploader.dto.ApiResponse;
import com.https.fileuploader.service.FileService;
import com.https.fileuploader.service.RequestResponseService;
import com.https.fileuploader.util.ConvertionUtility;
import com.https.fileuploader.util.FileUtility;

import javassist.bytecode.stackmap.TypeData.ClassName;

@Service
public class FileServiceImpl implements FileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassName.class);

	private final String DIRECTORY;

	private final String secretKey;

	@Autowired
	private RequestResponseService requestResponseService;

	public FileServiceImpl(@Value("${file.upload.dir}") String directory,
			@Value("${file.upload.secretKey}") String secretKey) {
		this.DIRECTORY = directory;
		this.secretKey = secretKey;
	}

	@Override
	public ApiResponse uploadFile(MultipartFile file, String filename, String timestamp, String sign,
			HttpServletResponse response) {

//		response.setCharacterEncoding("UTF-8");

		// Validate the sign
		if (!isValidSign(filename, timestamp, sign)) {
//			return new ApiResponse(403, "Invalid sign");
		}

		if (file.isEmpty()) {
			new ApiResponse(400, "File is empty");
		}

		LOGGER.info("File Size  ::  " + file.getSize());

		Map<String, Object> uploadRequest = new HashMap<>();

		uploadRequest.put("file", file.getOriginalFilename());
		uploadRequest.put("filename", filename);
		uploadRequest.put("timestamp", timestamp);
		uploadRequest.put("sign", sign);
		uploadRequest.put("filesize", ConvertionUtility.bytesToKilobytes(file.getSize()));

		requestResponseService.saveRequestData(ConvertionUtility.convertMapToByteArray(uploadRequest));

		// Ensure the directory exists
		File dir = new File(DIRECTORY);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// Generate file path
		Path path = Paths.get(DIRECTORY, filename);

		// Check if file with the same name exists
//		if (Files.exists(path)) {
//			return new ApiResponse(409, "File with the same name already exists");
//		}

		try {
			// Save the file
			Files.write(path, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return new ApiResponse(500, "Failed to save file");
		}

		return new ApiResponse(HttpStatus.OK.value(), filename, " the high success ");
	}

	@Override
	public ApiResponse uploadFileApiRegister(String contentType, Map<String, Object> bodyData,
			Map<String, Object> formData, MultipartFile file) {
		try {

			Map<String, Object> requestBody = new HashMap<>();
			if (file != null) {
				formData.put("file", file.getBytes());
				requestResponseService.saveRequestData(ConvertionUtility.convertMapToByteArray(formData));
				requestBody.put("status", "success");
				requestBody.put("message", "Multipart request data saved successfully");
			}

			return new ApiResponse(200, file != null ? file.getOriginalFilename() : null, "File uploaded successfully");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ApiResponse(500, "Failed to save file");
		}

	}

	private boolean isValidSign(String filename, String timestamp, String sign) {
		try {
			String data = filename + timestamp + secretKey;
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hashInBytes = md.digest(data.getBytes());

			// Convert byte array into signum representation
			StringBuilder sb = new StringBuilder();
			for (byte b : hashInBytes) {
				sb.append(String.format("%02x", b));
			}

			// Base64 encode the MD5 hash
			String calculatedSign = Base64.getEncoder().encodeToString(sb.toString().getBytes());

			// Compare the provided sign with the calculated sign
			return sign.equals(calculatedSign);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public ApiResponse getRequestData(String filename) {
		LOGGER.info("Inside Store File !!!!!");

		// Ensure the directory exists
		File dir = new File(DIRECTORY);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// Generate file path
		Path path = Paths.get(DIRECTORY, filename != null && !filename.isEmpty() ? filename : new Date().toString());

		if (FileUtility.checkFileExistance(path.toUri().getPath())) {
			try {
				// Convert the file to ByteArrayResource
				ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));

				if (byteArrayResource != null) {
					// Use the ByteArrayResource as needed
					return new ApiResponse(HttpStatus.OK.value(), byteArrayResource, HttpStatus.OK.name());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Someting went wrong, Try after some time!!");
		}
		return null;
	}

	@Override
	public ApiResponse chekFileExistOrNot(String filename) {
		LOGGER.info("Inside Check File !!!!!");

		// Ensure the directory exists
		File dir = new File(DIRECTORY);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// Generate file path
		Path path = Paths.get(DIRECTORY, filename != null && !filename.isEmpty() ? filename : new Date().toString());

		if (FileUtility.checkFileExistance(path.toUri().getPath())) {

			return new ApiResponse(HttpStatus.OK.value(), "File is Exist!!!");

		} else {
			return new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Someting went wrong, Try after some time!!");
		}
	}

}
