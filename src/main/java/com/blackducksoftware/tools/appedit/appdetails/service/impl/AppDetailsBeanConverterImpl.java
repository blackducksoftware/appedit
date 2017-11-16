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
package com.blackducksoftware.tools.appedit.appdetails.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.blackducksoftware.tools.appedit.appdetails.model.AppDetails;
import com.blackducksoftware.tools.appedit.appdetails.model.ViewAppBean;
import com.blackducksoftware.tools.appedit.appdetails.service.AppDetailsBeanConverter;
import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.connector.codecenter.common.AttributeValuePojo;

/**
 * Converts back and forth between generic AppDetails (Code Center-centric) and
 * ViewAppBean (view-friendly).
 *
 * @author sbillings
 *
 */
public class AppDetailsBeanConverterImpl implements AppDetailsBeanConverter {
    private AppEditConfigManager config;

    @Inject
    public void setConfig(AppEditConfigManager config) {
	this.config = config;
    }

    /**
     * Convert an AppDetails bean to a ViewAppBean.
     */
    @Override
    public ViewAppBean createViewAppBean(AppDetails appDetails) {
	Map<String, String> attrMap = config.getAttributeMap();

	ViewAppBean viewAppBean = new ViewAppBean();
	viewAppBean.setAppId(appDetails.getAppId());
	viewAppBean.setAppName(appDetails.getAppName());

	List<String> attrNames = new ArrayList<String>(attrMap.keySet().size());
	List<AttributeValuePojo> attrValues = new ArrayList<>(attrMap.keySet()
		.size());

	// Copy custom attributes, preserving the order in the config file
	for (int i = 0; i < attrMap.keySet().size(); i++) {
	    String attrLabel = config.getAttrLabel(i);
	    String attrCodeCenterName = attrMap.get(attrLabel);
	    attrNames.add(attrLabel);
	    attrValues.add(appDetails
		    .getCustomAttributeValue(attrCodeCenterName));
	}

	viewAppBean.setAttrNames(attrNames);
	viewAppBean.setAttrValues(attrValues);

	return viewAppBean;
    }

    /**
     * Convert a ViewAppBean to an AppDetails bean.
     */
    @Override
    public AppDetails createAppDetails(ViewAppBean viewAppBean) {
	AppDetails appDetails = new AppDetails(viewAppBean.getAppId(),
		viewAppBean.getAppName());

	Map<String, String> attrMap = config.getAttributeMap();
	for (String attrLabel : attrMap.keySet()) {
	    int attrValueIndex = viewAppBean.getAttrNames().indexOf(attrLabel);
	    appDetails.addCustomAttributeValue(attrMap.get(attrLabel),
		    viewAppBean.getAttrValues().get(attrValueIndex));
	}
	return appDetails;
    }
}
