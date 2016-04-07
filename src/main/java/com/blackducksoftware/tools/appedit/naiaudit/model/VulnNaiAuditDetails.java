package com.blackducksoftware.tools.appedit.naiaudit.model;

public class VulnNaiAuditDetails {
    private AppCompVulnKey appCompVulnKey;

    private String vulnerabilityNaiAuditStatus;

    private String vulnerabilityNaiAuditComment;

    public VulnNaiAuditDetails(AppCompVulnKey appCompVulnKey,
            String vulerabilityNaiAuditStatus,
            String vulnerabilityNaiAuditComment) {
        this.appCompVulnKey = appCompVulnKey;
        vulnerabilityNaiAuditStatus = vulerabilityNaiAuditStatus;
        this.vulnerabilityNaiAuditComment = vulnerabilityNaiAuditComment;
    }

    public AppCompVulnKey getAppCompVulnKey() {
        return appCompVulnKey;
    }

    public void setAppCompVulnKey(AppCompVulnKey appCompVulnKey) {
        this.appCompVulnKey = appCompVulnKey;
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
        return "VulnNaiAuditDetails [appCompVulnKey=" + appCompVulnKey + ", vulnerabilityNaiAuditStatus=" + vulnerabilityNaiAuditStatus
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
