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
package com.blackducksoftware.tools.appedit.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.blackducksoftware.tools.appedit.codecenter.CcUserAuthenticator;

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

            // Get config
            String configFilename = System.getProperty("user.home") + "/"
                    + AppEditConstants.CONFIG_FILENAME;
            AppEditConfigManager config = null;
            try {
                config = new AppEditConfigManager(configFilename);
            } catch (Exception e) {
                logger.error("Error constructing configuration manager");
                throw new AuthenticationServiceException(e.getMessage(), e);
            }

            // Validate input
            InputValidatorLogin inputValidator = new InputValidatorLogin(config);
            if ((!inputValidator.validateUsername(username))
                    || (!inputValidator.validatePassword(password))) {
                String msg = "Authorization failed: The user name or password provided was not valid. ";
                logger.error(msg);
                throw new AuthenticationServiceException(msg);
            }

            // Authenticate in Code Center
            UserAuthenticator userAuthenticator = new CcUserAuthenticator(
                    config);
            AuthenticationResult authResult = userAuthenticator.authenticate(
                    username, password);
            if (!authResult.isAuthenticated()) {
                throw new AuthenticationServiceException(
                        authResult.getMessage());
            }

            // Grant access
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority(authResult.getRole().name()));
            return new UsernamePasswordAuthenticationToken(username, password,
                    authorities);
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }
}
