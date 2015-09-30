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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *******************************************************************************/
package com.blackducksoftware.tools.appedit.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Input validator for the login screen fields.
 *
 * @author sbillings
 *
 */
public class InputValidatorLogin {
    private final Pattern usernamePattern;
    private final Pattern passwordPattern;

    public InputValidatorLogin(AppEditConfigManager config) {
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

    static boolean validate(String s, Pattern pattern) {
	Matcher matcher = pattern.matcher(s);
	boolean matches = matcher.matches();
	return matches;
    }

}
