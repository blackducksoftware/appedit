package com.blackducksoftware.tools.appedit.naiaudit.model;

import java.util.Date;

public class VulnNaiAuditChange {
    private final Date changeTime;
    private final AppCompVulnKey appCompVulnKey;
    private final String ccUserName;
    private final String oldNaiAuditStatus;
    private final String oldNaiAuditComment;
    private final String newNaiAuditStatus;
    private final String newNaiAuditComment;

    public VulnNaiAuditChange(Date changeTime, AppCompVulnKey appCompVulnKey,
	    String ccUserName, String oldNaiAuditStatus,
	    String oldNaiAuditComment, String newNaiAuditStatus,
	    String newNaiAuditComment) {

	this.changeTime = changeTime;
	this.appCompVulnKey = appCompVulnKey;
	this.ccUserName = ccUserName;
	this.oldNaiAuditStatus = oldNaiAuditStatus;
	this.oldNaiAuditComment = oldNaiAuditComment;
	this.newNaiAuditStatus = newNaiAuditStatus;
	this.newNaiAuditComment = newNaiAuditComment;
    }

    public Date getChangeTime() {
	return changeTime;
    }

    public AppCompVulnKey getAppCompVulnKey() {
	return appCompVulnKey;
    }

    public String getCcUserName() {
	return ccUserName;
    }

    public String getOldNaiAuditStatus() {
	return oldNaiAuditStatus;
    }

    public String getOldNaiAuditComment() {
	return oldNaiAuditComment;
    }

    public String getNewNaiAuditStatus() {
	return newNaiAuditStatus;
    }

    public String getNewNaiAuditComment() {
	return newNaiAuditComment;
    }

    @Override
    public String toString() {
	return "VulnNaiAuditChange [changeTime=" + changeTime
		+ ", appCompVulnKey=" + appCompVulnKey + ", ccUserName="
		+ ccUserName + ", oldNaiAuditStatus=" + oldNaiAuditStatus
		+ ", oldNaiAuditComment=" + oldNaiAuditComment
		+ ", newNaiAuditStatus=" + newNaiAuditStatus
		+ ", newNaiAuditComment=" + newNaiAuditComment + "]";
    }

}
