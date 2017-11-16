/**
 * Application Details Edit Webapp
 *
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.blackducksoftware.tools.appedit.naiaudit.model;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;

/**
 * A key that uniquely identifies a vulnerability.
 *
 * @author sbillings
 *
 */
public class AppCompVulnKey {
	private final String applicationId;
	private final String requestId;
	private final String componentId;
	private final String vulnerabilityId;
	private final String asString;

	public AppCompVulnKey(final String applicationId, final String requestId, final String componentId,
			final String vulnerabilityId) {
		this.applicationId = applicationId;
		this.requestId = requestId;
		this.componentId = componentId;
		this.vulnerabilityId = vulnerabilityId;
		asString = applicationId + "|" + requestId + "|" + componentId + "|" + vulnerabilityId;
	}

	public String getAsString() {
		return asString;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public String getRequestId() {
		return requestId;
	}

	public String getComponentId() {
		return componentId;
	}

	public String getVulnerabilityId() {
		return vulnerabilityId;
	}

	public static AppCompVulnKey createFromString(final String keyString)
			throws AppEditException {
		final String[] selectedKeyParts = keyString.split("\\|");
		if (selectedKeyParts.length != 4) {
			throw new AppEditException(keyString
					+ " is not a valid representation of an AppCompVulnKey");
		}
		final String applicationId = selectedKeyParts[0];
		final String requestId = selectedKeyParts[1];
		final String componentId = selectedKeyParts[2];
		final String vulnerabilityId = selectedKeyParts[3];
		final AppCompVulnKey key = new AppCompVulnKey(applicationId, requestId, componentId,
				vulnerabilityId);
		return key;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result + ((asString == null) ? 0 : asString.hashCode());
		result = prime * result + ((componentId == null) ? 0 : componentId.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((vulnerabilityId == null) ? 0 : vulnerabilityId.hashCode());
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
		final AppCompVulnKey other = (AppCompVulnKey) obj;
		if (applicationId == null) {
			if (other.applicationId != null) {
				return false;
			}
		} else if (!applicationId.equals(other.applicationId)) {
			return false;
		}
		if (asString == null) {
			if (other.asString != null) {
				return false;
			}
		} else if (!asString.equals(other.asString)) {
			return false;
		}
		if (componentId == null) {
			if (other.componentId != null) {
				return false;
			}
		} else if (!componentId.equals(other.componentId)) {
			return false;
		}
		if (requestId == null) {
			if (other.requestId != null) {
				return false;
			}
		} else if (!requestId.equals(other.requestId)) {
			return false;
		}
		if (vulnerabilityId == null) {
			if (other.vulnerabilityId != null) {
				return false;
			}
		} else if (!vulnerabilityId.equals(other.vulnerabilityId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "AppCompVulnKey [applicationId=" + applicationId + ", requestId=" + requestId + ", componentId="
				+ componentId + ", vulnerabilityId=" + vulnerabilityId + ", asString=" + asString + "]";
	}
}
