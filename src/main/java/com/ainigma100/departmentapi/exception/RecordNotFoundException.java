package com.ainigma100.departmentapi.exception;

public class RecordNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 623305602923318781L;

	public RecordNotFoundException(String message) {
		super(message);
	}

}
