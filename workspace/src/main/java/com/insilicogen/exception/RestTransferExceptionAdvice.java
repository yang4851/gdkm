package com.insilicogen.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestTransferExceptionAdvice {

	@ExceptionHandler(RestTransferException.class)
	public ResponseEntity<String> handleException(RestTransferException e) {
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "text/plain;charset=UTF-8");
	    
	    return new ResponseEntity<String>(e.getMessage(), headers, e.getHttpStatus());
	}
}
