package com.blackducksoftware.tools.appedit.naiaudit.dao.cc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.dao.AppCompVulnDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.commonframework.core.exception.CommonFrameworkException;
import com.blackducksoftware.tools.connector.codecenter.ICodeCenterServerWrapper;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;
import com.blackducksoftware.tools.connector.codecenter.common.CodeCenterComponentPojo;
import com.blackducksoftware.tools.connector.codecenter.common.RequestPojo;
import com.blackducksoftware.tools.connector.codecenter.common.RequestVulnerabilityPojo;

public class CcAppCompVulnDetailsDao implements AppCompVulnDetailsDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
            .getName());

    private final ICodeCenterServerWrapper ccsw;

    public CcAppCompVulnDetailsDao(ICodeCenterServerWrapper ccsw) {
        this.ccsw = ccsw;
    }

    @Override
    public AppCompVulnDetails updateAppCompVulnDetails(AppCompVulnDetails appCompVulnDetails) {

        return new AppCompVulnDetails(new AppCompVulnKey("test_app_id1", "test_comp_id1", "test_vuln_id1"),
                "test comp name1", "test comp version1",
                "test vuln name1",
                "vulnerabilitySeverity",
                "vulnerabilityPublishDate",
                "vulnerabilityDescription",
                "vulnerabilityTargetRemediationDate",
                "vulnerabilityActualRemediationDate",
                "test remediation status1",
                "vulnerabilityRemediationComments");
    }

    @Override
    public Map<AppCompVulnKey, AppCompVulnDetails> getAppCompVulnDetailsMap(String applicationId) throws AppEditException {
        logger.debug("getAppCompVulnDetailsMap() called with appId: " + applicationId);
        Map<AppCompVulnKey, AppCompVulnDetails> result = new HashMap<>();

        // TODO: THis approach seems to be wrong; get requests instead
        // List<CodeCenterComponentPojo> comps;
        // try {
        // comps = ccsw.getApplicationManager().getComponentsByAppId(CodeCenterComponentPojo.class, applicationId, null,
        // false);
        // } catch (CommonFrameworkException e) {
        // throw new AppEditException("Error getting application with ID " + applicationId + ": " + e.getMessage(), e);
        // }
        // for (CodeCenterComponentPojo comp : comps) {
        // ComponentIdToken componentIdToken = new ComponentIdToken();
        // componentIdToken.setId(comp.getId());
        // VulnerabilityPageFilter vulnerabilityPageFilter = new VulnerabilityPageFilter();
        // vulnerabilityPageFilter.setFirstRowIndex(0);
        // vulnerabilityPageFilter.setLastRowIndex(Integer.MAX_VALUE);
        // List<VulnerabilitySummary> vulnerabilities;
        // try {
        // vulnerabilities = ccsw.getInternalApiWrapper().getVulnerabilityApi()
        // .searchDirectMatchedVulnerabilitiesByCatalogComponent(componentIdToken, vulnerabilityPageFilter);
        // } catch (SdkFault e) {
        // throw new AppEditException("Error getting vulnerabilities for component " + comp.getName() + " / " +
        // comp.getVersion() + ": " + e.getMessage(),
        // e);
        // }
        // for (VulnerabilitySummary vuln : vulnerabilities) {
        // logger.debug("Processing: Comp: " + comp.getName() + " / " + comp.getVersion() + ": Vuln: " +
        // vuln.getName().getName());
        // AppCompVulnKey key = new AppCompVulnKey(applicationId, comp.getId(), vuln.getId().getId());
        // AppCompVulnDetails appCompVulnDetails = new AppCompVulnDetails(key, "appName belongs in NaiAuditViewData",
        // "appVersion belongs in NaiAuditViewData", comp.getName(), comp.getVersion(),
        // vuln.getName().getName(), "RemStatus: don't have yet");
        // result.put(key, appCompVulnDetails);
        // }
        // }

        List<RequestPojo> requests;
        try {
            requests = ccsw.getApplicationManager().getRequestsByAppId(applicationId);
        } catch (CommonFrameworkException e) {
            throw new AppEditException("Error getting application with ID " + applicationId + ": " + e.getMessage(), e);
        }
        for (RequestPojo request : requests) {
            String requestId = request.getRequestId();

            CodeCenterComponentPojo comp;
            try {
                comp = ccsw.getComponentManager().getComponentById(CodeCenterComponentPojo.class, request.getComponentId());
            } catch (CommonFrameworkException e1) {
                throw new AppEditException("Error getting component with ID " + request.getComponentId() + ": " + e1.getMessage(), e1);
            }
            List<RequestVulnerabilityPojo> requestVulnerabilities;
            try {
                requestVulnerabilities = ccsw.getRequestManager().getVulnerabilitiesByRequestId(requestId);
            } catch (CommonFrameworkException e) {
                throw new AppEditException("Error getting vulnerabilities for request ID " + requestId + ": " + e.getMessage(), e);
            }
            for (RequestVulnerabilityPojo requestVulnerability : requestVulnerabilities) {

                logger.debug("Processing: Comp: " + comp.getName() + " / " + comp.getVersion() + ": Vuln: " + requestVulnerability.getVulnerabilityName());
                AppCompVulnKey key = new AppCompVulnKey(applicationId, comp.getId(), requestVulnerability.getVulnerabilityId());

                Date datePublished = requestVulnerability.getDatePublished();
                Date targetRemediationDate = requestVulnerability.getTargetRemediationDate();
                Date actualRemediationDate = requestVulnerability.getActualRemediationDate();

                String datePublishedString;
                if (datePublished == null) {
                    datePublishedString = "<not set>";
                } else {
                    datePublishedString = datePublished.toString();
                }

                String targetRemediationDateString;
                if (targetRemediationDate == null) {
                    targetRemediationDateString = "<not set>";
                } else {
                    targetRemediationDateString = targetRemediationDate.toString();
                }

                String actualRemediationDateString;
                if (actualRemediationDate == null) {
                    actualRemediationDateString = "<not set>";
                } else {
                    actualRemediationDateString = actualRemediationDate.toString();
                }
                AppCompVulnDetails appCompVulnDetails = new AppCompVulnDetails(key, comp.getName(), comp.getVersion(),
                        requestVulnerability.getVulnerabilityName(),
                        requestVulnerability.getBaseScore(),
                        datePublishedString,
                        requestVulnerability.getDescription(),
                        targetRemediationDateString,
                        actualRemediationDateString,
                        requestVulnerability.getReviewStatusName(),
                        requestVulnerability.getComments());
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
