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

import java.util.List;

/**
 * The NAI Audit details needed by the View.
 *
 * @author sbillings
 *
 */
public class NaiAuditViewData {
	private String applicationId;

	private String applicationName;

	private String applicationVersion;

	private List<String> itemList;

	private String vulnerabilityNaiAuditStatus;

	private String comment;

	private int firstRowIndex;
	private int displayedRowCount;

	public List<String> getItemList() {
		return itemList;
	}

	public void setItemList(final List<String> itemList) {
		this.itemList = itemList;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	public String getVulnerabilityNaiAuditStatus() {
		return vulnerabilityNaiAuditStatus;
	}

	public void setVulnerabilityNaiAuditStatus(
			final String vulnerabilityNaiAuditStatus) {
		this.vulnerabilityNaiAuditStatus = vulnerabilityNaiAuditStatus;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(final String applicationId) {
		this.applicationId = applicationId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(final String applicationName) {
		this.applicationName = applicationName;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(final String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	public int getFirstRowIndex() {
		return firstRowIndex;
	}

	public void setFirstRowIndex(final int firstRowIndex) {
		this.firstRowIndex = firstRowIndex;
	}

	public int getDisplayedRowCount() {
		return displayedRowCount;
	}

	public void setDisplayedRowCount(final int displayedRowCount) {
		this.displayedRowCount = displayedRowCount;
	}

	@Override
	public String toString() {
		return "NaiAuditViewData [applicationId=" + applicationId + ", applicationName=" + applicationName
				+ ", applicationVersion=" + applicationVersion + ", itemList=" + itemList
				+ ", vulnerabilityNaiAuditStatus=" + vulnerabilityNaiAuditStatus + ", comment=" + comment
				+ ", firstRowIndex=" + firstRowIndex + ", displayedRowCount=" + displayedRowCount + "]";
	}

}
