/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.blackducksoftware.tools.appedit.naiaudit.dao;

import java.util.Map;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;

/**
 * Application Component Vulnerability Details DAO. Gets/updates details
 * specific to a vulnerability on a specific application as used in a specific
 * application.
 * 
 * @author sbillings
 *
 */
public interface AppCompVulnDetailsDao {
    AppCompVulnDetails updateAppCompVulnDetails(
	    AppCompVulnDetails appCompVulnDetails) throws AppEditException;

    ApplicationPojo getApplicationByNameVersion(String appName,
	    String appVersion) throws AppEditException;

    ApplicationPojo getApplicationById(String appId) throws AppEditException;

    Map<AppCompVulnKey, AppCompVulnDetails> getAppCompVulnDetailsMap(
	    String applicationId) throws AppEditException;
}
