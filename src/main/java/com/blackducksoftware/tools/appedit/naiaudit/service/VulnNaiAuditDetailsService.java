/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
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
