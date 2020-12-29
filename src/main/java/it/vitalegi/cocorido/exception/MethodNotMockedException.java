package it.vitalegi.cocorido.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MethodNotMockedException extends RuntimeException {
	public MethodNotMockedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public MethodNotMockedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public MethodNotMockedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MethodNotMockedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
