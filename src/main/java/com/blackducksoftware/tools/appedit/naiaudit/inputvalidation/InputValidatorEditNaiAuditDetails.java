/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
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
