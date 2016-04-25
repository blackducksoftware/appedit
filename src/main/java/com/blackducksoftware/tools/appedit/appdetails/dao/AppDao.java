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
package com.blackducksoftware.tools.appedit.appdetails.dao;

import com.blackducksoftware.tools.appedit.appdetails.model.AppDetails;
import com.blackducksoftware.tools.connector.codecenter.attribute.AttributeDefinitionPojo;

/**
 * Application Data Access Object (DAO).
 *
 * @author sbillings
 *
 */
public interface AppDao {

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

}
