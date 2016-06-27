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