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

import com.blackducksoftware.tools.appedit.core.AppEditConstants;

/**
 * NAI Audit Details for one vulnerability.
 * 
 * @author sbillings
 *
 */
public class VulnNaiAuditDetails {
    private final AppCompVulnKey appCompVulnKey;
    private final String origNaiAuditStatus;
    private final String origNaiAuditComment;

    private String vulnerabilityNaiAuditStatus;

    private String vulnerabilityNaiAuditComment;
    private String vulnerabilityNaiAuditCommentShort;
    private String username;

    public VulnNaiAuditDetails(AppCompVulnKey appCompVulnKey,
	    String vulnerabilityNaiAuditStatus,
	    String vulnerabilityNaiAuditComment) {
	this.appCompVulnKey = appCompVulnKey;
	this.vulnerabilityNaiAuditStatus = vulnerabilityNaiAuditStatus;
	this.vulnerabilityNaiAuditComment = vulnerabilityNaiAuditComment;
	this.vulnerabilityNaiAuditCommentShort = shortenAuditComment(vulnerabilityNaiAuditComment);
	this.origNaiAuditStatus = vulnerabilityNaiAuditStatus;
	this.origNaiAuditComment = vulnerabilityNaiAuditComment;
    }

    private String shortenAuditComment(String longComment) {
	if (longComment == null) {
	    return "";
	}

	if (longComment.length() <= AppEditConstants.ABBREVIATED_NAI_AUDIT_COMMENT_LENGTH + 3) {
	    return longComment;
	}

	return longComment.substring(0,
		AppEditConstants.ABBREVIATED_NAI_AUDIT_COMMENT_LENGTH) + "...";
    }

    public AppCompVulnKey getAppCompVulnKey() {
	return appCompVulnKey;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getVulnerabilityNaiAuditStatus() {
	return vulnerabilityNaiAuditStatus;
    }

    public void setVulnerabilityNaiAuditStatus(
	    String vulnerabilityNaiAuditStatus) {
	this.vulnerabilityNaiAuditStatus = vulnerabilityNaiAuditStatus;
    }

    public String getVulnerabilityNaiAuditComment() {
	return vulnerabilityNaiAuditComment;
    }

    public void setVulnerabilityNaiAuditComment(
	    String vulnerabilityNaiAuditComment) {
	this.vulnerabilityNaiAuditComment = vulnerabilityNaiAuditComment;
	this.vulnerabilityNaiAuditCommentShort = shortenAuditComment(vulnerabilityNaiAuditComment);
    }

    public String getOrigNaiAuditStatus() {
	return origNaiAuditStatus;
    }

    public String getOrigNaiAuditComment() {
	return origNaiAuditComment;
    }

    public String getVulnerabilityNaiAuditCommentShort() {
	return vulnerabilityNaiAuditCommentShort;
    }

    @Override
    public String toString() {
	return "VulnNaiAuditDetails [appCompVulnKey=" + appCompVulnKey
		+ ", origNaiAuditStatus=" + origNaiAuditStatus
		+ ", origNaiAuditComment=" + origNaiAuditComment
		+ ", vulnerabilityNaiAuditStatus="
		+ vulnerabilityNaiAuditStatus
		+ ", vulnerabilityNaiAuditComment="
		+ vulnerabilityNaiAuditComment + ", username=" + username + "]";
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
