package com.blackducksoftware.tools.appedit.naiaudit.dao.cc;

import java.util.ArrayList;
import java.util.List;

import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnNaiAuditDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;

public class CcVulnNaiAuditDetailsDao implements VulnNaiAuditDetailsDao {

    @Override
    public VulnNaiAuditDetails updateVulnNaiAuditDetails(VulnNaiAuditDetails vulnNaiAuditDetails) {

        return new VulnNaiAuditDetails("test_applicationId", "test_componentId", "test_vulnerabilityId", "test_applicationName", "test_applicationVersion",
                "test_componentName", "test_componentVersion", "test_vulnerabilityName");
    }

    @Override
    public List<VulnNaiAuditDetails> getVulnNaiAuditDetailsList(String applicationId) {
        VulnNaiAuditDetails vulnNaiAuditDetails = new VulnNaiAuditDetails("test_applicationId", "test_componentId", "test_vulnerabilityId",
                "test_applicationName", "test_applicationVersion",
                "test_componentName", "test_componentVersion", "test_vulnerabilityName");
        List<VulnNaiAuditDetails> vulnNaiAuditDetailsList = new ArrayList<>();
        vulnNaiAuditDetailsList.add(vulnNaiAuditDetails);
        return vulnNaiAuditDetailsList;
    }

}
