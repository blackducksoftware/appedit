package com.blackducksoftware.tools.appedit.naiaudit.dao.cc;

import java.util.ArrayList;
import java.util.List;

import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnNaiAuditDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;
import com.blackducksoftware.tools.commonframework.core.exception.CommonFrameworkException;
import com.blackducksoftware.tools.connector.codecenter.CodeCenterServerWrapper;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;

public class CcVulnNaiAuditDetailsDao implements VulnNaiAuditDetailsDao {
    private final CodeCenterServerWrapper ccsw;

    public CcVulnNaiAuditDetailsDao(CodeCenterServerWrapper ccsw) {
        this.ccsw = ccsw;

        // TODO TEMP code:
        ApplicationPojo app;
        try {
            app = ccsw.getApplicationManager().getApplicationByNameVersion("SB001", "Unspecified");
        } catch (CommonFrameworkException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
        System.out.println("\n\n\nLoaded app: " + app.getName());
    }

    @Override
    public VulnNaiAuditDetails updateVulnNaiAuditDetails(VulnNaiAuditDetails vulnNaiAuditDetails) {

        return new VulnNaiAuditDetails(new AppCompVulnKey("test_applicationId", "test_componentId", "test_vulnerabilityId"), "test_applicationName",
                "test_applicationVersion",
                "test_componentName", "test_componentVersion", "test_vulnerabilityName", "test_remediationStatus",
                "test_naiAuditStatus", "test_naiAuditComment");
    }

    @Override
    public List<VulnNaiAuditDetails> getVulnNaiAuditDetailsList(String applicationId) {
        List<VulnNaiAuditDetails> vulnNaiAuditDetailsList = new ArrayList<>();

        VulnNaiAuditDetails vulnNaiAuditDetails = new VulnNaiAuditDetails(new AppCompVulnKey("test_applicationId1", "test_componentId1",
                "test_vulnerabilityId1"),
                "test_applicationName1", "test_applicationVersion1",
                "test_componentName1", "test_componentVersion1", "test_vulnerabilityName1", "test_remediationStatus1",
                "test_naiAuditStatus1", "test_naiAuditComment1");
        vulnNaiAuditDetailsList.add(vulnNaiAuditDetails);

        vulnNaiAuditDetails = new VulnNaiAuditDetails(new AppCompVulnKey("test_applicationId2", "test_componentId2", "test_vulnerabilityId2"),
                "test_applicationName2", "test_applicationVersion2",
                "test_componentName2", "test_componentVersion2", "test_vulnerabilityName2", "test_remediationStatus2",
                "test_naiAuditStatus2", "test_naiAuditComment2");
        vulnNaiAuditDetailsList.add(vulnNaiAuditDetails);

        return vulnNaiAuditDetailsList;
    }

}
