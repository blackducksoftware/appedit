package com.blackducksoftware.tools.appedit.naiaudit.dao;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditChange;

public interface VulnNaiAuditChangeHistoryDao {
    void insertVulnNaiAuditChange(VulnNaiAuditChange vunlNaiAuditChange)
	    throws AppEditException;

}
