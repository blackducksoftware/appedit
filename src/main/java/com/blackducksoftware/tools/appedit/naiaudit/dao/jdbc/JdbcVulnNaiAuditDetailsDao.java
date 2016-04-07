package com.blackducksoftware.tools.appedit.naiaudit.dao.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnNaiAuditDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;

public class JdbcVulnNaiAuditDetailsDao implements VulnNaiAuditDetailsDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
            .getName());

    public JdbcVulnNaiAuditDetailsDao() {

    }

    @Override
    public VulnNaiAuditDetails updateVulnNaiAuditDetails(VulnNaiAuditDetails vulnNaiAuditDetails) {

        return new VulnNaiAuditDetails(new AppCompVulnKey("test_applicationId", "test_componentId", "test_vulnerabilityId"),
                "test_naiAuditStatus", "test_naiAuditComment");
    }

    @Override
    public Map<AppCompVulnKey, VulnNaiAuditDetails> getVulnNaiAuditDetailsMap(String applicationId) {
        Map<AppCompVulnKey, VulnNaiAuditDetails> vulnNaiAuditDetailsMap = new HashMap<>();
        AppCompVulnKey key = new AppCompVulnKey("test_applicationId1", "test_componentId1",
                "test_vulnerabilityId1");
        VulnNaiAuditDetails vulnNaiAuditDetails = new VulnNaiAuditDetails(key,
                "test_naiAuditStatus1", "test_naiAuditComment1");
        vulnNaiAuditDetailsMap.put(key, vulnNaiAuditDetails);

        key = new AppCompVulnKey("test_applicationId2", "test_componentId2", "test_vulnerabilityId2");
        vulnNaiAuditDetails = new VulnNaiAuditDetails(key,
                "test_naiAuditStatus2", "test_naiAuditComment2");
        vulnNaiAuditDetailsMap.put(key, vulnNaiAuditDetails);

        return vulnNaiAuditDetailsMap;
    }

}
