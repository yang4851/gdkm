package com.insilicogen.gdkm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="NGS data process unavaliable")
public class NgsDataProcessException extends NgsDataException {

	private static final long serialVersionUID = 1210712865388309074L;

	public NgsDataProcessException() {
		super();
		setHttpStatus(HttpStatus.BAD_REQUEST);
	}

	public NgsDataProcessException(String msg) {
		super(msg, HttpStatus.BAD_REQUEST);
	}
	
	public NgsDataProcessException(String msg, HttpStatus httpStatus) {
		super(msg, httpStatus);
	}

	public NgsDataProcessException(Throwable nestedThrowable) {
		super(nestedThrowable);
		setHttpStatus(HttpStatus.BAD_REQUEST);
	}

	public NgsDataProcessException(String msg, Throwable nestedThrowable) {
		super(msg, nestedThrowable);
		setHttpStatus(HttpStatus.BAD_REQUEST);
	}
}
