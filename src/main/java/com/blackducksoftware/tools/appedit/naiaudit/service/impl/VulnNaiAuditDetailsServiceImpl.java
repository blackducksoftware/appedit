package com.blackducksoftware.tools.appedit.naiaudit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.dao.AppCompVulnDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnNaiAuditChangeHistoryDao;
import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnNaiAuditDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditChange;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;

public class VulnNaiAuditDetailsServiceImpl implements
	VulnNaiAuditDetailsService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());

    // config (wired)
    private AppEditConfigManager config;

    @Inject
    public void setConfig(AppEditConfigManager config) {
	this.config = config;
    }

    // DAO objects (wired)
    private VulnNaiAuditDetailsDao vulnNaiAuditDetailsDao;

    @Inject
    public void setVulnNaiAuditDetailsDao(
	    VulnNaiAuditDetailsDao vulnNaiAuditDetailsDao) {
	this.vulnNaiAuditDetailsDao = vulnNaiAuditDetailsDao;
    }

    private AppCompVulnDetailsDao appCompVulnDetailsDao;

    @Inject
    public void setAppCompVulnDetailsDao(
	    AppCompVulnDetailsDao appCompVulnDetailsDao) {
	this.appCompVulnDetailsDao = appCompVulnDetailsDao;
    }

    private VulnNaiAuditChangeHistoryDao vulnNaiAuditChangeHistoryDao;

    @Inject
    public void setVulnNaiAuditChangeHistoryDao(
	    VulnNaiAuditChangeHistoryDao vulnNaiAuditChangeHistoryDao) {
	this.vulnNaiAuditChangeHistoryDao = vulnNaiAuditChangeHistoryDao;
    }

    /**
     * Get an applications components+vulnerabilities.
     * 
     * @param applicationId
     * @return
     * @throws AppEditException
     */
    @Override
    public List<AppCompVulnComposite> getAppCompVulnCompositeList(
	    String applicationId) throws AppEditException {
	List<AppCompVulnComposite> result = new ArrayList<>();
	Map<AppCompVulnKey, AppCompVulnDetails> ccParts = appCompVulnDetailsDao
		.getAppCompVulnDetailsMap(applicationId);
	Map<AppCompVulnKey, VulnNaiAuditDetails> auditParts = vulnNaiAuditDetailsDao
		.getVulnNaiAuditDetailsMap(applicationId);
	for (AppCompVulnKey key : ccParts.keySet()) {
	    VulnNaiAuditDetails auditDetails;
	    if (auditParts.containsKey(key)) {
		auditDetails = auditParts.get(key);
	    } else {
		auditDetails = new VulnNaiAuditDetails(key, "", "");
		vulnNaiAuditDetailsDao.insertVulnNaiAuditDetails(auditDetails);
	    }
	    AppCompVulnComposite appCompVulnComposite = new AppCompVulnComposite(
		    key, ccParts.get(key), auditDetails);
	    result.add(appCompVulnComposite);
	}
	return result;
    }

    /**
     * Update the NAI Audit details for one component's vulnerability.
     * 
     * @param appCompVulnComposite
     * @return
     * @throws AppEditException
     */
    @Override
    public AppCompVulnComposite updateVulnNaiAuditDetails(
	    AppCompVulnComposite appCompVulnComposite) throws AppEditException {

	logger.debug("updateVulnNaiAuditDetails() called with: "
		+ appCompVulnComposite);

	updateRemStatusIfNeeded(appCompVulnComposite);

	String origRemediationComment = appCompVulnComposite.getCcPart()
		.getVulnerabilityRemediationComments();
	String incomingNaiAuditComment = appCompVulnComposite.getAuditPart()
		.getVulnerabilityNaiAuditComment();
	String newRemediationComment = deriveNewRemediationComment(
		origRemediationComment, incomingNaiAuditComment);

	appCompVulnComposite.getCcPart().setVulnerabilityRemediationComments(
		newRemediationComment);

	// append nai comment to previous nai comment
	appCompVulnComposite.getAuditPart().setVulnerabilityNaiAuditComment(
		incomingNaiAuditComment);

	AppCompVulnDetails ccPart = appCompVulnDetailsDao
		.updateAppCompVulnDetails(appCompVulnComposite.getCcPart());
	VulnNaiAuditDetails auditPart = vulnNaiAuditDetailsDao
		.updateVulnNaiAuditDetails(appCompVulnComposite.getAuditPart());

	addToChangeHistory(appCompVulnComposite, auditPart);

	return new AppCompVulnComposite(ccPart.getAppCompVulnKey(), ccPart,
		auditPart);
    }

    private void addToChangeHistory(AppCompVulnComposite appCompVulnComposite,
	    VulnNaiAuditDetails auditPart) throws AppEditException {
	VulnNaiAuditChange vulnNaiAuditChange = new VulnNaiAuditChange(
		new Date(), appCompVulnComposite.getKey(),
		auditPart.getUsername(), auditPart.getOrigNaiAuditStatus(),
		auditPart.getOrigNaiAuditComment(), appCompVulnComposite
			.getAuditPart().getVulnerabilityNaiAuditStatus(),
		appCompVulnComposite.getAuditPart()
			.getVulnerabilityNaiAuditComment());
	insertVulnNaiAuditChange(vulnNaiAuditChange);
    }

    private String deriveNewRemediationComment(String origRemediationComment,
	    String incomingNaiAuditComment) {
	origRemediationComment = ensureNotNull(origRemediationComment);
	String separator = "";
	if (!StringUtils.isBlank(origRemediationComment)) {
	    separator = "\n\n";
	}
	String nowString = config.getNaiAuditDateFormat().format(new Date());
	String newRemediationComment = origRemediationComment + separator + "["
		+ nowString + ": NAI Audit Comment: " + incomingNaiAuditComment
		+ "]";
	return newRemediationComment;
    }

    private String ensureNotNull(String origRemediationComment) {
	if (origRemediationComment == null) {
	    origRemediationComment = "";
	}
	return origRemediationComment;
    }

    private void updateRemStatusIfNeeded(
	    AppCompVulnComposite appCompVulnComposite) {
	if (appCompVulnComposite.getAuditPart()
		.getVulnerabilityNaiAuditStatus()
		.equals(config.getNaiAuditRejectedStatusName())) {
	    logger.debug("Auditor rejected, changing rem status to: "
		    + config.getNaiAuditRejectedStatusChangesRemStatusTo());
	    appCompVulnComposite.getCcPart().setVulnerabilityRemediationStatus(
		    config.getNaiAuditRejectedStatusChangesRemStatusTo());
	}
    }

    /**
     * Get Application by name/version.
     * 
     * @param appName
     * @param appVersion
     * @return
     * @throws AppEditException
     */
    @Override
    public ApplicationPojo getApplicationByNameVersion(String appName,
	    String appVersion) throws AppEditException {
	ApplicationPojo app = appCompVulnDetailsDao
		.getApplicationByNameVersion(appName, appVersion);
	return app;
    }

    /**
     * Get application by ID.
     * 
     * @param appId
     * @return
     * @throws AppEditException
     */
    @Override
    public ApplicationPojo getApplicationById(String appId)
	    throws AppEditException {
	ApplicationPojo app = appCompVulnDetailsDao.getApplicationById(appId);
	return app;
    }

    private void insertVulnNaiAuditChange(VulnNaiAuditChange vulnNaiAuditChange)
	    throws AppEditException {
	vulnNaiAuditChangeHistoryDao
		.insertVulnNaiAuditChange(vulnNaiAuditChange);
    }

}
