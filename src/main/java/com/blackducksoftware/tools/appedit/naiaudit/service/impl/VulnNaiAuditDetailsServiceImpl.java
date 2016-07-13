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
package com.blackducksoftware.tools.appedit.naiaudit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.NaiAuditNoChangeException;
import com.blackducksoftware.tools.appedit.naiaudit.dao.AppCompVulnDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnNaiAuditChangeHistoryDao;
import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnNaiAuditDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditChange;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;

/**
 * NAI Audit services implementation.
 *
 * @author sbillings
 *
 */
public class VulnNaiAuditDetailsServiceImpl implements
VulnNaiAuditDetailsService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getName());

	// config (wired)
	private AppEditConfigManager config;

	@Inject
	public void setConfig(final AppEditConfigManager config) {
		this.config = config;
	}

	// DAO objects (wired)
	private VulnNaiAuditDetailsDao vulnNaiAuditDetailsDao;

	@Inject
	public void setVulnNaiAuditDetailsDao(
			final VulnNaiAuditDetailsDao vulnNaiAuditDetailsDao) {
		this.vulnNaiAuditDetailsDao = vulnNaiAuditDetailsDao;
	}

	private AppCompVulnDetailsDao appCompVulnDetailsDao;

	@Inject
	public void setAppCompVulnDetailsDao(
			final AppCompVulnDetailsDao appCompVulnDetailsDao) {
		this.appCompVulnDetailsDao = appCompVulnDetailsDao;
	}

	private VulnNaiAuditChangeHistoryDao vulnNaiAuditChangeHistoryDao;

	@Inject
	public void setVulnNaiAuditChangeHistoryDao(
			final VulnNaiAuditChangeHistoryDao vulnNaiAuditChangeHistoryDao) {
		this.vulnNaiAuditChangeHistoryDao = vulnNaiAuditChangeHistoryDao;
	}

	/**
	 * Get an application's components+vulnerabilities.
	 *
	 * @param applicationId
	 * @return
	 * @throws AppEditException
	 */
	@Override
	public List<AppCompVulnComposite> getAppCompVulnCompositeList(
			final String applicationId) throws AppEditException {
		final List<AppCompVulnComposite> result = new ArrayList<>();
		final Map<AppCompVulnKey, AppCompVulnDetails> ccParts = appCompVulnDetailsDao
				.getAppCompVulnDetailsMap(applicationId);
		final Map<AppCompVulnKey, VulnNaiAuditDetails> auditParts = vulnNaiAuditDetailsDao
				.getVulnNaiAuditDetailsMap(applicationId);
		for (final AppCompVulnKey key : ccParts.keySet()) {
			VulnNaiAuditDetails auditDetails;
			if (auditParts.containsKey(key)) {
				auditDetails = auditParts.get(key);
			} else {
				auditDetails = new VulnNaiAuditDetails(key, "", "");
				vulnNaiAuditDetailsDao.insertVulnNaiAuditDetails(auditDetails);
			}
			final AppCompVulnDetails ccDetails = ccParts.get(key);

			if (!include(ccDetails, auditDetails)) {
				continue;
			}

			final AppCompVulnComposite appCompVulnComposite = new AppCompVulnComposite(
					key, ccDetails, auditDetails);
			result.add(appCompVulnComposite);
		}
		return result;
	}

	private boolean include(final AppCompVulnDetails ccDetails, final VulnNaiAuditDetails auditDetails) {
		if (ccDetails.getVulnerabilityRemediationStatus().equals(config.getNaiAuditRemStatusToAudit())) {
			return true;
		}
		// if here: rem status is NOT NAI, so only include if NAI audit status
		// has been set
		if (StringUtils.isBlank(auditDetails.getVulnerabilityNaiAuditStatus())) {
			return false;
		}
		// if here: NAI audit status has a value; if it's the first
		// (unreviewed): don't include vuln in list
		if (auditDetails.getVulnerabilityNaiAuditStatus().equals(config.getNaiAuditStatusChoices().get(0))) {
			return false;
		}
		return true;
	}

	/**
	 * Update the NAI Audit details for one component's vulnerability.
	 *
	 * @param appCompVulnComposite
	 * @return
	 * @throws AppEditException
	 */
	@Override
	public AppCompVulnComposite updateVulnNaiAuditDetails(
			final AppCompVulnComposite appCompVulnComposite) throws AppEditException {

		logger.debug("updateVulnNaiAuditDetails() called with: "
				+ appCompVulnComposite);

		if (!auditStatusChanged(appCompVulnComposite)) {
			final String msg = "Audit status unchanged for this vulnerability, so no changes (including comment) made to it: "
					+ appCompVulnComposite.getCcPart().getComponentName()
					+ " / "
					+ appCompVulnComposite.getCcPart().getComponentVersion()
					+ ": "
					+ appCompVulnComposite.getCcPart().getVulnerabilityName();
			logger.warn(msg);
			throw new NaiAuditNoChangeException(msg);
		}

		updateRemediationDetailsIfNeeded(appCompVulnComposite);

		final String origRemediationComment = appCompVulnComposite.getCcPart()
				.getVulnerabilityRemediationComments();
		final String incomingNaiAuditComment = appCompVulnComposite.getAuditPart()
				.getVulnerabilityNaiAuditComment();
		final String newRemediationComment = deriveNewRemediationComment(
				origRemediationComment, incomingNaiAuditComment,
				appCompVulnComposite.getAuditPart()
				.getVulnerabilityNaiAuditStatus());

		appCompVulnComposite.getCcPart().setVulnerabilityRemediationComments(
				newRemediationComment);

		appCompVulnComposite.getAuditPart().setVulnerabilityNaiAuditComment(
				incomingNaiAuditComment);

		final AppCompVulnDetails ccPart = appCompVulnDetailsDao
				.updateAppCompVulnDetails(appCompVulnComposite.getCcPart());
		final VulnNaiAuditDetails auditPart = vulnNaiAuditDetailsDao
				.updateVulnNaiAuditDetails(appCompVulnComposite.getAuditPart());

		addToChangeHistory(appCompVulnComposite, auditPart);

		return new AppCompVulnComposite(ccPart.getAppCompVulnKey(), ccPart,
				auditPart);
	}

	private boolean auditStatusChanged(final AppCompVulnComposite appCompVulnComposite) {
		logger.debug("Checking to see if a change was made to audit status for: " + appCompVulnComposite);
		if (appCompVulnComposite.getAuditPart().getVulnerabilityNaiAuditStatus()
				.equals(appCompVulnComposite.getAuditPart().getOrigNaiAuditStatus())) {
			logger.debug("No change detected; audit status remained at: "
					+ appCompVulnComposite.getAuditPart().getVulnerabilityNaiAuditStatus());
			return false;
		} else {
			logger.debug("Change detected from " + appCompVulnComposite.getAuditPart().getOrigNaiAuditStatus() + " to "
					+ appCompVulnComposite.getAuditPart().getVulnerabilityNaiAuditStatus());
			return true;
		}
	}

	private void addToChangeHistory(final AppCompVulnComposite appCompVulnComposite,
			final VulnNaiAuditDetails auditPart) throws AppEditException {
		final VulnNaiAuditChange vulnNaiAuditChange = new VulnNaiAuditChange(
				new Date(), appCompVulnComposite.getKey(),
				auditPart.getUsername(), auditPart.getOrigNaiAuditStatus(),
				auditPart.getOrigNaiAuditComment(), appCompVulnComposite
				.getAuditPart().getVulnerabilityNaiAuditStatus(),
				appCompVulnComposite.getAuditPart()
				.getVulnerabilityNaiAuditComment());
		insertVulnNaiAuditChange(vulnNaiAuditChange);
	}

	private String deriveNewRemediationComment(String origRemediationComment,
			final String incomingNaiAuditComment, final String incomingNaiAuditStatus) {
		origRemediationComment = ensureNotNull(origRemediationComment);
		String separator = "";
		if (!StringUtils.isBlank(origRemediationComment)) {
			separator = "\n\n";
		}
		final String nowString = config.getNaiAuditDateFormat().format(new Date());
		final String newRemediationComment = origRemediationComment + separator + "["
				+ nowString + ": NAI Audit Status: " + incomingNaiAuditStatus
				+ "; " + incomingNaiAuditComment + "]";
		return newRemediationComment;
	}

	private String ensureNotNull(String origRemediationComment) {
		if (origRemediationComment == null) {
			origRemediationComment = "";
		}
		return origRemediationComment;
	}

	private void updateRemediationDetailsIfNeeded(
			final AppCompVulnComposite appCompVulnComposite) {
		if (appCompVulnComposite.getAuditPart()
				.getVulnerabilityNaiAuditStatus()
				.equals(config.getNaiAuditRejectedStatusName())) {
			logger.debug("Auditor rejected; remediation status will be changed to: '"
					+ config.getNaiAuditRejectedStatusChangesRemStatusTo() + "', and remediation dates will be cleared");
			appCompVulnComposite.getCcPart().setVulnerabilityRemediationStatus(
					config.getNaiAuditRejectedStatusChangesRemStatusTo());
			appCompVulnComposite.getCcPart().setVulnerabilityTargetRemediationDate(null);
			appCompVulnComposite.getCcPart().setVulnerabilityActualRemediationDate(null);
		}
	}

	/**
	 * Get Application by name/version.
	 *
	 * @param appName
	 * @param appVersion
	 * @return
	 * @throws AppEditException
	 */
	@Override
	public ApplicationPojo getApplicationByNameVersion(final String appName,
			final String appVersion, final boolean refreshCache) throws AppEditException {
		final ApplicationPojo app = appCompVulnDetailsDao
				.getApplicationByNameVersion(appName, appVersion, refreshCache);
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
		final ApplicationPojo app = appCompVulnDetailsDao.getApplicationById(appId,
				refreshCache);
		return app;
	}

	private void insertVulnNaiAuditChange(final VulnNaiAuditChange vulnNaiAuditChange)
			throws AppEditException {
		vulnNaiAuditChangeHistoryDao
		.insertVulnNaiAuditChange(vulnNaiAuditChange);
	}

	@Override
	public AppCompVulnComposite getAppCompVulnComposite(final AppCompVulnKey key)
			throws AppEditException {
		final AppCompVulnDetails ccPart = appCompVulnDetailsDao
				.getAppCompVulnDetails(key);
		final VulnNaiAuditDetails auditPart = vulnNaiAuditDetailsDao
				.getVulnNaiAuditDetails(key);

		final AppCompVulnComposite composite = new AppCompVulnComposite(key, ccPart,
				auditPart);
		return composite;
	}

}
