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
