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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.connector.codecenter.common.AttributeValuePojo;

/**
 * View-friendly version of AppDetails. Attribute values are stored by UI label.
 * This is used by the UI since Spring MVC wants a bean with named
 * getters/setters (including custom attributes).
 *
 * @author sbillings
 *
 */
public class ViewAppBean {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());

    private String appId;

    private String appName;

    private List<String> attrNames = new ArrayList<String>();

    private List<AttributeValuePojo> attrValues = new ArrayList<>();

    public ViewAppBean() {
	logger.info("ViewAppBean: default constructor called");
    }

    public ViewAppBean(String appId, String appName, List<String> attrNames,
	    List<AttributeValuePojo> attrValues) {
	logger.info("ViewAppBean: parameterized constructor called");
	this.appId = appId;
	this.appName = appName;

	this.attrNames = attrNames;
	this.attrValues = attrValues;
    }

    public String getAppId() {
	return appId;
    }

    public void setAppId(String appId) {
	this.appId = appId;
    }

    public String getAppName() {
	return appName;
    }

    public void setAppName(String appName) {
	this.appName = appName;
    }

    public List<String> getAttrNames() {
	return attrNames;
    }

    public void setAttrNames(List<String> attrNames) {
	this.attrNames = attrNames;
    }

    public List<AttributeValuePojo> getAttrValues() {
	return attrValues;
    }

    public void setAttrValues(List<AttributeValuePojo> attrValues) {
	this.attrValues = attrValues;
    }

    @Override
    public String toString() {
	return "AppImpl [appId=" + appId + ", appName=" + appName
		+ ", attrNames=" + attrNames + "]" + ", attrValues="
		+ attrValues + "]";
    }
}
