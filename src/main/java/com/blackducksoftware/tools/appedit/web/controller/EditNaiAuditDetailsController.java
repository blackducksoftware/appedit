package com.blackducksoftware.tools.appedit.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.inputvalidation.InputValidatorEditNaiAuditDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.NaiAuditViewData;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;

/**
 * Controller for requests for and form submissions from the Edit NAI Audit
 * Details screen.
 *
 * @author sbillings
 *
 */
@Controller
@SessionAttributes({ "app", "dataSource" })
public class EditNaiAuditDetailsController {
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
    @RequestMapping(value = "/editnaiauditdetails", method = RequestMethod.GET)
    public String showNaiAuditDetails(WebRequest request, ModelMap model) {
	logger.debug("/editnaiauditdetails GET (redirected here)");
	String appId = request.getParameter("appId");
	String appName = request.getParameter("appName");
	logger.debug("appId: " + appId + "; appName: " + appName);
	if ((appId == null) && (appName == null)) {
	    return "redirect:/error/400";
	}

	ApplicationPojo app;
	// Load the app (so we have name, version)
	if (appId == null) {
	    try {
		app = vulnNaiAuditDetailsService.getApplicationByNameVersion(
			appName, config.getAppVersion());
	    } catch (AppEditException e) {
		String msg = "Error loading application " + appName + ": "
			+ e.getMessage();
		logger.error(msg);
		model.addAttribute("message", msg);
		return "error/programError";
	    }
	    appId = app.getId();
	} else {
	    try {
		app = vulnNaiAuditDetailsService.getApplicationById(appId);
	    } catch (AppEditException e) {
		String msg = "Error loading application with ID " + appId
			+ ": " + e.getMessage();
		logger.error(msg);
		model.addAttribute("message", msg);
		return "error/programError";
	    }
	    appName = app.getName();
	}
	List<AppCompVulnComposite> vulnNaiAuditDetailsList;
	try {
	    vulnNaiAuditDetailsList = vulnNaiAuditDetailsService
		    .getAppCompVulnCompositeList(appId);
	} catch (AppEditException e) {
	    String msg = "Error getting vulnerability details for application: "
		    + e.getMessage();
	    logger.error(msg);
	    model.addAttribute("message", msg);
	    return "error/programError";
	}
	logger.info("appDetails.getAppId(): " + appId);
	populateModelWithFormData(model, app.getId(), app.getName(),
		app.getVersion(), vulnNaiAuditDetailsList);
	return "editNaiAuditDetailsForm";
    }

