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
import com.blackducksoftware.tools.appedit.naiaudit.model.FullTextType;
import com.blackducksoftware.tools.appedit.naiaudit.model.FullTextViewData;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;

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

	FullTextViewData fullTextViewData = new FullTextViewData(
		"applicationName", "applicationVersion", "componentName",
		"componentVersion", "vulnerabilityName",
		FullTextType.REMEDIATION_COMMENT, "fullText");

	model.addAttribute(fullTextViewData);
	return "fullText";
    }
}
