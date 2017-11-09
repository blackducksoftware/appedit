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
package com.blackducksoftware.tools.appedit.core.dao.hybrid;

import java.util.Properties;

import javax.inject.Inject;
import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.sdk.codecenter.fault.SdkFault;
import com.blackducksoftware.sdk.codecenter.role.data.RoleNameToken;
import com.blackducksoftware.tools.appedit.appdetails.dao.AppDao;
import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.dao.UserAuthenticationDao;
import com.blackducksoftware.tools.appedit.core.dao.UserRoleDao;
import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.core.exception.AuthenticationException;
import com.blackducksoftware.tools.appedit.core.model.AuthenticationResult;
import com.blackducksoftware.tools.appedit.core.model.Role;
import com.blackducksoftware.tools.commonframework.core.config.ConfigurationManager;
import com.blackducksoftware.tools.commonframework.core.exception.CommonFrameworkException;
import com.blackducksoftware.tools.connector.codecenter.CodeCenterServerWrapper;
import com.blackducksoftware.tools.connector.codecenter.user.CodeCenterUserPojo;


/**
 * Authenticates a given username/password, using both Code Center and the given
 * UserRoleDao.
 *
 * @author sbillings
 *
 */
public class HybridUserAuthenticationDao implements UserAuthenticationDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getName());

	private String auditorRoleId;

	private AppEditConfigManager config;

	@Inject
	public void setConfig(final AppEditConfigManager config) {
		this.config = config;
	}

	private UserRoleDao userRoleDao;

	@Inject
	public void setUserRoleDao(final UserRoleDao userRoleDao) {
		this.userRoleDao = userRoleDao;
	}
	
	private AppDao appDao;
	
	@Inject
	public void setAppDao(final AppDao appDao){
		this.appDao = appDao;
	}
	

	/**
	 * Default constructor
	 *
	 * @throws Exception
	 */
	public HybridUserAuthenticationDao() throws Exception {
		logger.debug("Default constructor called");
	}

	/**
	 * Construct with given config (used by integration test).
	 *
	 * @param config
	 * @throws Exception
	 */
	public HybridUserAuthenticationDao(final AppEditConfigManager config)
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
			user = appDao.getUser(username);
			ensureAuditorRoleIdIsPopulated(userSpecificCcsw, user.getId(), username);
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

	private void ensureAuditorRoleIdIsPopulated(final CodeCenterServerWrapper userSpecificCcsw, final String userId,
			final String username) throws AuthenticationException {
		if (auditorRoleId != null) {
			return;
		}
		final String auditorRoleName = config.getAuditorRoleName();
		final RoleNameToken roleNameToken = new RoleNameToken();
		roleNameToken.setName(auditorRoleName);
		com.blackducksoftware.sdk.codecenter.role.data.Role ccRole;

		try {
			logger.debug("SDK: Getting role for role name: " + auditorRoleName);
			ccRole = userSpecificCcsw.getInternalApiWrapper().getProxy().getRoleApi().getRole(roleNameToken);
			logger.debug("SDK: Done getting auditor role; the auditor role ID is: " + ccRole.getRoleId().getId());
		} catch (final SdkFault e) {
			logger.debug("SDK: Error getting role");
			final String message = "Error looking up the auditor role " + auditorRoleName + e.getMessage()
					+ "; Authorizing this user as an end user";
			final AuthenticationResult authResult = new AuthenticationResult(userId, username, true,
					"Login as User was successful.", Role.ROLE_USER);
			throw new AuthenticationException(authResult, message);
		}
		this.auditorRoleId = ccRole.getRoleId().getId();
	}

	private boolean isAuditor(final CodeCenterServerWrapper userSpecificCcsw,
			final String username, final CodeCenterUserPojo user)
					throws AuthenticationException {
		// Now see if this user is an auditor
		boolean isAuditor = false;
		try {
			isAuditor = userIsAuditor(userSpecificCcsw, username, user);
		} catch (final AppEditException e) {
			final String message = "Error attempting to authorize this user an an auditor: "
					+ e.getMessage() + "; Authorizing this user as an end user";
			final AuthenticationResult authResult = new AuthenticationResult(
					user.getId(), username, true,
					"Login as User was successful.", Role.ROLE_USER);
			throw new AuthenticationException(authResult, message);
		}
		return isAuditor;
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

	private boolean userIsAuditor(final CodeCenterServerWrapper userSpecificCcsw, final String username,
			final CodeCenterUserPojo user) throws AppEditException {
		logger.debug("userIsAuditor(): Username: " + username);
		if (!config.isEditNaiAuditEnabled()) {
			logger.info("The Edit NAI Audit feature has not been enabled");
			return false;
		}

		final long userId = Long.parseLong(user.getId());
		final boolean isAuditor = userRoleDao.userHasRole(userId, this.auditorRoleId);
		return isAuditor;
	}
}
