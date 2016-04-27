package com.blackducksoftware.tools.appedit.naiaudit.dao;

import java.util.Map;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;

/**
 * Application Component Vulnerability Details DAO. Gets/updates details
 * specific to a vulnerability on a specific application as used in a specific
 * application.
 * 
 * @author sbillings
 *
 */
public interface AppCompVulnDetailsDao {
    AppCompVulnDetails updateAppCompVulnDetails(
	    AppCompVulnDetails appCompVulnDetails) throws AppEditException;

    ApplicationPojo getApplicationByNameVersion(String appName,
	    String appVersion) throws AppEditException;

    ApplicationPojo getApplicationById(String appId) throws AppEditException;

    Map<AppCompVulnKey, AppCompVulnDetails> getAppCompVulnDetailsMap(
	    String applicationId) throws AppEditException;
}
