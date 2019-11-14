package com.insilicogen.gdkm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid Raw data.")
public class RawDataException extends NgsDataException {

	private static final long serialVersionUID = -9092054233558343335L;

	public RawDataException() {
		super();
	}

	public RawDataException(String msg) {
		super(msg);
	}
	
	public RawDataException(String msg, HttpStatus httpStatus) {
		super(msg, httpStatus);
	}

	public RawDataException(Throwable nestedThrowable) {
		super(nestedThrowable);
	}

	public RawDataException(String msg, Throwable nestedThrowable) {
		super(msg, nestedThrowable);
	}
}
