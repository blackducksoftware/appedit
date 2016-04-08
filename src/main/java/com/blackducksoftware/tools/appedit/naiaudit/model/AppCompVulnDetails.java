package com.blackducksoftware.tools.appedit.naiaudit.model;

import java.util.Date;

import com.blackducksoftware.tools.connector.codecenter.common.VulnerabilitySeverity;

public class AppCompVulnDetails {
    private final AppCompVulnKey appCompVulnKey;

    private final String componentName;

    private final String componentVersion;

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

    private final Date vulnerabilityTargetRemediationDate;

    private final Date vulnerabilityActualRemediationDate;

    private final String vulnerabilityRemediationStatus;

    private String vulnerabilityRemediationComments;

    // TODO: This should have a builder
    public AppCompVulnDetails(AppCompVulnKey appCompVulnKey,
	    String componentName, String componentVersion,
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
	this.componentName = componentName;
	this.componentVersion = componentVersion;
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
	this.vulnerabilityTargetRemediationDate = vulnerabilityTargetRemediationDate;
	this.vulnerabilityActualRemediationDate = vulnerabilityActualRemediationDate;
	this.vulnerabilityRemediationStatus = vulnerabilityRemediationStatus;
	this.vulnerabilityRemediationComments = vulnerabilityRemediationComments;
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

    @Override
    public String toString() {
	return "AppCompVulnDetails [appCompVulnKey=" + appCompVulnKey
		+ ", componentName=" + componentName + ", componentVersion="
		+ componentVersion + ", vulnerabilityName=" + vulnerabilityName
		+ ", vulnerabilitySeverity=" + vulnerabilitySeverityString
		+ ", vulnerabilityBaseScore=" + vulnerabilityBaseScore
		+ ", vulnerabilityExploitableScore="
		+ vulnerabilityExploitableScore + ", vulnerabilityImpactScore="
		+ vulnerabilityImpactScore + ", vulnerabilityDateCreated="
		+ vulnerabilityDateCreated + ", vulnerabilityDateModified="
		+ vulnerabilityDateModified + ", vulnerabilityDatePublished="
		+ vulnerabilityDatePublished + ", vulnerabilityDescription="
		+ vulnerabilityDescription
		+ ", vulnerabilityTargetRemediationDate="
		+ vulnerabilityTargetRemediationDate
		+ ", vulnerabilityActualRemediationDate="
		+ vulnerabilityActualRemediationDate
		+ ", vulnerabilityRemediationStatus="
		+ vulnerabilityRemediationStatus
		+ ", vulnerabilityRemediationComments="
		+ vulnerabilityRemediationComments + "]";
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
