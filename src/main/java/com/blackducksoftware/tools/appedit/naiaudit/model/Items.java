package com.blackducksoftware.tools.appedit.naiaudit.model;

import java.util.List;

public class Items {
    private String applicationId;

    private List<String> itemList;

    private String vulnerabilityNaiAuditStatus;

    private String comment;

    public List<String> getItemList() {
        return itemList;
    }

    public void setItemList(List<String> itemList) {
        this.itemList = itemList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVulnerabilityNaiAuditStatus() {
        return vulnerabilityNaiAuditStatus;
    }

    public void setVulnerabilityNaiAuditStatus(String vulnerabilityNaiAuditStatus) {
        this.vulnerabilityNaiAuditStatus = vulnerabilityNaiAuditStatus;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    @Override
    public String toString() {
        return "Items [applicationId=" + applicationId + ", itemList=" + itemList + ", vulnerabilityNaiAuditStatus=" + vulnerabilityNaiAuditStatus
                + ", comment=" + comment + "]";
    }
}
