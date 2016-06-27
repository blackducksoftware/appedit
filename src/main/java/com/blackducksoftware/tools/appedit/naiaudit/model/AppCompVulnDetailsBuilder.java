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
package com.blackducksoftware.tools.appedit.naiaudit.model;

import java.util.Date;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.connector.codecenter.common.VulnerabilitySeverity;

/**
 * A builder used to create AppCompVulnDetails objects.
 * 
 * @author sbillings
 *
 */
public class AppCompVulnDetailsBuilder {
    private AppCompVulnKey appCompVulnKey;
    private String applicationName;
    private String applicationVersion;
    private String componentName;
    private String componentVersion;
    private String requestId;
    private String vulnerabilityName;
    private VulnerabilitySeverity vulnerabilitySeverity;
    private String vulnerabilityBaseScore;
    private String vulnerabilityExploitableScore;
    private String vulnerabilityImpactScore;
    private Date vulnerabilityDateCreated;
    private Date vulnerabilityDateModified;
    private Date vulnerabilityDatePublished;
    private String vulnerabilityDescription;
    private Date vulnerabilityTargetRemediationDate;
    private Date vulnerabilityActualRemediationDate;
    private String vulnerabilityRemediationStatus;
    private String vulnerabilityRemediationComments;

    private boolean appCompVulnKeySet = false;
    private boolean applicationNameSet = false;
    private boolean applicationVersionSet = false;
    private boolean componentNameSet = false;
    private boolean componentVersionSet = false;
    private boolean requestIdSet = false;
    private boolean vulnerabilityNameSet = false;
    private boolean vulnerabilitySeveritySet = false;
    private boolean vulnerabilityBaseScoreSet = false;
    private boolean vulnerabilityExploitableScoreSet = false;
    private boolean vulnerabilityImpactScoreSet = false;
    private boolean vulnerabilityDateCreatedSet = false;
    private boolean vulnerabilityDateModifiedSet = false;
    private boolean vulnerabilityDatePublishedSet = false;
    private boolean vulnerabilityDescriptionSet = false;
    private boolean vulnerabilityTargetRemediationDateSet = false;
    private boolean vulnerabilityActualRemediationDateSet = false;
    private boolean vulnerabilityRemediationStatusSet = false;
    private boolean vulnerabilityRemediationCommentsSet = false;

    public AppCompVulnDetailsBuilder setAppCompVulnKey(
	    AppCompVulnKey appCompVulnKey) {
	this.appCompVulnKey = appCompVulnKey;
	appCompVulnKeySet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setApplicationName(String applicationName) {
	this.applicationName = applicationName;
	applicationNameSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setApplicationVersion(
	    String applicationVersion) {
	this.applicationVersion = applicationVersion;
	applicationVersionSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setComponentName(String componentName) {
	this.componentName = componentName;
	componentNameSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setComponentVersion(String componentVersion) {
	this.componentVersion = componentVersion;
	componentVersionSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setRequestId(String requestId) {
	this.requestId = requestId;
	requestIdSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setVulnerabilityName(
	    String vulnerabilityName) {
	this.vulnerabilityName = vulnerabilityName;
	vulnerabilityNameSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setVulnerabilitySeverity(
	    VulnerabilitySeverity vulnerabilitySeverity) {
	this.vulnerabilitySeverity = vulnerabilitySeverity;
	vulnerabilitySeveritySet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setVulnerabilityBaseScore(
	    String vulnerabilityBaseScore) {
	this.vulnerabilityBaseScore = vulnerabilityBaseScore;
	vulnerabilityBaseScoreSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setVulnerabilityExploitableScore(
	    String vulnerabilityExploitableScore) {
	this.vulnerabilityExploitableScore = vulnerabilityExploitableScore;
	vulnerabilityExploitableScoreSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setVulnerabilityImpactScore(
	    String vulnerabilityImpactScore) {
	this.vulnerabilityImpactScore = vulnerabilityImpactScore;
	vulnerabilityImpactScoreSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setVulnerabilityDateCreated(
	    Date vulnerabilityDateCreated) {
	this.vulnerabilityDateCreated = vulnerabilityDateCreated;
	vulnerabilityDateCreatedSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setVulnerabilityDateModified(
	    Date vulnerabilityDateModified) {
	this.vulnerabilityDateModified = vulnerabilityDateModified;
	vulnerabilityDateModifiedSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setVulnerabilityDatePublished(
	    Date vulnerabilityDatePublished) {
	this.vulnerabilityDatePublished = vulnerabilityDatePublished;
	vulnerabilityDatePublishedSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setVulnerabilityDescription(
	    String vulnerabilityDescription) {
	this.vulnerabilityDescription = vulnerabilityDescription;
	vulnerabilityDescriptionSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setVulnerabilityTargetRemediationDate(
	    Date vulnerabilityTargetRemediationDate) {
	this.vulnerabilityTargetRemediationDate = vulnerabilityTargetRemediationDate;
	vulnerabilityTargetRemediationDateSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setVulnerabilityActualRemediationDate(
	    Date vulnerabilityActualRemediationDate) {
	this.vulnerabilityActualRemediationDate = vulnerabilityActualRemediationDate;
	vulnerabilityActualRemediationDateSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setVulnerabilityRemediationStatus(
	    String vulnerabilityRemediationStatus) {
	this.vulnerabilityRemediationStatus = vulnerabilityRemediationStatus;
	vulnerabilityRemediationStatusSet = true;
	return this;
    }

    public AppCompVulnDetailsBuilder setVulnerabilityRemediationComments(
	    String vulnerabilityRemediationComments) {
	this.vulnerabilityRemediationComments = vulnerabilityRemediationComments;
	vulnerabilityRemediationCommentsSet = true;
	return this;
    }

    public AppCompVulnDetails createAppCompVulnDetails()
	    throws AppEditException {
	if (!appCompVulnKeySet || !applicationNameSet || !applicationVersionSet
		|| !componentNameSet || !componentVersionSet || !requestIdSet
		|| !vulnerabilityNameSet || !vulnerabilitySeveritySet
		|| !vulnerabilityBaseScoreSet
		|| !vulnerabilityExploitableScoreSet
		|| !vulnerabilityImpactScoreSet || !vulnerabilityDateCreatedSet
		|| !vulnerabilityDateModifiedSet
		|| !vulnerabilityDatePublishedSet
		|| !vulnerabilityDescriptionSet
		|| !vulnerabilityTargetRemediationDateSet
		|| !vulnerabilityActualRemediationDateSet
		|| !vulnerabilityRemediationStatusSet
		|| !vulnerabilityRemediationCommentsSet) {
	    throw new AppEditException(
		    "Error creating AppCompVulnDetails object: A manditory field has not been set.");
	}
	return new AppCompVulnDetails(appCompVulnKey, applicationName,
		applicationVersion, componentName, componentVersion, requestId,
		vulnerabilityName, vulnerabilitySeverity,
		vulnerabilityBaseScore, vulnerabilityExploitableScore,
		vulnerabilityImpactScore, vulnerabilityDateCreated,
		vulnerabilityDateModified, vulnerabilityDatePublished,
		vulnerabilityDescription, vulnerabilityTargetRemediationDate,
		vulnerabilityActualRemediationDate,
		vulnerabilityRemediationStatus,
		vulnerabilityRemediationComments);
    }
}
