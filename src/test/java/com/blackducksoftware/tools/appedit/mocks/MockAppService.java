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
package com.blackducksoftware.tools.appedit.mocks;

import java.util.ArrayList;
import java.util.List;

import com.blackducksoftware.tools.appedit.appdetails.model.AppDetails;
import com.blackducksoftware.tools.appedit.appdetails.model.ViewAppBean;
import com.blackducksoftware.tools.appedit.appdetails.service.AppService;
import com.blackducksoftware.tools.connector.codecenter.attribute.AttributeDefinitionPojo;
import com.blackducksoftware.tools.connector.codecenter.common.AttributeValuePojo;

public class MockAppService implements AppService {

    @Override
    public boolean authorizeUser(String appId, String username) {
	if ("notAuthorizedUserName".equals(username)) {
	    return false;
	}
	return true;
    }

    @Override
    public AppDetails loadFromId(String appId, boolean refreshCache)
	    throws Exception {
	if ("bogus".equals(appId)) {
	    return null;
	}

	if ("throwException".equals(appId)) {
	    throw new Exception("Mock: error loading app");
	}

	AppDetails app = new AppDetails(appId, appId);
	AttributeValuePojo value = new AttributeValuePojo("attr1Id",
		"attr1Name", "attr1InitialValue");
	app.addCustomAttributeValue(value.getName(), value);

	return app;
    }

    @Override
    public AppDetails loadFromName(String appName, boolean refreshCache)
	    throws Exception {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void update(AppDetails app) throws Exception {
	if ("failOnUpdate".equals(app.getAppId())) {
	    throw new Exception("Mock: Error updating application");
	}

    }

    @Override
    public AttributeDefinitionPojo getAttributeDefinitionByName(String attrName)
	    throws Exception {

	if ("bogus".equals(attrName)) {
	    throw new Exception("mock: bad attr name");
	}

	return new AttributeDefinitionPojo(attrName, attrName, "Textfield", "",
		"Enter something");
    }

    @Override
    public ViewAppBean createViewAppBean(AppDetails appDetails) {

	ViewAppBean viewAppBean = new ViewAppBean();
	viewAppBean.setAppId(appDetails.getAppId());
	viewAppBean.setAppName(appDetails.getAppName());
	List<String> attrNames = new ArrayList<>();
	attrNames.add("attr1Name");
	viewAppBean.setAttrNames(attrNames);
	List<AttributeValuePojo> attrValues = new ArrayList<>();
	attrValues.add(new AttributeValuePojo("attr1Id", "attr1Name",
		"attr1Value"));
	viewAppBean.setAttrValues(attrValues);
	return viewAppBean;
    }

}
