package com.blackducksoftware.tools.appedit.naiaudit.inputvalidation;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.AppEditConstants;
import com.blackducksoftware.tools.appedit.core.inputvalidation.InputValidatorLogin;

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
