package com.insilicogen.gdkm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid NGS data.")
public class NgsDataException extends RestServerException {

	private static final long serialVersionUID = -9092054233558343335L;

	public NgsDataException() {
		super();
	}

	public NgsDataException(String msg) {
		super(msg);
	}
	
	public NgsDataException(String msg, HttpStatus httpStatus) {
		super(msg, httpStatus);
	}

	public NgsDataException(Throwable nestedThrowable) {
		super(nestedThrowable);
	}

	public NgsDataException(String msg, Throwable nestedThrowable) {
		super(msg, nestedThrowable);
	}
}
