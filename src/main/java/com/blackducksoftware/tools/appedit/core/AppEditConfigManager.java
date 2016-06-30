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

/**
 *
 */
package com.blackducksoftware.tools.appedit.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.commonframework.core.config.ConfigurationManager;
import com.blackducksoftware.tools.commonframework.core.config.ConfigurationPassword;

/**
 * Configuration Manager for the AppEdit application.
 */
public class AppEditConfigManager extends ConfigurationManager {
	private static final String DB_PASSWORD_DEFAULT = "mallard";

	private static final String DB_USER_DEFAULT = "blackduck";

	private static final String DB_PORT_DEFAULT = "55433";
	private static final String DB_DATABASE_DEFAULT = "bds_catalog";

	private static final String ATTR_PROPERTY_REGEX_SUFFIX = "regex";

	private static final String ATTR_PROPERTY_CCNAME_SUFFIX = "ccname";

	private static final String ATTR_PROPERTY_LABEL_SUFFIX = "label";

	private static final String ATTR_PROPERTY_PREFIX = "attr";

	private static final String APP_VERSION_PROPERTY = "app.version";

	private static final String APP_VERSION_DEFAULT = "Unspecified";

	private static final String AUDITOR_ROLE_NAME_PROPERTY = "auditor.role.name";

	private static final String FIELD_INPUT_VALIDATION_REGEX_USERNAME_PROPERTY = "field.input.validation.regex.username";

	private static final String FIELD_INPUT_VALIDATION_REGEX_PASSWORD_PROPERTY = "field.input.validation.regex.psw";
	private static final String FIELD_INPUT_VALIDATION_REGEX_NAIAUDITCOMMENT_PROPERTY = "field.input.validation.regex.naiauditcomment";
	private static final String FIELD_INPUT_VALIDATION_REGEX_USERNAME_DEFAULT = "[A-Za-z0-9@_.-]+";
	private static final String FIELD_INPUT_VALIDATION_REGEX_PASSWORD_DEFAULT = ".+";
	private static final String FIELD_INPUT_VALIDATION_REGEX_NAIAUDITCOMMENT_DEFAULT = ".*";

	private static final String DB_SERVER_PROPERTY = "db.server";
	private static final String DB_PORT_PROPERTY = "db.port";
	private static final String DB_USER_PROPERTY = "db.user";
	private static final String DB_PASSWORD_PROPERTY_PREFIX = "db";
	private static final String DB_DATABASE_PROPERTY = "db.database";
	private static final String NAI_AUDIT_STATUS_CHOICE_PROPERTY_PREFIX = "nai.audit.status";

	private static final String NAI_AUDIT_DATE_FORMAT_PROPERTY = "nai.audit.date.format";
	private static final String NAI_AUDIT_DATE_FORMAT_DEFAULT = "MMM-dd-yyyy hh:mm:ss";

	private static final String NAI_AUDIT_REM_STATUS_TO_AUDIT_PROPERTY = "nai.audit.rem.status.to.audit";
	private static final String NAI_AUDIT_REJECTED_STATUS_NAME_PROPERTY = "nai.audit.rejected.status.name";
	private static final String NAI_AUDIT_REJECTED_STATUS_CHANGES_REM_STATUS_TO_PROPERTY = "nai.audit.rejected.status.changes.rem.status.to";

	private static final String NAI_AUDIT_PRELOAD_COMPONENTS = "nai.audit.preload.components";
	private static final String NAI_AUDIT_PRELOAD_BATCH_SIZE = "nai.audit.preload.batch.size";

