package com.blackducksoftware.tools.appedit.naiaudit.model;

import java.util.Date;

public class AppCompVulnDetails {
    private final AppCompVulnKey appCompVulnKey;

    private final String componentName;

    private final String componentVersion;

    private final String vulnerabilityName;

    private final String vulnerabilitySeverity;

    private final Date vulnerabilityPublishDate;

    private final String vulnerabilityDescription;

    private final Date vulnerabilityTargetRemediationDate;

    private final Date vulnerabilityActualRemediationDate;

    private final String vulnerabilityRemediationStatus;

    private final String vulnerabilityRemediationComments;

    public AppCompVulnDetails(AppCompVulnKey appCompVulnKey, String componentName, String componentVersion,
            String vulnerabilityName,
            String vulnerabilitySeverity,
            Date vulnerabilityPublishDate,
            String vulnerabilityDescription,
            Date vulnerabilityTargetRemediationDate,
            Date vulnerabilityActualRemediationDate,
            String vulnerabilityRemediationStatus,
            String vulnerabilityRemediationComments) {
        this.appCompVulnKey = appCompVulnKey;
        this.componentName = componentName;
        this.componentVersion = componentVersion;
        this.vulnerabilityName = vulnerabilityName;
        this.vulnerabilitySeverity = vulnerabilitySeverity;
        this.vulnerabilityPublishDate = vulnerabilityPublishDate;
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
        return "AppCompVulnDetails [appCompVulnKey=" + appCompVulnKey + ", componentName=" + componentName + ", componentVersion=" + componentVersion
                + ", vulnerabilityName=" + vulnerabilityName + ", vulnerabilitySeverity=" + vulnerabilitySeverity + ", vulnerabilityPublishDate="
                + vulnerabilityPublishDate + ", vulnerabilityDescription=" + vulnerabilityDescription + ", vulnerabilityTargetRemediationDate="
                + vulnerabilityTargetRemediationDate + ", vulnerabilityActualRemediationDate=" + vulnerabilityActualRemediationDate
                + ", vulnerabilityRemediationStatus=" + vulnerabilityRemediationStatus + ", vulnerabilityRemediationComments="
                + vulnerabilityRemediationComments + "]";
    }

    public String getVulnerabilitySeverity() {
        return vulnerabilitySeverity;
    }

    public Date getVulnerabilityPublishDate() {
        return vulnerabilityPublishDate;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((appCompVulnKey == null) ? 0 : appCompVulnKey.hashCode());
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
