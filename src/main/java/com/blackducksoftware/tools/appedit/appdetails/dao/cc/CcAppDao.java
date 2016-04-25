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
package com.blackducksoftware.tools.appedit.appdetails.dao.cc;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.sdk.codecenter.application.data.ApplicationIdToken;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.tools.appedit.appdetails.dao.AppDao;
import com.blackducksoftware.tools.appedit.appdetails.model.AppDetails;
import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.commonframework.core.exception.CommonFrameworkException;
import com.blackducksoftware.tools.connector.codecenter.CodeCenterServerWrapper;
import com.blackducksoftware.tools.connector.codecenter.ICodeCenterServerWrapper;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationUserPojo;
import com.blackducksoftware.tools.connector.codecenter.attribute.AttributeDefinitionPojo;
import com.blackducksoftware.tools.connector.codecenter.common.AttributeValuePojo;

/**
 * Loads AppDetails data from Code Center / Updates Code Center with data from a
 * given AppDetails. Construct a new one for each app (don't re-use one CcAppDao
 * instance for a different app).
 *
 * This class is (now) immutable. AbstractAttribute's used to be cached in these
 * objects, avoiding (in some cases) the need to fetch them from CC during
 * update, but making this class mutable, necessitating that these objects never
 * be reused for a different app. The safety gained by making this class
 * immutable seems worth the negligible performance hit from not caching them.
 *
 * @author sbillings
 *
 */
public class CcAppDao implements AppDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());

    private AppEditConfigManager config;

    @Inject
    public void setConfig(AppEditConfigManager config) {
	this.config = config;
    }

    private ICodeCenterServerWrapper ccsw;

    @Inject
    public void setCcsw(ICodeCenterServerWrapper ccsw) {
	this.ccsw = ccsw;
    }

    public CcAppDao() throws Exception {
	logger.debug("Default constructor called");
    }

    public CcAppDao(AppEditConfigManager config) throws Exception {
	logger.debug("Constructor passed config; creating CodeCenterServerWrapper");
	this.config = config;
	ccsw = new CodeCenterServerWrapper(config);
    }

    /**
     * Returns true if the given user is authorized to access the application
     * specified by the given appId.
     */
    @Override
    public boolean authorizeUser(String appId, String username) {
	logger.debug("Verifying that user " + username
		+ " has access to app ID " + appId);
	ApplicationIdToken appIdToken = new ApplicationIdToken();
	appIdToken.setId(appId);

	try {
	    List<ApplicationUserPojo> roles = ccsw.getApplicationManager()
		    .getAllUsersAssignedToApplication(appId);
	    for (ApplicationUserPojo role : roles) {
		logger.debug("Found a role for user: " + role.getUserName()
			+ ": " + role.getRoleName());

		if (username.equals(role.getUserName())) {
		    logger.info("Access by user " + username + " to app ID "
			    + appId + " is granted");
		    return true;
		}
	    }
	    logger.warn("Access by user " + username + " to app ID " + appId
		    + " is denied: User is not assigned to application's team.");
	    return false;
	} catch (CommonFrameworkException e) {
	    logger.error("Error retrieving application roles: "
		    + e.getMessage());
	    return false;
	}
    }

    /**
     * Get the details of an application by name.
     */
    @Override
    public AppDetails loadFromName(String appName) throws Exception {

	ApplicationNameVersionToken appNameToken = new ApplicationNameVersionToken();
	appNameToken.setName(appName);
	appNameToken.setVersion(config.getAppVersion());
	ApplicationPojo app = ccsw.getApplicationManager()
		.getApplicationByNameVersion(appName, config.getAppVersion());
	AppDetails appDetails = deriveAppDetails(app);

	return appDetails;
    }

    /**
     * Get the details of an application by ID.
     */
    @Override
    public AppDetails loadFromId(String appId) throws Exception {

	ApplicationPojo app = ccsw.getApplicationManager().getApplicationById(
		appId);
	AppDetails appDetails = deriveAppDetails(app);

	return appDetails;
    }

    private AppDetails deriveAppDetails(ApplicationPojo app) throws Exception {
	AppDetails appDetails = new AppDetails(app.getId(), app.getName());

	// Look through all custom attrs for this app, and add to appDetails any
	// attrs (and their values) that the config says we care about
	// This pulls any existing values from Code Center.
	Map<String, AttributeValuePojo> attrValues = app
		.getAttributeValuesByName();
	for (String attrName : attrValues.keySet()) {
	    AttributeValuePojo attrValue = attrValues.get(attrName);

	    if (config.getCcAttributeNames().contains(attrName)) {
		appDetails.addCustomAttributeValue(attrName, attrValue);
	    }
	}
	return appDetails;
    }

    /**
     * Update an application. The application to update and the changes to make
     * are both indicated in the argument.
     */
    @Override
    public void update(AppDetails app) throws Exception {
	logger.info("CcDataSource.update called for app: " + app);

	Set<AttributeValuePojo> changedAttrValues = new TreeSet<>();
	for (String attrName : config.getCcAttributeNames()) {
	    logger.debug("Will update attribute " + attrName);
	    changedAttrValues.add(app.getCustomAttributeValue(attrName));
	}

	try {
	    ccsw.getApplicationManager().updateAttributeValues(app.getAppId(),
		    changedAttrValues);
	} catch (CommonFrameworkException e) {
	    throw new Exception("Error updating app " + app.getAppName() + ": "
		    + e.getMessage());
	}
    }

    @Override
    public String toString() {
	return "CcDataSource [customAttributeNames="
		+ config.getCcAttributeNames() + "]";
    }

    @Override
    public AttributeDefinitionPojo getAttributeDefinitionByName(String attrName)
	    throws Exception {

	return ccsw.getAttributeDefinitionManager()
		.getAttributeDefinitionByName(attrName);
    }

}
