package com.nexsol.cargo.core.api.controller;

import com.nexsol.cargo.core.api.support.error.CoreApiErrorType;
import com.nexsol.cargo.core.api.support.error.CoreApiException;
import com.nexsol.cargo.core.api.support.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

	private static final Logger log = LoggerFactory.getLogger(ApiControllerAdvice.class);

	@ExceptionHandler(CoreApiException.class)
	public ResponseEntity<ApiResponse<Object>> handleCoreException(CoreApiException e) {

		switch (e.getCoreApiErrorType().getLogLevel()) {
			case ERROR -> log.error("CoreException : {}", e.getMessage(), e);
			case WARN -> log.warn("CoreException : {}", e.getMessage(), e);
			default -> log.info("CoreException : {}", e.getMessage(), e);
		}

		ApiResponse<Object> apiResponse = ApiResponse.error(e.getCoreApiErrorType(), e.getData());

		return new ResponseEntity<>(apiResponse, e.getCoreApiErrorType().getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
		log.error("Exception : {}", e.getMessage(), e);

		ApiResponse<Object> apiResponse = ApiResponse.error(CoreApiErrorType.DEFAULT_ERROR, null);

		return new ResponseEntity<>(apiResponse, CoreApiErrorType.DEFAULT_ERROR.getStatus());
	}

}