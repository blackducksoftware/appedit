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
package com.blackducksoftware.tools.appedit.codecenter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.sdk.codecenter.application.data.Application;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationIdToken;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationNameVersionToken;
import com.blackducksoftware.sdk.codecenter.application.data.ApplicationUpdate;
import com.blackducksoftware.sdk.codecenter.attribute.data.AbstractAttribute;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeNameOrIdToken;
import com.blackducksoftware.sdk.codecenter.attribute.data.AttributeNameToken;
import com.blackducksoftware.sdk.codecenter.common.data.AttributeValue;
import com.blackducksoftware.sdk.codecenter.fault.SdkFault;
import com.blackducksoftware.sdk.codecenter.role.data.ApplicationRoleAssignment;
import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.application.AppDao;
import com.blackducksoftware.tools.appedit.core.application.AppDetails;
import com.blackducksoftware.tools.connector.codecenter.CodeCenterServerWrapper;

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

    private final AppEditConfigManager config;

    private final CodeCenterServerWrapper ccsw;

    public CcAppDao(AppEditConfigManager config) throws Exception {
        this.config = config;
        ccsw = new CodeCenterServerWrapper(config);
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
            List<ApplicationRoleAssignment> roles = ccsw
                    .getInternalApiWrapper().getProxy().getRoleApi()
                    .getApplicationRoles(appIdToken);
            for (ApplicationRoleAssignment role : roles) {
                logger.debug("Found a role for user: "
                        + role.getUserNameToken().getName() + ": "
                        + role.getRoleNameToken().getName());

                if (username.equals(role.getUserNameToken().getName())) {
                    logger.info("Access by user " + username + " to app ID "
                            + appId + " is granted");
                    return true;
                }
            }
            logger.warn("Access by user " + username + " to app ID " + appId
                    + " is denied: User is not assigned to application's team.");
            return false;
        } catch (SdkFault e) {
            logger.error("Error retrieving application roles: "
                    + e.getMessage());
            return false;
        }
    }

    /**
     * Get the details of an application by name.
     */
    @Override
    public AppDetails loadFromName(String appName) throws Exception {

        ApplicationNameVersionToken appNameToken = new ApplicationNameVersionToken();
        appNameToken.setName(appName);
        appNameToken.setVersion(config.getAppVersion());
        Application app = ccsw.getInternalApiWrapper().getProxy()
                .getApplicationApi().getApplication(appNameToken);
        AppDetails appDetails = deriveAppDetails(app);

        return appDetails;
    }

    /**
     * Get the details of an application by ID.
     */
    @Override
    public AppDetails loadFromId(String appId) throws Exception {

        ApplicationIdToken appIdToken = new ApplicationIdToken();
        appIdToken.setId(appId);
        Application app = ccsw.getInternalApiWrapper().getProxy()
                .getApplicationApi().getApplication(appIdToken);
        AppDetails appDetails = deriveAppDetails(app);

        return appDetails;
    }

    private AppDetails deriveAppDetails(Application app) throws Exception {
        AppDetails appDetails = new AppDetails(app.getId().getId(),
                app.getName());

        // Look through all custom attrs for this app, and add to appDetails any
        // attrs (and their values) that the config says we care about
        // This pulls any existing values from Code Center.
        List<AttributeValue> attrValues = app.getAttributeValues();
        for (AttributeValue attrValue : attrValues) {
            AttributeNameOrIdToken attrToken = attrValue.getAttributeId();
            AbstractAttribute attrDef = ccsw.getInternalApiWrapper().getProxy()
                    .getAttributeApi().getAttribute(attrToken);

            String attrName = attrDef.getName();
            if (config.getCcAttributeNames().contains(attrName)) {
                String attrValueString = "";
                if (attrValue.getValues().size() > 0) {
                    attrValueString = attrValue.getValues().get(0);
                }
                appDetails.addCustomAttributeValue(attrName, attrValueString);
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
        logger.info("CcDataSource.update called for app: " + app);

        ApplicationUpdate appUpdate = new ApplicationUpdate();

        ApplicationIdToken appIdToken = new ApplicationIdToken();
        appIdToken.setId(app.getAppId());
        appUpdate.setId(appIdToken);

        for (String attrName : config.getCcAttributeNames()) {
            // AbstractAttribute's used to be cached, avoiding (in some
            // cases) the need to fetch them from CC here, but making this class
            // mutable. The negligible performance benefit does not
            // seem worth the risk. Without that caching, this class
            // is now immutable.

            logger.info("Looking up attribute definition");
            AttributeNameToken attrToken = new AttributeNameToken();
            attrToken.setName(attrName);
            AbstractAttribute attrDef = ccsw.getInternalApiWrapper().getProxy()
                    .getAttributeApi().getAttribute(attrToken);

            logger.debug("attr type: " + attrDef.getName());
            logger.debug("attr type: " + attrDef.getAttrType());

            AttributeValue attrValueObject = new AttributeValue();
            attrValueObject.setAttributeId(attrDef.getId());
            attrValueObject.getValues().add(
                    app.getCustomAttributeValue(attrName));

            logger.info("Setting attribute " + attrName + " to "
                    + app.getCustomAttributeValue(attrName));
            appUpdate.getAttributeValues().add(attrValueObject);
        }

        try {
            ccsw.getInternalApiWrapper().getApplicationApi()
                    .updateApplication(appUpdate);
        } catch (SdkFault e) {
            throw new Exception("Error updating app " + app.getAppName() + ": "
                    + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "CcDataSource [customAttributeNames="
                + config.getCcAttributeNames() + "]";
    }

}
