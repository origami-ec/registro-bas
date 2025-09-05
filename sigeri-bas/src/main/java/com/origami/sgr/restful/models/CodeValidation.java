package com.origami.sgr.restful.models;

import java.io.Serializable;

public class CodeValidation implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean validCode;
	
	
	
	public CodeValidation(boolean validCode) {
		super();
		this.validCode = validCode;
	}

	public boolean isValidCode() {
		return validCode;
	}

	public void setValidCode(boolean validCode) {
		this.validCode = validCode;
	}
	
	
	
}
