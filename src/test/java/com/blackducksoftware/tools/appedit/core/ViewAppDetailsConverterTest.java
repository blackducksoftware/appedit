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
package com.blackducksoftware.tools.appedit.core;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import com.blackducksoftware.tools.appedit.appdetails.model.AppDetails;
import com.blackducksoftware.tools.appedit.appdetails.model.ViewAppBean;
import com.blackducksoftware.tools.appedit.appdetails.service.impl.AppDetailsBeanConverterImpl;
import com.blackducksoftware.tools.connector.codecenter.common.AttributeValuePojo;

public class ViewAppDetailsConverterTest {
    private static final String ITSM_ATTR_NAME = "Sample Textfield";

    private static final String ITRC_ATTR_NAME = "Other Textfield";

    private static AppEditConfigManager config = null;

    private static AppDetailsBeanConverterImpl converter;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	Properties props = createProperties();
	config = new AppEditConfigManager(props);
	converter = new AppDetailsBeanConverterImpl();
	converter.setConfig(config);
    }

    @Test
    public void testToView() {
	AppDetails appDetails = new AppDetails("test app id", "test app name");
	appDetails.addCustomAttributeValue(ITSM_ATTR_NAME,
		new AttributeValuePojo("attrId1", "itsm", "test itsm value"));
	appDetails.addCustomAttributeValue(ITRC_ATTR_NAME,
		new AttributeValuePojo("attrId2", "other", "test other value"));

	ViewAppBean viewAppBean = converter.createViewAppBean(appDetails);
	assertEquals("test app id", viewAppBean.getAppId());
	assertEquals("test app name", viewAppBean.getAppName());

	assertEquals(
		"test itsm value",
		viewAppBean.getAttrValues()
			.get(viewAppBean.getAttrNames().indexOf("itsm"))
			.getValue());
	assertEquals(
		"test other value",
		viewAppBean.getAttrValues()
			.get(viewAppBean.getAttrNames().indexOf("itrc"))
			.getValue());
    }

    @Test
    public void testFromView() {
	ViewAppBean viewAppBean = new ViewAppBean();
	viewAppBean.setAppId("test app id");
	viewAppBean.setAppName("test app name");

	List<String> attrNames = new ArrayList<String>();
	attrNames.add("itsm");
	attrNames.add("itrc");

	List<AttributeValuePojo> attrValues = new ArrayList<>();
	attrValues.add(new AttributeValuePojo("id1", "itsm", "itsm value"));
	attrValues.add(new AttributeValuePojo("id2", "other", "other value"));

	viewAppBean.setAttrNames(attrNames);
	viewAppBean.setAttrValues(attrValues);

	AppDetails appDetails = converter.createAppDetails(viewAppBean);
	assertEquals("test app id", appDetails.getAppId());
	assertEquals("test app name", appDetails.getAppName());
	assertEquals(null, appDetails.getCustomAttributeValue("bogus"));
	assertEquals("itsm value",
		appDetails.getCustomAttributeValue(ITSM_ATTR_NAME).getValue());
	assertEquals("other value",
		appDetails.getCustomAttributeValue(ITRC_ATTR_NAME).getValue());
    }

    private static Properties createProperties() {
	Properties props = new Properties();

	props.setProperty("cc.server.name", "http://cc-integration/");
	props.setProperty("cc.user.name", "unitTester@blackducksoftware.com");
	props.setProperty("cc.password", "blackduck");
	props.setProperty("cc.password.isplaintext", "true");
	props.setProperty("app.version", "Unspecified");

	props.setProperty("db.server", "test db server");

	props.setProperty("attr.0.label", "itsm");
	props.setProperty("attr.0.ccname", ITSM_ATTR_NAME);
	props.setProperty("attr.0.regex", ".+");

	props.setProperty("attr.1.label", "itrc");
	props.setProperty("attr.1.ccname", ITRC_ATTR_NAME);
	props.setProperty("attr.1.regex", ".+");
	return props;
    }
}
