package com.blackducksoftware.tools.appedit.naiaudit.dao.cc;

import java.util.HashMap;
import java.util.Map;

import com.blackducksoftware.tools.appedit.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.dao.AppCompVulnDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.commonframework.core.exception.CommonFrameworkException;
import com.blackducksoftware.tools.connector.codecenter.ICodeCenterServerWrapper;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;

public class CcAppCompVulnDetailsDao implements AppCompVulnDetailsDao {
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
        Map<AppCompVulnKey, AppCompVulnDetails> result = new HashMap<>();

        AppCompVulnKey key = new AppCompVulnKey("test_app_id1", "test_comp_id1", "test_vuln_id1");
        AppCompVulnDetails appCompVulnDetails = new AppCompVulnDetails(key, "test app name1",
                "test app version1", "test comp name1", "test comp version1",
                "test vuln name1", "test remediation status1");
        result.put(key, appCompVulnDetails);

        key = new AppCompVulnKey("test_app_id2", "test_comp_id2", "test_vuln_id2");
        appCompVulnDetails = new AppCompVulnDetails(key, "test app name2",
                "test app version2", "test comp name2", "test comp version2",
                "test vuln name2", "test remediation status2");
        result.put(key, appCompVulnDetails);

        try {
            ccsw.getApplicationManager().getApplicationById(applicationId);
        } catch (CommonFrameworkException e) {
            // TODO Auto-generated catch block
            throw new AppEditException("Error getting application with ID " + applicationId + ": " + e.getMessage(), e);
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

}
