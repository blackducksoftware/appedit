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
    public void setConfig(AppEditConfigManager config) {
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
    public CcUserAuthenticationDao(AppEditConfigManager config)
	    throws Exception {
	logger.debug("Config passed via constructor");
	this.config = config;
    }

    /**
     * Attempt to authenticate the given user.
     */
    @Override
    public AuthenticationResult authenticate(String username, String password) {

	AppEditConfigManager userSpecificConfig;
	CodeCenterUserPojo user;
	boolean isAuditor;
	try {
	    userSpecificConfig = createConfig(username, password);
	    CodeCenterServerWrapper userSpecificCcsw = createCodeCenterServerWrapper(userSpecificConfig);
	    user = getUser(userSpecificCcsw, username);
	    isAuditor = isAuditor(userSpecificCcsw, username, user);
	} catch (AuthenticationException e1) {
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

    private boolean isAuditor(CodeCenterServerWrapper userSpecificCcsw,
	    String username, CodeCenterUserPojo user)
	    throws AuthenticationException {
	// Now see if this user is an auditor
	boolean isAuditor = false;
	try {
	    isAuditor = userIsAuditor(username, userSpecificCcsw);
	} catch (CommonFrameworkException e) {
	    String message = "Error attempting to authorize this user an an auditor: "
		    + e.getMessage() + "; Authorizing this user as an end user";
	    AuthenticationResult authResult = new AuthenticationResult(
		    user.getId(), username, true,
		    "Login as User was successful.", Role.ROLE_USER);
	    throw new AuthenticationException(authResult, message);
	}
	return isAuditor;
    }

    private CodeCenterUserPojo getUser(
	    CodeCenterServerWrapper userSpecificCcsw, String username)
	    throws AuthenticationException {
	// Authorize by performing an operation this user should be able to do
	CodeCenterUserPojo user;
	try {
	    user = userSpecificCcsw.getUserManager().getUserByName(username);
	} catch (CommonFrameworkException e) {
	    String message = "Authorization failed: " + e.getMessage();
	    AuthenticationResult authResult = new AuthenticationResult(null,
		    null, false, message, Role.ROLE_NONE);
	    throw new AuthenticationException(authResult, message);
	} catch (SOAPFaultException e) {
	    String message = "Authorization failed: " + e.getMessage();
	    AuthenticationResult authResult = new AuthenticationResult(null,
		    null, false, message, Role.ROLE_NONE);
	    throw new AuthenticationException(authResult, message);
	}
	return user;
    }

    private CodeCenterServerWrapper createCodeCenterServerWrapper(
	    ConfigurationManager userSpecificConfig)
	    throws AuthenticationException {
	CodeCenterServerWrapper userSpecificCcsw = null;
	try {
	    userSpecificCcsw = new CodeCenterServerWrapper(userSpecificConfig);
	} catch (Exception e) {
	    String message = "Authentication failed: " + e.getMessage();
	    AuthenticationResult authResult = new AuthenticationResult(null,
		    null, false, message, Role.ROLE_NONE);
	    throw new AuthenticationException(authResult, message);
	}
	return userSpecificCcsw;
    }

    private AppEditConfigManager createConfig(String username, String password)
	    throws AuthenticationException {
	Properties userSpecificProps = (Properties) config.getProps().clone();
	userSpecificProps.setProperty("cc.user.name", username); // change
								 // username
	userSpecificProps.setProperty("cc.password", password); // and password
	userSpecificProps.setProperty("cc.password.isencrypted", "false");
	AppEditConfigManager userSpecificConfig;
	try {
	    userSpecificConfig = new AppEditConfigManager(userSpecificProps);
	} catch (Exception e1) {
	    String message = "Authentication failed: " + e1.getMessage();
	    AuthenticationResult authResult = new AuthenticationResult(null,
		    null, false, message, Role.ROLE_NONE);

	    throw new AuthenticationException(authResult, message);
	}
	return userSpecificConfig;
    }

    private boolean userIsAuditor(String username,
	    CodeCenterServerWrapper userSpecificCcsw)
	    throws CommonFrameworkException {
	if (!config.isEditNaiAuditEnabled()) {
	    logger.info("The Edit NAI Audit feature has not been enabled");
	    return false;
	}

	List<UserRoleAssignment> roleAssignments;
	UserNameToken userNameToken = new UserNameToken();
	userNameToken.setName(username);
	try {
	    roleAssignments = userSpecificCcsw.getInternalApiWrapper()
		    .getProxy().getRoleApi().getUserRoles(userNameToken);
	} catch (SdkFault e) {
	    throw new CommonFrameworkException("Error getting user's roles: "
		    + e.getMessage());
	}
	boolean isAuditor = hasAuditorRole(userSpecificCcsw, username,
		roleAssignments);
	return isAuditor;
    }

    private boolean hasAuditorRole(CodeCenterServerWrapper userSpecificCcsw,
	    String username, List<UserRoleAssignment> roleAssignments)
	    throws CommonFrameworkException {

	for (UserRoleAssignment roleAssignment : roleAssignments) {

	    com.blackducksoftware.sdk.codecenter.role.data.Role ccRole;
	    try {
		ccRole = userSpecificCcsw.getInternalApiWrapper().getProxy()
			.getRoleApi().getRole(roleAssignment.getRoleIdToken());
	    } catch (SdkFault e) {
		throw new CommonFrameworkException(
			"Error getting details for user role "
				+ roleAssignment.getRoleNameToken().getName()
				+ ": " + e.getMessage());
	    }
	    if (ccRole.getRoleType() != RoleTypeEnum.OVERALL) {
		continue;
	    }
	    logger.debug("User: " + username + "; CC Role: "
		    + roleAssignment.getRoleNameToken().getName());
	    if (config.getAuditorRoleName().equals(
		    roleAssignment.getRoleNameToken().getName())) {
		return true;
	    }
	}
	return false;
    }
}
