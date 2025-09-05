package com.origami.sgr.exception;

public class SqlRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SqlRuntimeException() {
		super();
	}

	public SqlRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SqlRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SqlRuntimeException(String message) {
		super(message);
	}

	public SqlRuntimeException(Throwable cause) {
		super(cause);
	}
	
	
	
}
