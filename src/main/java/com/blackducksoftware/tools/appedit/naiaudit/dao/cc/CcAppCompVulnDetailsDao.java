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
	public void setCcsw(final ICodeCenterServerWrapper ccsw) {
		this.ccsw = ccsw;
	}

	private AppEditConfigManager config;

	@Inject
	public void setConfig(final AppEditConfigManager config) {
		this.config = config;
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
			ccsw.getRequestManager().updateRequestVulnerability(
					updatedRequestVulnerability);
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

		CodeCenterComponentPojo comp;
		try {
			comp = ccsw.getComponentManager().getComponentById(
					CodeCenterComponentPojo.class, key.getComponentId());
		} catch (final CommonFrameworkException e1) {
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

			CodeCenterComponentPojo comp;
			try {
				comp = ccsw.getComponentManager()
						.getComponentById(CodeCenterComponentPojo.class,
								request.getComponentId());
			} catch (final CommonFrameworkException e1) {
				throw new AppEditException("Error getting component with ID "
						+ request.getComponentId() + ": " + e1.getMessage(), e1);
			}

			final String remStatusName = config.getNaiAuditRemStatusToAudit();
			logger.debug("Remediation status to audit: " + remStatusName);

			List<RequestVulnerabilityPojo> requestVulnerabilities;
			try {
				requestVulnerabilities = ccsw.getRequestManager()
.getVulnerabilitiesByRequestIdRemediationStatus(
						requestId, remStatusName);
			} catch (final CommonFrameworkException e) {
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
			final Map<AppCompVulnKey, AppCompVulnDetails> result,
			final ApplicationPojo app, final CodeCenterComponentPojo comp,
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
			final CodeCenterComponentPojo comp,
			final RequestVulnerabilityPojo requestVulnerability) {
		final AppCompVulnKey key = new AppCompVulnKey(applicationId, comp.getId(),
				requestVulnerability.getVulnerabilityId());
		return key;
	}

	private AppCompVulnDetails deriveAppCompVulnDetails(final AppCompVulnKey key,
			final ApplicationPojo app, final CodeCenterComponentPojo comp,
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
