package com.blackducksoftware.tools.appedit.naiaudit.service;

import java.util.List;

import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;

public interface VulnNaiAuditDetailsService {
    List<AppCompVulnComposite> getAppCompVulnCompositeList(String applicationId);

    AppCompVulnComposite updateVulnNaiAuditDetails(AppCompVulnComposite appCompVulnComposite);
}
