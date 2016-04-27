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
