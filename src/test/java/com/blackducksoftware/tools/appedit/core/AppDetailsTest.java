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

import org.junit.Test;

import com.blackducksoftware.tools.appedit.core.application.AppDetails;

public class AppDetailsTest {

    @Test
    public void test() {
	AppDetails appDetails = new AppDetails("testappid", "test app name");
	assertEquals("testappid", appDetails.getAppId());
	assertEquals("test app name", appDetails.getAppName());
	assertEquals(null, appDetails.getCustomAttributeValue("bogus"));
	appDetails.addCustomAttributeValue("test custom attr name",
		"test custom attr value");
	assertEquals("test custom attr value",
		appDetails.getCustomAttributeValue("test custom attr name"));
    }

}
