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
package com.blackducksoftware.tools.appedit.web.controller;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;

/**
 * An exception thrown from a controller.
 * 
 * @author sbillings
 *
 */
public class AppEditControllerException extends AppEditException {
    private static final long serialVersionUID = 2054672808081635193L;
    private final String returnValue;

    public AppEditControllerException(String returnValue, String message) {
	super(message);
	this.returnValue = returnValue;
    }

    public String getReturnValue() {
	return returnValue;
    }

}
