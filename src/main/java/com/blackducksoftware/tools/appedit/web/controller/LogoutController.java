package com.blackducksoftware.tools.appedit.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Logout controller
 * 
 * @author sbillings
 *
 */
@Controller
public class LogoutController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request,
	    HttpServletResponse response) {
	logger.info("Logging out.");
	Authentication auth = SecurityContextHolder.getContext()
		.getAuthentication();
	if (auth != null) {
	    new SecurityContextLogoutHandler().logout(request, response, auth);
	}
	return "redirect:/loginRequest?logout";
    }
}
