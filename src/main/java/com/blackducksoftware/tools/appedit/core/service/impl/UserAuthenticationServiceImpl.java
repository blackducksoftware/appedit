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
package com.blackducksoftware.tools.appedit.core.service.impl;

import javax.inject.Inject;

import com.blackducksoftware.tools.appedit.core.dao.UserAuthenticationDao;
import com.blackducksoftware.tools.appedit.core.model.AuthenticationResult;
import com.blackducksoftware.tools.appedit.core.service.UserAuthenticationService;

/**
 * User authentication service implementation
 * 
 * @author sbillings
 *
 */
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private UserAuthenticationDao userAuthenticationDao;

    @Inject
    public void setUserAuthenticationDao(
	    UserAuthenticationDao userAuthenticationDao) {
	this.userAuthenticationDao = userAuthenticationDao;
    }

    @Override
    public AuthenticationResult authenticate(String username, String password) {
	return userAuthenticationDao.authenticate(username, password);
    }

}
