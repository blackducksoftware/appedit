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
package com.blackducksoftware.tools.appedit.naiaudit.service;

import java.util.List;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
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
	    String appVersion) throws AppEditException;

    /**
     * Get application by ID.
     * 
     * @param appId
     * @return
     * @throws AppEditException
     */
    ApplicationPojo getApplicationById(String appId) throws AppEditException;

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
