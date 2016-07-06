/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
			final UserAuthenticationService userAuthenticationService) {
		this.userAuthenticationService = userAuthenticationService;
	}

	private InputValidatorLogin inputValidatorLogin;

	@Inject
	public void setInputValidatorLogin(final InputValidatorLogin inputValidatorLogin) {
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
	public boolean supports(final Class<? extends Object> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	/**
	 * Attempt to authenticate the given user.
	 */
	@Override
	public Authentication authenticate(final Authentication authentication) {
		try {
			// User provided data from login page
			final String username = (String) authentication.getPrincipal();
			final String password = (String) authentication.getCredentials();

			validateInput(username, password);

			final UsernamePasswordAuthenticationToken auth = generateAuthenticationToken(
					username, password);

			return auth;
		} catch (final Exception e) {
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
	}

	private UsernamePasswordAuthenticationToken generateAuthenticationToken(
			final String username, final String password) {
		final AuthenticationResult authResult = authenticateUser(username, password);
		// Grant access
		final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities
		.add(new SimpleGrantedAuthority(authResult.getRole().name()));
		final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
				username, password, authorities);
		auth.setDetails(authResult);
		return auth;
	}

	private AuthenticationResult authenticateUser(final String username,
			final String password) {
		// Authenticate in Code Center

		final AuthenticationResult authResult = userAuthenticationService
				.authenticate(username, password);
		if (!authResult.isAuthenticated()) {
			logger.debug("Authentication failed");
			throw new AuthenticationServiceException(authResult.getMessage());
		}
		return authResult;
	}

	private void validateInput(final String username, final String password) {
		// Validate input
		if ((!inputValidatorLogin.validateUsername(username))
				|| (!inputValidatorLogin.validatePassword(password))) {
			final String msg = "Authorization failed: The user name or password provided was not valid. ";
			logger.error(msg);
			throw new AuthenticationServiceException(msg);
		}
	}
}
