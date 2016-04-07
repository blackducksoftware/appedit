package com.blackducksoftware.tools.appedit.naiaudit.model;

public class AppCompVulnDetails {
    private AppCompVulnKey appCompVulnKey;

    private String applicationName;

    private String applicationVersion;

    private String componentName;

    private String componentVersion;

    private String vulnerabilityName;

    private String vulnerabilityRemediationStatus;

    public AppCompVulnDetails(AppCompVulnKey appCompVulnKey, String applicationName, String applicationVersion, String componentName, String componentVersion,
            String vulnerabilityName, String vulnerabilityRemediationStatus) {
        this.appCompVulnKey = appCompVulnKey;
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.componentName = componentName;
        this.componentVersion = componentVersion;
        this.vulnerabilityName = vulnerabilityName;
        this.vulnerabilityRemediationStatus = vulnerabilityRemediationStatus;
    }

    public AppCompVulnKey getAppCompVulnKey() {
        return appCompVulnKey;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
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
        return "AppCompVulnDetails [appCompVulnKey=" + appCompVulnKey + ", applicationName=" + applicationName + ", applicationVersion=" + applicationVersion
                + ", componentName=" + componentName + ", componentVersion=" + componentVersion + ", vulnerabilityName=" + vulnerabilityName
                + ", vulnerabilityRemediationStatus=" + vulnerabilityRemediationStatus + "]";
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
