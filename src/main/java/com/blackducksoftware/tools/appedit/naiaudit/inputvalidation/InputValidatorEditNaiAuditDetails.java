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
package com.blackducksoftware.tools.appedit.naiaudit.inputvalidation;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.AppEditConstants;
import com.blackducksoftware.tools.appedit.core.inputvalidation.InputValidatorLogin;

/**
 * Input validation for NAI Audit comment values.
 * 
 * @author sbillings
 *
 */
public class InputValidatorEditNaiAuditDetails {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());
    private final AppEditConfigManager config;

    public InputValidatorEditNaiAuditDetails(AppEditConfigManager config) {
	this.config = config;
    }

    /**
     * Validate a comment value.
     * 
     * @param comment
     * @return
     */
    public boolean validateCommentValue(String comment) {
	if (comment.length() > AppEditConstants.NAI_AUDIT_COMMENT_MAX_LENGTH) {
	    logger.error("The NAI Audit Comment entered is too long");
	    return false;
	}

	String patternString = config
		.getFieldInputValidationRegexNaiAuditComment();
	Pattern pattern = Pattern.compile(patternString);
	return InputValidatorLogin.validate(comment, pattern);
    }
}
