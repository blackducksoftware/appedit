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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *******************************************************************************/
package com.blackducksoftware.tools.appedit.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.blackducksoftware.tools.appedit.appdetails.model.AppDetails;
import com.blackducksoftware.tools.connector.codecenter.common.AttributeValuePojo;

public class AppDetailsTest {

    private static final String TEST_CUSTOM_ATTR_NAME = "test custom attr name";

    @Test
    public void test() {
        AppDetails appDetails = new AppDetails("testappid", "test app name");
        assertEquals("testappid", appDetails.getAppId());
        assertEquals("test app name", appDetails.getAppName());
        assertEquals(null, appDetails.getCustomAttributeValue("bogus"));

        AttributeValuePojo attrValue = new AttributeValuePojo("testAttrId", TEST_CUSTOM_ATTR_NAME, "test custom attr value");
        appDetails.addCustomAttributeValue(TEST_CUSTOM_ATTR_NAME, attrValue);
        assertEquals("test custom attr value",
                appDetails.getCustomAttributeValue(TEST_CUSTOM_ATTR_NAME).getValue());
    }

}
