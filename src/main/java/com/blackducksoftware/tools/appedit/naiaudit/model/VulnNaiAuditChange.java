/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.blackducksoftware.tools.appedit.naiaudit.model;

import java.util.Date;

/**
 * Details of one change to NAI Audit details.
 * 
 * @author sbillings
 *
 */
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
