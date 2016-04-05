package com.blackducksoftware.tools.appedit.naiaudit.dao;

import java.util.List;

import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;

public interface VulnNaiAuditDetailsDao {
    VulnNaiAuditDetails updateVulnNaiAuditDetails(VulnNaiAuditDetails vunlNaiAuditDetails);

    List<VulnNaiAuditDetails> getVulnNaiAuditDetailsList(String applicationId);
}
