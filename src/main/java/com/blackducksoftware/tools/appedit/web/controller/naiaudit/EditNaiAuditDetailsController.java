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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.AppEditConstants;
import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.NaiAuditNoChangeException;
import com.blackducksoftware.tools.appedit.naiaudit.inputvalidation.InputValidatorEditNaiAuditDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.NaiAuditUpdateStatus;
import com.blackducksoftware.tools.appedit.naiaudit.model.NaiAuditViewData;
import com.blackducksoftware.tools.appedit.naiaudit.model.RowUpdateResult;
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
	private final int currentDisplayedRowCount = INITIAL_DISPLAYED_ROW_COUNT;

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
	 * Handles Edit NAI Audit Details row submission.
	 *
	 * @param key
	 * @param status
	 * @param comment
	 * @return
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/editnaiauditdetails", method = RequestMethod.POST)
	public RowUpdateResult saveRow(@RequestParam final String key, @RequestParam final String status,
			@RequestParam final String comment) {
		logger.debug("saveRow(): " + key + ": Status: " + status + "; Comment: " + comment);
		AppCompVulnComposite updatedRowData;
		try {
			updatedRowData = updateVulnAndReturnNewRowData(key, status, comment);
		} catch (final NaiAuditNoChangeException e1) {
			final String msg = e1.getMessage();
			logger.error(msg);
			return new RowUpdateResult(NaiAuditUpdateStatus.UNCHANGED, msg, null);
		} catch (final AppEditException e) {
			final String msg = e.getMessage();
			logger.error(msg);
			return new RowUpdateResult(NaiAuditUpdateStatus.FAILED, msg, null);
		}
		return new RowUpdateResult(NaiAuditUpdateStatus.SUCCEEDED, "OK", updatedRowData);
	}

	private AppCompVulnComposite updateVulnAndReturnNewRowData(final String selectedRowKey, final String status,
			final String comment) throws AppEditException {
		logger.info("Selected vulnerability key: " + selectedRowKey);

		final AppCompVulnKey key = generateKey(selectedRowKey);
		if (key == null) {
			logger.debug("Skipping selected row key: " + selectedRowKey);
			return null;
		}
		validateInput(status, comment);
		final AppCompVulnComposite vuln = vulnNaiAuditDetailsService.getAppCompVulnComposite(key);
		final String currentUser = getUser();
		vuln.getAuditPart().setVulnerabilityNaiAuditStatus(status);
		vuln.getAuditPart().setVulnerabilityNaiAuditComment(comment);
		vuln.getAuditPart().setUsername(currentUser);

		final AppCompVulnComposite finalVuln = vulnNaiAuditDetailsService.updateVulnNaiAuditDetails(vuln);

		return finalVuln;
	}

	private void validateInput(final String status, final String comment) throws AppEditException {
		if (status.length() <= 0) {
			final String msg = "NAI Audit Status must have a value";
			throw new AppEditException(msg);
		}
		if (comment.length() > AppEditConstants.NAI_AUDIT_COMMENT_MAX_LENGTH) {
			final String msg = "The comment entered is too long. Maximum length is "
					+ AppEditConstants.NAI_AUDIT_COMMENT_MAX_LENGTH + " characters";
			throw new AppEditException(msg);
		}
		final InputValidatorEditNaiAuditDetails inputValidator = new InputValidatorEditNaiAuditDetails(config);
		if (!inputValidator.validateCommentValue(comment)) {
			final String msg = "The comment entered is invalid.";
			throw new AppEditException(msg);
		}
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

		logger.debug("Sending to view: " + vulnNaiAuditDetailsList.size() + " vulnerabilities");
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
}
