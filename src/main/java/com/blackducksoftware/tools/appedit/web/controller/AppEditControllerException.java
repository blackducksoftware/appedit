package com.blackducksoftware.tools.appedit.web.controller;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;

/**
 * An exception thrown from a controller.
 * 
 * @author sbillings
 *
 */
public class AppEditControllerException extends AppEditException {
    private static final long serialVersionUID = 2054672808081635193L;
    private final String returnValue;

    public AppEditControllerException(String returnValue, String message) {
	super(message);
	this.returnValue = returnValue;
    }

    public String getReturnValue() {
	return returnValue;
    }

}
