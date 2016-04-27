/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
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