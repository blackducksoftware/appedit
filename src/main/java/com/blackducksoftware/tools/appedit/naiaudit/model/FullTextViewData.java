package com.blackducksoftware.tools.appedit.naiaudit.model;

public class FullTextViewData {
    private String applicationName;
    private String applicationVersion;
    private String componentName;
    private String componentVersion;
    private String vulnerabilityName;
    private FullTextItemType itemType;
    private AppCompVulnKey itemKey;
    private String fullText;

    public FullTextViewData() {
    }

    public FullTextViewData(String applicationName, String applicationVersion,
	    String componentName, String componentVersion,
	    String vulnerabilityName, FullTextItemType itemType,
	    AppCompVulnKey itemKey, String fullText) {
	super();
	this.applicationName = applicationName;
	this.applicationVersion = applicationVersion;
	this.componentName = componentName;
	this.componentVersion = componentVersion;
	this.vulnerabilityName = vulnerabilityName;
	this.itemType = itemType;
	this.itemKey = itemKey;
	this.fullText = fullText;
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

    public String getComponentName() {
	return componentName;
    }

    public void setComponentName(String componentName) {
	this.componentName = componentName;
    }

    public String getComponentVersion() {
	return componentVersion;
    }

    public void setComponentVersion(String componentVersion) {
	this.componentVersion = componentVersion;
    }

    public String getVulnerabilityName() {
	return vulnerabilityName;
    }

    public void setVulnerabilityName(String vulnerabilityName) {
	this.vulnerabilityName = vulnerabilityName;
    }

    public FullTextItemType getItemType() {
	return itemType;
    }

    public void setItemType(FullTextItemType itemType) {
	this.itemType = itemType;
    }

    public AppCompVulnKey getItemKey() {
	return itemKey;
    }

    public void setItemKey(AppCompVulnKey itemKey) {
	this.itemKey = itemKey;
    }

    public String getFullText() {
	return fullText;
    }

    public void setFullText(String fullText) {
	this.fullText = fullText;
    }

    @Override
    public String toString() {
	return "FullTextViewData [applicationName=" + applicationName
		+ ", applicationVersion=" + applicationVersion
		+ ", componentName=" + componentName + ", componentVersion="
		+ componentVersion + ", vulnerabilityName=" + vulnerabilityName
		+ ", itemType=" + itemType + ", itemKey=" + itemKey
		+ ", fullText=" + fullText + "]";
    }

}
