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
package com.blackducksoftware.tools.appedit.appdetails.service;

import com.blackducksoftware.tools.appedit.appdetails.model.AppDetails;
import com.blackducksoftware.tools.appedit.appdetails.model.ViewAppBean;
import com.blackducksoftware.tools.connector.codecenter.attribute.AttributeDefinitionPojo;

/**
 * Application Services
 * 
 * @author sbillings
 *
 */
public interface AppService {
    /**
     * Attempt to authorize the given user.
     *
     * @param appId
     * @param username
     * @return
     */
    boolean authorizeUser(String appId, String username);

    /**
     * Load an application's details by ID.
     *
     * @param appId
     * @return
     * @throws Exception
     */
    AppDetails loadFromId(String appId, boolean refreshCache) throws Exception;

    /**
     * Load an application's details by name.
     *
     * @param appName
     * @return
     * @throws Exception
     */
    AppDetails loadFromName(String appName, boolean refreshCache)
	    throws Exception;

    /**
     * Update an application. Both the application to update and the changes to
     * make are specified in the argument.
     *
     * @param app
     * @throws Exception
     */
    void update(AppDetails app) throws Exception;

    /**
     * Get attribute definition by name.
     * 
     * @param attrName
     * @return
     * @throws Exception
     */
    AttributeDefinitionPojo getAttributeDefinitionByName(String attrName)
	    throws Exception;

    /**
     * Create a ViewAppBean from an AppDetails bean.
     * 
     * @param appDetails
     * @return
     */
    ViewAppBean createViewAppBean(AppDetails appDetails);
}
