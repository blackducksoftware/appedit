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

    /**
     * Attempts to authenticate the given user.
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public AuthenticationResult authenticate(String username, String password) {
	return userAuthenticationDao.authenticate(username, password);
    }

}
