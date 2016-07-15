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
package com.blackducksoftware.tools.appedit.naiaudit.dao.hybrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.dao.AppCompVulnDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.dao.ComponentNameVersionDao;
import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnerabilityDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetailsBuilder;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.IdNameVersion;
import com.blackducksoftware.tools.commonframework.core.exception.CommonFrameworkException;
import com.blackducksoftware.tools.connector.codecenter.ICodeCenterServerWrapper;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;
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
public class HybridAppCompVulnDetailsDao implements AppCompVulnDetailsDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getName());

	private ICodeCenterServerWrapper ccsw;

	@Inject
	public void setCcsw(final ICodeCenterServerWrapper ccsw) {
		this.ccsw = ccsw;
	}

	private AppEditConfigManager config;

	@Inject
	public void setConfig(final AppEditConfigManager config) {
		this.config = config;
	}

	private VulnerabilityDao vulnerabilityDao;

	@Inject
	public void setVulnerabilityDao(final VulnerabilityDao vulnerabilityDao) {
		this.vulnerabilityDao = vulnerabilityDao;
	}

	private ComponentNameVersionDao componentNameVersionDao;

	@Inject
	public void setComponentNameVersionDao(final ComponentNameVersionDao componentNameVersionDao) {
		this.componentNameVersionDao = componentNameVersionDao;
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
			final AppCompVulnDetails appCompVulnDetails) throws AppEditException {
		logger.debug("updateAppCompVulnDetails(); called with: "
				+ appCompVulnDetails);

		final RequestVulnerabilityPojo updatedRequestVulnerability = new RequestVulnerabilityPojo(
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
				appCompVulnDetails.getRequestId(),
				appCompVulnDetails.getVulnerabilityRemediationComments(),
				appCompVulnDetails.getVulnerabilityRemediationStatus(),
				appCompVulnDetails.getVulnerabilityTargetRemediationDate(),
				appCompVulnDetails.getVulnerabilityActualRemediationDate());
		logger.debug("updatedRequestVulnerability: "
				+ updatedRequestVulnerability);
		try {
			ccsw.getRequestManager().updateRequestVulnerability(updatedRequestVulnerability, config.isCcPre7_1_1());
		} catch (final CommonFrameworkException e) {
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
	public AppCompVulnDetails getAppCompVulnDetails(final AppCompVulnKey key)
			throws AppEditException {
		logger.debug("getAppCompVulnDetails() called with key: " + key);

		ApplicationPojo app;
		try {
			app = ccsw.getApplicationManager().getApplicationById(
					key.getApplicationId());
		} catch (final CommonFrameworkException e1) {
			throw new AppEditException("Error getting application with ID "
					+ key.getApplicationId() + ": " + e1.getMessage(), e1);
		}

		IdNameVersion comp;
		try {
			comp = componentNameVersionDao.getComponentNameVersionById(key.getComponentId());
		} catch (final AppEditException e1) {
			throw new AppEditException("Error getting component with ID "
					+ key.getComponentId() + ": " + e1.getMessage(), e1);
		}

		List<RequestVulnerabilityPojo> vulns;
		try {
			vulns = getVulnerabilitiesByAppIdCompId(key.getApplicationId(),
					key.getComponentId());
		} catch (final CommonFrameworkException e) {
			throw new AppEditException(
					"Error getting vulnerabilities for app ID "
							+ key.getApplicationId() + " comp ID: "
							+ key.getComponentId() + ": " + e.getMessage(), e);
		}
		for (final RequestVulnerabilityPojo vuln : vulns) {
			if (vuln.getVulnerabilityId().equals(key.getVulnerabilityId())) {
				final AppCompVulnDetails appCompVulnDetails = deriveAppCompVulnDetails(
						key, app, comp, vuln);
				return appCompVulnDetails;
			}
		}

		throw new AppEditException("Vulnerability with key " + key.toString()
				+ " not found");
	}

	private List<RequestVulnerabilityPojo> getVulnerabilitiesByAppIdCompId(
			final String appId, final String compId) throws CommonFrameworkException {
		final List<RequestVulnerabilityPojo> appVulns = new ArrayList<>();
		final List<RequestPojo> requests = ccsw.getApplicationManager()
				.getRequestsByAppId(appId);
		for (final RequestPojo request : requests) {
			final List<RequestVulnerabilityPojo> requestVulns = ccsw
					.getRequestManager().getVulnerabilitiesByRequestId(
							request.getRequestId());
			for (final RequestVulnerabilityPojo requestVuln : requestVulns) {
				appVulns.add(requestVuln);
			}
		}

		return appVulns;
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
			final String applicationId) throws AppEditException {
		logger.debug("getAppCompVulnDetailsMap() called with appId: "
				+ applicationId);

		ApplicationPojo app;
		try {
			app = ccsw.getApplicationManager()
					.getApplicationById(applicationId);
		} catch (final CommonFrameworkException e1) {
			throw new AppEditException("Error getting application with ID "
					+ applicationId + ": " + e1.getMessage(), e1);
		}

		List<RequestPojo> requests;
		try {
			requests = ccsw.getApplicationManager().getRequestsByAppId(
					applicationId);
		} catch (final CommonFrameworkException e) {
			throw new AppEditException(
					"Error getting requests for application with ID "
							+ applicationId + ": " + e.getMessage(), e);
		}
		final Map<AppCompVulnKey, AppCompVulnDetails> result = getNaiVulnerabilitiesForRequests(
				app, requests);

		return result;
	}

	private Map<AppCompVulnKey, AppCompVulnDetails> getNaiVulnerabilitiesForRequests(
			final ApplicationPojo app, final List<RequestPojo> requests)
					throws AppEditException {
		final Map<AppCompVulnKey, AppCompVulnDetails> vulnMap = new HashMap<>();
		for (final RequestPojo request : requests) {
			final String requestId = request.getRequestId();

			IdNameVersion comp;
			try {
				comp = componentNameVersionDao.getComponentNameVersionById(request.getComponentId());
			} catch (final AppEditException e1) {
				throw new AppEditException("Error getting component with ID "
						+ request.getComponentId() + ": " + e1.getMessage(), e1);
			}

			List<RequestVulnerabilityPojo> requestVulnerabilities;
			try {
				requestVulnerabilities = vulnerabilityDao
						.getVulnerabilitiesByRequestId(requestId);
			} catch (final AppEditException e) {
				logger.error("Error getting vulnerabilities for request ID " + requestId + ": " + e.getMessage());
				throw e;
			}
			collectRequestVulnerabilities(vulnMap, app, comp,
					requestVulnerabilities);
		}
		return vulnMap;
	}

	private void collectRequestVulnerabilities(
			final Map<AppCompVulnKey, AppCompVulnDetails> result,
			final ApplicationPojo app, final IdNameVersion comp,
			final List<RequestVulnerabilityPojo> requestVulnerabilities)
					throws AppEditException {
		for (final RequestVulnerabilityPojo requestVulnerability : requestVulnerabilities) {

			logger.debug("Processing: Comp: " + comp.getName() + " / "
					+ comp.getVersion() + ": Vuln: "
					+ requestVulnerability.getVulnerabilityName());
			final AppCompVulnKey key = deriveKey(app.getId(), comp,
					requestVulnerability);

			final AppCompVulnDetails appCompVulnDetails = deriveAppCompVulnDetails(
					key, app, comp, requestVulnerability);
			result.put(key, appCompVulnDetails);
		}
	}

	private AppCompVulnKey deriveKey(final String applicationId,
 final IdNameVersion comp,
			final RequestVulnerabilityPojo requestVulnerability) {
		final AppCompVulnKey key = new AppCompVulnKey(applicationId, requestVulnerability.getRequestId(), comp.getId(),
				requestVulnerability.getVulnerabilityId());
		return key;
	}

	private AppCompVulnDetails deriveAppCompVulnDetails(final AppCompVulnKey key,
 final ApplicationPojo app,
			final IdNameVersion comp,
			final RequestVulnerabilityPojo requestVulnerability)
					throws AppEditException {
		final AppCompVulnDetails appCompVulnDetails = (new AppCompVulnDetailsBuilder())
				.setAppCompVulnKey(key)
				.setApplicationName(app.getName())
				.setApplicationVersion(app.getVersion())
				.setComponentName(comp.getName())
				.setComponentVersion(comp.getVersion())
				.setRequestId(requestVulnerability.getRequestId())
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
	public ApplicationPojo getApplicationByNameVersion(final String appName,
			final String appVersion, final boolean refreshCache) throws AppEditException {
		if (refreshCache) {
			try {
				ccsw.getApplicationManager()
				.removeApplicationFromCacheByNameVersion(appName,
						appVersion);
			} catch (final CommonFrameworkException e) {
				throw new AppEditException("Error removing application "
						+ appName + " / " + appVersion + " from cache: "
						+ e.getMessage(), e);
			}
		}
		ApplicationPojo app;
		try {
			app = ccsw.getApplicationManager().getApplicationByNameVersion(
					appName, appVersion);
		} catch (final CommonFrameworkException e) {
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
	public ApplicationPojo getApplicationById(final String appId, final boolean refreshCache)
			throws AppEditException {
		if (refreshCache) {
			try {
				ccsw.getApplicationManager().removeApplicationFromCacheById(
						appId);
			} catch (final CommonFrameworkException e) {
				throw new AppEditException(
						"Error removing application with ID " + appId
						+ " from cache: " + e.getMessage(), e);
			}
		}
		ApplicationPojo app;
		try {
			app = ccsw.getApplicationManager().getApplicationById(appId);
		} catch (final CommonFrameworkException e) {
			throw new AppEditException("Error getting application with ID "
					+ appId + ": " + e.getMessage(), e);
		}
		return app;
	}

}
