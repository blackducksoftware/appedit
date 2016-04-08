package com.blackducksoftware.tools.appedit.naiaudit.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "vulnNaiAuditDetailsList")
public class VulnNaiAuditDetailsList {
    private List<VulnNaiAuditDetails> vulnNaiAuditDetailsList;

    public VulnNaiAuditDetailsList() {
	vulnNaiAuditDetailsList = new ArrayList<>();
    }

    public VulnNaiAuditDetailsList(
	    List<VulnNaiAuditDetails> vulnNaiAuditDetailsList) {
	this.vulnNaiAuditDetailsList = vulnNaiAuditDetailsList;
    }

    public List<VulnNaiAuditDetails> getVulnNaiAuditDetailsList() {
	return vulnNaiAuditDetailsList;
    }

    public void setVulnNaiAuditDetailsList(
	    List<VulnNaiAuditDetails> vulnNaiAuditDetailsList) {
	this.vulnNaiAuditDetailsList = vulnNaiAuditDetailsList;
    }

}
