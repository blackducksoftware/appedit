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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.blackducksoftware.tools.appedit.appdetails.model.ViewAppBean;
import com.blackducksoftware.tools.appedit.appdetails.service.AppService;
import com.blackducksoftware.tools.appedit.appdetails.service.impl.AppDetailsBeanConverterImpl;
import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.mocks.MockAppService;
import com.blackducksoftware.tools.appedit.web.controller.appdetails.EditAppDetailsController;
import com.blackducksoftware.tools.connector.codecenter.common.AttributeValuePojo;

public class EditAppDetailsControllerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void testGetEndUserValidRequest() throws Exception {
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	AppService appService = new MockAppService();

	AppDetailsBeanConverterImpl appDetailsBeanConverter = new AppDetailsBeanConverterImpl();
	appDetailsBeanConverter.setConfig(config);

	EditAppDetailsController controller = new EditAppDetailsController();
	controller.setConfig(config);
	controller.setAppDetailsBeanConverter(appDetailsBeanConverter);
	controller.setAppService(appService);

	MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
		"GET", "/editappdetails");

	httpServletRequest.setParameter("appId", "app1Id");
	WebRequest webRequest = new ServletWebRequest(httpServletRequest);
	Model model = new ExtendedModelMap();

	List<GrantedAuthority> authorities = new ArrayList<>();
	authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	Authentication auth = new TestingAuthenticationToken("testUserName",
		null, authorities);
	SecurityContextHolder.getContext().setAuthentication(auth);

	// Test

	String returnValue = controller.showEditForm(webRequest, model);

	// Verify

	assertEquals("editAppDetailsForm", returnValue);
	String message = (String) model.asMap().get("message");
	assertEquals(null, message);

	ViewAppBean app = (ViewAppBean) model.asMap().get("app");

	assertEquals("app1Id", app.getAppId());
	assertEquals("app1Id", app.getAppName());
	assertEquals("attr1Name", app.getAttrNames().get(0));
	assertEquals("attr1Value", app.getAttrValues().get(0).getValue());

    }

    @Test
    public void testGetAuditor() throws Exception {
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	AppService appService = new MockAppService();

	AppDetailsBeanConverterImpl appDetailsBeanConverter = new AppDetailsBeanConverterImpl();
	appDetailsBeanConverter.setConfig(config);

	EditAppDetailsController controller = new EditAppDetailsController();
	controller.setConfig(config);
	controller.setAppDetailsBeanConverter(appDetailsBeanConverter);
	controller.setAppService(appService);

	MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
		"GET", "/editappdetails");

	httpServletRequest.setParameter("appId", "app1Id");
	WebRequest webRequest = new ServletWebRequest(httpServletRequest);
	Model model = new ExtendedModelMap();

	List<GrantedAuthority> authorities = new ArrayList<>();
	authorities.add(new SimpleGrantedAuthority("ROLE_AUDITOR"));
	Authentication auth = new TestingAuthenticationToken("testUserName",
		null, authorities);
	SecurityContextHolder.getContext().setAuthentication(auth);

	// Test

	String returnValue = controller.showEditForm(webRequest, model);

	// Verify

	assertEquals("redirect:editnaiauditdetails?appId=app1Id", returnValue);
	String message = (String) model.asMap().get("message");
	assertEquals(null, message);

    }

    @Test
    public void testGetEndUserNotAuthorizedOnApp() throws Exception {
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	AppService appService = new MockAppService();

	AppDetailsBeanConverterImpl appDetailsBeanConverter = new AppDetailsBeanConverterImpl();
	appDetailsBeanConverter.setConfig(config);

	EditAppDetailsController controller = new EditAppDetailsController();
	controller.setConfig(config);
	controller.setAppDetailsBeanConverter(appDetailsBeanConverter);
	controller.setAppService(appService);

	MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
		"GET", "/editappdetails");

	httpServletRequest.setParameter("appId", "app1Id");
	WebRequest webRequest = new ServletWebRequest(httpServletRequest);
	Model model = new ExtendedModelMap();

	List<GrantedAuthority> authorities = new ArrayList<>();
	authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	Authentication auth = new TestingAuthenticationToken(
		"notAuthorizedUserName", null, authorities);
	SecurityContextHolder.getContext().setAuthentication(auth);

	// Test

	String returnValue = controller.showEditForm(webRequest, model);

	// Verify

	assertEquals("error/programError", returnValue);
	String message = (String) model.asMap().get("message");
	assertEquals("You are not authorized to access this application",
		message);
    }

    @Test
    public void testGetEndUserAppNotSpecified() throws Exception {
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	AppService appService = new MockAppService();

	AppDetailsBeanConverterImpl appDetailsBeanConverter = new AppDetailsBeanConverterImpl();
	appDetailsBeanConverter.setConfig(config);

	EditAppDetailsController controller = new EditAppDetailsController();
	controller.setConfig(config);
	controller.setAppDetailsBeanConverter(appDetailsBeanConverter);
	controller.setAppService(appService);

	MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
		"GET", "/editappdetails");

	WebRequest webRequest = new ServletWebRequest(httpServletRequest);
	Model model = new ExtendedModelMap();

	List<GrantedAuthority> authorities = new ArrayList<>();
	authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	Authentication auth = new TestingAuthenticationToken("testUser", null,
		authorities);
	SecurityContextHolder.getContext().setAuthentication(auth);

	// Test

	String returnValue = controller.showEditForm(webRequest, model);

	// Verify

	assertEquals("error/programError", returnValue);
	String message = (String) model.asMap().get("message");
	assertEquals("The URL must specify either appId or appName.", message);
    }

    @Test
    public void testGetEndUserMissingApp() throws Exception {
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	AppService appService = new MockAppService();

	AppDetailsBeanConverterImpl appDetailsBeanConverter = new AppDetailsBeanConverterImpl();
	appDetailsBeanConverter.setConfig(config);

	EditAppDetailsController controller = new EditAppDetailsController();
	controller.setConfig(config);
	controller.setAppDetailsBeanConverter(appDetailsBeanConverter);
	controller.setAppService(appService);

	MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
		"GET", "/editappdetails");

	httpServletRequest.setParameter("appId", "bogus");
	WebRequest webRequest = new ServletWebRequest(httpServletRequest);
	Model model = new ExtendedModelMap();

	List<GrantedAuthority> authorities = new ArrayList<>();
	authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	Authentication auth = new TestingAuthenticationToken("testUser", null,
		authorities);
	SecurityContextHolder.getContext().setAuthentication(auth);

	// Test

	String returnValue = controller.showEditForm(webRequest, model);

	// Verify

	assertEquals("error/programError", returnValue);
	String message = (String) model.asMap().get("message");
	assertEquals("Application not found", message);
    }

    @Test
    public void testGetEndUserExceptionLoadingApp() throws Exception {
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	AppService appService = new MockAppService();

	AppDetailsBeanConverterImpl appDetailsBeanConverter = new AppDetailsBeanConverterImpl();
	appDetailsBeanConverter.setConfig(config);

	EditAppDetailsController controller = new EditAppDetailsController();
	controller.setConfig(config);
	controller.setAppDetailsBeanConverter(appDetailsBeanConverter);
	controller.setAppService(appService);

	MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
		"GET", "/editappdetails");

	httpServletRequest.setParameter("appId", "throwException");
	WebRequest webRequest = new ServletWebRequest(httpServletRequest);
	Model model = new ExtendedModelMap();

	List<GrantedAuthority> authorities = new ArrayList<>();
	authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	Authentication auth = new TestingAuthenticationToken("testUser", null,
		authorities);
	SecurityContextHolder.getContext().setAuthentication(auth);

	// Test

	String returnValue = controller.showEditForm(webRequest, model);

	// Verify

	assertEquals("error/programError", returnValue);
	String message = (String) model.asMap().get("message");
	assertEquals("Error loading application: Mock: error loading app",
		message);
    }

    @Test
    public void testGetEndUserErrorAddingMissingAttributes() throws Exception {
	Properties props;
	props = getProperties();
	props.setProperty("attr.1.label", "Attr Two");
	props.setProperty("attr.1.ccname", "bogus");
	props.setProperty("attr.1.regex", ".*");
	AppEditConfigManager config = new AppEditConfigManager(props);

	AppService appService = new MockAppService();

	AppDetailsBeanConverterImpl appDetailsBeanConverter = new AppDetailsBeanConverterImpl();
	appDetailsBeanConverter.setConfig(config);

	EditAppDetailsController controller = new EditAppDetailsController();
	controller.setConfig(config);
	controller.setAppDetailsBeanConverter(appDetailsBeanConverter);
	controller.setAppService(appService);

	MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
		"GET", "/editappdetails");

	httpServletRequest.setParameter("appId", "app1Id");
	WebRequest webRequest = new ServletWebRequest(httpServletRequest);
	Model model = new ExtendedModelMap();

	List<GrantedAuthority> authorities = new ArrayList<>();
	authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	Authentication auth = new TestingAuthenticationToken("testUserName",
		null, authorities);
	SecurityContextHolder.getContext().setAuthentication(auth);

	// Test

	String returnValue = controller.showEditForm(webRequest, model);

	// Verify

	assertEquals("error/programError", returnValue);
	String message = (String) model.asMap().get("message");
	assertEquals(
		"Error populating missing attribute values: mock: bad attr name",
		message);
    }

    @Test
    public void testPostValidData() throws Exception {
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	AppService appService = new MockAppService();

	AppDetailsBeanConverterImpl appDetailsBeanConverter = new AppDetailsBeanConverterImpl();
	appDetailsBeanConverter.setConfig(config);

	EditAppDetailsController controller = new EditAppDetailsController();
	controller.setConfig(config);
	controller.setAppDetailsBeanConverter(appDetailsBeanConverter);
	controller.setAppService(appService);

	ModelMap model = new ExtendedModelMap();

	List<GrantedAuthority> authorities = new ArrayList<>();
	authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	Authentication auth = new TestingAuthenticationToken("testUserName",
		null, authorities);
	SecurityContextHolder.getContext().setAuthentication(auth);

	List<String> attrNames = new ArrayList<>();
	attrNames.add("ITSM");
	List<AttributeValuePojo> attrValues = new ArrayList<>();

	attrValues.add(new AttributeValuePojo("attr1Id", "attr1Name",
		"attr1Value"));
	ViewAppBean app = new ViewAppBean("app1Id", "app1Name", attrNames,
		attrValues);

	model.put("app", app);

	String action = null;

	// Test

	String returnValue = controller.updateApp(app, action, model);

	assertEquals("editAppDetailsResult", returnValue);
	String message = (String) model.get("message");
	assertEquals(null, message);

	ViewAppBean resultsApp = (ViewAppBean) model.get("app");
	assertEquals("app1Id", app.getAppId());
	assertEquals("attr1Value", app.getAttrValues().get(0).getValue());
    }

    @Test
    public void testPostAttrValueViolatesPattern() throws Exception {
	Properties props;
	props = getProperties();
	props.setProperty("attr.0.regex", "[A-Za-z]*");
	AppEditConfigManager config = new AppEditConfigManager(props);

	AppService appService = new MockAppService();

	AppDetailsBeanConverterImpl appDetailsBeanConverter = new AppDetailsBeanConverterImpl();
	appDetailsBeanConverter.setConfig(config);

	EditAppDetailsController controller = new EditAppDetailsController();
	controller.setConfig(config);
	controller.setAppDetailsBeanConverter(appDetailsBeanConverter);
	controller.setAppService(appService);

	ModelMap model = new ExtendedModelMap();

	List<GrantedAuthority> authorities = new ArrayList<>();
	authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	Authentication auth = new TestingAuthenticationToken("testUserName",
		null, authorities);
	SecurityContextHolder.getContext().setAuthentication(auth);

	List<String> attrNames = new ArrayList<>();
	attrNames.add("ITSM");
	List<AttributeValuePojo> attrValues = new ArrayList<>();

	attrValues.add(new AttributeValuePojo("attr1Id", "attr1Name",
		"attr1Value"));
	ViewAppBean app = new ViewAppBean("app1Id", "app1Name", attrNames,
		attrValues);

	model.put("app", app);

	String action = null;

	// Test

	String returnValue = controller.updateApp(app, action, model);

	assertEquals("error/programError", returnValue);
	String message = (String) model.get("message");
	assertEquals("The value of ITSM is invalid.", message);

    }

    @Test
    public void testPostUserNotAuthOnApp() throws Exception {
	Properties props;
	props = getProperties();
	AppEditConfigManager config = new AppEditConfigManager(props);

	AppService appService = new MockAppService();

	AppDetailsBeanConverterImpl appDetailsBeanConverter = new AppDetailsBeanConverterImpl();
	appDetailsBeanConverter.setConfig(config);

	EditAppDetailsController controller = new EditAppDetailsController();
	controller.setConfig(config);
	controller.setAppDetailsBeanConverter(appDetailsBeanConverter);
	controller.setAppService(appService);

	ModelMap model = new ExtendedModelMap();

	List<GrantedAuthority> authorities = new ArrayList<>();
	authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	Authentication auth = new TestingAuthenticationToken(
		"notAuthorizedUserName", null, authorities);
	SecurityContextHolder.getContext().setAuthentication(auth);

	List<String> attrNames = new ArrayList<>();
	attrNames.add("ITSM");
	List<AttributeValuePojo> attrValues = new ArrayList<>();

	attrValues.add(new AttributeValuePojo("attr1Id", "attr1Name",
		"attr1Value"));
	ViewAppBean app = new ViewAppBean("app1Id", "app1Name", attrNames,
		attrValues);

	model.put("app", app);

	String action = null;

	// Test

	String returnValue = controller.updateApp(app, action, model);

	assertEquals("error/programError", returnValue);
	String message = (String) model.get("message");
	assertEquals("You are not authorized to access this application",
		message);

    }

    @Test
    public void testPostUpdateFailure() throws Exception {
	Properties props;
	props = getProperties();
	AppEditConfigManager config = new AppEditConfigManager(props);

	AppService appService = new MockAppService();

	AppDetailsBeanConverterImpl appDetailsBeanConverter = new AppDetailsBeanConverterImpl();
	appDetailsBeanConverter.setConfig(config);

	EditAppDetailsController controller = new EditAppDetailsController();
	controller.setConfig(config);
	controller.setAppDetailsBeanConverter(appDetailsBeanConverter);
	controller.setAppService(appService);

	ModelMap model = new ExtendedModelMap();

	List<GrantedAuthority> authorities = new ArrayList<>();
	authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	Authentication auth = new TestingAuthenticationToken("testUser", null,
		authorities);
	SecurityContextHolder.getContext().setAuthentication(auth);

	List<String> attrNames = new ArrayList<>();
	attrNames.add("ITSM");
	List<AttributeValuePojo> attrValues = new ArrayList<>();

	attrValues.add(new AttributeValuePojo("attr1Id", "attr1Name",
		"attr1Value"));
	ViewAppBean app = new ViewAppBean("failOnUpdate", "app1Name",
		attrNames, attrValues);

	model.put("app", app);

	String action = null;

	// Test

	String returnValue = controller.updateApp(app, action, model);

	assertEquals("error/programError", returnValue);
	String message = (String) model.get("message");
	assertEquals(
		"Error updating application app1Name: Mock: Error updating application",
		message);

    }

    private Properties getProperties() {
	Properties props;
	props = new Properties();
	props.setProperty("cc.server.name", "not used");
	props.setProperty("cc.user.name", "not used");
	props.setProperty("cc.user.name", "not used");
	props.setProperty("cc.password", "not used");
	props.setProperty("attr.0.label", "ITSM");
	props.setProperty("attr.0.ccname", "attr1Name");
	props.setProperty("attr.0.regex", ".*");
	props.setProperty("field.input.validation.regex.username",
		"\\[A-Za-z0-9@_.-\\]+");
	props.setProperty("field.input.validation.regex.psw", ".+");
	props.setProperty("app.version", "Unspecified");

	props.setProperty("db.server", "not used");
	props.setProperty("auditor.role.name", "Auditor");
	props.setProperty("nai.audit.status.0", "Unreviewed");
	props.setProperty("nai.audit.status.1", "Approved");
	props.setProperty("nai.audit.status.2", "Rejected");

	props.setProperty("nai.audit.rejected.status.name", "Rejected");
	props.setProperty("nai.audit.rejected.status.changes.rem.status.to",
		"NAI Rejected By Auditor");
	props.setProperty("nai.audit.date.format", "MMM-dd-yyyy hh:mm:ss z");
	return props;
    }
}
