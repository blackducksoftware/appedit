package com.blackducksoftware.tools.appedit.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;
import com.blackducksoftware.tools.connector.codecenter.common.AttributeValuePojo;
import com.blackducksoftware.tools.connector.codecenter.common.VulnerabilitySeverity;
import com.blackducksoftware.tools.connector.common.ApprovalStatus;

public class MockVulnNaiAuditDetailsService implements
	VulnNaiAuditDetailsService {

    @Override
    public ApplicationPojo getApplicationByNameVersion(String appName,
	    String appVersion) throws AppEditException {

	return new ApplicationPojo(appName, appName, appVersion,
		new ArrayList<AttributeValuePojo>(), ApprovalStatus.APPROVED,
		false, "owner");
    }

    @Override
    public ApplicationPojo getApplicationById(String appId)
	    throws AppEditException {
	return new ApplicationPojo(appId, appId, "Unspecified",
		new ArrayList<AttributeValuePojo>(), ApprovalStatus.APPROVED,
		false, "owner");
    }

    @Override
    public List<AppCompVulnComposite> getAppCompVulnCompositeList(
	    String applicationId) throws AppEditException {

	Date now = new Date();

	AppCompVulnKey key = new AppCompVulnKey("app1Id", "request1Id",
		"component1Id", "vulnerability1Id");
	AppCompVulnDetails ccPart = new AppCompVulnDetails(key,
		"component1Name", "component1Version", "vulnerability1Name",
		VulnerabilitySeverity.HIGH, "vulnerability1BaseScore",
		"vulnerability1ExploitableScore", "vulnerability1ImpactScore",
		now, now, now, "vulnerability1Description", now, now,
		"vulnerability1RemediationStatus",
		"vulnerability1RemediationComments");
	VulnNaiAuditDetails auditPart = new VulnNaiAuditDetails(key,
		"vulnerability1NaiAuditStatus", "vulnerability1NaiAuditComment");

	AppCompVulnComposite appCompVulnComposite = new AppCompVulnComposite(
		key, ccPart, auditPart);
	List<AppCompVulnComposite> result = new ArrayList<AppCompVulnComposite>();
	result.add(appCompVulnComposite);
	return result;
    }

    @Override
    public AppCompVulnComposite updateVulnNaiAuditDetails(
	    AppCompVulnComposite appCompVulnComposite) throws AppEditException {

	return appCompVulnComposite;
    }

}
