package com.blackducksoftware.tools.appedit.web.controller.naiaudit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AjaxController {

	@ResponseBody
	@RequestMapping(value = "/xxx", method = RequestMethod.POST)
	public String naiAuditRowSave(@RequestParam final String key, @RequestParam final String status,
			@RequestParam final String comment) {
		System.out.println("naiAuditRowSave(): " + key + ": Status: " + status + "; Comment: " + comment);
		return "OK";
	}

}
