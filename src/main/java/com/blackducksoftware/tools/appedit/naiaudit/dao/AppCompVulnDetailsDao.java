package com.blackducksoftware.tools.appedit.naiaudit.dao;

import java.util.Map;

import com.blackducksoftware.tools.appedit.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;

public interface AppCompVulnDetailsDao {
    AppCompVulnDetails updateAppCompVulnDetails(AppCompVulnDetails appCompVulnDetails) throws AppEditException;

    ApplicationPojo getApplicationByNameVersion(String appName, String appVersion) throws AppEditException;

    Map<AppCompVulnKey, AppCompVulnDetails> getAppCompVulnDetailsMap(String applicationId) throws AppEditException;
}
