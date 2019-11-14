package com.insilicogen.gdkm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="user error")
public class UserException extends RestServerException {

	private static final long serialVersionUID = 1947209745781160211L;
	
	public UserException() {
		super();
	}

	public UserException(String msg) {
		super(msg);
	}
	
	public UserException(String msg, HttpStatus httpStatus) {
		super(msg, httpStatus);
	}

	public UserException(Throwable nestedThrowable) {
		super(nestedThrowable);
	}

	public UserException(String msg, Throwable nestedThrowable) {
		super(msg, nestedThrowable);
	}
}
