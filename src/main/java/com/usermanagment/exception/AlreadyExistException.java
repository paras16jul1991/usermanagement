package com.usermanagment.exception;

public class AlreadyExistException extends Exception {
	private String message;

	public AlreadyExistException(String string) {
		this.message = string;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
