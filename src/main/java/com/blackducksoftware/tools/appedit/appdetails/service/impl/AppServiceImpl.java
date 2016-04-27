/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.blackducksoftware.tools.appedit.appdetails.service.impl;

import javax.inject.Inject;

import com.blackducksoftware.tools.appedit.appdetails.dao.AppDao;
import com.blackducksoftware.tools.appedit.appdetails.model.AppDetails;
import com.blackducksoftware.tools.appedit.appdetails.model.ViewAppBean;
import com.blackducksoftware.tools.appedit.appdetails.service.AppDetailsBeanConverter;
import com.blackducksoftware.tools.appedit.appdetails.service.AppService;
import com.blackducksoftware.tools.connector.codecenter.attribute.AttributeDefinitionPojo;

/**
 * Application Services
 * 
 * @author sbillings
 *
 */
public class AppServiceImpl implements AppService {

    private AppDao appDao;

    @Inject
    public void setAppDao(AppDao appDao) {
	this.appDao = appDao;
    }

    private AppDetailsBeanConverter appDetailsBeanConverter;

    @Inject
    public void setAppDetailsBeanConverter(
	    AppDetailsBeanConverter appDetailsBeanConverter) {
	this.appDetailsBeanConverter = appDetailsBeanConverter;
    }

    /**
     * Attempt to authorize the given user.
     *
     * @param appId
     * @param username
     * @return
     */
    @Override
    public boolean authorizeUser(String appId, String username) {
	return appDao.authorizeUser(appId, username);
    }

    /**
     * Load an application's details by ID.
     *
     * @param appId
     * @return
     * @throws Exception
     */
    @Override
    public AppDetails loadFromId(String appId) throws Exception {
	return appDao.loadFromId(appId);
    }

    /**
     * Load an application's details by name.
     *
     * @param appName
     * @return
     * @throws Exception
     */
    @Override
    public AppDetails loadFromName(String appName) throws Exception {
	return appDao.loadFromName(appName);
    }

    /**
     * Update an application. Both the application to update and the changes to
     * make are specified in the argument.
     *
     * @param app
     * @throws Exception
     */
    @Override
    public void update(AppDetails app) throws Exception {
	appDao.update(app);
    }

    /**
     * Get attribute definition by name.
     * 
     * @param attrName
     * @return
     * @throws Exception
     */
    @Override
    public AttributeDefinitionPojo getAttributeDefinitionByName(String attrName)
	    throws Exception {
	return appDao.getAttributeDefinitionByName(attrName);
    }

    /**
     * Create a ViewAppBean from an AppDetails bean.
     * 
     * @param appDetails
     * @return
     */
    @Override
    public ViewAppBean createViewAppBean(AppDetails appDetails) {
	return appDetailsBeanConverter.createViewAppBean(appDetails);
    }

}
