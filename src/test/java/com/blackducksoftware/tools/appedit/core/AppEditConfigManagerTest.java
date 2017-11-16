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
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

public class AppEditConfigManagerTest {

    @Test
    public void testBasic() throws Exception {
	Properties props = new Properties();
	props.setProperty("cc.server.name", "test server");
	props.setProperty("cc.user.name", "test user");
	props.setProperty("cc.password", "test password");
	props.setProperty("app.version", "Unspecified");

	props.setProperty("db.server", "test db server");

	props.setProperty("attr.0.label", "CustomAttr name 1");
	props.setProperty("attr.0.ccname", "CC attr 1");
	props.setProperty("attr.0.regex", ".+");

	props.setProperty("attr.1.label", "CustomAttr name 2");
	props.setProperty("attr.1.ccname", "CC attr 2");
	props.setProperty("attr.1.regex", ".+");

	AppEditConfigManager config = new AppEditConfigManager(props);

	assertEquals("Unspecified", config.getAppVersion());

	Map<String, String> attrMap = config.getAttributeMap();
	assertTrue(attrMap.containsKey("CustomAttr name 1"));
	assertTrue(attrMap.containsKey("CustomAttr name 2"));

	assertEquals("CC attr 1", attrMap.get("CustomAttr name 1"));
	assertEquals("CC attr 2", attrMap.get("CustomAttr name 2"));

	Set<String> customAttrNames = config.getAttributeNames();
	assertTrue(customAttrNames.contains("CustomAttr name 1"));
	assertTrue(customAttrNames.contains("CustomAttr name 2"));

	Set<String> CcNames = config.getCcAttributeNames();
	assertTrue(CcNames.contains("CC attr 1"));
	assertTrue(CcNames.contains("CC attr 2"));
    }

    @Test
    public void testFormInputValidationRegexes() throws Exception {
	Properties props = new Properties();
	props.setProperty("cc.server.name", "test server");
	props.setProperty("cc.user.name", "test user");
	props.setProperty("cc.password", "test password");
	props.setProperty("app.version", "Unspecified");

	props.setProperty("db.server", "test db server");

	props.setProperty("attr.0.label", "CustomAttr name 1");
	props.setProperty("attr.0.ccname", "CC attr 1");
	props.setProperty("attr.0.regex", "[G-I]+");

	props.setProperty("attr.1.label", "CustomAttr name 2");
	props.setProperty("attr.1.ccname", "CC attr 2");
	props.setProperty("attr.1.regex", ".+");

	props.setProperty("field.input.validation.regex.username", "[A-C]+");
	props.setProperty("field.input.validation.regex.psw", "[D-F]+");

	AppEditConfigManager config = new AppEditConfigManager(props);

	assertEquals("[A-C]+", config.getFieldInputValidationRegexUsername());
	assertEquals("[D-F]+", config.getFieldInputValidationRegexPassword());
	assertEquals("[G-I]+",
		config.getFieldInputValidationRegexAttr("CustomAttr name 1"));
    }
}
