/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License version 2 only
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *******************************************************************************/
package com.blackducksoftware.tools.appedit.web.controller.naiaudit;

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
import com.blackducksoftware.tools.appedit.core.AppEditConstants;
import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.inputvalidation.InputValidatorEditNaiAuditDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.NaiAuditViewData;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;
import com.blackducksoftware.tools.appedit.web.controller.AppEditControllerException;
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

	try {
	    ApplicationPojo app = getApplication(request);
	    processVulnNaiAuditDetailsList(model, app);
	} catch (AppEditControllerException e) {
	    logger.error(e.getMessage());
	    model.addAttribute("message", e.getMessage());
	    return e.getReturnValue();
	}
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

	List<AppCompVulnComposite> fullVulnNaiAuditDetailsList;
	try {
	    fullVulnNaiAuditDetailsList = getFullVulnNaiAuditDetailsList(formData);
	    verifyRowIsSelected(model, formData, fullVulnNaiAuditDetailsList);
	    checkCommentLength(model, formData, fullVulnNaiAuditDetailsList);
	    validateCommentValue(config, model, formData,
		    fullVulnNaiAuditDetailsList);

	    String currentUser = getUser();
	    for (String selectedRowKey : formData.getItemList()) {
		updateVulnFromRow(model, formData, fullVulnNaiAuditDetailsList,
			currentUser, selectedRowKey);
	    }
	} catch (AppEditControllerException e1) {
	    logger.error(e1.getMessage());
	    model.addAttribute("message", e1.getMessage());
	    return e1.getReturnValue();
	}

	populateModelWithFormData(model, formData.getApplicationId(),
		formData.getApplicationName(),
		formData.getApplicationVersion(), fullVulnNaiAuditDetailsList);
	return "editNaiAuditDetailsForm";
    }

    private void updateVulnFromRow(ModelMap model, NaiAuditViewData formData,
	    List<AppCompVulnComposite> fullVulnNaiAuditDetailsList,
	    String currentUser, String selectedRowKey)
	    throws AppEditControllerException {
	logger.info("Selected vulnerability key: " + selectedRowKey);
	AppCompVulnKey key = generateKey(selectedRowKey);
	AppCompVulnComposite selectedVuln = getVuln(model,
		fullVulnNaiAuditDetailsList, key, selectedRowKey);

	applyUserChangesToVulnObject(selectedVuln, formData, currentUser);
	logger.info("Updating vulnerability with: " + selectedVuln);
	updateVulnerability(selectedVuln);
    }

    private void updateVulnerability(AppCompVulnComposite selectedVuln)
	    throws AppEditControllerException {
	try {
	    vulnNaiAuditDetailsService.updateVulnNaiAuditDetails(selectedVuln);
	} catch (AppEditException e) {
	    String msg = "Error updating NAI Audit details: " + e.getMessage();
	    throw new AppEditControllerException(
		    "error/programError", msg);
	}
    }

    private void applyUserChangesToVulnObject(
	    AppCompVulnComposite selectedVuln, NaiAuditViewData formData,
	    String currentUser) {
	selectedVuln.getAuditPart().setVulnerabilityNaiAuditStatus(
		formData.getVulnerabilityNaiAuditStatus());
	selectedVuln.getAuditPart().setVulnerabilityNaiAuditComment(
		formData.getComment());
	selectedVuln.getAuditPart().setUsername(currentUser);
    }

    private AppCompVulnComposite getVuln(ModelMap model,
	    List<AppCompVulnComposite> fullVulnNaiAuditDetailsList,
	    AppCompVulnKey key, String keyString)
	    throws AppEditControllerException {
	AppCompVulnComposite selectedVuln = findVuln(
		fullVulnNaiAuditDetailsList, key);
	if (selectedVuln == null) {
	    String msg = "The selected row key (" + keyString
		    + ") not found in full vulnerabilities list.";

	    throw new AppEditControllerException(
		    "error/programError", msg);
	}
	return selectedVuln;
    }

    private AppCompVulnKey generateKey(String selectedRowKey)
	    throws AppEditControllerException {
	String[] selectedKeyParts = selectedRowKey.split("\\|");
	if (selectedKeyParts.length != 4) {
	    String msg = "The selected row key (" + selectedRowKey
		    + ") is invalid; failed extracting IDs.";

	    throw new AppEditControllerException(
		    "error/programError", msg);
	}
	String applicationId = selectedKeyParts[0];
	String requestId = selectedKeyParts[1];
	String componentId = selectedKeyParts[2];
	String vulnerabilityId = selectedKeyParts[3];
	AppCompVulnKey key = new AppCompVulnKey(applicationId, requestId,
		componentId, vulnerabilityId);
	return key;
    }

    private String getUser() {
	Authentication auth = SecurityContextHolder.getContext()
		.getAuthentication();

	String currentUser = auth.getName();
	logger.info("User: " + currentUser);
	return currentUser;
    }

    private void validateCommentValue(AppEditConfigManager config, ModelMap model,
	    NaiAuditViewData formData,
	    List<AppCompVulnComposite> fullVulnNaiAuditDetailsList)
	    throws AppEditControllerException {
	// Validate input
	InputValidatorEditNaiAuditDetails inputValidator = new InputValidatorEditNaiAuditDetails(
		config);
	if (!inputValidator.validateCommentValue(formData.getComment())) {
	    String msg = "The comment entered is invalid.";
	    populateModelWithFormData(model, formData.getApplicationId(),
		    formData.getApplicationName(),
		    formData.getApplicationVersion(),
		    fullVulnNaiAuditDetailsList);
	    throw new AppEditControllerException(
		    "editNaiAuditDetailsForm", msg);
	}
    }

    private void verifyRowIsSelected(ModelMap model, NaiAuditViewData formData,
	    List<AppCompVulnComposite> fullVulnNaiAuditDetailsList)
	    throws AppEditControllerException {
	List<String> selectedRows = formData.getItemList();
	if ((selectedRows == null) || (selectedRows.size() == 0)) {
	    String msg = "No rows selected.";
	    populateModelWithFormData(model, formData.getApplicationId(),
		    formData.getApplicationName(),
		    formData.getApplicationVersion(),
		    fullVulnNaiAuditDetailsList);
	    throw new AppEditControllerException(
		    "editNaiAuditDetailsForm", msg);
	}
    }

    private void checkCommentLength(ModelMap model, NaiAuditViewData formData,
	    List<AppCompVulnComposite> fullVulnNaiAuditDetailsList)
	    throws AppEditControllerException {
	if (formData.getComment().length() > AppEditConstants.NAI_AUDIT_COMMENT_MAX_LENGTH) {
	    String msg = "The comment entered is too long. Maximum length is "
		    + AppEditConstants.NAI_AUDIT_COMMENT_MAX_LENGTH
		    + " characters";

	    populateModelWithFormData(model, formData.getApplicationId(),
		    formData.getApplicationName(),
		    formData.getApplicationVersion(),
		    fullVulnNaiAuditDetailsList);

	    throw new AppEditControllerException(
		    "editNaiAuditDetailsForm", msg);
	}
    }

    private List<AppCompVulnComposite> getFullVulnNaiAuditDetailsList(
	    NaiAuditViewData formData)
	    throws AppEditControllerException {
	List<AppCompVulnComposite> fullVulnNaiAuditDetailsList;
	try {
	    fullVulnNaiAuditDetailsList = vulnNaiAuditDetailsService
		    .getAppCompVulnCompositeList(formData.getApplicationId());
	} catch (AppEditException e) {
	    String msg = "Error getting vulnerability details for application with ID "
		    + formData.getApplicationId() + e.getMessage();

	    throw new AppEditControllerException(
		    "error/programError", msg);
	}
	return fullVulnNaiAuditDetailsList;
    }

    private List<AppCompVulnComposite> processVulnNaiAuditDetailsList(
	    ModelMap model, ApplicationPojo app)
	    throws AppEditControllerException {
	List<AppCompVulnComposite> vulnNaiAuditDetailsList;
	try {
	    vulnNaiAuditDetailsList = vulnNaiAuditDetailsService
		    .getAppCompVulnCompositeList(app.getId());
	} catch (AppEditException e) {
	    String msg = "Error getting vulnerability details for application: "
		    + e.getMessage();
	    throw new AppEditControllerException(
		    "error/programError", msg);
	}
	populateModelWithFormData(model, app.getId(), app.getName(),
		app.getVersion(), vulnNaiAuditDetailsList);
	return vulnNaiAuditDetailsList;
    }

    private ApplicationPojo getApplication(WebRequest request)
	    throws AppEditControllerException {
	ApplicationPojo app;
	String appId = request.getParameter("appId");
	String appName = request.getParameter("appName");
	logger.debug("appId: " + appId + "; appName: " + appName);

	if ((appId == null) && (appName == null)) {
	    throw new AppEditControllerException(
		    "redirect:/error/400", null);
	}

	// Load the app (so we have name, version)
	if (appId == null) {
	    try {
		app = vulnNaiAuditDetailsService.getApplicationByNameVersion(
			appName, config.getAppVersion());
	    } catch (AppEditException e) {
		String msg = "Error loading application " + appName + ": "
			+ e.getMessage();
		throw new AppEditControllerException(
			"error/programError", msg);
	    }
	    appId = app.getId();
	} else {
	    try {
		app = vulnNaiAuditDetailsService.getApplicationById(appId);
	    } catch (AppEditException e) {
		String msg = "Error loading application with ID " + appId
			+ ": " + e.getMessage();
		throw new AppEditControllerException(
			"error/programError", msg);
	    }

	}
	logger.info("Application name: " + app.getName());
	return app;
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
