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
