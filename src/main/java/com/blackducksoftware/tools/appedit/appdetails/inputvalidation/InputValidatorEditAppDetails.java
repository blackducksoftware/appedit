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
package com.blackducksoftware.tools.appedit.appdetails.inputvalidation;

import java.util.regex.Pattern;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.inputvalidation.InputValidatorLogin;

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

	String patternString = config
		.getFieldInputValidationRegexAttr(attrLabel);
	if (patternString == null) {
	    return false;
	}
	Pattern pattern = Pattern.compile(patternString);
	return InputValidatorLogin.validate(attrValue, pattern);
    }
}
