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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blackducksoftware.tools.appedit.appdetails.inputvalidation.InputValidatorEditAppDetails;

public class EditAppDetailsInputValidatorTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void test() throws Exception {
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
	props.setProperty("attr.1.regex", "[A-C]+");

	props.setProperty("field.input.validation.regex.username", "[A-C]+");
	props.setProperty("field.input.validation.regex.password", "[D-F]+");

	AppEditConfigManager config = new AppEditConfigManager(props);

	InputValidatorEditAppDetails inputValidator = new InputValidatorEditAppDetails(
		config);

	assertTrue(inputValidator.validateAttributeValue("CustomAttr name 1",
		"GHIIHG"));
	assertFalse(inputValidator.validateAttributeValue("CustomAttr name 1",
		"GHIIHGA"));
	assertFalse(inputValidator.validateAttributeValue("CustomAttr name 1",
		"ghi"));
	assertFalse(inputValidator.validateAttributeValue("CustomAttr name 1",
		"\""));
	assertFalse(inputValidator.validateAttributeValue("CustomAttr name 1",
		"'"));

	assertTrue(inputValidator.validateAttributeValue("CustomAttr name 2",
		"ABCCBA"));
	assertFalse(inputValidator.validateAttributeValue("CustomAttr name 2",
		"ABCABCG"));
	assertFalse(inputValidator.validateAttributeValue("CustomAttr name 2",
		"abc"));
	assertFalse(inputValidator.validateAttributeValue("CustomAttr name 2",
		"\""));
	assertFalse(inputValidator.validateAttributeValue("CustomAttr name 2",
		"'"));
    }

}
