/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
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

	public VulnNaiAuditDetails(final AppCompVulnKey appCompVulnKey,
			final String vulnerabilityNaiAuditStatus,
			final String vulnerabilityNaiAuditComment) {
		this.appCompVulnKey = appCompVulnKey;
		this.vulnerabilityNaiAuditStatus = vulnerabilityNaiAuditStatus;
		this.vulnerabilityNaiAuditComment = vulnerabilityNaiAuditComment;
		this.vulnerabilityNaiAuditCommentShort = shortenAuditComment(vulnerabilityNaiAuditComment);
		this.origNaiAuditStatus = vulnerabilityNaiAuditStatus;
		this.origNaiAuditComment = vulnerabilityNaiAuditComment;
	}

	private String shortenAuditComment(final String longComment) {
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

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getVulnerabilityNaiAuditStatus() {
		return vulnerabilityNaiAuditStatus;
	}

	public void setVulnerabilityNaiAuditStatus(
			final String vulnerabilityNaiAuditStatus) {
		this.vulnerabilityNaiAuditStatus = vulnerabilityNaiAuditStatus;
	}

	public String getVulnerabilityNaiAuditComment() {
		return vulnerabilityNaiAuditComment;
	}

	public void setVulnerabilityNaiAuditComment(
			final String vulnerabilityNaiAuditComment) {
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
		return "VulnNaiAuditDetails [appCompVulnKey=" + appCompVulnKey + ", origNaiAuditStatus=" + origNaiAuditStatus
				+ ", vulnerabilityNaiAuditStatus=" + vulnerabilityNaiAuditStatus + ", username=" + username + "]";
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
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final VulnNaiAuditDetails other = (VulnNaiAuditDetails) obj;
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
