package com.insilicogen.gdkm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="user does not exist")
public class UserNotFoundException extends UserException {

	private static final long serialVersionUID = 5192767928817510942L;

	public UserNotFoundException() {
		super();
		setHttpStatus(HttpStatus.NOT_FOUND);
	}

	public UserNotFoundException(String msg) {
		super(msg, HttpStatus.NOT_FOUND);
	}
	
	public UserNotFoundException(String msg, HttpStatus httpStatus) {
		super(msg, httpStatus);
	}

	public UserNotFoundException(Throwable nestedThrowable) {
		super(nestedThrowable);
		setHttpStatus(HttpStatus.NOT_FOUND);
	}

	public UserNotFoundException(String msg, Throwable nestedThrowable) {
		super(msg, nestedThrowable);
		setHttpStatus(HttpStatus.NOT_FOUND);
	}
}
