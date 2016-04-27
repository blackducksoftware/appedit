package com.blackducksoftware.tools.appedit.core.exception;

public class AppEditException extends Exception {

    private static final long serialVersionUID = -9063771081100082384L;

    public AppEditException(String message) {
	super(message);
    }

    public AppEditException(Throwable cause) {
	super(cause);
    }

    public AppEditException(String message, Throwable cause) {
	super(message, cause);
    }

    public AppEditException(String message, Throwable cause,
	    boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

}
