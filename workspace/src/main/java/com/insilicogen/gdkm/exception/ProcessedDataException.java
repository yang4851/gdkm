package com.insilicogen.gdkm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid Processed data.")
public class ProcessedDataException extends NgsDataException {

	private static final long serialVersionUID = -9092054233558343335L;

	public ProcessedDataException() {
		super();
	}

	public ProcessedDataException(String msg) {
		super(msg);
	}
	
	public ProcessedDataException(String msg, HttpStatus httpStatus) {
		super(msg, httpStatus);
	}

	public ProcessedDataException(Throwable nestedThrowable) {
		super(nestedThrowable);
	}

	public ProcessedDataException(String msg, Throwable nestedThrowable) {
		super(msg, nestedThrowable);
	}
}
