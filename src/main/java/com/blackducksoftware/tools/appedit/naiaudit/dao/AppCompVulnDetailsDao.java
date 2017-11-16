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
    /**
     * Update vulnerability details.
     * 
     * @param appCompVulnDetails
     * @return
     * @throws AppEditException
     */
    AppCompVulnDetails updateAppCompVulnDetails(
	    AppCompVulnDetails appCompVulnDetails) throws AppEditException;

    /**
     * Get application by name/version.
     * 
     * @param appName
     * @param appVersion
     * @return
     * @throws AppEditException
     */
    ApplicationPojo getApplicationByNameVersion(String appName,
	    String appVersion, boolean refreshCache) throws AppEditException;

    /**
     * Get application by ID.
     * 
     * @param appId
     * @return
     * @throws AppEditException
     */
    ApplicationPojo getApplicationById(String appId, boolean refreshCache)
	    throws AppEditException;

    /**
     * Get the details on one vulnerability.
     * 
     * @param key
     * @return
     */
    AppCompVulnDetails getAppCompVulnDetails(AppCompVulnKey key)
	    throws AppEditException;

    /**
     * Get a map containing all of the vulnerability details for the given
     * application.
     * 
     * @param applicationId
     * @return
     * @throws AppEditException
     */
    Map<AppCompVulnKey, AppCompVulnDetails> getAppCompVulnDetailsMap(
	    String applicationId) throws AppEditException;
}
