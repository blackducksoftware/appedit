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
        List<VulnNaiAuditDetails> vulnNaiAuditDetailsList = new ArrayList<>();

        VulnNaiAuditDetails vulnNaiAuditDetails = new VulnNaiAuditDetails("test_applicationId1", "test_componentId1", "test_vulnerabilityId1",
                "test_applicationName1", "test_applicationVersion1",
                "test_componentName1", "test_componentVersion1", "test_vulnerabilityName1");
        vulnNaiAuditDetailsList.add(vulnNaiAuditDetails);

        vulnNaiAuditDetails = new VulnNaiAuditDetails("test_applicationId2", "test_componentId2", "test_vulnerabilityId2",
                "test_applicationName2", "test_applicationVersion2",
                "test_componentName2", "test_componentVersion2", "test_vulnerabilityName2");
        vulnNaiAuditDetailsList.add(vulnNaiAuditDetails);

        return vulnNaiAuditDetailsList;
    }

}
