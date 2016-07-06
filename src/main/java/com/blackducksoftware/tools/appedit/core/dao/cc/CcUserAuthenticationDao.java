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
package com.blackducksoftware.tools.appedit.core.dao.cc;

import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.sdk.codecenter.fault.SdkFault;
import com.blackducksoftware.sdk.codecenter.role.data.RoleTypeEnum;
import com.blackducksoftware.sdk.codecenter.role.data.UserRoleAssignment;
import com.blackducksoftware.sdk.codecenter.user.data.UserNameToken;
import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.dao.UserAuthenticationDao;
import com.blackducksoftware.tools.appedit.core.exception.AuthenticationException;
import com.blackducksoftware.tools.appedit.core.model.AuthenticationResult;
import com.blackducksoftware.tools.appedit.core.model.Role;
import com.blackducksoftware.tools.commonframework.core.config.ConfigurationManager;
import com.blackducksoftware.tools.commonframework.core.exception.CommonFrameworkException;
import com.blackducksoftware.tools.connector.codecenter.CodeCenterServerWrapper;
import com.blackducksoftware.tools.connector.codecenter.user.CodeCenterUserPojo;

// import com.blackducksoftware.tools.commonframework.core.config.server.ServerBean;

/**
 * Authenticates a given username/password via Code Center.
 *
 * @author sbillings
 *
 */
