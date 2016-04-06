package com.blackducksoftware.tools.appedit.naiaudit.model;

public class VulnNaiAuditDetails {
    private AppCompVulnKey appCompVulnKey;

    private String applicationName;

    private String applicationVersion;

    private String componentName;

    private String componentVersion;

    private String vulnerabilityName;

    private String vulnerabilityRemediationStatus;

    private String vulnerabilityNaiAuditStatus;

    private String vulnerabilityNaiAuditComment;

    public VulnNaiAuditDetails(AppCompVulnKey appCompVulnKey, String applicationName, String applicationVersion,
            String componentName, String componentVersion, String vulnerabilityName,
            String vulnerabilityRemediationStatus,
            String vulerabilityNaiAuditStatus,
            String vulnerabilityNaiAuditComment) {
        this.appCompVulnKey = appCompVulnKey;
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.componentName = componentName;
        this.componentVersion = componentVersion;
        this.vulnerabilityName = vulnerabilityName;
        this.vulnerabilityRemediationStatus = vulnerabilityRemediationStatus;
        vulnerabilityNaiAuditStatus = vulerabilityNaiAuditStatus;
        this.vulnerabilityNaiAuditComment = vulnerabilityNaiAuditComment;
    }

    public AppCompVulnKey getAppCompVulnKey() {
        return appCompVulnKey;
    }

    public void setAppCompVulnKey(AppCompVulnKey appCompVulnKey) {
        this.appCompVulnKey = appCompVulnKey;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentVersion() {
        return componentVersion;
    }

    public void setComponentVersion(String componentVersion) {
        this.componentVersion = componentVersion;
    }

    public String getVulnerabilityName() {
        return vulnerabilityName;
    }

    public void setVulnerabilityName(String vulnerabilityName) {
        this.vulnerabilityName = vulnerabilityName;
    }

    public String getVulnerabilityRemediationStatus() {
        return vulnerabilityRemediationStatus;
    }

    public void setVulnerabilityRemediationStatus(String vulnerabilityRemediationStatus) {
        this.vulnerabilityRemediationStatus = vulnerabilityRemediationStatus;
    }

    public String getVulnerabilityNaiAuditStatus() {
        return vulnerabilityNaiAuditStatus;
    }

    public void setVulnerabilityNaiAuditStatus(String vulnerabilityNaiAuditStatus) {
        this.vulnerabilityNaiAuditStatus = vulnerabilityNaiAuditStatus;
    }

    public String getVulnerabilityNaiAuditComment() {
        return vulnerabilityNaiAuditComment;
    }

    public void setVulnerabilityNaiAuditComment(String vulnerabilityNaiAuditComment) {
        this.vulnerabilityNaiAuditComment = vulnerabilityNaiAuditComment;
    }

    @Override
    public String toString() {
        return "VulnNaiAuditDetails [appCompVulnKey=" + appCompVulnKey + ", applicationName=" + applicationName + ", applicationVersion=" + applicationVersion
                + ", componentName=" + componentName + ", componentVersion=" + componentVersion + ", vulnerabilityName=" + vulnerabilityName
                + ", vulnerabilityRemediationStatus=" + vulnerabilityRemediationStatus + ", vulnerabilityNaiAuditStatus=" + vulnerabilityNaiAuditStatus
                + ", vulnerabilityNaiAuditComment=" + vulnerabilityNaiAuditComment + "]";
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
        VulnNaiAuditDetails other = (VulnNaiAuditDetails) obj;
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
