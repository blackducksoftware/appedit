package com.blackducksoftware.tools.appedit.naiaudit.dao;

import java.util.Map;

import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;

public interface AppCompVulnDetailsDao {
    AppCompVulnDetails updateAppCompVulnDetails(AppCompVulnDetails appCompVulnDetails);

    Map<AppCompVulnKey, AppCompVulnDetails> getAppCompVulnDetailsMap(String applicationId);
}
