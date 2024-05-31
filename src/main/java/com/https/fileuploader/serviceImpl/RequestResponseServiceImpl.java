package com.https.fileuploader.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.https.fileuploader.dto.ApiResponse;
import com.https.fileuploader.entity.RequestResponseEntity;
import com.https.fileuploader.repository.RequestResponseRepository;
import com.https.fileuploader.service.RequestResponseService;

@Service
public class RequestResponseServiceImpl implements RequestResponseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseService.class);

	private final String DIRECTORY;

	public RequestResponseServiceImpl(@Value("${file.upload.dir}") String directory) {
		this.DIRECTORY = directory;
	}

	@Autowired
	private RequestResponseRepository requestDataRepository;

	@Override
	public RequestResponseEntity saveRequestData(byte[] bs) {
		RequestResponseEntity request = new RequestResponseEntity();
		request.setRequestPayload(bs);
		request.setCreatedAt(LocalDateTime.now());
		return requestDataRepository.save(request);
	}

	@Override
	public void storeFileInDir(MultipartFile file, String filename, InputStream inputStream) {
		try {

			LOGGER.info("Inside Store File !!!!!");

			// Ensure the directory exists
			File dir = new File(DIRECTORY);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			// Generate file path
			Path path = Paths.get(DIRECTORY,
					filename != null && !filename.isEmpty() ? filename : new Date().toString());
			if (file != null) {
				// Save MultipartFile
				Files.write(path, file.getBytes());
				LOGGER.info("File store success !!!!!");
			} else if (inputStream != null) {
				// Save InputStream
				try (FileOutputStream outputStream = new FileOutputStream(path.toFile())) {
					byte[] buffer = new byte[1024];
					int bytesRead;
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}

					LOGGER.info("File Write success !!!!!");
				}
			}
		} catch (Exception e) {
			LOGGER.info(e.toString());
			LOGGER.info(e.getMessage());
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

		try {
			// Convert the file to ByteArrayResource
			ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));

			if (byteArrayResource != null) {
				// Use the ByteArrayResource as needed
				return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), byteArrayResource);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name());

	}

}
