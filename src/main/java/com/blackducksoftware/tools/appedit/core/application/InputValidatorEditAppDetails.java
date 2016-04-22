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
package com.blackducksoftware.tools.appedit.core.application;

import java.util.regex.Pattern;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.InputValidatorLogin;

/**
 * Input validator for the fields (custom attr values) on the editappdetails
 * screen.
 *
 * Not thread-safe (because AppEditConfigManager is not thread-safe), so keep
 * references local.
 *
 * @author sbillings
 *
 */
public class InputValidatorEditAppDetails {
    private final AppEditConfigManager config; // AppEditConfigManager is not
					       // thread-safe

    public InputValidatorEditAppDetails(AppEditConfigManager config) {
	this.config = config;
    }

    /**
     * Returns true if the given attribute value matches the regex pattern for
     * the given attribute name.
     *
     * @param attrLabel
     * @param attrValue
     * @return
     */
    public boolean validateAttributeValue(String attrLabel, String attrValue) {

	// TODO: enforce length too

	String patternString = config
		.getFieldInputValidationRegexAttr(attrLabel);
	Pattern pattern = Pattern.compile(patternString);
	return InputValidatorLogin.validate(attrValue, pattern);
    }
}
