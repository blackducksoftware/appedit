/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
	private static final String SELECT_ALL_VISIBLE_CHECKBOX_KEY_STRING = "selectAllValue";
	private static final int INITIAL_FIRST_ROW_INDEX = 0;
	private static final int INITIAL_DISPLAYED_ROW_COUNT = 5;
	private AppEditConfigManager config;

	private int currentFirstRowIndex = INITIAL_FIRST_ROW_INDEX;
	private int currentDisplayedRowCount = INITIAL_DISPLAYED_ROW_COUNT;

	@Inject
	public void setConfig(final AppEditConfigManager config) {
		this.config = config;
	}

	private VulnNaiAuditDetailsService vulnNaiAuditDetailsService;

	@Inject
	public void setVulnNaiAuditDetailsService(
			final VulnNaiAuditDetailsService vulnNaiAuditDetailsService) {
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
	public String showNaiAuditDetails(final WebRequest request, final ModelMap model) {
		logger.debug("/editnaiauditdetails GET (redirected here)");

		try {
			final ApplicationPojo app = getApplication(request);
			populateModelForApp(model, app);
		} catch (final AppEditControllerException e) {
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
			@ModelAttribute("selectedVulnerabilities") final NaiAuditViewData formData,
			@RequestParam final String action, final ModelMap model) {

		logger.info("EditNaiAuditDetailsController.saveNaiAuditDetails(): selectedVulnerabilities: "
				+ formData);

		currentFirstRowIndex = formData.getFirstRowIndex();
		currentDisplayedRowCount = formData.getDisplayedRowCount();

		logger.debug("Application name / version: "
				+ formData.getApplicationName() + " / "
				+ formData.getApplicationVersion());

		logger.debug("currentFirstRowIndex: " + currentFirstRowIndex);
		logger.debug("currentDisplayedRowCount: " + currentDisplayedRowCount);

		List<AppCompVulnComposite> fullVulnNaiAuditDetailsList;
		try {
			fullVulnNaiAuditDetailsList = getFullVulnNaiAuditDetailsList(formData);
			verifyRowIsSelected(model, formData, fullVulnNaiAuditDetailsList);
			validateStatusValue(model, formData, fullVulnNaiAuditDetailsList);
			checkCommentLength(model, formData, fullVulnNaiAuditDetailsList);
			validateCommentValue(config, model, formData,
					fullVulnNaiAuditDetailsList);

			final String currentUser = getUser();
			for (final String selectedRowKey : formData.getItemList()) {
				updateVulnFromRow(model, formData, fullVulnNaiAuditDetailsList,
						currentUser, selectedRowKey);
			}

			final ApplicationPojo app = getApplication(formData);
			populateModelForApp(model, app);
		} catch (final AppEditControllerException e1) {
			logger.error(e1.getMessage());
			model.addAttribute("message", e1.getMessage());
			return e1.getReturnValue();
		}

		return "editNaiAuditDetailsForm";
	}

	private ApplicationPojo getApplication(final NaiAuditViewData formData) throws AppEditControllerException {
		ApplicationPojo app;
		try {
			app = vulnNaiAuditDetailsService.getApplicationById(formData.getApplicationId(), true);
		} catch (final AppEditException e) {
			final String msg = "Error updating getting application: " + e.getMessage();
			throw new AppEditControllerException("error/programError", msg);
		}
		return app;
	}

	private void updateVulnFromRow(final ModelMap model, final NaiAuditViewData formData,
			final List<AppCompVulnComposite> fullVulnNaiAuditDetailsList,
			final String currentUser, final String selectedRowKey)
					throws AppEditControllerException {
		logger.info("Selected vulnerability key: " + selectedRowKey);
		final AppCompVulnKey key = generateKey(selectedRowKey);
		if (key == null) {
			logger.debug("Skipping selected row key: " + selectedRowKey);
			return;
		}
		final AppCompVulnComposite selectedVuln = getVuln(model,
				fullVulnNaiAuditDetailsList, key, selectedRowKey);

		if (selectedVuln != null) {
			applyUserChangesToVulnObject(selectedVuln, formData, currentUser);
			logger.info("Updating vulnerability with: " + selectedVuln);
			updateVulnerability(selectedVuln);
		}
	}

	private void updateVulnerability(final AppCompVulnComposite selectedVuln)
			throws AppEditControllerException {
		try {
			vulnNaiAuditDetailsService.updateVulnNaiAuditDetails(selectedVuln);
		} catch (final AppEditException e) {
			final String msg = "Error updating NAI Audit details: " + e.getMessage();
			throw new AppEditControllerException("error/programError", msg);
		}
	}

	private void applyUserChangesToVulnObject(
			final AppCompVulnComposite selectedVuln, final NaiAuditViewData formData,
			final String currentUser) {
		selectedVuln.getAuditPart().setVulnerabilityNaiAuditStatus(
				formData.getVulnerabilityNaiAuditStatus());
		selectedVuln.getAuditPart().setVulnerabilityNaiAuditComment(
				formData.getComment());
		selectedVuln.getAuditPart().setUsername(currentUser);
	}

	private AppCompVulnComposite getVuln(final ModelMap model,
			final List<AppCompVulnComposite> fullVulnNaiAuditDetailsList,
			final AppCompVulnKey key, final String keyString)
					throws AppEditControllerException {

		if (SELECT_ALL_VISIBLE_CHECKBOX_KEY_STRING.equals(keyString)) {
			logger.debug("Ignoring vulnerability keyString: " + keyString);
			return null;
		}
		final AppCompVulnComposite selectedVuln = findVuln(
				fullVulnNaiAuditDetailsList, key);
		if (selectedVuln == null) {
			final String msg = "The selected row key (" + keyString
					+ ") not found in full vulnerabilities list.";

			throw new AppEditControllerException("error/programError", msg);
		}
		return selectedVuln;
	}

	private AppCompVulnKey generateKey(final String selectedRowKey)
			throws AppEditControllerException {

		if (SELECT_ALL_VISIBLE_CHECKBOX_KEY_STRING.equals(selectedRowKey)) {
			logger.debug("Ignoring vulnerability keyString: " + selectedRowKey);
			return null;
		}

		AppCompVulnKey key;
		try {
			key = AppCompVulnKey.createFromString(selectedRowKey);
		} catch (final AppEditException e) {
			final String msg = "The selected row key (" + selectedRowKey
					+ ") is invalid; failed extracting IDs.";

			throw new AppEditControllerException("error/programError", msg);
		}

		return key;
	}

	private String getUser() {
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		final String currentUser = auth.getName();
		logger.info("User: " + currentUser);
		return currentUser;
	}

	private void validateCommentValue(final AppEditConfigManager config,
			final ModelMap model, final NaiAuditViewData formData,
			final List<AppCompVulnComposite> fullVulnNaiAuditDetailsList)
					throws AppEditControllerException {
		// Validate input
		final InputValidatorEditNaiAuditDetails inputValidator = new InputValidatorEditNaiAuditDetails(
				config);
		if (!inputValidator.validateCommentValue(formData.getComment())) {
			final String msg = "The comment entered is invalid.";
			populateModelWithFormData(model, formData.getApplicationId(),
					formData.getApplicationName(),
					formData.getApplicationVersion(),
					fullVulnNaiAuditDetailsList);
			throw new AppEditControllerException("editNaiAuditDetailsForm", msg);
		}
	}

	private void verifyRowIsSelected(final ModelMap model, final NaiAuditViewData formData,
			final List<AppCompVulnComposite> fullVulnNaiAuditDetailsList)
					throws AppEditControllerException {
		final List<String> selectedRows = formData.getItemList();
		if ((selectedRows == null) || (selectedRows.size() == 0)) {
			final String msg = "No rows selected.";
			populateModelWithFormData(model, formData.getApplicationId(),
					formData.getApplicationName(),
					formData.getApplicationVersion(),
					fullVulnNaiAuditDetailsList);
			throw new AppEditControllerException("editNaiAuditDetailsForm", msg);
		}
	}

	private void checkCommentLength(final ModelMap model, final NaiAuditViewData formData,
			final List<AppCompVulnComposite> fullVulnNaiAuditDetailsList)
					throws AppEditControllerException {
		if (formData.getComment().length() > AppEditConstants.NAI_AUDIT_COMMENT_MAX_LENGTH) {
			final String msg = "The comment entered is too long. Maximum length is "
					+ AppEditConstants.NAI_AUDIT_COMMENT_MAX_LENGTH
					+ " characters";

			populateModelWithFormData(model, formData.getApplicationId(),
					formData.getApplicationName(),
					formData.getApplicationVersion(),
					fullVulnNaiAuditDetailsList);

			throw new AppEditControllerException("editNaiAuditDetailsForm", msg);
		}
	}

	private void validateStatusValue(final ModelMap model, final NaiAuditViewData formData,
			final List<AppCompVulnComposite> fullVulnNaiAuditDetailsList)
					throws AppEditControllerException {
		if (formData.getVulnerabilityNaiAuditStatus().length() <= 0) {
			final String msg = "NAI Audit Status is required";

			populateModelWithFormData(model, formData.getApplicationId(),
					formData.getApplicationName(),
					formData.getApplicationVersion(),
					fullVulnNaiAuditDetailsList);

			throw new AppEditControllerException("editNaiAuditDetailsForm", msg);
		}
	}

	private List<AppCompVulnComposite> getFullVulnNaiAuditDetailsList(
			final NaiAuditViewData formData) throws AppEditControllerException {
		List<AppCompVulnComposite> fullVulnNaiAuditDetailsList;
		try {
			fullVulnNaiAuditDetailsList = vulnNaiAuditDetailsService
					.getAppCompVulnCompositeList(formData.getApplicationId());
		} catch (final AppEditException e) {
			final String msg = "Error getting vulnerability details for application with ID "
					+ formData.getApplicationId() + e.getMessage();

			throw new AppEditControllerException("error/programError", msg);
		}
		return fullVulnNaiAuditDetailsList;
	}

	private List<AppCompVulnComposite> populateModelForApp(
			final ModelMap model, final ApplicationPojo app)
					throws AppEditControllerException {
		List<AppCompVulnComposite> vulnNaiAuditDetailsList;
		try {
			vulnNaiAuditDetailsList = vulnNaiAuditDetailsService
					.getAppCompVulnCompositeList(app.getId());
		} catch (final AppEditException e) {
			final String msg = "Error getting vulnerability details for application: "
					+ e.getMessage();
			throw new AppEditControllerException("error/programError", msg);
		}
		populateModelWithFormData(model, app.getId(), app.getName(),
				app.getVersion(), vulnNaiAuditDetailsList);
		return vulnNaiAuditDetailsList;
	}

	private ApplicationPojo getApplication(final WebRequest request)
			throws AppEditControllerException {
		ApplicationPojo app;
		String appId = request.getParameter("appId");
		final String appName = request.getParameter("appName");
		logger.debug("appId: " + appId + "; appName: " + appName);

		if ((appId == null) && (appName == null)) {
			throw new AppEditControllerException("redirect:/error/400", null);
		}

		// Load the app (so we have name, version)
		if (appId == null) {
			try {
				app = vulnNaiAuditDetailsService.getApplicationByNameVersion(
						appName, config.getAppVersion(), true);
			} catch (final AppEditException e) {
				final String msg = "Error loading application " + appName + ": "
						+ e.getMessage();
				throw new AppEditControllerException("error/programError", msg);
			}
			appId = app.getId();
		} else {
			try {
				app = vulnNaiAuditDetailsService
						.getApplicationById(appId, true);
			} catch (final AppEditException e) {
				final String msg = "Error loading application with ID " + appId
						+ ": " + e.getMessage();
				throw new AppEditControllerException("error/programError", msg);
			}

		}
		logger.info("Application name: " + app.getName());
		return app;
	}

	private void populateModelWithFormData(final ModelMap model, final String appId,
			final String appName, final String appVersion,
			final List<AppCompVulnComposite> vulnNaiAuditDetailsList) {

		logger.debug("Sending to view: " + vulnNaiAuditDetailsList);
		final NaiAuditViewData auditFormData = new NaiAuditViewData();
		auditFormData.setApplicationId(appId);
		auditFormData.setApplicationName(appName);
		auditFormData.setApplicationVersion(appVersion);
		adjustCurrentFirstRowIndex(vulnNaiAuditDetailsList);
		auditFormData.setFirstRowIndex(currentFirstRowIndex);
		auditFormData.setDisplayedRowCount(currentDisplayedRowCount);

		model.addAttribute("selectedVulnerabilities", auditFormData);

		model.addAttribute("vulnNaiAuditDetailsList", vulnNaiAuditDetailsList);
	}

	/**
	 * Rows might have been removed, so make sure first row index is valid given
	 * current #rows.
	 *
	 * @param auditFormData
	 */
	private void adjustCurrentFirstRowIndex(final List<AppCompVulnComposite> vulnNaiAuditDetailsList) {
		if (vulnNaiAuditDetailsList.size() == 0) {
			currentFirstRowIndex = 0;
		} else if (currentFirstRowIndex > vulnNaiAuditDetailsList.size() - 1) {
			currentFirstRowIndex = vulnNaiAuditDetailsList.size() - 1;
		}
	}

	/**
	 * Called by Spring to populate the vulnerabilityNaiAuditStatusOptions model
	 * attribute.
	 *
	 * @return
	 */
	@ModelAttribute("vulnerabilityNaiAuditStatusOptions")
	public List<String> populateVulnerabilityNaiAuditStatusOptions() {
		final List<String> vulnerabilityNaiAuditStatusOptions = new ArrayList<>();
		for (final String choice : config.getNaiAuditStatusChoices()) {
			vulnerabilityNaiAuditStatusOptions.add(choice);
		}
		return vulnerabilityNaiAuditStatusOptions;
	}

	private AppCompVulnComposite findVuln(final List<AppCompVulnComposite> vulnList,
			final AppCompVulnKey key) {
		for (final AppCompVulnComposite vuln : vulnList) {
			if (vuln.getKey().equals(key)) {
				return vuln;
			}
		}
		return null;
	}
}
