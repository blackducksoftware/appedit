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
package com.blackducksoftware.tools.appedit.core.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.ViewAppBean;

/**
 * Converts back and forth between generic AppDetails (Code Center-centric) and
 * ViewAppBean (view-friendly).
 *
 * @author sbillings
 *
 */
public class AppDetailsBeanConverter {
    private final AppEditConfigManager config;

    public AppDetailsBeanConverter(AppEditConfigManager config) {
	this.config = config;
    }

    /**
     * Create a View-friendly application bean from a Code Center-centric
     * application bean.
     *
     * @param appDetails
     * @return
     */
    public ViewAppBean createViewAppBean(AppDetails appDetails) {
	Map<String, String> attrMap = config.getAttributeMap();

	ViewAppBean viewAppBean = new ViewAppBean();
	viewAppBean.setAppId(appDetails.getAppId());
	viewAppBean.setAppName(appDetails.getAppName());

	List<String> attrNames = new ArrayList<String>(attrMap.keySet().size());
	List<String> attrValues = new ArrayList<String>(attrMap.keySet().size());

	// Copy custom attributes, preserving the order in the config file
	for (int i = 0; i < attrMap.keySet().size(); i++) {
	    String attrLabel = config.getAttrLabel(i);
	    String attrCodeCenterName = attrMap.get(attrLabel);
	    attrNames.add(attrLabel);
	    attrValues.add(appDetails
		    .getCustomAttributeValue(attrCodeCenterName));
	}

	viewAppBean.setAttrNames(attrNames);
	viewAppBean.setAttrValues(attrValues);

	return viewAppBean;
    }

    /**
     * Create a Code Center-centric application bean from a View-friendly
     * application bean.
     *
     * @param viewAppBean
     * @return
     */
    public AppDetails createAppDetails(ViewAppBean viewAppBean) {
	AppDetails appDetails = new AppDetails(viewAppBean.getAppId(),
		viewAppBean.getAppName());

	Map<String, String> attrMap = config.getAttributeMap();
	for (String attrLabel : attrMap.keySet()) {
	    int attrValueIndex = viewAppBean.getAttrNames().indexOf(attrLabel);
	    appDetails.addCustomAttributeValue(attrMap.get(attrLabel),
		    viewAppBean.getAttrValues().get(attrValueIndex));
	}
	return appDetails;
    }
}
