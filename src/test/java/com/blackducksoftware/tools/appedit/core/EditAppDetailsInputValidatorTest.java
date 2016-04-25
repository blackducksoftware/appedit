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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *******************************************************************************/
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
