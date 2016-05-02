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
package com.blackducksoftware.tools.appedit.naiaudit.dao.cc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.dao.AppCompVulnDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetailsBuilder;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.commonframework.core.exception.CommonFrameworkException;
import com.blackducksoftware.tools.connector.codecenter.ICodeCenterServerWrapper;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;
import com.blackducksoftware.tools.connector.codecenter.common.CodeCenterComponentPojo;
import com.blackducksoftware.tools.connector.codecenter.common.RequestPojo;
import com.blackducksoftware.tools.connector.codecenter.common.RequestVulnerabilityPojo;

/**
 * Code Center Application Component Vulnerability Details DAO. Gets/updates
 * details (remediation status and comments) specific to a vulnerability on a
 * specific application as used in a specific application.
 * 
 * @author sbillings
 *
 */
public class CcAppCompVulnDetailsDao implements AppCompVulnDetailsDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());

    private ICodeCenterServerWrapper ccsw;

    @Inject
    public void setCcsw(ICodeCenterServerWrapper ccsw) {
	this.ccsw = ccsw;
    }

    /**
     * Update vulnerability details.
     * 
     * @param appCompVulnDetails
     * @return
     * @throws AppEditException
     */
    @Override
    public AppCompVulnDetails updateAppCompVulnDetails(
	    AppCompVulnDetails appCompVulnDetails) throws AppEditException {
	logger.debug("updateAppCompVulnDetails(); called with: "
		+ appCompVulnDetails);

	RequestVulnerabilityPojo updatedRequestVulnerability = new RequestVulnerabilityPojo(
		appCompVulnDetails.getAppCompVulnKey().getVulnerabilityId(),
		appCompVulnDetails.getVulnerabilityName(),
		appCompVulnDetails.getVulnerabilityDescription(),
		appCompVulnDetails.getVulnerabilitySeverity(),
		appCompVulnDetails.getVulnerabilityBaseScore(),
		appCompVulnDetails.getVulnerabilityExploitableScore(),
		appCompVulnDetails.getVulnerabilityImpactScore(),
		appCompVulnDetails.getVulnerabilityDateCreated(),
		appCompVulnDetails.getVulnerabilityDateModified(),
		appCompVulnDetails.getVulnerabilityDatePublished(),
		appCompVulnDetails.getAppCompVulnKey().getRequestId(),
		appCompVulnDetails.getVulnerabilityRemediationComments(),
		appCompVulnDetails.getVulnerabilityRemediationStatus(),
		appCompVulnDetails.getVulnerabilityTargetRemediationDate(),
		appCompVulnDetails.getVulnerabilityActualRemediationDate());
	logger.debug("updatedRequestVulnerability: "
		+ updatedRequestVulnerability);
	try {
	    ccsw.getRequestManager().updateRequestVulnerability(
		    updatedRequestVulnerability);
	} catch (CommonFrameworkException e) {
	    throw new AppEditException(
		    "Error updating Vulnerability metadata for "
			    + appCompVulnDetails.getAppCompVulnKey() + ": "
			    + e.getMessage(), e);
	}

	return appCompVulnDetails;
    }

    /**
     * Get details for a specific vulnerability
     */
    @Override
    public AppCompVulnDetails getAppCompVulnDetails(AppCompVulnKey key)
	    throws AppEditException {
	logger.debug("getAppCompVulnDetails() called with key: " + key);

	ApplicationPojo app;
	try {
	    app = ccsw.getApplicationManager().getApplicationById(
		    key.getApplicationId());
	} catch (CommonFrameworkException e1) {
	    throw new AppEditException("Error getting application with ID "
		    + key.getApplicationId() + ": " + e1.getMessage(), e1);
	}

	CodeCenterComponentPojo comp;
	try {
	    comp = ccsw.getComponentManager().getComponentById(
		    CodeCenterComponentPojo.class, key.getComponentId());
	} catch (CommonFrameworkException e1) {
	    throw new AppEditException("Error getting component with ID "
		    + key.getComponentId() + ": " + e1.getMessage(), e1);
	}

	List<RequestVulnerabilityPojo> vulns;
	try {
	    vulns = ccsw.getRequestManager().getVulnerabilitiesByRequestId(
		    key.getRequestId());
	} catch (CommonFrameworkException e) {
	    throw new AppEditException(
		    "Error getting vulnerabilities for request ID "
			    + key.getRequestId() + ": " + e.getMessage(), e);
	}
	for (RequestVulnerabilityPojo vuln : vulns) {
	    if (vuln.getVulnerabilityId().equals(key.getVulnerabilityId())) {
		AppCompVulnDetails appCompVulnDetails = deriveAppCompVulnDetails(
			key, app, comp, vuln);
		return appCompVulnDetails;
	    }
	}

	throw new AppEditException("Vulnerability with key " + key.toString()
		+ " not found");
    }

    /**
     * Get a map containing all of the vulnerability details for the given
     * application.
     * 
     * @param applicationId
     * @return
     * @throws AppEditException
     */
    @Override
    public Map<AppCompVulnKey, AppCompVulnDetails> getAppCompVulnDetailsMap(
	    String applicationId) throws AppEditException {
	logger.debug("getAppCompVulnDetailsMap() called with appId: "
		+ applicationId);

	ApplicationPojo app;
	try {
	    app = ccsw.getApplicationManager()
		    .getApplicationById(applicationId);
	} catch (CommonFrameworkException e1) {
	    throw new AppEditException("Error getting application with ID "
		    + applicationId + ": " + e1.getMessage(), e1);
	}

	List<RequestPojo> requests;
	try {
	    requests = ccsw.getApplicationManager().getRequestsByAppId(
		    applicationId);
	} catch (CommonFrameworkException e) {
	    throw new AppEditException(
		    "Error getting requests for application with ID "
			    + applicationId + ": " + e.getMessage(), e);
	}
	Map<AppCompVulnKey, AppCompVulnDetails> result = getAllVulnerabilitiesForRequests(
		app, requests);

	return result;
    }

    private Map<AppCompVulnKey, AppCompVulnDetails> getAllVulnerabilitiesForRequests(
	    ApplicationPojo app, List<RequestPojo> requests)
	    throws AppEditException {
	Map<AppCompVulnKey, AppCompVulnDetails> vulnMap = new HashMap<>();
	for (RequestPojo request : requests) {
	    String requestId = request.getRequestId();

	    CodeCenterComponentPojo comp;
	    try {
		comp = ccsw.getComponentManager()
			.getComponentById(CodeCenterComponentPojo.class,
				request.getComponentId());
	    } catch (CommonFrameworkException e1) {
		throw new AppEditException("Error getting component with ID "
			+ request.getComponentId() + ": " + e1.getMessage(), e1);
	    }
	    List<RequestVulnerabilityPojo> requestVulnerabilities;
	    try {
		requestVulnerabilities = ccsw.getRequestManager()
			.getVulnerabilitiesByRequestId(requestId);
	    } catch (CommonFrameworkException e) {
		throw new AppEditException(
			"Error getting vulnerabilities for request ID "
				+ requestId + ": " + e.getMessage(), e);
	    }
	    collectRequestVulnerabilities(vulnMap, app, comp,
		    requestVulnerabilities);
	}
	return vulnMap;
    }

    private void collectRequestVulnerabilities(
	    Map<AppCompVulnKey, AppCompVulnDetails> result,
	    ApplicationPojo app, CodeCenterComponentPojo comp,
	    List<RequestVulnerabilityPojo> requestVulnerabilities)
	    throws AppEditException {
	for (RequestVulnerabilityPojo requestVulnerability : requestVulnerabilities) {

	    logger.debug("Processing: Comp: " + comp.getName() + " / "
		    + comp.getVersion() + ": Vuln: "
		    + requestVulnerability.getVulnerabilityName());
	    AppCompVulnKey key = deriveKey(app.getId(), comp,
		    requestVulnerability);

	    AppCompVulnDetails appCompVulnDetails = deriveAppCompVulnDetails(
		    key, app, comp, requestVulnerability);
	    result.put(key, appCompVulnDetails);
	}
    }

    private AppCompVulnKey deriveKey(String applicationId,
	    CodeCenterComponentPojo comp,
	    RequestVulnerabilityPojo requestVulnerability) {
	AppCompVulnKey key = new AppCompVulnKey(applicationId,
		requestVulnerability.getRequestId(), comp.getId(),
		requestVulnerability.getVulnerabilityId());
	return key;
    }

    private AppCompVulnDetails deriveAppCompVulnDetails(AppCompVulnKey key,
	    ApplicationPojo app, CodeCenterComponentPojo comp,
	    RequestVulnerabilityPojo requestVulnerability)
	    throws AppEditException {
	AppCompVulnDetails appCompVulnDetails = (new AppCompVulnDetailsBuilder())
		.setAppCompVulnKey(key)
		.setApplicationName(app.getName())
		.setApplicationVersion(app.getVersion())
		.setComponentName(comp.getName())
		.setComponentVersion(comp.getVersion())
		.setVulnerabilityName(
			requestVulnerability.getVulnerabilityName())
		.setVulnerabilitySeverity(requestVulnerability.getSeverity())
		.setVulnerabilityBaseScore(requestVulnerability.getBaseScore())
		.setVulnerabilityExploitableScore(
			requestVulnerability.getExploitabilityScore())
		.setVulnerabilityImpactScore(
			requestVulnerability.getImpactScore())
		.setVulnerabilityDateCreated(
			requestVulnerability.getDateCreated())
		.setVulnerabilityDateModified(
			requestVulnerability.getDateModified())
		.setVulnerabilityDatePublished(
			requestVulnerability.getDatePublished())
		.setVulnerabilityDescription(
			requestVulnerability.getDescription())
		.setVulnerabilityTargetRemediationDate(
			requestVulnerability.getTargetRemediationDate())
		.setVulnerabilityActualRemediationDate(
			requestVulnerability.getActualRemediationDate())
		.setVulnerabilityRemediationStatus(
			requestVulnerability.getReviewStatusName())
		.setVulnerabilityRemediationComments(
			requestVulnerability.getComments())
		.createAppCompVulnDetails();

	return appCompVulnDetails;
    }

    /**
     * Get application by name/version.
     * 
     * @param appName
     * @param appVersion
     * @return
     * @throws AppEditException
     */
    @Override
    public ApplicationPojo getApplicationByNameVersion(String appName,
	    String appVersion, boolean refreshCache) throws AppEditException {
	if (refreshCache) {
	    try {
		ccsw.getApplicationManager()
			.removeApplicationFromCacheByNameVersion(appName,
				appVersion);
	    } catch (CommonFrameworkException e) {
		throw new AppEditException("Error removing application "
			+ appName + " / " + appVersion + " from cache: "
			+ e.getMessage(), e);
	    }
	}
	ApplicationPojo app;
	try {
	    app = ccsw.getApplicationManager().getApplicationByNameVersion(
		    appName, appVersion);
	} catch (CommonFrameworkException e) {
	    throw new AppEditException("Error getting application " + appName
		    + " / " + appVersion + ": " + e.getMessage(), e);
	}
	return app;
    }

    /**
     * Get application by ID.
     * 
     * @param appId
     * @return
     * @throws AppEditException
     */
    @Override
    public ApplicationPojo getApplicationById(String appId, boolean refreshCache)
	    throws AppEditException {
	if (refreshCache) {
	    try {
		ccsw.getApplicationManager().removeApplicationFromCacheById(
			appId);
	    } catch (CommonFrameworkException e) {
		throw new AppEditException(
			"Error removing application with ID " + appId
				+ " from cache: " + e.getMessage(), e);
	    }
	}
	ApplicationPojo app;
	try {
	    app = ccsw.getApplicationManager().getApplicationById(appId);
	} catch (CommonFrameworkException e) {
	    throw new AppEditException("Error getting application with ID "
		    + appId + ": " + e.getMessage(), e);
	}
	return app;
    }

}
