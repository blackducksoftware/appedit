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
