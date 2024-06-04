package com.https.fileuploader.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
public class LoggingFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

		long startTime = System.currentTimeMillis();
		filterChain.doFilter(requestWrapper, responseWrapper);
		long timeTaken = System.currentTimeMillis() - startTime;

		String requestBody = getStringValue(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
		String responseBody = getStringValue(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding());

		try {
			Enumeration<String> attributeNames = request.getParameterNames();

			if (ServletFileUpload.isMultipartContent(request)) {
				requestBody = requestBody + "file : [IN Binary],";
				DiskFileItemFactory factory = new DiskFileItemFactory();

				// Create a new file upload handler
				ServletFileUpload upload = new ServletFileUpload(factory);

				// Parse the request
				Map<String, List<FileItem>> items = upload.parseParameterMap(request);

				// Process the uploaded items
				for (List<FileItem> itemList : items.values()) {
					for (FileItem item : itemList) {
						// If the item is a file
						if (!item.isFormField()) {
							// Get the file key
							String fieldName = item.getFieldName();
							// Get the file name
							String fileName = item.getName();
							// Get the file size
							long fileSize = item.getSize();

							LOGGER.info("Field Name: " + fieldName);
							LOGGER.info("File Name: " + fileName);
							LOGGER.info("File Size: " + fileSize + " bytes");
						}
					}
				}
			}

			if (attributeNames.hasMoreElements()) {

				while (attributeNames.hasMoreElements()) {
					String attributeName = attributeNames.nextElement();
					Object attributeValue = request.getParameter(attributeName);
					requestBody = requestBody + attributeName + " : " + attributeValue.toString() + ",\n";
					LOGGER.info(attributeName + " : " + attributeValue.toString());
				}
			} else {
				String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
				requestBody = body;
				LOGGER.info("Request Body" + " : " + body);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		LOGGER.info(
				"FINISHED PROCESSING : METHOD={}; REQUESTURI={}; REQUEST PAYLOAD={}; RESPONSE CODE={}; RESPONSE={}; TIME TAKEN={}",
				request.getMethod(), request.getRequestURI(), requestBody, response.getStatus(), responseBody,
				timeTaken);
		responseWrapper.copyBodyToResponse();

	}

	private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
		try {
			return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

}
