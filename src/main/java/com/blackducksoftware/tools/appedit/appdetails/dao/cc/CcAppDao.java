/**
 * Application Details Edit Webapp
 *
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.blackducksoftware.tools.appedit.appdetails.dao.cc;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.sdk.codecenter.application.data.ApplicationIdToken;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.tools.appedit.appdetails.dao.AppDao;
import com.blackducksoftware.tools.appedit.appdetails.model.AppDetails;
import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.exception.AuthenticationException;
import com.blackducksoftware.tools.appedit.core.model.AuthenticationResult;
import com.blackducksoftware.tools.appedit.core.model.Role;
import com.blackducksoftware.tools.commonframework.core.exception.CommonFrameworkException;
import com.blackducksoftware.tools.connector.codecenter.CodeCenterServerWrapper;
import com.blackducksoftware.tools.connector.codecenter.ICodeCenterServerWrapper;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationUserPojo;
import com.blackducksoftware.tools.connector.codecenter.attribute.AttributeDefinitionPojo;
import com.blackducksoftware.tools.connector.codecenter.common.AttributeValuePojo;
import com.blackducksoftware.tools.connector.codecenter.user.CodeCenterUserPojo;

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
     * Authorize and returns user.
     */
    public CodeCenterUserPojo getUser(final String username) throws AuthenticationException {
		// Authorize by performing an operation this user should be able to do
		logger.debug("getUser()");
		CodeCenterUserPojo user;
		try {
			user = ccsw.getUserManager().getUserByName(username);
		} catch (final CommonFrameworkException e) {
			final String message = "Authorization failed: " + e.getMessage();
			final AuthenticationResult authResult = new AuthenticationResult(null,
					null, false, message, Role.ROLE_NONE);
			throw new AuthenticationException(authResult, message);
		} catch (final SOAPFaultException e) {
			final String message = "Authorization failed: " + e.getMessage();
			final AuthenticationResult authResult = new AuthenticationResult(null,
					null, false, message, Role.ROLE_NONE);
			throw new AuthenticationException(authResult, message);
		}
		return user;
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
    public AppDetails loadFromName(String appName, boolean refreshCache)
	    throws Exception {
	if (refreshCache) {
	    ccsw.getApplicationManager()
		    .removeApplicationFromCacheByNameVersion(appName,
			    config.getAppVersion());
	}
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
    public AppDetails loadFromId(String appId, boolean refreshCache)
	    throws Exception {
	if (refreshCache) {
	    ccsw.getApplicationManager().removeApplicationFromCacheById(appId);
	}
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
	logger.info("CcAppDao.update called for app: " + app);

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

    /**
     * Get attribute definition by name.
     */
    @Override
    public AttributeDefinitionPojo getAttributeDefinitionByName(String attrName)
	    throws Exception {

	return ccsw.getAttributeDefinitionManager()
		.getAttributeDefinitionByName(attrName);
    }

}
