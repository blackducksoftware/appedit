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
package com.blackducksoftware.tools.appedit.mocks;

import java.util.List;

import com.blackducksoftware.tools.commonframework.core.config.ConfigurationManager;
import com.blackducksoftware.tools.commonframework.standard.common.ProjectPojo;
import com.blackducksoftware.tools.connector.codecenter.CodeCenterAPIWrapper;
import com.blackducksoftware.tools.connector.codecenter.ICodeCenterServerWrapper;
import com.blackducksoftware.tools.connector.codecenter.application.IApplicationManager;
import com.blackducksoftware.tools.connector.codecenter.attribute.IAttributeDefinitionManager;
import com.blackducksoftware.tools.connector.codecenter.component.ICodeCenterComponentManager;
import com.blackducksoftware.tools.connector.codecenter.externalId.IExternalIdManager;
import com.blackducksoftware.tools.connector.codecenter.protexservers.IProtexServerManager;
import com.blackducksoftware.tools.connector.codecenter.request.IRequestManager;
import com.blackducksoftware.tools.connector.codecenter.user.ICodeCenterUserManager;
import com.blackducksoftware.tools.connector.common.ILicenseManager;
import com.blackducksoftware.tools.connector.common.LicensePojo;

public class MockCodeCenterServerWrapper implements ICodeCenterServerWrapper {

    @Override
    public ProjectPojo getProjectByName(String projectName) throws Exception {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ProjectPojo getProjectByID(String projectID) throws Exception {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public <T> List<T> getProjects(Class<T> classType) throws Exception {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ConfigurationManager getConfigManager() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public CodeCenterAPIWrapper getInternalApiWrapper() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public IApplicationManager getApplicationManager() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public IExternalIdManager getExternalIdManager() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public IAttributeDefinitionManager getAttributeDefinitionManager() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ILicenseManager<LicensePojo> getLicenseManager() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public IProtexServerManager getProtexServerManager() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ICodeCenterComponentManager getComponentManager() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ICodeCenterUserManager getUserManager() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public IRequestManager getRequestManager() {
	// TODO Auto-generated method stub
	return null;
    }

}
