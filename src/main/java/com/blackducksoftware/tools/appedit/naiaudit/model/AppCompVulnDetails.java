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
package com.blackducksoftware.tools.appedit.naiaudit.model;

import java.util.Date;

import com.blackducksoftware.tools.appedit.core.AppEditConstants;
import com.blackducksoftware.tools.connector.codecenter.common.VulnerabilitySeverity;

/**
 * Vulnerability details.
 * 
 * @author sbillings
 *
 */
public class AppCompVulnDetails {
    private final String applicationName;
    private final String applicationVersion;
    private final AppCompVulnKey appCompVulnKey;

    private final String componentName;

    private final String componentVersion;
    private final String requestId;

    private final String vulnerabilityName;

    private final VulnerabilitySeverity vulnerabilitySeverity;
    private final String vulnerabilitySeverityString;

    private final String vulnerabilityBaseScore;
    private final String vulnerabilityExploitableScore;
    private final String vulnerabilityImpactScore;
    private final Date vulnerabilityDateCreated;
    private final Date vulnerabilityDateModified;

    private final Date vulnerabilityDatePublished;

    private final String vulnerabilityDescription;
    private final String vulnerabilityDescriptionShort;

    private final Date vulnerabilityTargetRemediationDate;

    private final Date vulnerabilityActualRemediationDate;

    private String vulnerabilityRemediationStatus;

    private String vulnerabilityRemediationComments;
    private String vulnerabilityRemediationCommentsShort;
    private String vulnerabilityRemediationCommentsPopUpText;

    /**
     * Don't call constructor directly. Use AppCompVulnDetailsBuilder instead.
     */
    AppCompVulnDetails(AppCompVulnKey appCompVulnKey, String applicationName,
	    String applicationVersion, String componentName,
	    String componentVersion, String requestId,
	    String vulnerabilityName,
	    VulnerabilitySeverity vulnerabilitySeverity,

	    String vulnerabilityBaseScore,
	    String vulnerabilityExploitableScore,
	    String vulnerabilityImpactScore, Date vulnerabilityDateCreated,
	    Date vulnerabilityDateModified,

	    Date vulnerabilityDatePublished, String vulnerabilityDescription,

	    Date vulnerabilityTargetRemediationDate,
	    Date vulnerabilityActualRemediationDate,
	    String vulnerabilityRemediationStatus,
	    String vulnerabilityRemediationComments) {
	this.appCompVulnKey = appCompVulnKey;
	this.applicationName = applicationName;
	this.applicationVersion = applicationVersion;
	this.componentName = componentName;
	this.componentVersion = componentVersion;
	this.requestId = requestId;
	this.vulnerabilityName = vulnerabilityName;
	this.vulnerabilitySeverity = vulnerabilitySeverity;
	this.vulnerabilitySeverityString = vulnerabilitySeverity.name();

	this.vulnerabilityBaseScore = vulnerabilityBaseScore;
	this.vulnerabilityExploitableScore = vulnerabilityExploitableScore;
	this.vulnerabilityImpactScore = vulnerabilityImpactScore;
	this.vulnerabilityDateCreated = vulnerabilityDateCreated;
	this.vulnerabilityDateModified = vulnerabilityDateModified;

	this.vulnerabilityDatePublished = vulnerabilityDatePublished;
	this.vulnerabilityDescription = vulnerabilityDescription;
	this.vulnerabilityDescriptionShort = shortenDescription(vulnerabilityDescription);
	this.vulnerabilityTargetRemediationDate = vulnerabilityTargetRemediationDate;
	this.vulnerabilityActualRemediationDate = vulnerabilityActualRemediationDate;
	this.vulnerabilityRemediationStatus = vulnerabilityRemediationStatus;
	this.vulnerabilityRemediationComments = vulnerabilityRemediationComments;
	this.vulnerabilityRemediationCommentsShort = generateCommentTableRowText(vulnerabilityRemediationComments);
	this.vulnerabilityRemediationCommentsPopUpText = generateCommentPopUpText(vulnerabilityRemediationComments);
    }

    private String shortenDescription(String longDescription) {
	if (longDescription == null) {
	    return "";
	}

	if (longDescription.length() <= AppEditConstants.ABREVIATED_VULNERABILITY_DESCRIPTION_LENGTH + 3) {
	    return longDescription;
	}

	return longDescription.substring(0,
		AppEditConstants.ABREVIATED_VULNERABILITY_DESCRIPTION_LENGTH)
		+ "...";
    }

    private String generateCommentTableRowText(String longComment) {
	if (longComment == null) {
	    return "";
	}
	int lastNewlineIndex = longComment.lastIndexOf('\n');
	if (lastNewlineIndex < 0)
	    return longComment;
	String shortComment = "...\n\n"
		+ longComment.substring(lastNewlineIndex + 1);

	if (shortComment.length() > AppEditConstants.ABBREVIATED_REMEDIATION_COMMENT_LENGTH + 3) {
	    return shortComment.substring(0,
		    AppEditConstants.ABBREVIATED_REMEDIATION_COMMENT_LENGTH)
		    + "...";
	}
	return shortComment;
    }

