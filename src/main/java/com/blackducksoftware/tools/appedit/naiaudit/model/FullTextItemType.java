package com.blackducksoftware.tools.appedit.naiaudit.model;

public enum FullTextItemType {
    VULNERABILITY_DESCRIPTION("Vulnerability Description"), REMEDIATION_COMMENTS(
	    "Remediation Comments"), NAI_AUDIT_COMMENT("NAI Audit Comment");

    private final String description;

    FullTextItemType(String description) {
	this.description = description;
    }

    public String getDescription() {
	return description;
    }

}
