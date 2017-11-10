/**
 * Application Details Edit Webapp
 *
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.blackducksoftware.tools.appedit.appdetails.model;

import java.util.HashMap;
import java.util.Map;

import com.blackducksoftware.tools.connector.codecenter.common.AttributeValuePojo;

/**
 * Code Center-centric application details bean. Attribute values are stored by
 * Code Center attribute name, but it ONLY has the attributes that the config
 * tells us to manage.
 *
 * @author sbillings
 *
 */
public class AppDetails {
    private final String appName;

    private final String appId;

    private final Map<String, AttributeValuePojo> customAttributeValues = new HashMap<>();

    public AppDetails(String appId, String appName) {
	this.appId = appId;
	this.appName = appName;
    }

    /**
     * Getter for application name.
     *
     * @return
     */
    public String getAppName() {
	return appName;
    }

    /**
     * Getter for application ID.
     *
     * @return
     */
    public String getAppId() {
	return appId;
    }

    /**
     * Add a custom attribute name/value.
     *
     * @param attrName
     * @param attrValue
     */
    public void addCustomAttributeValue(String attrName,
	    AttributeValuePojo attrValue) {
	customAttributeValues.put(attrName, attrValue);
    }

    /**
     * Get a custom attribute value by name.
     *
     * @param attrName
     * @return
     */
    public AttributeValuePojo getCustomAttributeValue(String attrName) {
	if (customAttributeValues.containsKey(attrName)) {
	    return customAttributeValues.get(attrName);
	} else {
	    return null;
	}
    }

    @Override
    public String toString() {
	return "AppDetails [appName=" + appName + ", appId=" + appId
		+ ", customAttributeValues=" + customAttributeValues + "]";
    }

}
