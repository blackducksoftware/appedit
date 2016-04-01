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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

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
