package com.blackducksoftware.tools.appedit.naiaudit.model;

public class VulnNaiAuditDetails {
    private String applicationId;

    private String componentId;

    private String vulnerabilityId;

    private String applicationName;

    private String applicationVersion;

    private String componentName;

    private String componentVersion;

    private String vulnerabilityName;

    public VulnNaiAuditDetails() {
        applicationId = "";
        componentId = "";
        vulnerabilityId = "";
        applicationName = "";
        applicationVersion = "";
        componentName = "";
        componentVersion = "";
        vulnerabilityName = "";
    }

    public VulnNaiAuditDetails(String applicationId, String componentId, String vulnerabilityId, String applicationName, String applicationVersion,
            String componentName, String componentVersion, String vulnerabilityName) {
        this.applicationId = applicationId;
        this.componentId = componentId;
        this.vulnerabilityId = vulnerabilityId;
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.componentName = componentName;
        this.componentVersion = componentVersion;
        this.vulnerabilityName = vulnerabilityName;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getVulnerabilityId() {
        return vulnerabilityId;
    }

    public void setVulnerabilityId(String vulnerabilityId) {
        this.vulnerabilityId = vulnerabilityId;
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

    @Override
    public String toString() {
        return "VulnNaiAuditDetails [applicationId=" + applicationId + ", componentId=" + componentId + ", vulnerabilityId=" + vulnerabilityId
                + ", applicationName=" + applicationName + ", applicationVersion=" + applicationVersion + ", componentName=" + componentName
                + ", componentVersion=" + componentVersion + ", vulnerabilityName=" + vulnerabilityName + "]";
    }

}
