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
package com.blackducksoftware.tools.appedit.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller for login requests (resulting for not-yet-authenticated requests
 * for a password-protected page). Tells Spring to display the login page.
 *
 * @author sbillings
 *
 */
@Controller
public class LoginRequestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());

    /**
     * Handle request for login requests (resulting for not-yet-authenticated
     * requests for a password-protected page). Tells Spring to display the
     * login page.
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/loginRequest", method = RequestMethod.GET)
    public String loginRequest(WebRequest request, Model model) {
	logger.info("LoginRequestController.loginRequest() called");
	return "loginForm";
    }
}
