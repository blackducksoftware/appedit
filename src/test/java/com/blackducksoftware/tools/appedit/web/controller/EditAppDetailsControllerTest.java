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
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.blackducksoftware.tools.appedit.appdetails.model.ViewAppBean;
import com.blackducksoftware.tools.appedit.appdetails.service.AppService;
import com.blackducksoftware.tools.appedit.appdetails.service.impl.AppDetailsBeanConverterImpl;
import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.mocks.MockAppService;
import com.blackducksoftware.tools.appedit.mocks.MockVulnNaiAuditDetailsService;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;

public class EditAppDetailsControllerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void test() throws Exception {
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

	VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();

	controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

	MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
		"GET", "/editnaiauditdetails");

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
