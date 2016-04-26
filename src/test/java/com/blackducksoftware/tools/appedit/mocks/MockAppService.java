package com.blackducksoftware.tools.appedit.mocks;

import java.util.ArrayList;
import java.util.List;

import com.blackducksoftware.tools.appedit.appdetails.model.AppDetails;
import com.blackducksoftware.tools.appedit.appdetails.model.ViewAppBean;
import com.blackducksoftware.tools.appedit.appdetails.service.AppService;
import com.blackducksoftware.tools.connector.codecenter.attribute.AttributeDefinitionPojo;
import com.blackducksoftware.tools.connector.codecenter.common.AttributeValuePojo;

public class MockAppService implements AppService {

    @Override
    public boolean authorizeUser(String appId, String username) {
	if ("notAuthorizedUserName".equals(username)) {
	    return false;
	}
	return true;
    }

    @Override
    public AppDetails loadFromId(String appId) throws Exception {
	if ("bogus".equals(appId)) {
	    return null;
	}

	if ("throwException".equals(appId)) {
	    throw new Exception("Mock: error loading app");
	}

	AppDetails app = new AppDetails(appId, appId);
	AttributeValuePojo value = new AttributeValuePojo("attr1Id",
		"attr1Name", "attr1InitialValue");
	app.addCustomAttributeValue(value.getName(), value);

	return app;
    }

    @Override
    public AppDetails loadFromName(String appName) throws Exception {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void update(AppDetails app) throws Exception {
	// TODO Auto-generated method stub

    }

    @Override
    public AttributeDefinitionPojo getAttributeDefinitionByName(String attrName)
	    throws Exception {

	if ("bogus".equals(attrName)) {
	    throw new Exception("mock: bad attr name");
	}

	return new AttributeDefinitionPojo(attrName, attrName, "Textfield", "",
		"Enter something");
    }

    @Override
    public ViewAppBean createViewAppBean(AppDetails appDetails) {

	ViewAppBean viewAppBean = new ViewAppBean();
	viewAppBean.setAppId(appDetails.getAppId());
	viewAppBean.setAppName(appDetails.getAppName());
	List<String> attrNames = new ArrayList<>();
	attrNames.add("attr1Name");
	viewAppBean.setAttrNames(attrNames);
	List<AttributeValuePojo> attrValues = new ArrayList<>();
	attrValues.add(new AttributeValuePojo("attr1Id", "attr1Name",
		"attr1Value"));
	viewAppBean.setAttrValues(attrValues);
	return viewAppBean;
    }

}
