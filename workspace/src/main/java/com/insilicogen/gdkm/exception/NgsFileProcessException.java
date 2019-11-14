package com.insilicogen.gdkm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="NGS file process unavaliable")
public class NgsFileProcessException extends NgsDataException {

	private static final long serialVersionUID = 1210712865388309074L;

	public NgsFileProcessException() {
		super();
		setHttpStatus(HttpStatus.BAD_REQUEST);
	}

	public NgsFileProcessException(String msg) {
		super(msg, HttpStatus.BAD_REQUEST);
	}
	
	public NgsFileProcessException(String msg, HttpStatus httpStatus) {
		super(msg, httpStatus);
	}

	public NgsFileProcessException(Throwable nestedThrowable) {
		super(nestedThrowable);
		setHttpStatus(HttpStatus.BAD_REQUEST);
	}

	public NgsFileProcessException(String msg, Throwable nestedThrowable) {
		super(msg, nestedThrowable);
		setHttpStatus(HttpStatus.BAD_REQUEST);
	}
}
