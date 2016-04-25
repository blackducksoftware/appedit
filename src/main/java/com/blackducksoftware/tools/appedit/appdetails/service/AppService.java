package com.blackducksoftware.tools.appedit.appdetails.service;

import com.blackducksoftware.tools.appedit.appdetails.model.AppDetails;
import com.blackducksoftware.tools.appedit.appdetails.model.ViewAppBean;
import com.blackducksoftware.tools.connector.codecenter.attribute.AttributeDefinitionPojo;

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
    AppDetails loadFromId(String appId) throws Exception;

    /**
     * Load an application's details by name.
     *
     * @param appName
     * @return
     * @throws Exception
     */
    AppDetails loadFromName(String appName) throws Exception;

    /**
     * Update an application. Both the application to update and the changes to
     * make are specified in the argument.
     *
     * @param app
     * @throws Exception
     */
    void update(AppDetails app) throws Exception;

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
