/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
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
package com.blackducksoftware.tools.appedit.appdetails.service;

import com.blackducksoftware.tools.appedit.appdetails.model.AppDetails;
import com.blackducksoftware.tools.appedit.appdetails.model.ViewAppBean;

/**
 * Converts AppDetails bean to/from ViewAppBean
 * 
 * @author sbillings
 *
 */
public interface AppDetailsBeanConverter {

    /**
     * Create a View-friendly application bean from a Code Center-centric
     * application bean.
     *
     * @param appDetails
     * @return
     */
    public abstract ViewAppBean createViewAppBean(AppDetails appDetails);

    /**
     * Create a Code Center-centric application bean from a View-friendly
     * application bean.
     *
     * @param viewAppBean
     * @return
     */
    public abstract AppDetails createAppDetails(ViewAppBean viewAppBean);

}