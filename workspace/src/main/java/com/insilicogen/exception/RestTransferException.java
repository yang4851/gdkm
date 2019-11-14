package com.insilicogen.exception;

import java.io.PrintStream;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Unknown error")
public class RestTransferException extends RuntimeException {

	private static final long serialVersionUID = -1;

	protected Throwable nestedThrowable;

	protected HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	
	public RestTransferException() {
		this.nestedThrowable = null;
	}

	public RestTransferException(String msg) {
		super(msg);
		this.nestedThrowable = null;
	}
	
	public RestTransferException(String msg, HttpStatus httpStatus) {
		super(msg);
		this.httpStatus = httpStatus;
		this.nestedThrowable = null;
	}

	public RestTransferException(Throwable nestedThrowable) {
		this.nestedThrowable = nestedThrowable;
	}

	public RestTransferException(String msg, Throwable nestedThrowable) {
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
