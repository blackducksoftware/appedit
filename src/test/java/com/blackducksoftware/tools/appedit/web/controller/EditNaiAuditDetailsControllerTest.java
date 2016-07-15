package com.blackducksoftware.tools.appedit.web.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.mocks.MockVulnNaiAuditDetailsService;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.appedit.naiaudit.model.NaiAuditUpdateStatus;
import com.blackducksoftware.tools.appedit.naiaudit.model.NaiAuditViewData;
import com.blackducksoftware.tools.appedit.naiaudit.model.RowUpdateResult;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;
import com.blackducksoftware.tools.appedit.web.controller.naiaudit.EditNaiAuditDetailsController;

public class EditNaiAuditDetailsControllerTest {

	private static final String TEST_KEY = "testAppId|testRequestId|testCompId|testVulnId";
	private final String COMMENT_512_BYTES = "123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789012";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testGetValid() throws Exception {

		// Setup

		final EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
		Properties props;
		props = getProperties();

		final AppEditConfigManager config = new AppEditConfigManager(props);

		controller.setConfig(config);

		final VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
		controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

		final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
				"GET", "/editnaiauditdetails");

		httpServletRequest.setParameter("appId", "app1Id");
		final WebRequest webRequest = new ServletWebRequest(httpServletRequest);
		final ModelMap model = new ExtendedModelMap();

		// Test

		final String returnValue = controller.showNaiAuditDetails(webRequest, model);

		// Verify

		assertEquals("editNaiAuditDetailsForm", returnValue);

		final String message = (String) model.get("message");
		assertEquals(null, message);

		final NaiAuditViewData naiAuditViewData = (NaiAuditViewData) model
				.get("selectedVulnerabilities");
		assertEquals("app1Id", naiAuditViewData.getApplicationId());
		assertEquals("app1Id", naiAuditViewData.getApplicationName());
		assertEquals(null, naiAuditViewData.getComment());
		assertEquals(null, naiAuditViewData.getVulnerabilityNaiAuditStatus());
		assertEquals(null, naiAuditViewData.getItemList());

		final List<AppCompVulnComposite> vulnNaiAuditDetailsList = (List<AppCompVulnComposite>) model
				.get("vulnNaiAuditDetailsList");

