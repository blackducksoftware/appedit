package com.blackducksoftware.tools.appedit.naiaudit.service;

import java.util.List;

import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;

public interface VulnNaiAuditDetailsService {
    List<VulnNaiAuditDetails> getVulnNaiAuditDetailsList(String applicationId);

    VulnNaiAuditDetails updateVulnNaiAuditDetails(VulnNaiAuditDetails vulnNaiAuditDetails);
}