public class CcUserAuthenticationDao implements UserAuthenticationDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getName());

	private AppEditConfigManager config;

	@Inject
	public void setConfig(final AppEditConfigManager config) {
		this.config = config;
	}

	/**
	 * Default constructor
	 *
	 * @throws Exception
	 */
	public CcUserAuthenticationDao() throws Exception {
		logger.debug("Default constructor called");
	}

	/**
	 * Construct with given config (used by integration test).
	 *
	 * @param config
	 * @throws Exception
	 */
	public CcUserAuthenticationDao(final AppEditConfigManager config)
			throws Exception {
		logger.debug("Config passed via constructor");
		this.config = config;
	}

	/**
	 * Attempt to authenticate the given user.
	 */
	@Override
	public AuthenticationResult authenticate(final String username, final String password) {
		logger.debug("authenticate(): Username: " + username);
		AppEditConfigManager userSpecificConfig;
		CodeCenterUserPojo user;
		boolean isAuditor;
		try {
			userSpecificConfig = createConfig(username, password);
			final CodeCenterServerWrapper userSpecificCcsw = createCodeCenterServerWrapper(userSpecificConfig);
			user = getUser(userSpecificCcsw, username);
			isAuditor = isAuditor(userSpecificCcsw, username, user);
		} catch (final AuthenticationException e1) {
			logger.warn(e1.getMessage());
			return e1.getAuthResult();
		}

		if (isAuditor) {
			logger.info("User " + username
					+ " has been authorized as an Auditor");
			return new AuthenticationResult(user.getId(), username, true,
					"Login as Auditor was successful.", Role.ROLE_AUDITOR);
		}

		logger.info("User " + username + " has been authorized as a User");
		return new AuthenticationResult(user.getId(), username, true,
				"Login as User was successful.", Role.ROLE_USER);
	}

	private boolean isAuditor(final CodeCenterServerWrapper userSpecificCcsw,
			final String username, final CodeCenterUserPojo user)
					throws AuthenticationException {
		// Now see if this user is an auditor
		boolean isAuditor = false;
		try {
			isAuditor = userIsAuditor(username, userSpecificCcsw);
		} catch (final CommonFrameworkException e) {
			final String message = "Error attempting to authorize this user an an auditor: "
					+ e.getMessage() + "; Authorizing this user as an end user";
			final AuthenticationResult authResult = new AuthenticationResult(
					user.getId(), username, true,
					"Login as User was successful.", Role.ROLE_USER);
			throw new AuthenticationException(authResult, message);
		}
		return isAuditor;
	}

	private CodeCenterUserPojo getUser(
			final CodeCenterServerWrapper userSpecificCcsw, final String username)
					throws AuthenticationException {
		// Authorize by performing an operation this user should be able to do
		logger.debug("getUser()");
		CodeCenterUserPojo user;
		try {
			user = userSpecificCcsw.getUserManager().getUserByName(username);
		} catch (final CommonFrameworkException e) {
			final String message = "Authorization failed: " + e.getMessage();
			final AuthenticationResult authResult = new AuthenticationResult(null,
					null, false, message, Role.ROLE_NONE);
			throw new AuthenticationException(authResult, message);
		} catch (final SOAPFaultException e) {
			final String message = "Authorization failed: " + e.getMessage();
			final AuthenticationResult authResult = new AuthenticationResult(null,
					null, false, message, Role.ROLE_NONE);
			throw new AuthenticationException(authResult, message);
		}
		return user;
	}

	private CodeCenterServerWrapper createCodeCenterServerWrapper(
			final ConfigurationManager userSpecificConfig)
					throws AuthenticationException {
		CodeCenterServerWrapper userSpecificCcsw = null;
		try {
			userSpecificCcsw = new CodeCenterServerWrapper(userSpecificConfig);
		} catch (final Exception e) {
			final String message = "Authentication failed: " + e.getMessage();
			final AuthenticationResult authResult = new AuthenticationResult(null,
					null, false, message, Role.ROLE_NONE);
			throw new AuthenticationException(authResult, message);
		}
		return userSpecificCcsw;
	}

	private AppEditConfigManager createConfig(final String username, final String password)
			throws AuthenticationException {
		final Properties userSpecificProps = (Properties) config.getProps().clone();
		userSpecificProps.setProperty("cc.user.name", username); // change
		// username
		userSpecificProps.setProperty("cc.password", password); // and password
		userSpecificProps.setProperty("cc.password.isencrypted", "false");
		AppEditConfigManager userSpecificConfig;
		try {
			userSpecificConfig = new AppEditConfigManager(userSpecificProps);
		} catch (final Exception e1) {
			final String message = "Authentication failed: " + e1.getMessage();
			final AuthenticationResult authResult = new AuthenticationResult(null,
					null, false, message, Role.ROLE_NONE);

			throw new AuthenticationException(authResult, message);
		}
		return userSpecificConfig;
	}

	private boolean userIsAuditor(final String username,
			final CodeCenterServerWrapper userSpecificCcsw)
					throws CommonFrameworkException {
		logger.debug("userIsAuditor(): Username: " + username);
		if (!config.isEditNaiAuditEnabled()) {
			logger.info("The Edit NAI Audit feature has not been enabled");
			return false;
		}

		List<UserRoleAssignment> roleAssignments;
		final UserNameToken userNameToken = new UserNameToken();
		userNameToken.setName(username);
		try {
			logger.debug("SDK: Getting user's roles for user: " + username);
			roleAssignments = userSpecificCcsw.getInternalApiWrapper()
					.getProxy().getRoleApi().getUserRoles(userNameToken);
			logger.debug("SDK: Done getting user's roles; role count: " + roleAssignments.size());
		} catch (final SdkFault e) {
			logger.debug("SDK: Error getting user's roles");
			throw new CommonFrameworkException("Error getting user's roles: "
					+ e.getMessage());
		}
		final boolean isAuditor = hasAuditorRole(userSpecificCcsw, username,
				roleAssignments);
		return isAuditor;
	}

	private boolean hasAuditorRole(final CodeCenterServerWrapper userSpecificCcsw,
			final String username, final List<UserRoleAssignment> roleAssignments)
					throws CommonFrameworkException {
		logger.info("Checking user " + username + " to see if it has been assigned the auditor role");
		for (final UserRoleAssignment roleAssignment : roleAssignments) {

			com.blackducksoftware.sdk.codecenter.role.data.Role ccRole;
			try {
				logger.debug("SDK: Getting role for role ID: " + roleAssignment.getRoleIdToken().getId());
				ccRole = userSpecificCcsw.getInternalApiWrapper().getProxy()
						.getRoleApi().getRole(roleAssignment.getRoleIdToken());
				logger.debug("SDK: Done getting role");
			} catch (final SdkFault e) {
				logger.debug("SDK: Error getting role");
				throw new CommonFrameworkException(
						"Error getting details for user role "
								+ roleAssignment.getRoleNameToken().getName()
								+ ": " + e.getMessage());
			}
			if (ccRole.getRoleType() != RoleTypeEnum.OVERALL) {
				logger.debug("This role is not an overall role so cannot be the auditor role: "
						+ ccRole.getName().getName());
				continue;
			}
			logger.debug("User: " + username + "; CC Role: "
					+ roleAssignment.getRoleNameToken().getName());
			if (config.getAuditorRoleName().equals(
					roleAssignment.getRoleNameToken().getName())) {
				logger.info("Found auditor role; this user is an auditor");
				return true;
			}
		}
		logger.info("Did not find auditor role; this user is not an auditor");
		return false;
	}
}
