package com.insilicogen.gdkm.exception;

import java.io.PrintStream;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="internal server error")
public class RestServerException extends RuntimeException {

	private static final long serialVersionUID = -7795947687238203362L;

	protected Throwable nestedThrowable;

	protected HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	
	public RestServerException() {
		this.nestedThrowable = null;
	}

	public RestServerException(String msg) {
		super(msg);
		this.nestedThrowable = null;
	}
	
	public RestServerException(String msg, HttpStatus httpStatus) {
		super(msg);
		this.httpStatus = httpStatus;
		this.nestedThrowable = null;
	}

	public RestServerException(Throwable nestedThrowable) {
		this.nestedThrowable = nestedThrowable;
	}

	public RestServerException(String msg, Throwable nestedThrowable) {
		super(msg);
		this.nestedThrowable = nestedThrowable;
	}

	public void printStackTrace() {
		super.printStackTrace();
		if (nestedThrowable != null) {
			nestedThrowable.printStackTrace();
		}
	}

	public void pritnStackStrace(PrintStream ps) {
		super.printStackTrace(ps);
		if (nestedThrowable != null) {
			nestedThrowable.printStackTrace(ps);
		}
	}
	
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
}
