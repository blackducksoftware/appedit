/*******************************************************************************
 * Copyright (C) 2015 Black Duck Software, Inc.
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
package com.blackducksoftware.tools.appedit.web.auth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.blackducksoftware.tools.appedit.core.inputvalidation.InputValidatorLogin;
import com.blackducksoftware.tools.appedit.core.model.AuthenticationResult;
import com.blackducksoftware.tools.appedit.core.service.UserAuthenticationService;

/**
 * A Spring Security AuthenticationProvider. Decides whether or not a given set
 * of user credentials is authorized to log in. Uses CcUserAuthenticator to do
 * the authorization through Code Center.
 *
 * @author sbillings
 *
 */
public class AppEditAuthenticationProvider implements AuthenticationProvider {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());

    private UserAuthenticationService userAuthenticationService;

    @Inject
    public void setUserAuthenticationService(
	    UserAuthenticationService userAuthenticationService) {
	this.userAuthenticationService = userAuthenticationService;
    }

    private InputValidatorLogin inputValidatorLogin;

    @Inject
    public void setInputValidatorLogin(InputValidatorLogin inputValidatorLogin) {
	this.inputValidatorLogin = inputValidatorLogin;
    }

    /**
     * Returns true when asked if UsernamePassword authentication method is
     * supported, false otherwise.
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<? extends Object> authentication) {
	return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    /**
     * Attempt to authenticate the given user.
     */
    @Override
    public Authentication authenticate(Authentication authentication) {
	try {
	    // User provided data from login page
	    String username = (String) authentication.getPrincipal();
	    String password = (String) authentication.getCredentials();

	    validateInput(username, password);

	    UsernamePasswordAuthenticationToken auth = generateAuthenticationToken(
		    username, password);

	    return auth;
	} catch (Exception e) {
	    throw new AuthenticationServiceException(e.getMessage(), e);
	}
    }

    private UsernamePasswordAuthenticationToken generateAuthenticationToken(
	    String username, String password) {
	AuthenticationResult authResult = authenticateUser(username, password);
	// Grant access
	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	authorities
		.add(new SimpleGrantedAuthority(authResult.getRole().name()));
	UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
		username, password, authorities);
	auth.setDetails(authResult);
	return auth;
    }

    private AuthenticationResult authenticateUser(String username,
	    String password) {
	// Authenticate in Code Center

	AuthenticationResult authResult = userAuthenticationService
		.authenticate(username, password);
	if (!authResult.isAuthenticated()) {
	    throw new AuthenticationServiceException(authResult.getMessage());
	}
	return authResult;
    }

    private void validateInput(String username, String password) {
	// Validate input
	if ((!inputValidatorLogin.validateUsername(username))
		|| (!inputValidatorLogin.validatePassword(password))) {
	    String msg = "Authorization failed: The user name or password provided was not valid. ";
	    logger.error(msg);
	    throw new AuthenticationServiceException(msg);
	}
    }
}
