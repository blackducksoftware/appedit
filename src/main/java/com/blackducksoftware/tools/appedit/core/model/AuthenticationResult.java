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
package com.blackducksoftware.tools.appedit.core.model;

import java.io.Serializable;

/**
 * Holds the results of an authentication attempt. Results consist of a boolean
 * (if true, the user is authenticated) and a message describing, and possibly
 * elaborating on, the authentication result.
 *
 * @author sbillings
 *
 */
public class AuthenticationResult implements Serializable {
    private static final long serialVersionUID = 1207534474718985530L;
    private final String userId;
    private final String username;

    private final boolean authenticated;

    private final String message;

    private final Role role;

    public AuthenticationResult(String userId, String username,
	    boolean authenticated, String message, Role role) {
	super();
	this.userId = userId;
	this.username = username;
	this.authenticated = authenticated;
	this.message = message;
	this.role = role;
    }

    public String getUserId() {
	return userId;
    }

    public String getUsername() {
	return username;
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