		assertEquals("component1Name", vulnNaiAuditDetailsList.get(0)
				.getCcPart().getComponentName());
		assertEquals("vulnerability1Name", vulnNaiAuditDetailsList.get(0)
				.getCcPart().getVulnerabilityName());
	}

	@Test
	public void testGetMissingAppId() throws Exception {

		// Setup

		final EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
		Properties props;
		props = getProperties();

		final AppEditConfigManager config = new AppEditConfigManager(props);

		controller.setConfig(config);

		final VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
		controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

		final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
				"GET", "/editnaiauditdetails");

		final WebRequest webRequest = new ServletWebRequest(httpServletRequest);
		final ModelMap model = new ExtendedModelMap();

		// Test

		final String returnValue = controller.showNaiAuditDetails(webRequest, model);

		// Verify

		assertEquals("redirect:/error/400", returnValue);
		final String message = (String) model.get("message");
		assertEquals(null, message);
	}

	@Test
	public void testGetInvalidAppId() throws Exception {

		// Setup

		final EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
		Properties props;
		props = getProperties();

		final AppEditConfigManager config = new AppEditConfigManager(props);

		controller.setConfig(config);

		final VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
		controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

		final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
				"GET", "/editnaiauditdetails");
		httpServletRequest.setParameter("appId", "bogus");
		final WebRequest webRequest = new ServletWebRequest(httpServletRequest);
		final ModelMap model = new ExtendedModelMap();

		// Test

		final String returnValue = controller.showNaiAuditDetails(webRequest, model);

		// Verify

		assertEquals("error/programError", returnValue);
		final String message = (String) model.get("message");
		assertEquals(
				"Error loading application with ID bogus: mock: application not found",
				message);
	}

	@Test
	public void testGetErrorGettingVulns() throws Exception {

		// Setup

		final EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
		Properties props;
		props = getProperties();

		final AppEditConfigManager config = new AppEditConfigManager(props);

		controller.setConfig(config);

		final VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
		controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

		final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
				"GET", "/editnaiauditdetails");
		httpServletRequest.setParameter("appId",
				"bogus_for_getAppCompVulnCompositeList");
		final WebRequest webRequest = new ServletWebRequest(httpServletRequest);
		final ModelMap model = new ExtendedModelMap();

		// Test

		final String returnValue = controller.showNaiAuditDetails(webRequest, model);

		// Verify

		assertEquals("error/programError", returnValue);
		final String message = (String) model.get("message");
		assertEquals(
				"Error getting vulnerability details for application: mock: failure getting app comp vuln comp list",
				message);
	}

	@Test
	public void testSaveRow() throws Exception {

		// Setup

		final EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
		Properties props;
		props = getProperties();

		final AppEditConfigManager config = new AppEditConfigManager(props);

		controller.setConfig(config);

		final VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
		controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

		final Authentication auth = new TestingAuthenticationToken("testUserName",
				null);
		SecurityContextHolder.getContext().setAuthentication(auth);

		// Test

		final RowUpdateResult result = controller.saveRow(TEST_KEY,
				"testStatus", COMMENT_512_BYTES);

		// Verify

		assertEquals(NaiAuditUpdateStatus.SUCCEEDED, result.getStatus());
		assertEquals("vulnerability1Id", result.getNewRowData().getKey().getVulnerabilityId());

		assertEquals("vulnerability1RemediationStatus", result.getNewRowData().getCcPart()
				.getVulnerabilityRemediationStatus());
		assertEquals(COMMENT_512_BYTES, result.getNewRowData().getAuditPart()
				.getVulnerabilityNaiAuditComment());

		assertEquals("app1Id", result.getNewRowData().getKey().getApplicationId());
		// This is what the mock produces
		assertEquals("application1Name", result.getNewRowData().getCcPart().getApplicationName());
		assertEquals("application1Version", result.getNewRowData().getCcPart().getApplicationVersion());

		assertEquals("component1Name", result.getNewRowData().getCcPart().getComponentName());
		assertEquals("vulnerability1Name", result.getNewRowData().getCcPart().getVulnerabilityName());

		assertEquals("testStatus", result.getNewRowData().getAuditPart()
				.getVulnerabilityNaiAuditStatus());
	}


	@Test
	public void testPostCommentTooLong() throws Exception {

		// Setup

		final EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
		Properties props;
		props = getProperties();

		final AppEditConfigManager config = new AppEditConfigManager(props);

		controller.setConfig(config);

		final VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
		controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

		final Authentication auth = new TestingAuthenticationToken("testUserName",
				null);
		SecurityContextHolder.getContext().setAuthentication(auth);

		// Test

		final RowUpdateResult result = controller.saveRow(TEST_KEY, "testStatus", COMMENT_512_BYTES + "a");

		// Verify

		assertEquals(NaiAuditUpdateStatus.FAILED, result.getStatus());
		assertEquals("The comment entered is too long. Maximum length is 512 characters", result.getMessage());
	}

	@Test
	public void testPostCommentViolatesPattern() throws Exception {

		// Setup

		final EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
		Properties props;
		props = getProperties();
		props.setProperty("field.input.validation.regex.naiauditcomment",
				"[a-zA-Z]*");

		final AppEditConfigManager config = new AppEditConfigManager(props);

		controller.setConfig(config);

		final VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
		controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

		final Authentication auth = new TestingAuthenticationToken("testUserName",
				null);
		SecurityContextHolder.getContext().setAuthentication(auth);

		// Test

		final RowUpdateResult result = controller.saveRow(TEST_KEY, "testStatus", COMMENT_512_BYTES);

		// Verify

		assertEquals(NaiAuditUpdateStatus.FAILED, result.getStatus());
		assertEquals("The comment entered is invalid.", result.getMessage());
	}

	@Test
	public void testPostInvalidSelectedKeyFormat() throws Exception {

		// Setup

		final EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
		Properties props;
		props = getProperties();

		final AppEditConfigManager config = new AppEditConfigManager(props);

		controller.setConfig(config);

		final VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
		controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

		final Authentication auth = new TestingAuthenticationToken("testUserName",
				null);
		SecurityContextHolder.getContext().setAuthentication(auth);

		// Test

		final RowUpdateResult result = controller.saveRow("invalidKey", "testStatus", COMMENT_512_BYTES);

		// Verify

		assertEquals(NaiAuditUpdateStatus.FAILED, result.getStatus());
		assertEquals("The selected row key (invalidKey) is invalid; failed extracting IDs.", result.getMessage());
	}

	private Properties getProperties() {
		Properties props;
		props = new Properties();
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
		return props;
	}

}
