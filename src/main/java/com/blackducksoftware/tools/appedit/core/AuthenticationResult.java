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

/**
 * Holds the results of an authentication attempt. Results consist of a boolean
 * (if true, the user is authenticated) and a message describing, and possibly
 * elaborating on, the authentication result.
 *
 * @author sbillings
 *
 */
public class AuthenticationResult {
    private final boolean authenticated;

    private final String message;

    private final Role role;

    public AuthenticationResult(boolean authenticated, String message, Role role) {
        super();
        this.authenticated = authenticated;
        this.message = message;
        this.role = role;
    }

    /**
     * Returns true if the user was authenticated.
     *
     * @return
     */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * Returns a message describing the authentication result.
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    public Role getRole() {
        return role;
    }
}
