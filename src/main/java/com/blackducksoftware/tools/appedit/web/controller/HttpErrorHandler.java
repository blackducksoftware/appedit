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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * HTTP Error handler. Directs various errors to the appropriate JSP page.
 *
 * @author sbillings
 *
 */
@Controller
public class HttpErrorHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());

    @RequestMapping(value = "/error/400")
    public String error400() {
	logger.warn("Handling 400 error");
	return "error/400";
    }

    @RequestMapping(value = "/error/404")
    public String error404() {
	logger.warn("Handling 404 error");
	return "error/404";
    }

    @RequestMapping(value = "/error/403")
    public String error403() {
	logger.warn("Handling 403 error");
	return "error/403";
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllException(Exception ex) {
	logger.warn("Handling exception");
	ModelAndView model = new ModelAndView("error/exception_error");
	return model;

    }

}
