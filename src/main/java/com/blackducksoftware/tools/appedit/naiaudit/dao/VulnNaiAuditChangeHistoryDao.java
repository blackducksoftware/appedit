package com.blackducksoftware.tools.appedit.naiaudit.dao;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditChange;

/**
 * Data Access Object for the change history (audit trail) that records changes
 * to NAI Audit data.
 * 
 * @author sbillings
 *
 */
public interface VulnNaiAuditChangeHistoryDao {
    void insertVulnNaiAuditChange(VulnNaiAuditChange vunlNaiAuditChange)
	    throws AppEditException;

}
