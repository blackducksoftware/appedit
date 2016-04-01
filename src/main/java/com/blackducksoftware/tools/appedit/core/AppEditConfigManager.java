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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *******************************************************************************/

/**
 *
 */
package com.blackducksoftware.tools.appedit.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.commonframework.core.config.ConfigurationManager;

/**
 * Configuration Manager for the AppEdit application.
 *
 * Not thread-safe, so keep references local.
 */
public class AppEditConfigManager extends ConfigurationManager {
    private static final String ATTR_PROPERTY_REGEX_SUFFIX = "regex";

    private static final String ATTR_PROPERTY_CCNAME_SUFFIX = "ccname";

    private static final String ATTR_PROPERTY_LABEL_SUFFIX = "label";

    private static final String ATTR_PROPERTY_PREFIX = "attr";

    private static final String APP_VERSION_PROPERTY = "app.version";

    private static final String APP_VERSION_DEFAULT = "Unspecified";

    private static final String FIELD_INPUT_VALIDATION_REGEX_USERNAME_PROPERTY = "field.input.validation.regex.username";

    private static final String FIELD_INPUT_VALIDATION_REGEX_PASSWORD_PROPERTY = "field.input.validation.regex.psw";

    private static final String FIELD_INPUT_VALIDATION_REGEX_USERNAME_DEFAULT = "[A-Za-z0-9@_.-]+";

    private static final String FIELD_INPUT_VALIDATION_REGEX_PASSWORD_DEFAULT = ".+";

    private final Logger log = LoggerFactory.getLogger(this.getClass()
            .getName());

    private String appVersion = "";

    private String fieldInputValidationRegexUsername = FIELD_INPUT_VALIDATION_REGEX_USERNAME_DEFAULT;

    private String fieldInputValidationRegexPassword = FIELD_INPUT_VALIDATION_REGEX_PASSWORD_DEFAULT;

    private String programVersion = "";

    private Map<String, String> attributeMap = new HashMap<String, String>();

    private Map<String, String> attrRegexMap = new HashMap<String, String>();

    private Map<Integer, String> attrLabelsByIndex = new HashMap<Integer, String>();

    public AppEditConfigManager() {
        super();
    }

    public AppEditConfigManager(String filename) throws Exception {
        super(filename);
        init();
    }

    public AppEditConfigManager(Properties props) throws Exception {
        super(props, APPLICATION.CODECENTER);
        init();
    }

    private void init() throws Exception {
        log.info("init()");
        try {
            String dynamicVersion = getClass().getPackage()
                    .getImplementationVersion();
            if (dynamicVersion != null) {
                programVersion = dynamicVersion;
            }
        } catch (Throwable t) {
            log.debug("Could not determine version for this program", t);
        }

        appVersion = getOptionalProperty(APP_VERSION_PROPERTY);
        if (appVersion == null) {
            appVersion = APP_VERSION_DEFAULT;
        }

        fieldInputValidationRegexUsername = getOptionalProperty(FIELD_INPUT_VALIDATION_REGEX_USERNAME_PROPERTY);
        if (fieldInputValidationRegexUsername == null) {
            fieldInputValidationRegexUsername = FIELD_INPUT_VALIDATION_REGEX_USERNAME_DEFAULT;
        }

        fieldInputValidationRegexPassword = getOptionalProperty(FIELD_INPUT_VALIDATION_REGEX_PASSWORD_PROPERTY);
        if (fieldInputValidationRegexPassword == null) {
            fieldInputValidationRegexPassword = FIELD_INPUT_VALIDATION_REGEX_PASSWORD_DEFAULT;
        }

        for (int i = 0;; i++) {
            String keyBase = ATTR_PROPERTY_PREFIX + "." + i + ".";
            String label = getOptionalProperty(keyBase
                    + ATTR_PROPERTY_LABEL_SUFFIX);
            if (label == null) {
                break;
            }
            String ccName = getProperty(keyBase + ATTR_PROPERTY_CCNAME_SUFFIX);
            String regex = getProperty(keyBase + ATTR_PROPERTY_REGEX_SUFFIX);

            attributeMap.put(label, ccName);
            attrRegexMap.put(label, regex);
            attrLabelsByIndex.put(i, label); // remember the sequence
        }
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
        Set<String> ccAttributeNames = new HashSet<String>();
        for (String key : attributeMap.keySet()) {
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

    public String getFieldInputValidationRegexAttr(String attrLabel) {

        return attrRegexMap.get(attrLabel);
    }

    public String getAttrLabel(int i) {
        return attrLabelsByIndex.get(i);
    }
}
