//package com.https.fileuploader.config;
//
//import java.io.IOException;
//import java.util.Enumeration;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.tomcat.util.http.fileupload.FileItem;
//import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
//import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class RequestLoggingFilter implements Filter {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);
//
//	@Override
//	public void init(FilterConfig filterConfig) throws ServletException {
//		// Initialization logic if needed
//	}
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//		HttpServletRequest httpRequest = (HttpServletRequest) request;
//
//		// Log request information
//		LOGGER.info("Request URI: " + httpRequest.getRequestURI());
//		LOGGER.info("Request Method: " + httpRequest.getMethod());
//
//		Enumeration<String> attributeNames = httpRequest.getParameterNames();
//
//		if (ServletFileUpload.isMultipartContent(httpRequest)) {
//			DiskFileItemFactory factory = new DiskFileItemFactory();
//
//			// Create a new file upload handler
//			ServletFileUpload upload = new ServletFileUpload(factory);
//
//			// Parse the request
//			Map<String, List<FileItem>> items = upload.parseParameterMap(httpRequest);
//
//			// Process the uploaded items
//			for (List<FileItem> itemList : items.values()) {
//				for (FileItem item : itemList) {
//					// If the item is a file
//					if (!item.isFormField()) {
//						// Get the file key
//						String fieldName = item.getFieldName();
//						// Get the file name
//						String fileName = item.getName();
//						// Get the file size
//						long fileSize = item.getSize();
//
//						LOGGER.info("Field Name: " + fieldName);
//						LOGGER.info("File Name: " + fileName);
//						LOGGER.info("File Size: " + fileSize + " bytes");
//					}
//				}
//			}
//		}
//
//		if (attributeNames.hasMoreElements()) {
//
//			while (attributeNames.hasMoreElements()) {
//				String attributeName = attributeNames.nextElement();
//				Object attributeValue = httpRequest.getParameter(attributeName);
//				LOGGER.info(attributeName + " : " + attributeValue.toString());
//			}
//		} else {
//			String body = httpRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//			LOGGER.info("Request Body" + " : " + body);
//		}
//
//		// Continue the filter chain
//		chain.doFilter(request, response);
//	}
//
//	@Override
//	public void destroy() {
//		// Cleanup logic if needed
//	}
//}
