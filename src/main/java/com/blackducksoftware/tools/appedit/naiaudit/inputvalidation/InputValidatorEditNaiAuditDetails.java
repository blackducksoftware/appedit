/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
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
