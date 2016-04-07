package com.blackducksoftware.tools.appedit.naiaudit.dao.cc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.sdk.codecenter.cola.data.ComponentIdToken;
import com.blackducksoftware.sdk.codecenter.fault.SdkFault;
import com.blackducksoftware.sdk.codecenter.vulnerability.data.VulnerabilityPageFilter;
import com.blackducksoftware.sdk.codecenter.vulnerability.data.VulnerabilitySummary;
import com.blackducksoftware.tools.appedit.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.dao.AppCompVulnDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.commonframework.core.exception.CommonFrameworkException;
import com.blackducksoftware.tools.connector.codecenter.ICodeCenterServerWrapper;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;
import com.blackducksoftware.tools.connector.codecenter.common.CodeCenterComponentPojo;

public class CcAppCompVulnDetailsDao implements AppCompVulnDetailsDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
            .getName());

    private final ICodeCenterServerWrapper ccsw;

    public CcAppCompVulnDetailsDao(ICodeCenterServerWrapper ccsw) {
        this.ccsw = ccsw;
    }

    @Override
    public AppCompVulnDetails updateAppCompVulnDetails(AppCompVulnDetails appCompVulnDetails) {

        return new AppCompVulnDetails(new AppCompVulnKey("test_app_id1", "test_comp_id1", "test_vuln_id1"), "test app name1", "test app version1",
                "test comp name1", "test comp version1",
                "test vuln name1", "test remediation status1");
    }

    @Override
    public Map<AppCompVulnKey, AppCompVulnDetails> getAppCompVulnDetailsMap(String applicationId) throws AppEditException {
        logger.debug("getAppCompVulnDetailsMap() called with appId: " + applicationId);
        Map<AppCompVulnKey, AppCompVulnDetails> result = new HashMap<>();

        // key = new AppCompVulnKey("test_app_id1", "test_comp_id1", "test_vuln_id1");
        // appCompVulnDetails = new AppCompVulnDetails(key, "test app name1",
        // "test app version1", "test comp name1", "test comp version1",
        // "test vuln name1", "test remediation status1");
        // result.put(key, appCompVulnDetails);
        //
        // key = new AppCompVulnKey("test_app_id2", "test_comp_id2", "test_vuln_id2");
        // appCompVulnDetails = new AppCompVulnDetails(key, "test app name2",
        // "test app version2", "test comp name2", "test comp version2",
        // "test vuln name2", "test remediation status2");
        // result.put(key, appCompVulnDetails);

        List<CodeCenterComponentPojo> comps;
        try {
            comps = ccsw.getApplicationManager().getComponentsByAppId(CodeCenterComponentPojo.class, applicationId, null, false);
        } catch (CommonFrameworkException e) {
            throw new AppEditException("Error getting application with ID " + applicationId + ": " + e.getMessage(), e);
        }
        for (CodeCenterComponentPojo comp : comps) {
            ComponentIdToken componentIdToken = new ComponentIdToken();
            componentIdToken.setId(comp.getId());
            VulnerabilityPageFilter vulnerabilityPageFilter = new VulnerabilityPageFilter();
            vulnerabilityPageFilter.setFirstRowIndex(0);
            vulnerabilityPageFilter.setLastRowIndex(Integer.MAX_VALUE);
            List<VulnerabilitySummary> vulnerabilities;
            try {
                vulnerabilities = ccsw.getInternalApiWrapper().getVulnerabilityApi()
                        .searchDirectMatchedVulnerabilitiesByCatalogComponent(componentIdToken, vulnerabilityPageFilter);
            } catch (SdkFault e) {
                throw new AppEditException("Error getting vulnerabilities for component " + comp.getName() + " / " + comp.getVersion() + ": " + e.getMessage(),
                        e);
            }
            for (VulnerabilitySummary vuln : vulnerabilities) {
                logger.debug("Processing: Comp: " + comp.getName() + " / " + comp.getVersion() + ": Vuln: " + vuln.getName().getName());
                AppCompVulnKey key = new AppCompVulnKey(applicationId, comp.getId(), vuln.getId().getId());
                AppCompVulnDetails appCompVulnDetails = new AppCompVulnDetails(key, "appName belongs in NaiAuditViewData",
                        "appVersion belongs in NaiAuditViewData", comp.getName(), comp.getVersion(),
                        vuln.getName().getName(), "RemStatus: don't have yet");
                result.put(key, appCompVulnDetails);
            }
        }

        return result;
    }

    @Override
    public ApplicationPojo getApplicationByNameVersion(String appName, String appVersion) throws AppEditException {
        ApplicationPojo app;
        try {
            app = ccsw.getApplicationManager().getApplicationByNameVersion(appName, appVersion);
        } catch (CommonFrameworkException e) {
            throw new AppEditException("Error getting application " + appName + " / " + appVersion + ": " + e.getMessage(), e);
        }
        return app;
    }

    @Override
    public ApplicationPojo getApplicationById(String appId) throws AppEditException {
        ApplicationPojo app;
        try {
            app = ccsw.getApplicationManager().getApplicationById(appId);
        } catch (CommonFrameworkException e) {
            throw new AppEditException("Error getting application with ID " + appId + ": " + e.getMessage(), e);
        }
        return app;
    }

}