    private String generateCommentPopUpText(String longComment) {
	if (longComment == null) {
	    return "";
	}

	int origLen = longComment.length();

	if (origLen > AppEditConstants.POPUP_REMEDIATION_COMMENT_LENGTH + 3) {
	    int startPos = origLen
		    - (AppEditConstants.POPUP_REMEDIATION_COMMENT_LENGTH + 3);
	    return "..." + longComment.substring(startPos);
	}
	return longComment;
    }

    public AppCompVulnKey getAppCompVulnKey() {
	return appCompVulnKey;
    }

    public String getComponentName() {
	return componentName;
    }

    public String getComponentVersion() {
	return componentVersion;
    }

    public String getVulnerabilityName() {
	return vulnerabilityName;
    }

    public String getVulnerabilityRemediationStatus() {
	return vulnerabilityRemediationStatus;
    }

    public String getApplicationName() {
	return applicationName;
    }

    public String getApplicationVersion() {
	return applicationVersion;
    }

    public String getRequestId() {
	return requestId;
    }

    @Override
    public String toString() {
	return "AppCompVulnDetails [applicationName=" + applicationName
		+ ", applicationVersion=" + applicationVersion
		+ ", appCompVulnKey=" + appCompVulnKey + ", componentName="
		+ componentName + ", componentVersion=" + componentVersion
		+ ", requestId=" + requestId + ", vulnerabilityName="
		+ vulnerabilityName + ", vulnerabilitySeverity="
		+ vulnerabilitySeverity + ", vulnerabilitySeverityString="
		+ vulnerabilitySeverityString + ", vulnerabilityBaseScore="
		+ vulnerabilityBaseScore + ", vulnerabilityExploitableScore="
		+ vulnerabilityExploitableScore + ", vulnerabilityImpactScore="
		+ vulnerabilityImpactScore + ", vulnerabilityDateCreated="
		+ vulnerabilityDateCreated + ", vulnerabilityDateModified="
		+ vulnerabilityDateModified + ", vulnerabilityDatePublished="
		+ vulnerabilityDatePublished + ", vulnerabilityDescription="
		+ vulnerabilityDescription + ", vulnerabilityDescriptionShort="
		+ vulnerabilityDescriptionShort
		+ ", vulnerabilityTargetRemediationDate="
		+ vulnerabilityTargetRemediationDate
		+ ", vulnerabilityActualRemediationDate="
		+ vulnerabilityActualRemediationDate
		+ ", vulnerabilityRemediationStatus="
		+ vulnerabilityRemediationStatus
		+ ", vulnerabilityRemediationComments="
		+ vulnerabilityRemediationComments
		+ ", vulnerabilityRemediationCommentsShort="
		+ vulnerabilityRemediationCommentsShort
		+ ", vulnerabilityRemediationCommentsPopUpText="
		+ vulnerabilityRemediationCommentsPopUpText + "]";
    }

    public String getVulnerabilitySeverityString() {
	return vulnerabilitySeverityString;
    }

    public VulnerabilitySeverity getVulnerabilitySeverity() {
	return vulnerabilitySeverity;
    }

    public String getVulnerabilityBaseScore() {
	return vulnerabilityBaseScore;
    }

    public String getVulnerabilityExploitableScore() {
	return vulnerabilityExploitableScore;
    }

    public String getVulnerabilityImpactScore() {
	return vulnerabilityImpactScore;
    }

    public Date getVulnerabilityDateCreated() {
	return vulnerabilityDateCreated;
    }

    public Date getVulnerabilityDateModified() {
	return vulnerabilityDateModified;
    }

    public Date getVulnerabilityDatePublished() {
	return vulnerabilityDatePublished;
    }

    public String getVulnerabilityDescription() {
	return vulnerabilityDescription;
    }

    public Date getVulnerabilityTargetRemediationDate() {
	return vulnerabilityTargetRemediationDate;
    }

    public Date getVulnerabilityActualRemediationDate() {
	return vulnerabilityActualRemediationDate;
    }

    public String getVulnerabilityRemediationComments() {
	return vulnerabilityRemediationComments;
    }

    public void setVulnerabilityRemediationComments(
	    String vulnerabilityRemediationComments) {
	this.vulnerabilityRemediationComments = vulnerabilityRemediationComments;
	this.vulnerabilityRemediationCommentsShort = generateCommentTableRowText(vulnerabilityRemediationComments);
	this.vulnerabilityRemediationCommentsPopUpText = generateCommentPopUpText(vulnerabilityRemediationComments);
    }

    public void setVulnerabilityRemediationStatus(
	    String vulnerabilityRemediationStatus) {
	this.vulnerabilityRemediationStatus = vulnerabilityRemediationStatus;
    }

    public String getVulnerabilityDescriptionShort() {
	return vulnerabilityDescriptionShort;
    }

    public String getVulnerabilityRemediationCommentsShort() {
	return vulnerabilityRemediationCommentsShort;
    }

    public String getVulnerabilityRemediationCommentsPopUpText() {
	return vulnerabilityRemediationCommentsPopUpText;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((appCompVulnKey == null) ? 0 : appCompVulnKey.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	AppCompVulnDetails other = (AppCompVulnDetails) obj;
	if (appCompVulnKey == null) {
	    if (other.appCompVulnKey != null) {
		return false;
	    }
	} else if (!appCompVulnKey.equals(other.appCompVulnKey)) {
	    return false;
	}
	return true;
    }

}
