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
