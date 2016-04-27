/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License version 2 only
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
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
