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
package com.blackducksoftware.tools.appedit.web.controller.appdetails;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.blackducksoftware.tools.appedit.appdetails.inputvalidation.InputValidatorEditAppDetails;
import com.blackducksoftware.tools.appedit.appdetails.model.AppDetails;
import com.blackducksoftware.tools.appedit.appdetails.model.ViewAppBean;
import com.blackducksoftware.tools.appedit.appdetails.service.AppDetailsBeanConverter;
import com.blackducksoftware.tools.appedit.appdetails.service.AppService;
import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.model.Role;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;
import com.blackducksoftware.tools.appedit.web.controller.AppEditControllerException;
import com.blackducksoftware.tools.connector.codecenter.attribute.AttributeDefinitionPojo;
import com.blackducksoftware.tools.connector.codecenter.common.AttributeValuePojo;

/**
 * Controller for requests for and form submissions from the Edit App Details
 * screen.
 *
 * @author sbillings
 *
 */
@Controller
@SessionAttributes({ "app" })
public class EditAppDetailsController {
    private AppEditConfigManager config;

    private VulnNaiAuditDetailsService vulnNaiAuditDetailsService;

    @Inject
    public void setConfig(AppEditConfigManager config) {
	this.config = config;
    }

    private AppService appService;

    @Inject
    public void setAppService(AppService appService) {
	this.appService = appService;
    }

    @Inject
    public void setVulnNaiAuditDetailsService(
	    VulnNaiAuditDetailsService vulnNaiAuditDetailsService) {
	this.vulnNaiAuditDetailsService = vulnNaiAuditDetailsService;
    }

    private AppDetailsBeanConverter appDetailsBeanConverter;

    @Inject
    public void setAppDetailsBeanConverter(
	    AppDetailsBeanConverter appDetailsBeanConverter) {
	this.appDetailsBeanConverter = appDetailsBeanConverter;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());

    /**
     * Handles requests for the Edit Application Details page for a given appId.
     * Loads app from Code Center.
     */
    @RequestMapping(value = "/editappdetails", method = RequestMethod.GET)
    public String showEditForm(WebRequest request, Model model) {
	logger.info("Received request for Edit Application Details page.");

	AppDetails appDetails = null;
	try {
	    String paramString = getParamString(request);
	    // Throws exception if user is an auditor, resulting in a redirect
	    // to edit nai audit details screen
	    verifyUserIsNotAnAuditor(paramString);

	    // If here (no exception): This user is an end-user
	    appDetails = loadApplication(request);
	    verifyUserIsOnAppTeam(appDetails);
	    verifyAllAttributesExist(appDetails);
	} catch (AppEditControllerException e) {
	    logger.error(e.getMessage());
	    model.addAttribute("message", e.getMessage());
	    return e.getReturnValue();
	}

	// Convert the generic appDetails object to view-friendly appDetails
	// object

	ViewAppBean app = appService.createViewAppBean(appDetails);

	// Put the objects the JSP will need into the model
	model.addAttribute("app", app);

	return "editAppDetailsForm";
    }

    /**
     * Handles Edit App Details form submissions. Updates app in Code Center.
     */
    @RequestMapping(value = "/editappdetails", method = RequestMethod.POST)
    public String updateApp(@ModelAttribute("app") ViewAppBean app,
	    @RequestParam String action, ModelMap model) {

	logger.info("EditAppDetails.updateApp(): app: " + app);

	try {
	    validateInput(app);

	    // Convert the View-friendly appDetails to a generic appDetails
	    // object
	    AppDetails appDetails = appDetailsBeanConverter
		    .createAppDetails(app);

	    verifyUserIsOnAppTeam(appDetails);
	    updateApp(appDetails);
	} catch (AppEditControllerException e) {
	    logger.error(e.getMessage());
	    model.addAttribute("message", e.getMessage());
	    return e.getReturnValue();
	}

	return "editAppDetailsResult";
    }

    private void updateApp(AppDetails appDetails)
	    throws AppEditControllerException {
	try {
	    appService.update(appDetails); // update app in Code Center
	} catch (Exception e) {
	    String msg = "Error updating application "
		    + appDetails.getAppName() + ": " + e.getMessage();

	    throw new AppEditControllerException("error/programError", msg);
	}
    }

