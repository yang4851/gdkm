package com.insilicogen.gdkm.exception;

import org.springframework.http.HttpStatus;

public class TaxonomyException extends RestServerException {

	private static final long serialVersionUID = 1947209745781160211L;
	
	public TaxonomyException() {
		super();
	}

	public TaxonomyException(String msg) {
		super(msg);
	}
	
	public TaxonomyException(String msg, HttpStatus httpStatus) {
		super(msg, httpStatus);
	}

	public TaxonomyException(Throwable nestedThrowable) {
		super(nestedThrowable);
	}

	public TaxonomyException(String msg, Throwable nestedThrowable) {
		super(msg, nestedThrowable);
	}
}
