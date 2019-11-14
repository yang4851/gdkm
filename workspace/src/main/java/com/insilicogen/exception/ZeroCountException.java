package com.insilicogen.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="update count is zero")
public class ZeroCountException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ZeroCountException(){
		super("update count is zero");
	}
	

}