	private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());

	private String appVersion = "";

	private String auditorRoleName;

	private String fieldInputValidationRegexUsername = FIELD_INPUT_VALIDATION_REGEX_USERNAME_DEFAULT;

	private String fieldInputValidationRegexPassword = FIELD_INPUT_VALIDATION_REGEX_PASSWORD_DEFAULT;

	private String programVersion = "";

	private boolean editNaiAuditEnabled = false;

	private final Map<String, String> attributeMap = new HashMap<String, String>();

	private final Map<String, String> attrRegexMap = new HashMap<String, String>();

	private final Map<Integer, String> attrLabelsByIndex = new HashMap<Integer, String>();

	private String dbServer;
	private String dbDatabase = DB_DATABASE_DEFAULT;
	private String dbPort = DB_PORT_DEFAULT;
	private String dbUser = DB_USER_DEFAULT;
	private String dbPassword = DB_PASSWORD_DEFAULT;
	private List<String> naiAuditStatusChoices;
	private DateFormat naiAuditDateFormat;
	private String naiAuditRejectedStatusName;
	private String naiAuditRemStatusToAudit;
	private String naiAuditRejectedStatusChangesRemStatusTo;
	private String fieldInputValidationRegexNaiAuditComment;
	private boolean naiAuditPreloadComponents;
	private int naiAuditPreloadBatchSize = 1000;

	public AppEditConfigManager() {
		super();
	}

	public AppEditConfigManager(final String filename) throws Exception {
		super(filename);
		init();
	}

	public AppEditConfigManager(final Properties props) throws Exception {
		super(props, APPLICATION.CODECENTER);
		init();
	}

	private void init() throws Exception {
		log.info("init()");
		try {
			final String dynamicVersion = getClass().getPackage()
					.getImplementationVersion();
			if (dynamicVersion != null) {
				programVersion = dynamicVersion;
			}
		} catch (final Throwable t) {
			log.debug("Could not determine version for this program", t);
		}

		dbServer = getOptionalProperty(DB_SERVER_PROPERTY);
		String propValue = getOptionalProperty(DB_PORT_PROPERTY);
		if (propValue != null) {
			dbPort = propValue;
		}
		propValue = getOptionalProperty(DB_DATABASE_PROPERTY);
		if (propValue != null) {
			dbDatabase = propValue;
		}
		propValue = getOptionalProperty(DB_USER_PROPERTY);
		if (propValue != null) {
			dbUser = propValue;
		}

		final ConfigurationPassword configurationPassword = ConfigurationPassword
				.createFromProperty(getProps(), DB_PASSWORD_PROPERTY_PREFIX); // Read
		// the
		// value
		// of
		// db.password
		if (configurationPassword.getPlainText() != null) {
			dbPassword = configurationPassword.getPlainText(); // get the plain
			// text value of
			// the password
			// (even if it
			// was encrypted
			// in the
			// property file)
		}

		appVersion = getOptionalProperty(APP_VERSION_PROPERTY);
		if (appVersion == null) {
			appVersion = APP_VERSION_DEFAULT;
		}

		auditorRoleName = getOptionalProperty(AUDITOR_ROLE_NAME_PROPERTY);

		fieldInputValidationRegexUsername = getOptionalProperty(FIELD_INPUT_VALIDATION_REGEX_USERNAME_PROPERTY);
		if (fieldInputValidationRegexUsername == null) {
			fieldInputValidationRegexUsername = FIELD_INPUT_VALIDATION_REGEX_USERNAME_DEFAULT;
		}

		fieldInputValidationRegexPassword = getOptionalProperty(FIELD_INPUT_VALIDATION_REGEX_PASSWORD_PROPERTY);
		if (fieldInputValidationRegexPassword == null) {
			fieldInputValidationRegexPassword = FIELD_INPUT_VALIDATION_REGEX_PASSWORD_DEFAULT;
		}

		for (int i = 0;; i++) {
			final String keyBase = ATTR_PROPERTY_PREFIX + "." + i + ".";
			final String label = getOptionalProperty(keyBase
					+ ATTR_PROPERTY_LABEL_SUFFIX);
			if (label == null) {
				break;
			}
			final String ccName = getProperty(keyBase + ATTR_PROPERTY_CCNAME_SUFFIX);
			final String regex = getProperty(keyBase + ATTR_PROPERTY_REGEX_SUFFIX);

			attributeMap.put(label, ccName);
			attrRegexMap.put(label, regex);
			attrLabelsByIndex.put(i, label); // remember the sequence
		}

		naiAuditStatusChoices = new ArrayList<>();
		for (int i = 0;; i++) {
			final String propKey = NAI_AUDIT_STATUS_CHOICE_PROPERTY_PREFIX + "." + i;
			final String naiAuditStatusChoice = getOptionalProperty(propKey);
			if (naiAuditStatusChoice == null) {
				break;
			}
			naiAuditStatusChoices.add(naiAuditStatusChoice);
		}

		final String naiAuditDateFormatString = getOptionalProperty(NAI_AUDIT_DATE_FORMAT_PROPERTY);
		if (naiAuditDateFormatString == null) {
			naiAuditDateFormat = new SimpleDateFormat(
					NAI_AUDIT_DATE_FORMAT_DEFAULT);
		} else {
			naiAuditDateFormat = new SimpleDateFormat(naiAuditDateFormatString);
		}

		naiAuditRejectedStatusName = getOptionalProperty(NAI_AUDIT_REJECTED_STATUS_NAME_PROPERTY);
		naiAuditRemStatusToAudit = getOptionalProperty(NAI_AUDIT_REM_STATUS_TO_AUDIT_PROPERTY);
		naiAuditRejectedStatusChangesRemStatusTo = getOptionalProperty(NAI_AUDIT_REJECTED_STATUS_CHANGES_REM_STATUS_TO_PROPERTY);

		fieldInputValidationRegexNaiAuditComment = getOptionalProperty(FIELD_INPUT_VALIDATION_REGEX_NAIAUDITCOMMENT_PROPERTY);
		if (fieldInputValidationRegexNaiAuditComment == null) {
			fieldInputValidationRegexNaiAuditComment = FIELD_INPUT_VALIDATION_REGEX_NAIAUDITCOMMENT_DEFAULT;
		}
		final String naiAuditPreloadComponentsString = getOptionalProperty(NAI_AUDIT_PRELOAD_COMPONENTS);
		if ("true".equalsIgnoreCase(naiAuditPreloadComponentsString)) {
			naiAuditPreloadComponents = true;
		} else {
			naiAuditPreloadComponents = false;
		}

		final String naiAuditPreloadBatchSizeString = getOptionalProperty(NAI_AUDIT_PRELOAD_BATCH_SIZE);
		if (naiAuditPreloadBatchSizeString != null) {
			naiAuditPreloadBatchSize = Integer.parseInt(naiAuditPreloadBatchSizeString);
		}

		enableOrDisableNaiAudit();
	}

	private void enableOrDisableNaiAudit() {
		editNaiAuditEnabled = false;
		if (StringUtils.isBlank(dbServer)) {
			log.info("Property " + DB_SERVER_PROPERTY
					+ " is not set; disabling NAI Audit functionality");
			return;
		}
		if (StringUtils.isBlank(auditorRoleName)) {
			log.info("Property " + AUDITOR_ROLE_NAME_PROPERTY
					+ " is not set; disabling NAI Audit functionality");
			return;
		}
		if (naiAuditStatusChoices.size() == 0) {
			log.info("No NAI Audit Status choices defined; disabling NAI Audit functionality");
			return;
		}
		if (StringUtils.isBlank(naiAuditRejectedStatusName)) {
			log.info("Property " + NAI_AUDIT_REJECTED_STATUS_NAME_PROPERTY
					+ " is not set; disabling NAI Audit functionality");
			return;
		}
		if (StringUtils.isBlank(naiAuditRemStatusToAudit)) {
			log.info("Property " + NAI_AUDIT_REM_STATUS_TO_AUDIT_PROPERTY
					+ " is not set; disabling NAI Audit functionality");
			return;
		}
		if (StringUtils.isBlank(naiAuditRejectedStatusChangesRemStatusTo)) {
			log.info("Property "
					+ NAI_AUDIT_REJECTED_STATUS_CHANGES_REM_STATUS_TO_PROPERTY
					+ " is not set; disabling NAI Audit functionality");
			return;
		}

		log.info("Enabling NAI Audit functionality (all required properties for NAI Audit functionality have been set).");
		editNaiAuditEnabled = true;
	}

	public String getFieldInputValidationRegexNaiAuditComment() {
		return fieldInputValidationRegexNaiAuditComment;
	}

	public String getDbServer() {
		return dbServer;
	}

	public String getDbDatabase() {
		return dbDatabase;
	}

	public String getDbPort() {
		return dbPort;
	}

	public String getDbUser() {
		return dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public String getProgramVersion() {
		return programVersion;
	}

	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}

	public Set<String> getAttributeNames() {
		return attributeMap.keySet();
	}

	public Set<String> getCcAttributeNames() {
		final Set<String> ccAttributeNames = new HashSet<String>();
		for (final String key : attributeMap.keySet()) {
			ccAttributeNames.add(attributeMap.get(key));
		}
		return ccAttributeNames;
	}

	public String getFieldInputValidationRegexUsername() {
		return fieldInputValidationRegexUsername;
	}

	public String getFieldInputValidationRegexPassword() {
		return fieldInputValidationRegexPassword;
	}

	public String getFieldInputValidationRegexAttr(final String attrLabel) {

		return attrRegexMap.get(attrLabel);
	}

	public String getAttrLabel(final int i) {
		return attrLabelsByIndex.get(i);
	}

	public String getAuditorRoleName() {
		return auditorRoleName;
	}

	public boolean isEditNaiAuditEnabled() {
		return editNaiAuditEnabled;
	}

	public List<String> getNaiAuditStatusChoices() {
		return naiAuditStatusChoices;
	}

	public DateFormat getNaiAuditDateFormat() {
		return naiAuditDateFormat;
	}

	public String getNaiAuditRejectedStatusName() {
		return naiAuditRejectedStatusName;
	}

	public String getNaiAuditRemStatusToAudit() {
		return naiAuditRemStatusToAudit;
	}

	public String getNaiAuditRejectedStatusChangesRemStatusTo() {
		return naiAuditRejectedStatusChangesRemStatusTo;
	}

	public boolean isNaiAuditPreloadComponents() {
		return naiAuditPreloadComponents;
	}

	public int getNaiAuditPreloadBatchSize() {
		return naiAuditPreloadBatchSize;
	}

}