    private void validateInput(ViewAppBean app)
	    throws AppEditControllerException {
	// Validate input
	int i = 0;
	for (String attrLabel : app.getAttrNames()) {
	    InputValidatorEditAppDetails inputValidator = new InputValidatorEditAppDetails(
		    config);
	    if (!inputValidator.validateAttributeValue(attrLabel, app
		    .getAttrValues().get(i++).getValue())) {
		String msg = "The value of " + attrLabel + " is invalid.";
		throw new AppEditControllerException("error/programError", msg);
	    }
	}
    }

    private void verifyAllAttributesExist(AppDetails appDetails)
	    throws AppEditControllerException {
	// Make sure all the attrValues we need are populated
	try {
	    populateMissingAttrValues(config, appDetails);
	} catch (Exception e) {
	    String msg = "Error populating missing attribute values: "
		    + e.getMessage();

	    throw new AppEditControllerException("error/programError", msg);
	}
    }

    private void verifyUserIsOnAppTeam(AppDetails appDetails)
	    throws AppEditControllerException {
	// Make sure they are on this app's team (list of users that can access
	// it)
	String username = (String) SecurityContextHolder.getContext()
		.getAuthentication().getPrincipal();
	boolean isAuthorized = appService.authorizeUser(appDetails.getAppId(),
		username);
	if (!isAuthorized) {
	    String msg = "You are not authorized to access this application";
	    throw new AppEditControllerException("error/programError", msg);
	}
    }

    private AppDetails loadApplication(WebRequest request)
	    throws AppEditControllerException {
	String appId = request.getParameter("appId");
	String appName = request.getParameter("appName");
	// Load application
	AppDetails appDetails = null;
	try {
	    // load app from Code Center using whatever info we were given (try
	    // ID first)
	    if (appId != null) {
		appDetails = appService.loadFromId(appId);
	    } else {
		appDetails = appService.loadFromName(appName);
	    }
	} catch (Exception e) {
	    String msg = "Error loading application: " + e.getMessage();
	    throw new AppEditControllerException("error/programError", msg);
	}

	if (appDetails == null) {
	    String msg = "Application not found";
	    throw new AppEditControllerException("error/programError", msg);
	}
	return appDetails;
    }

    private void verifyUserIsNotAnAuditor(String paramString)
	    throws AppEditControllerException {
	// Get the user's roles
	Collection<GrantedAuthority> grantedAuthorities = (Collection<GrantedAuthority>) SecurityContextHolder
		.getContext().getAuthentication().getAuthorities();
	// If this user is an auditor, display the Edit NAI Audit Details
	// form
	for (GrantedAuthority auth : grantedAuthorities) {
	    String roleString = auth.getAuthority();
	    logger.info("Role: " + roleString);
	    Role role = Role.valueOf(roleString);
	    logger.info("Role enum value: " + role);
	    if (role == Role.ROLE_AUDITOR) {

		String redirectString = "redirect:editnaiauditdetails?"
			+ paramString;

		logger.info("User is an auditor. Redirecting to: "
			+ redirectString);

		throw new AppEditControllerException(redirectString, null);
	    }
	}
    }

    private String getParamString(WebRequest request)
	    throws AppEditControllerException {
	String paramString;
	// Process URL parameter: app ID/name
	String appId = request.getParameter("appId");
	String appName = request.getParameter("appName");
	if ((appId == null) && (appName == null)) {
	    String msg = "The URL must specify either appId or appName.";

	    throw new AppEditControllerException("error/programError", msg);
	}
	if (appId != null) {
	    paramString = "appId=" + appId;
	} else {
	    paramString = "appName=" + appName;
	}
	return paramString;
    }

    private void populateMissingAttrValues(AppEditConfigManager config2,
	    AppDetails appDetails) throws Exception {
	Map<String, String> attrMap = config.getAttributeMap();

	for (int i = 0; i < attrMap.keySet().size(); i++) {
	    String attrLabel = config.getAttrLabel(i);
	    String attrCodeCenterName = attrMap.get(attrLabel);
	    AttributeValuePojo attrValue = appDetails
		    .getCustomAttributeValue(attrCodeCenterName);
	    if (attrValue == null) {
		logger.info("The Code Center attribute for " + attrLabel + " ("
			+ attrCodeCenterName + ") has no value object.");

		AttributeDefinitionPojo attrDef = appService
			.getAttributeDefinitionByName(attrCodeCenterName);
		attrValue = new AttributeValuePojo(attrDef.getId(),
			attrCodeCenterName, "");
		logger.info("Created attrValue: " + attrValue);
		appDetails.addCustomAttributeValue(attrCodeCenterName,
			attrValue);
	    }
	}

    }

}
