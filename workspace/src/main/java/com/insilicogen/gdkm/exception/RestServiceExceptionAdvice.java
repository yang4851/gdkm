package com.insilicogen.gdkm.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestServiceExceptionAdvice {

	@ExceptionHandler(RestServerException.class)
	public ResponseEntity<String> handleException(RestServerException e) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain;charset=UTF-8");
		
		return new ResponseEntity<String>(e.getMessage(), headers, e.getHttpStatus());
	}
}
