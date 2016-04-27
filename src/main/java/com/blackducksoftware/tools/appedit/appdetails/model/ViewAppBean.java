/*******************************************************************************
 * Copyright (C) 2015, 2016 Black Duck Software, Inc.
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
