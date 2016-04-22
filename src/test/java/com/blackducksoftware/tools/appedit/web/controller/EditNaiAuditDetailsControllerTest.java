package com.blackducksoftware.tools.appedit.web.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.mocks.MockVulnNaiAuditDetailsService;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.appedit.naiaudit.model.NaiAuditViewData;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;

public class EditNaiAuditDetailsControllerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void test() throws Exception {

	// Setup

	EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
	Properties props = new Properties();
	props.setProperty("cc.server.name", "not used");
	props.setProperty("cc.user.name", "not used");
	props.setProperty("cc.user.name", "not used");
	props.setProperty("cc.password", "not used");
	props.setProperty("attr.0.label", "not used");
	props.setProperty("attr.0.ccname", "not used");
	props.setProperty("attr.0.regex", "not used");
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

	props.setProperty("auditor.role.name", "not used");

	AppEditConfigManager config = new AppEditConfigManager(props);

	controller.setConfig(config);

	VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
	controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

	// Test
	MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
		"GET", "/editnaiauditdetails");

	httpServletRequest.setParameter("appId", "app1Id");
	WebRequest webRequest = new ServletWebRequest(httpServletRequest);
	webRequest.setAttribute("appId", "app1Id", WebRequest.SCOPE_REQUEST);
	ModelMap model = new ExtendedModelMap();
	controller.showNaiAuditDetails(webRequest, model);

	// Verify

	NaiAuditViewData naiAuditViewData = (NaiAuditViewData) model
		.get("selectedVulnerabilities");
	assertEquals("app1Id", naiAuditViewData.getApplicationId());
	assertEquals("app1Id", naiAuditViewData.getApplicationName());
	assertEquals(null, naiAuditViewData.getComment());
	assertEquals(null, naiAuditViewData.getVulnerabilityNaiAuditStatus());
	assertEquals(null, naiAuditViewData.getItemList());

	List<AppCompVulnComposite> vulnNaiAuditDetailsList = (List<AppCompVulnComposite>) model
		.get("vulnNaiAuditDetailsList");

	assertEquals("component1Name", vulnNaiAuditDetailsList.get(0)
		.getCcPart().getComponentName());
	assertEquals("vulnerability1Name", vulnNaiAuditDetailsList.get(0)
		.getCcPart().getVulnerabilityName());
    }

}
