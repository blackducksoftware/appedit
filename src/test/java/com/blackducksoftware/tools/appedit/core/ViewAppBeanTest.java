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

import org.junit.Test;

import com.blackducksoftware.tools.appedit.appdetails.model.ViewAppBean;
import com.blackducksoftware.tools.connector.codecenter.common.AttributeValuePojo;

public class ViewAppBeanTest {

    @Test
    public void test() {
        ViewAppBean bean = new ViewAppBean();
        bean.setAppId("test app id");
        assertEquals("test app id", bean.getAppId());
        bean.setAppName("test app name");
        assertEquals("test app name", bean.getAppName());

        List<String> attrNames = new ArrayList<String>();
        attrNames.add("itsm");
        attrNames.add("other");

        List<AttributeValuePojo> attrValues = new ArrayList<>();
        attrValues.add(new AttributeValuePojo("attrId1", "itsm", "itsm value"));
        attrValues.add(new AttributeValuePojo("attrId2", "other", "other value"));

        bean.setAttrNames(attrNames);
        bean.setAttrValues(attrValues);

        assertEquals("itsm", bean.getAttrNames().get(0));
        assertEquals("other", bean.getAttrNames().get(1));

        assertEquals("itsm value", bean.getAttrValues().get(0).getValue());
        assertEquals("other value", bean.getAttrValues().get(1).getValue());
    }

}
