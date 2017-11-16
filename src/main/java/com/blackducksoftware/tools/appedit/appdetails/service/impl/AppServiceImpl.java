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
    public AppDetails loadFromId(String appId, boolean refreshCache)
	    throws Exception {
	return appDao.loadFromId(appId, refreshCache);
    }

    /**
     * Load an application's details by name.
     *
     * @param appName
     * @return
     * @throws Exception
     */
    @Override
    public AppDetails loadFromName(String appName, boolean refreshCache)
	    throws Exception {
	return appDao.loadFromName(appName, refreshCache);
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
