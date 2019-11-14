package com.insilicogen.gdkm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid research data.")
public class ResearchException extends RestServerException {

	private static final long serialVersionUID = -9092054233558343335L;

	public ResearchException() {
		super();
	}

	public ResearchException(String msg) {
		super(msg);
	}
	
	public ResearchException(String msg, HttpStatus httpStatus) {
		super(msg, httpStatus);
	}

	public ResearchException(Throwable nestedThrowable) {
		super(nestedThrowable);
	}

	public ResearchException(String msg, Throwable nestedThrowable) {
		super(msg, nestedThrowable);
	}
}
