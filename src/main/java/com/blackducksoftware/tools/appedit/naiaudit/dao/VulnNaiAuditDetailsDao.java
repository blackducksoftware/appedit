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
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;

/**
 * Interface for DAOs that add/get/update NAI Audit details.
 * 
 * @author sbillings
 *
 */
public interface VulnNaiAuditDetailsDao {

    /**
     * Create a new set of NAI Audit details.
     * 
     * @param vunlNaiAuditDetails
     * @return
     * @throws AppEditException
     */
    VulnNaiAuditDetails insertVulnNaiAuditDetails(
	    VulnNaiAuditDetails vunlNaiAuditDetails) throws AppEditException;

    /**
     * Update a set of NAI Audit details.
     * 
     * @param vunlNaiAuditDetails
     * @return
     * @throws AppEditException
     */
    VulnNaiAuditDetails updateVulnNaiAuditDetails(
	    VulnNaiAuditDetails vunlNaiAuditDetails) throws AppEditException;

    /**
     * Get the NAI Audit details for one vulnerability.
     * 
     * @param key
     * @return
     */
    VulnNaiAuditDetails getVulnNaiAuditDetails(AppCompVulnKey key)
	    throws AppEditException;

    /**
     * Get a map containing all NAI Audit Details for the given application.
     * 
     * @param applicationId
     * @return
     * @throws AppEditException
     */
    Map<AppCompVulnKey, VulnNaiAuditDetails> getVulnNaiAuditDetailsMap(
	    String applicationId) throws AppEditException;
}
