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
package com.blackducksoftware.tools.appedit.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetailsBuilder;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;
import com.blackducksoftware.tools.connector.codecenter.common.AttributeValuePojo;
import com.blackducksoftware.tools.connector.codecenter.common.VulnerabilitySeverity;
import com.blackducksoftware.tools.connector.common.ApprovalStatus;

public class MockVulnNaiAuditDetailsService implements
VulnNaiAuditDetailsService {

	@Override
	public ApplicationPojo getApplicationByNameVersion(final String appName,
			final String appVersion, final boolean refreshCache) throws AppEditException {

		return new ApplicationPojo(appName, appName, appVersion,
				new ArrayList<AttributeValuePojo>(), ApprovalStatus.APPROVED,
				false, "owner");
	}

	@Override
	public ApplicationPojo getApplicationById(final String appId, final boolean refreshCache)
			throws AppEditException {
		if ("bogus".equals(appId)) {
			throw new AppEditException("mock: application not found");
		}
		return new ApplicationPojo(appId, appId, "Unspecified",
				new ArrayList<AttributeValuePojo>(), ApprovalStatus.APPROVED,
				false, "owner");
	}

	@Override
	public List<AppCompVulnComposite> getAppCompVulnCompositeList(
			final String applicationId) throws AppEditException {

		if ("bogus_for_getAppCompVulnCompositeList".equals(applicationId)) {
			throw new AppEditException(
					"mock: failure getting app comp vuln comp list");
		}

		final AppCompVulnComposite appCompVulnComposite = generateComposite("vulnerability1Name");
		final List<AppCompVulnComposite> result = new ArrayList<AppCompVulnComposite>();
		result.add(appCompVulnComposite);
		return result;
	}

	private AppCompVulnComposite generateComposite(final String vulnerabilityName) throws AppEditException {
		final Date now = new Date();
		final AppCompVulnKey key = new AppCompVulnKey("app1Id", "componentUse1Id", "component1Id",
				"vulnerability1Id");

		final AppCompVulnDetails ccPart = (new AppCompVulnDetailsBuilder())
				.setAppCompVulnKey(key)
				.setApplicationName("application1Name")
				.setApplicationVersion("application1Version")
				.setComponentName("component1Name")
				.setComponentVersion("component1Version")
				.setRequestId("request1Id")
				.setVulnerabilityName(vulnerabilityName)
				.setVulnerabilitySeverity(VulnerabilitySeverity.HIGH)
				.setVulnerabilityBaseScore("vulnerability1BaseScore")
				.setVulnerabilityExploitableScore(
						"vulnerability1ExploitableScore")
						.setVulnerabilityImpactScore("vulnerability1ImpactScore")
						.setVulnerabilityDateCreated(now)
						.setVulnerabilityDateModified(now)
						.setVulnerabilityDatePublished(now)
						.setVulnerabilityDescription("vulnerability1Description")
						.setVulnerabilityTargetRemediationDate(now)
						.setVulnerabilityActualRemediationDate(now)
						.setVulnerabilityRemediationStatus(
								"vulnerability1RemediationStatus")
								.setVulnerabilityRemediationComments(
										"vulnerability1RemediationComments")
										.createAppCompVulnDetails();

		final VulnNaiAuditDetails auditPart = new VulnNaiAuditDetails(key,
				"vulnerability1NaiAuditStatus", "vulnerability1NaiAuditComment");

		final AppCompVulnComposite appCompVulnComposite = new AppCompVulnComposite(
				key, ccPart, auditPart);
		return appCompVulnComposite;
	}

	@Override
	public AppCompVulnComposite updateVulnNaiAuditDetails(
			final AppCompVulnComposite appCompVulnComposite) throws AppEditException {

		return appCompVulnComposite;
	}


	@Override
	public AppCompVulnComposite getAppCompVulnComposite(final AppCompVulnKey key)
			throws AppEditException {
		return generateComposite("vulnerability1Name");
	}

}
