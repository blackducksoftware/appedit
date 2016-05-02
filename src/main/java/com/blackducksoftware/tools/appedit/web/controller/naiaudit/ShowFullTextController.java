package com.blackducksoftware.tools.appedit.web.controller.naiaudit;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.FullTextItemType;
import com.blackducksoftware.tools.appedit.naiaudit.model.FullTextViewData;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;
import com.blackducksoftware.tools.appedit.web.controller.AppEditControllerException;

@Controller
@SessionAttributes({ "app", "dataSource" })
public class ShowFullTextController {
    private AppEditConfigManager config;

    @Inject
    public void setConfig(AppEditConfigManager config) {
	this.config = config;
    }

    private VulnNaiAuditDetailsService vulnNaiAuditDetailsService;

    @Inject
    public void setVulnNaiAuditDetailsService(
	    VulnNaiAuditDetailsService vulnNaiAuditDetailsService) {
	this.vulnNaiAuditDetailsService = vulnNaiAuditDetailsService;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());

    /**
     * Get requests for the Edit Application Details page by and auditor get
     * redirected here: auditors are presented with the NAI Audit screen
     * instead.
     * 
     */
    @RequestMapping(value = "/showfulltext", method = RequestMethod.GET)
    public String showNaiAuditDetails(WebRequest request, ModelMap model) {
	logger.debug("/editnaiauditdetails GET (redirected here)");

	String itemTypeString = request.getParameter("itemType");
	String itemKeyString = request.getParameter("itemKey");

	logger.debug("itemType: " + itemTypeString);
	logger.debug("itemKey: " + itemKeyString);

	FullTextViewData fullTextViewData;
	try {
	    AppCompVulnKey key = getKey(itemKeyString);
	    AppCompVulnComposite composite = getVulnDetails(key);
	    FullTextItemType type = FullTextItemType.valueOf(itemTypeString);
	    String fullText = getFullText(composite, type);
	    fullTextViewData = new FullTextViewData(composite.getCcPart()
		    .getApplicationName(), composite.getCcPart()
		    .getApplicationVersion(), composite.getCcPart()
		    .getComponentName(), composite.getCcPart()
		    .getComponentVersion(), composite.getCcPart()
		    .getVulnerabilityName(), type, key, fullText);
	} catch (AppEditControllerException e) {
	    String msg = e.getMessage();
	    logger.error(msg);
	    model.addAttribute("message", msg);
	    return e.getReturnValue();
	}

	model.addAttribute(fullTextViewData);
	return "fullText";
    }

    private String getFullText(AppCompVulnComposite composite,
	    FullTextItemType type) throws AppEditControllerException {
	String fullText = null;
	switch (type) {
	case REMEDIATION_COMMENTS:
	    fullText = composite.getCcPart()
		    .getVulnerabilityRemediationComments();
	    break;
	case VULNERABILITY_DESCRIPTION:
	    fullText = composite.getCcPart().getVulnerabilityDescription();
	    break;
	case NAI_AUDIT_COMMENT:
	    fullText = composite.getAuditPart().getOrigNaiAuditComment();
	    break;
	default:
	    String msg = "Invalid item type: " + type.toString();
	    throw new AppEditControllerException("error/programError", msg);
	}
	return fullText;
    }

    private AppCompVulnComposite getVulnDetails(AppCompVulnKey key)
	    throws AppEditControllerException {
	AppCompVulnComposite composite;
	try {
	    composite = vulnNaiAuditDetailsService.getAppCompVulnComposite(key);
	} catch (AppEditException e) {
	    throw new AppEditControllerException("error/programError",
		    e.getMessage());
	}
	return composite;
    }

    private AppCompVulnKey getKey(String itemKeyString)
	    throws AppEditControllerException {
	AppCompVulnKey key;
	try {
	    key = AppCompVulnKey.createFromString(itemKeyString);
	} catch (AppEditException e) {
	    throw new AppEditControllerException("error/programError",
		    e.getMessage());
	}
	return key;
    }
}
