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
