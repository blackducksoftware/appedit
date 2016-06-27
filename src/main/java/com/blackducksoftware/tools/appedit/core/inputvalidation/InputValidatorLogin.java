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
package com.blackducksoftware.tools.appedit.core.inputvalidation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;

/**
 * Input validator for the login screen fields.
 *
 * @author sbillings
 *
 */
public class InputValidatorLogin {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());

    private AppEditConfigManager config;

    private Pattern usernamePattern;

    private Pattern passwordPattern;

    public InputValidatorLogin(AppEditConfigManager config) {
	logger.debug("Constructor passed config");
	this.config = config;
	init();
    }

    private void init() {
	String usernameRegexString = config
		.getFieldInputValidationRegexUsername();
	String passwordRegexString = config
		.getFieldInputValidationRegexPassword();

	usernamePattern = Pattern.compile(usernameRegexString);
	passwordPattern = Pattern.compile(passwordRegexString);
    }

    /**
     * Returns true if the given username matches the username regex pattern.
     *
     * @param username
     * @return
     */
    public boolean validateUsername(String username) {
	return validate(username, usernamePattern);
    }

    /**
     * Returns true if the given password matches the password regex pattern.
     *
     * @param password
     * @return
     */
    public boolean validatePassword(String password) {
	return validate(password, passwordPattern);
    }

    public static boolean validate(String s, Pattern pattern) {
	Matcher matcher = pattern.matcher(s);
	boolean matches = matcher.matches();
	return matches;
    }

}
