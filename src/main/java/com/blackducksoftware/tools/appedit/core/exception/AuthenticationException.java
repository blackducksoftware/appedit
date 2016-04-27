/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
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
