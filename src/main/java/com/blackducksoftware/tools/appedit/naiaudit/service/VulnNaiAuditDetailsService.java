package com.blackducksoftware.tools.appedit.naiaudit.service;

import java.util.List;

import com.blackducksoftware.tools.appedit.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;

public interface VulnNaiAuditDetailsService {
    ApplicationPojo getApplicationByNameVersion(String appName,
	    String appVersion) throws AppEditException;

    ApplicationPojo getApplicationById(String appId) throws AppEditException;

    List<AppCompVulnComposite> getAppCompVulnCompositeList(String applicationId)
	    throws AppEditException;

    AppCompVulnComposite updateVulnNaiAuditDetails(
	    AppCompVulnComposite appCompVulnComposite) throws AppEditException;
}
