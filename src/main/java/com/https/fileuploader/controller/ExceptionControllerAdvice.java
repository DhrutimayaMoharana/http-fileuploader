package com.https.fileuploader.controller;

import java.io.FileNotFoundException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.https.fileuploader.dto.ApiResponse;

@ControllerAdvice
public class ExceptionControllerAdvice {

	@ExceptionHandler(ClassNotFoundException.class)
	public ResponseEntity<ApiResponse> handleClassNotFoundException(ClassNotFoundException ex, WebRequest request) {
		ApiResponse errorResponse = new ApiResponse(HttpStatus.NOT_FOUND.value(), "Class not found: " + ex.getMessage(),
				Instant.now().toEpochMilli());

		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<ApiResponse> handleFileNotFoundException(FileNotFoundException ex, WebRequest request) {
		ApiResponse errorResponse = new ApiResponse(HttpStatus.NOT_FOUND.value(), "File not found: " + ex.getMessage(),
				Instant.now().toEpochMilli());

		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleAllExceptions(Exception ex, WebRequest request) {
		ApiResponse errorResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(),
				Instant.now().toEpochMilli());

		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// You can add more specific exception handlers here if needed
}
