package com.insilicogen.gdkm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Research data does not exist")
public class ResearchNotFoundException extends ResearchException {

	private static final long serialVersionUID = -9092054233558343335L;
	
	public ResearchNotFoundException() {
		super();
		setHttpStatus(HttpStatus.NOT_FOUND);
	}

	public ResearchNotFoundException(String msg) {
		super(msg, HttpStatus.NOT_FOUND);
	}
	
	public ResearchNotFoundException(String msg, HttpStatus httpStatus) {
		super(msg, httpStatus);
	}

	public ResearchNotFoundException(Throwable nestedThrowable) {
		super(nestedThrowable);
		setHttpStatus(HttpStatus.NOT_FOUND);
	}

	public ResearchNotFoundException(String msg, Throwable nestedThrowable) {
		super(msg, nestedThrowable);
		setHttpStatus(HttpStatus.NOT_FOUND);
	}
}
