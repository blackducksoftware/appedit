/*******************************************************************************
 * Copyright (C) 2015 Black Duck Software, Inc.
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
package com.blackducksoftware.tools.appedit.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.blackducksoftware.tools.appedit.codecenter.CcAppDao;
import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.AppEditConstants;
import com.blackducksoftware.tools.appedit.core.ViewAppBean;
import com.blackducksoftware.tools.appedit.core.application.AppDao;
import com.blackducksoftware.tools.appedit.core.application.AppDetails;
import com.blackducksoftware.tools.appedit.core.application.AppDetailsBeanConverter;
import com.blackducksoftware.tools.appedit.core.application.InputValidatorEditAppDetails;

/**
 * Controller for requests for and form submissions from the Edit App Details
 * screen.
 *
 * @author sbillings
 *
 */
@Controller
@SessionAttributes({ "app", "dataSource" })
public class EditAppDetailsController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass()
            .getName());

    /**
     * Handles requests for the Edit Application Details page for a given appId.
     * Loads app from Code Center.
     */
    @RequestMapping(value = "/editappdetails", method = RequestMethod.GET)
    public String showEditForm(WebRequest request, Model model) {
        logger.info("Rendering Edit Application Details page.");

        String configFilename = System.getProperty("user.home") + "/"
                + AppEditConstants.CONFIG_FILENAME;
        AppEditConfigManager config = null;
        try {
            config = new AppEditConfigManager(configFilename);
        } catch (Exception e) {
            String msg = "Error constructing configuration manager: "
                    + e.getMessage();
            logger.error(msg);
            model.addAttribute("message", msg);
            return "error/programError";
        }

        AppDao appDao = null;
        try {
            appDao = new CcAppDao(config);
        } catch (Exception e) {
            String msg = "Error constructing data source: " + e.getMessage();
            logger.error(msg);
            model.addAttribute("message", msg);
            return "error/programError";
        }

        String appId = request.getParameter("appId");
        String appName = request.getParameter("appName");
        if ((appId == null) && (appName == null)) {
            String msg = "The URL must specify either appId or appName.";
            logger.error(msg);
            model.addAttribute("message", msg);
            return "error/programError";
        }

        AppDetails appDetails = null;
        try {
            // load app from Code Center using whatever info we were given (try
            // ID first)
            if (appId != null) {
                appDetails = appDao.loadFromId(appId);
            } else {
                appDetails = appDao.loadFromName(appName);
            }
        } catch (Exception e) {
            String msg = "Error loading application: " + e.getMessage();
            logger.error(msg);
            model.addAttribute("message", msg);
            return "error/programError";
        }

        // Get the logged-in user's details
        String username = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        // Make sure they are on this app's team (list of users that can access
        // it)
        boolean isAuthorized = appDao.authorizeUser(appDetails.getAppId(),
                username);
        if (!isAuthorized) {
            String msg = "You are not authorized to access this application";
            logger.error(msg);
            model.addAttribute("message", msg);
            return "error/programError";
        }

        // Convert the generic appDetails object to view-friendly appDetails
        // object
        AppDetailsBeanConverter converter = new AppDetailsBeanConverter(config);
        ViewAppBean app = converter.createViewAppBean(appDetails);

        model.addAttribute("app", app);
        model.addAttribute("dataSource", appDao);

        return "editAppDetailsForm";
    }

    /**
     * Handles Edit App Details form submissions. Updates app in Code Center.
     */
    @RequestMapping(value = "/editappdetails", method = RequestMethod.POST)
    public String updateApp(@ModelAttribute("app") ViewAppBean app,
            @ModelAttribute("dataSource") AppDao dataSource,
            @RequestParam String action, ModelMap model) {

        logger.info("EditAppDetails.updateApp(): app: " + app
                + "; dataSource: " + dataSource);

        // Load config
        String configFilename = System.getProperty("user.home") + "/"
                + AppEditConstants.CONFIG_FILENAME;
        AppEditConfigManager config = null;
        try {
            config = new AppEditConfigManager(configFilename);
        } catch (Exception e) {
            String msg = "Error constructing configuration manager: "
                    + e.getMessage();
            logger.error(msg);
            model.addAttribute("message", msg);
            return "error/programError";
        }

        // Validate input
        int i = 0;
        for (String attrLabel : app.getAttrNames()) {
            InputValidatorEditAppDetails inputValidator = new InputValidatorEditAppDetails(
                    config);
            if (!inputValidator.validateAttributeValue(attrLabel, app
                    .getAttrValues().get(i++).getValue())) {
                String msg = "The value of " + attrLabel + " is invalid.";
                logger.error(msg);
                model.addAttribute("message", msg);
                return "error/programError";
            }
        }

        // Convert the View-friendly appDetails to a generic appDetails object
        AppDetailsBeanConverter converter = new AppDetailsBeanConverter(config);
        AppDetails appDetails = converter.createAppDetails(app);

        // Get the logged-in user's details
        String username = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        // Make sure they are on this app's team (list of users that can access
        // it)
        boolean isAuthorized = dataSource.authorizeUser(appDetails.getAppId(),
                username);
        if (!isAuthorized) {
            String msg = "You are not authorized to access this application";
            logger.error(msg);
            model.addAttribute("message", msg);
            return "error/programError";
        }

        try {
            dataSource.update(appDetails); // update app in Code Center
        } catch (Exception e) {
            String msg = "Error updating application " + app.getAppName()
                    + ": " + e.getMessage();
            logger.error(msg);
            model.addAttribute("message", msg);
            return "error/programError";
        }

        return "editAppDetailsResult";
    }
}
