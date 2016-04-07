package com.blackducksoftware.tools.appedit.naiaudit.dao;

import java.util.Map;

import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;

public interface VulnNaiAuditDetailsDao {
    VulnNaiAuditDetails updateVulnNaiAuditDetails(VulnNaiAuditDetails vunlNaiAuditDetails);

    Map<AppCompVulnKey, VulnNaiAuditDetails> getVulnNaiAuditDetailsMap(String applicationId);
}
