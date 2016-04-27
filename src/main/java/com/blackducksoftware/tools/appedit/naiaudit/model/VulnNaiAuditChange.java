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
