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
package com.blackducksoftware.tools.appedit.codecenter;

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
import com.blackducksoftware.tools.appedit.core.AuthenticationResult;
import com.blackducksoftware.tools.appedit.core.Role;
import com.blackducksoftware.tools.appedit.core.UserAuthenticator;
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
public class CcUserAuthenticator implements UserAuthenticator {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
            .getName());

    private AppEditConfigManager config;

    @Inject
    public void setConfig(AppEditConfigManager config) {
        this.config = config;
    }

    public CcUserAuthenticator() throws Exception {
        logger.debug("Default constructor called");
    }

    public CcUserAuthenticator(AppEditConfigManager config) throws Exception {
        logger.debug("Config passed via constructor");
        this.config = config;
    }

    /**
     * Attempt to authenticate the given user.
     */
    @Override
    public AuthenticationResult authenticate(String username, String password) {

        Properties userSpecificProps = (Properties) config.getProps().clone();
        userSpecificProps.setProperty("cc.user.name", username); // change username
        userSpecificProps.setProperty("cc.password", password); // and password
        AppEditConfigManager userSpecificConfig;
        try {
            userSpecificConfig = new AppEditConfigManager(userSpecificProps);
        } catch (Exception e1) {
            String message = "Authentication failed: " + e1.getMessage();
            logger.info(message);
            return new AuthenticationResult(false, message, Role.ROLE_NONE);
        }

        CodeCenterServerWrapper userSpecificCcsw = null;
        try {
            // TODO Creating ALL of the API services is slow; go direct (not via CF)
            userSpecificCcsw = new CodeCenterServerWrapper(userSpecificConfig);
        } catch (Exception e) {
            String message = "Authentication failed: " + e.getMessage();
            logger.info(message);
            return new AuthenticationResult(false, message, Role.ROLE_NONE);
        }

        // Authorize by performing an operation this user should be able to do
        CodeCenterUserPojo user;
        try {
            user = userSpecificCcsw.getUserManager().getUserByName(username);
        } catch (CommonFrameworkException e) {
            String message = "Authorization failed: " + e.getMessage();
            logger.info(message);
            return new AuthenticationResult(false, message, Role.ROLE_NONE);
        } catch (SOAPFaultException e) {
            String message = "Authorization failed: " + e.getMessage();
            logger.info(message);
            return new AuthenticationResult(false, message, Role.ROLE_NONE);
        }

        // Now see if this user is an auditor
        boolean isAuditor = false;
        try {
            isAuditor = userIsAuditor(username, userSpecificCcsw);
        } catch (CommonFrameworkException e) {
            String message = "Error attempting to authorize this user an an auditor: " + e.getMessage() + "; Authorizing this user as an end user";
            logger.warn(message);
            return new AuthenticationResult(true, "Login as User was successful.", Role.ROLE_USER);
        }
        if (isAuditor) {
            logger.info("User " + username + " has been authorized as an Auditor");
            return new AuthenticationResult(true, "Login as Auditor was successful.", Role.ROLE_AUDITOR);
        }

        logger.info("User " + username + " has been authorized as a User");
        return new AuthenticationResult(true, "Login as User was successful.", Role.ROLE_USER);
    }

    private boolean userIsAuditor(String username, CodeCenterServerWrapper userSpecificCcsw) throws CommonFrameworkException {
        if (!config.isEditNaiAuditEnabled()) {
            logger.info("The Edit NAI Audit feature has not been enabled");
            return false;
        }
        // TODO port to CF Managers
        List<UserRoleAssignment> roleAssignments;
        UserNameToken userNameToken = new UserNameToken();
        userNameToken.setName(username);
        try {
            roleAssignments = userSpecificCcsw.getInternalApiWrapper().getProxy().getRoleApi().getUserRoles(userNameToken);
        } catch (SdkFault e) {
            throw new CommonFrameworkException("Error getting user's roles: " + e.getMessage());
        }
        for (UserRoleAssignment roleAssignment : roleAssignments) {

            com.blackducksoftware.sdk.codecenter.role.data.Role ccRole;
            try {
                ccRole = userSpecificCcsw.getInternalApiWrapper().getProxy().getRoleApi().getRole(roleAssignment.getRoleIdToken());
            } catch (SdkFault e) {
                throw new CommonFrameworkException("Error getting details for user role " + roleAssignment.getRoleNameToken().getName() + ": " + e.getMessage());
            }
            if (ccRole.getRoleType() != RoleTypeEnum.OVERALL) {
                continue;
            }
            logger.debug("User: " + username + "; CC Role: " + roleAssignment.getRoleNameToken().getName());
            if (config.getAuditorRoleName().equals(roleAssignment.getRoleNameToken().getName())) {
                return true;
            }
        }
        return false;
    }
}
