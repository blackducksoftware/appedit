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
