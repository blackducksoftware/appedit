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
