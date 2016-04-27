package com.blackducksoftware.tools.appedit.core.service;

import com.blackducksoftware.tools.appedit.core.model.AuthenticationResult;

/**
 * User authentication service
 * 
 * @author sbillings
 *
 */
public interface UserAuthenticationService {
    /**
     * Attempts to authenticate the given user.
     *
     * @param username
     * @param password
     * @return
     */
    AuthenticationResult authenticate(String username, String password);
}
