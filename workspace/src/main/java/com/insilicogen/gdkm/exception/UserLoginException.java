package com.insilicogen.gdkm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE, reason="invalid user information")
public class UserLoginException extends UserException {

	private static final long serialVersionUID = 1243941072847905553L;

	public UserLoginException() {
		super();
		setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
	}

	public UserLoginException(String msg) {
		super(msg, HttpStatus.NOT_ACCEPTABLE);
	}
	
	public UserLoginException(String msg, HttpStatus httpStatus) {
		super(msg, httpStatus);
	}

	public UserLoginException(Throwable nestedThrowable) {
		super(nestedThrowable);
		setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
	}

	public UserLoginException(String msg, Throwable nestedThrowable) {
		super(msg, nestedThrowable);
		setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
	}
}
