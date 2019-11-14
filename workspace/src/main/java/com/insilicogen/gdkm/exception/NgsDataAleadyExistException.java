package com.insilicogen.gdkm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="NGS data aleady exist")
public class NgsDataAleadyExistException extends NgsDataException {

	private static final long serialVersionUID = 4540042256059410350L;

	public NgsDataAleadyExistException() {
		super();
		setHttpStatus(HttpStatus.BAD_REQUEST);
	}

	public NgsDataAleadyExistException(String msg) {
		super(msg, HttpStatus.BAD_REQUEST);
	}
	
	public NgsDataAleadyExistException(String msg, HttpStatus httpStatus) {
		super(msg, httpStatus);
		setHttpStatus(HttpStatus.BAD_REQUEST);
	}

	public NgsDataAleadyExistException(Throwable nestedThrowable) {
		super(nestedThrowable);
		setHttpStatus(HttpStatus.BAD_REQUEST);
	}

	public NgsDataAleadyExistException(String msg, Throwable nestedThrowable) {
		super(msg, nestedThrowable);
		setHttpStatus(HttpStatus.BAD_REQUEST);
	}
}
