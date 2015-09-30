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
