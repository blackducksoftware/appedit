package com.blackducksoftware.tools.appedit.appdetails.service.impl;

import javax.inject.Inject;

import com.blackducksoftware.tools.appedit.appdetails.dao.AppDao;
import com.blackducksoftware.tools.appedit.appdetails.model.AppDetails;
import com.blackducksoftware.tools.appedit.appdetails.service.AppService;
import com.blackducksoftware.tools.connector.codecenter.attribute.AttributeDefinitionPojo;

public class AppServiceImpl implements AppService {

    private AppDao appDao;

    @Inject
    public void setAppDao(AppDao appDao) {
	this.appDao = appDao;
    }

    @Override
    public boolean authorizeUser(String appId, String username) {
	return appDao.authorizeUser(appId, username);
    }

    @Override
    public AppDetails loadFromId(String appId) throws Exception {
	return appDao.loadFromId(appId);
    }

    @Override
    public AppDetails loadFromName(String appName) throws Exception {
	return appDao.loadFromName(appName);
    }

    @Override
    public void update(AppDetails app) throws Exception {
	appDao.update(app);
    }

    @Override
    public AttributeDefinitionPojo getAttributeDefinitionByName(String attrName)
	    throws Exception {
	return appDao.getAttributeDefinitionByName(attrName);
    }

}
