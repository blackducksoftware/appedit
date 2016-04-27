package com.blackducksoftware.tools.appedit.core.exception;

import com.blackducksoftware.tools.appedit.core.model.AuthenticationResult;

public class AuthenticationException extends AppEditException {
    private static final long serialVersionUID = 4435214364484852902L;
    private AuthenticationResult authResult;

    public AuthenticationException(AuthenticationResult authResult,
	    String message) {
	super(message);
	this.authResult = authResult;
    }

    public AuthenticationResult getAuthResult() {
	return authResult;
    }
}
