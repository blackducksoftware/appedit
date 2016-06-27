/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.blackducksoftware.tools.appedit.naiaudit.service;

import java.util.List;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;

/**
 * NAI Audit services.
 * 
 * @author sbillings
 *
 */
public interface VulnNaiAuditDetailsService {

    /**
     * Get Application by name/version.
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
    AppCompVulnComposite getAppCompVulnComposite(AppCompVulnKey key)
	    throws AppEditException;

    /**
     * Get an applications components+vulnerabilities.
     * 
     * @param applicationId
     * @return
     * @throws AppEditException
     */
    List<AppCompVulnComposite> getAppCompVulnCompositeList(String applicationId)
	    throws AppEditException;

    /**
     * Update the NAI Audit details for one component's vulnerability.
     * 
     * @param appCompVulnComposite
     * @return
     * @throws AppEditException
     */
    AppCompVulnComposite updateVulnNaiAuditDetails(
	    AppCompVulnComposite appCompVulnComposite) throws AppEditException;

}