    /**
     * Handles Edit NAI Audit Details form submissions. Updates app in Code
     * Center.
     */
    @RequestMapping(value = "/editnaiauditdetails", method = RequestMethod.POST)
    public String saveNaiAuditDetails(
	    @ModelAttribute("selectedVulnerabilities") NaiAuditViewData formData,
	    @RequestParam String action, ModelMap model) {

	logger.info("EditNaiAuditDetailsController.saveNaiAuditDetails(): selectedVulnerabilities: "
		+ formData);

	logger.debug("Application name / version: "
		+ formData.getApplicationName() + " / "
		+ formData.getApplicationVersion());

	/*
	 * TODO: Validate input // Validate input int i = 0; for (String
	 * attrLabel : app.getAttrNames()) { InputValidatorEditAppDetails
	 * inputValidator = new InputValidatorEditAppDetails( config); if
	 * (!inputValidator.validateAttributeValue(attrLabel, app
	 * .getAttrValues().get(i++).getValue())) { String msg = "The value of "
	 * + attrLabel + " is invalid."; logger.error(msg);
	 * model.addAttribute("message", msg); return "error/programError"; } }
	 */

	List<String> selectedRows = formData.getItemList();
	List<AppCompVulnComposite> fullVulnNaiAuditDetailsList;
	try {
	    fullVulnNaiAuditDetailsList = vulnNaiAuditDetailsService
		    .getAppCompVulnCompositeList(formData.getApplicationId());
	} catch (AppEditException e) {
	    String msg = "Error getting vulnerability details for application with ID "
		    + formData.getApplicationId() + e.getMessage();
	    logger.error(msg);
	    model.addAttribute("message", msg);
	    return "error/programError";
	}
	List<VulnNaiAuditDetails> selectedVulnNaiAuditDetailsList;
	if (selectedRows == null) {
	    String msg = "No rows selected.";
	    logger.warn(msg);
	    model.addAttribute("message", msg);
	    populateModelWithFormData(model, formData.getApplicationId(),
		    formData.getApplicationName(),
		    formData.getApplicationVersion(),
		    fullVulnNaiAuditDetailsList);
	    return "editNaiAuditDetailsForm";
	} else {
	    // User selected one or more rows; update each one

	    // Validate input
	    InputValidatorEditNaiAuditDetails inputValidator = new InputValidatorEditNaiAuditDetails(
		    config);
	    if (!inputValidator.validateCommentValue(formData.getComment())) {
		String msg = "The comment entered is invalid.";
		logger.error(msg);
		model.addAttribute("message", msg);
		populateModelWithFormData(model, formData.getApplicationId(),
			formData.getApplicationName(),
			formData.getApplicationVersion(),
			fullVulnNaiAuditDetailsList);
		return "editNaiAuditDetailsForm";
	    }

	    Authentication auth = SecurityContextHolder.getContext()
		    .getAuthentication();

	    String currentUser = auth.getName();
	    logger.info("User: " + currentUser);

	    for (String selectedRowKey : selectedRows) {
		logger.info("Selected vulnerability key: " + selectedRowKey);
		String[] selectedKeyParts = selectedRowKey.split("\\|");
		if (selectedKeyParts.length != 4) {
		    String msg = "selected row key (" + selectedRowKey
			    + ") is invalid; failed extracting IDs.";
		    logger.error(msg);
		    model.addAttribute("message", msg);
		    return "error/programError";
		}
		String applicationId = selectedKeyParts[0];
		String requestId = selectedKeyParts[1];
		String componentId = selectedKeyParts[2];
		String vulnerabilityId = selectedKeyParts[3];
		AppCompVulnKey key = new AppCompVulnKey(applicationId,
			requestId, componentId, vulnerabilityId);

		AppCompVulnComposite selectedVuln = findVuln(
			fullVulnNaiAuditDetailsList, key);
		if (selectedVuln == null) {
		    String msg = "selected row key (" + selectedRowKey
			    + ") not found in full vulnerabilities list.";
		    logger.error(msg);
		    model.addAttribute("message", msg);
		    return "error/programError";
		}

		selectedVuln.getAuditPart().setVulnerabilityNaiAuditStatus(
			formData.getVulnerabilityNaiAuditStatus());
		selectedVuln.getAuditPart().setVulnerabilityNaiAuditComment(
			formData.getComment());
		selectedVuln.getAuditPart().setUsername(currentUser);
		logger.info("Updating vulnerability with: " + selectedVuln);
		try {
		    vulnNaiAuditDetailsService
			    .updateVulnNaiAuditDetails(selectedVuln);
		} catch (AppEditException e) {
		    String msg = "Error updating NAI Audit details: "
			    + e.getMessage();
		    logger.error(msg);
		    model.addAttribute("message", msg);
		    return "error/programError";
		}
	    }
	}
	populateModelWithFormData(model, formData.getApplicationId(),
		formData.getApplicationName(),
		formData.getApplicationVersion(), fullVulnNaiAuditDetailsList);
	return "editNaiAuditDetailsForm";
    }

    private void populateModelWithFormData(ModelMap model, String appId,
	    String appName, String appVersion,
	    List<AppCompVulnComposite> vulnNaiAuditDetailsList) {

	NaiAuditViewData auditFormData = new NaiAuditViewData();
	auditFormData.setApplicationId(appId);
	auditFormData.setApplicationName(appName);
	auditFormData.setApplicationVersion(appVersion);

	model.addAttribute("selectedVulnerabilities", auditFormData);

	model.addAttribute("vulnNaiAuditDetailsList", vulnNaiAuditDetailsList);
    }

    @ModelAttribute("vulnerabilityNaiAuditStatusOptions")
    public List<String> populateVulnerabilityNaiAuditStatusOptions() {
	List<String> vulnerabilityNaiAuditStatusOptions = new ArrayList<>();
	vulnerabilityNaiAuditStatusOptions.add(""); // First/default value:
						    // <empty>
	for (String choice : config.getNaiAuditStatusChoices()) {
	    vulnerabilityNaiAuditStatusOptions.add(choice);
	}
	return vulnerabilityNaiAuditStatusOptions;
    }

    private AppCompVulnComposite findVuln(List<AppCompVulnComposite> vulnList,
	    AppCompVulnKey key) {
	for (AppCompVulnComposite vuln : vulnList) {
	    if (vuln.getKey().equals(key)) {
		return vuln;
	    }
	}
	return null;
    }
}
