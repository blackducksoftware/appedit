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

import java.util.Properties;

import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.AuthenticationResult;
import com.blackducksoftware.tools.appedit.core.UserAuthenticator;
import com.blackducksoftware.tools.commonframework.core.exception.CommonFrameworkException;
import com.blackducksoftware.tools.connector.codecenter.CodeCenterServerWrapper;

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

    private final AppEditConfigManager config;

    public CcUserAuthenticator(AppEditConfigManager config) throws Exception {
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
            return new AuthenticationResult(false, message);
        }

        CodeCenterServerWrapper userSpecificCcsw = null;
        try {
            userSpecificCcsw = new CodeCenterServerWrapper(userSpecificConfig);
        } catch (Exception e) {
            String message = "Authentication failed: " + e.getMessage();
            logger.info(message);
            return new AuthenticationResult(false, message);
        }

        // Authorize by performing an operation this user should be able to do
        try {
            userSpecificCcsw.getUserManager().getUserByName(username);
        } catch (CommonFrameworkException e) {
            String message = "Authorization failed: " + e.getMessage();
            logger.info(message);
            return new AuthenticationResult(false, message);
        } catch (SOAPFaultException e) {
            String message = "Authorization failed: " + e.getMessage();
            logger.info(message);
            return new AuthenticationResult(false, message);
        }

        return new AuthenticationResult(true, "Login was successful.");
    }

}
