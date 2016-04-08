package com.blackducksoftware.tools.appedit.naiaudit.model;

public class AppCompVulnKey {
    private final String applicationId;
    private final String requestId;

    private final String componentId;

    private final String vulnerabilityId;

    private final String asString;

    public AppCompVulnKey(String applicationId, String requestId, String componentId, String vulnerabilityId) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result
				+ ((asString == null) ? 0 : asString.hashCode());
		result = prime * result
				+ ((componentId == null) ? 0 : componentId.hashCode());
		result = prime * result
				+ ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result
				+ ((vulnerabilityId == null) ? 0 : vulnerabilityId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppCompVulnKey other = (AppCompVulnKey) obj;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		if (asString == null) {
			if (other.asString != null)
				return false;
		} else if (!asString.equals(other.asString))
			return false;
		if (componentId == null) {
			if (other.componentId != null)
				return false;
		} else if (!componentId.equals(other.componentId))
			return false;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		if (vulnerabilityId == null) {
			if (other.vulnerabilityId != null)
				return false;
		} else if (!vulnerabilityId.equals(other.vulnerabilityId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AppCompVulnKey [applicationId=" + applicationId
				+ ", requestId=" + requestId + ", componentId=" + componentId
				+ ", vulnerabilityId=" + vulnerabilityId + ", asString="
				+ asString + "]";
	}

    
}
