package com.insilicogen.gdkm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="NGS data does not exist")
public class NgsDataNotFoundException extends NgsDataException {

	private static final long serialVersionUID = -9092054233558343335L;
	
	public NgsDataNotFoundException() {
		super();
		setHttpStatus(HttpStatus.NOT_FOUND);
	}

	public NgsDataNotFoundException(String msg) {
		super(msg, HttpStatus.NOT_FOUND);
	}
	
	public NgsDataNotFoundException(String msg, HttpStatus httpStatus) {
		super(msg, httpStatus);
	}

	public NgsDataNotFoundException(Throwable nestedThrowable) {
		super(nestedThrowable);
		setHttpStatus(HttpStatus.NOT_FOUND);
	}

	public NgsDataNotFoundException(String msg, Throwable nestedThrowable) {
		super(msg, nestedThrowable);
		setHttpStatus(HttpStatus.NOT_FOUND);
	}
}
