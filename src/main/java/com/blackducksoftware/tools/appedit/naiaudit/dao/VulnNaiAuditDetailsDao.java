package com.blackducksoftware.tools.appedit.naiaudit.dao;

import java.util.Map;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;

/**
 * Interface for DAOs that add/get/update NAI Audit details.
 * 
 * @author sbillings
 *
 */
public interface VulnNaiAuditDetailsDao {

    VulnNaiAuditDetails insertVulnNaiAuditDetails(
	    VulnNaiAuditDetails vunlNaiAuditDetails) throws AppEditException;

    VulnNaiAuditDetails updateVulnNaiAuditDetails(
	    VulnNaiAuditDetails vunlNaiAuditDetails) throws AppEditException;

    Map<AppCompVulnKey, VulnNaiAuditDetails> getVulnNaiAuditDetailsMap(
	    String applicationId) throws AppEditException;
}
