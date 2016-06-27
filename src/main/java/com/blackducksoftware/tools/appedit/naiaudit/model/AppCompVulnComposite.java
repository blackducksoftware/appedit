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

/**
 * This class composes a AppCompVulnDetails object and a VulnNaiAuditDetails
 * object to store all of the needed details for a vulnerability in one object.
 * 
 * @author sbillings
 *
 */
public class AppCompVulnComposite {
    private final AppCompVulnKey key;

    private final AppCompVulnDetails ccPart;

    private final VulnNaiAuditDetails auditPart;

    public AppCompVulnComposite(AppCompVulnKey key, AppCompVulnDetails ccPart,
	    VulnNaiAuditDetails auditPart) {
	this.key = key;
	this.ccPart = ccPart;
	this.auditPart = auditPart;
    }

    public AppCompVulnKey getKey() {
	return key;
    }

    public AppCompVulnDetails getCcPart() {
	return ccPart;
    }

    public VulnNaiAuditDetails getAuditPart() {
	return auditPart;
    }

    @Override
    public String toString() {
	return "AppCompVulnComposite [key=" + key + ", ccPart=" + ccPart
		+ ", auditPart=" + auditPart + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((key == null) ? 0 : key.hashCode());
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
	AppCompVulnComposite other = (AppCompVulnComposite) obj;
	if (key == null) {
	    if (other.key != null) {
		return false;
	    }
	} else if (!key.equals(other.key)) {
	    return false;
	}
	return true;
    }

}
