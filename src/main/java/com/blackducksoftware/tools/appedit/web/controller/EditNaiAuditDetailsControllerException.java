package com.blackducksoftware.tools.appedit.web.controller;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;

public class EditNaiAuditDetailsControllerException extends AppEditException {
    private static final long serialVersionUID = 2054672808081635193L;
    private final String returnValue;

    public EditNaiAuditDetailsControllerException(String returnValue,
	    String message) {
	super(message);
	this.returnValue = returnValue;
    }

    public String getReturnValue() {
	return returnValue;
    }

}
