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

    public void setVulnerabilityNaiAuditStatus(
	    String vulnerabilityNaiAuditStatus) {
	this.vulnerabilityNaiAuditStatus = vulnerabilityNaiAuditStatus;
    }

    public String getApplicationId() {
	return applicationId;
    }

    public void setApplicationId(String applicationId) {
	this.applicationId = applicationId;
    }

    public String getApplicationName() {
	return applicationName;
    }

    public void setApplicationName(String applicationName) {
	this.applicationName = applicationName;
    }

    public String getApplicationVersion() {
	return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
	this.applicationVersion = applicationVersion;
    }

    @Override
    public String toString() {
	return "NaiAuditViewData [applicationId=" + applicationId
		+ ", applicationName=" + applicationName
		+ ", applicationVersion=" + applicationVersion + ", itemList="
		+ itemList + ", vulnerabilityNaiAuditStatus="
		+ vulnerabilityNaiAuditStatus + ", comment=" + comment + "]";
    }
}
