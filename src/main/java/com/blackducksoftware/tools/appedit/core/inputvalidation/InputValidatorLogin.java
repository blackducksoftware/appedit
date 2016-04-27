/*******************************************************************************
 * Copyright (C) 2015, 2016 Black Duck Software, Inc.
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
