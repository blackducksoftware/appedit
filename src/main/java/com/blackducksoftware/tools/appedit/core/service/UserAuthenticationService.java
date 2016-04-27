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
package com.blackducksoftware.tools.appedit.core.service;

import com.blackducksoftware.tools.appedit.core.model.AuthenticationResult;

/**
 * User authentication service
 * 
 * @author sbillings
 *
 */
public interface UserAuthenticationService {
    /**
     * Attempts to authenticate the given user.
     *
     * @param username
     * @param password
     * @return
     */
    AuthenticationResult authenticate(String username, String password);
}
