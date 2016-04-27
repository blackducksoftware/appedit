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
