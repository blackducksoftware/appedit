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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.mocks.MockVulnNaiAuditDetailsService;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.appedit.naiaudit.model.NaiAuditViewData;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;
import com.blackducksoftware.tools.appedit.web.controller.naiaudit.EditNaiAuditDetailsController;

public class EditNaiAuditDetailsControllerTest {

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

	EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	controller.setConfig(config);

	VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
	controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

	MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
		"GET", "/editnaiauditdetails");

	httpServletRequest.setParameter("appId", "app1Id");
	WebRequest webRequest = new ServletWebRequest(httpServletRequest);
	ModelMap model = new ExtendedModelMap();

	// Test

	String returnValue = controller.showNaiAuditDetails(webRequest, model);

	// Verify

	assertEquals("editNaiAuditDetailsForm", returnValue);

	String message = (String) model.get("message");
	assertEquals(null, message);

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

    @Test
    public void testGetMissingAppId() throws Exception {

	// Setup

	EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	controller.setConfig(config);

	VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
	controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

	MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
		"GET", "/editnaiauditdetails");

	WebRequest webRequest = new ServletWebRequest(httpServletRequest);
	ModelMap model = new ExtendedModelMap();

	// Test

	String returnValue = controller.showNaiAuditDetails(webRequest, model);

	// Verify

	assertEquals("redirect:/error/400", returnValue);
	String message = (String) model.get("message");
	assertEquals(null, message);
    }

    @Test
    public void testGetInvalidAppId() throws Exception {

	// Setup

	EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	controller.setConfig(config);

	VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
	controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

	MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
		"GET", "/editnaiauditdetails");
	httpServletRequest.setParameter("appId", "bogus");
	WebRequest webRequest = new ServletWebRequest(httpServletRequest);
	ModelMap model = new ExtendedModelMap();

	// Test

	String returnValue = controller.showNaiAuditDetails(webRequest, model);

	// Verify

	assertEquals("error/programError", returnValue);
	String message = (String) model.get("message");
	assertEquals(
		"Error loading application with ID bogus: mock: application not found",
		message);
    }

    @Test
    public void testGetErrorGettingVulns() throws Exception {

	// Setup

	EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	controller.setConfig(config);

	VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
	controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

	MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(
		"GET", "/editnaiauditdetails");
	httpServletRequest.setParameter("appId",
		"bogus_for_getAppCompVulnCompositeList");
	WebRequest webRequest = new ServletWebRequest(httpServletRequest);
	ModelMap model = new ExtendedModelMap();

	// Test

	String returnValue = controller.showNaiAuditDetails(webRequest, model);

	// Verify

	assertEquals("error/programError", returnValue);
	String message = (String) model.get("message");
	assertEquals(
		"Error getting vulnerability details for application: mock: failure getting app comp vuln comp list",
		message);
    }

    @Test
    public void testPostRowSelected() throws Exception {

	// Setup

	EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	controller.setConfig(config);

	VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
	controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

	Authentication auth = new TestingAuthenticationToken("testUserName",
		null);
	SecurityContextHolder.getContext().setAuthentication(auth);

	NaiAuditViewData formData = new NaiAuditViewData();
	formData.setApplicationId("testAppId");
	formData.setApplicationName("testAppName");
	formData.setApplicationVersion("Unspecified");
	formData.setComment(COMMENT_512_BYTES);
	formData.setVulnerabilityNaiAuditStatus("testStatus");

	List<String> itemList = new ArrayList<>();
	itemList.add("app1Id|request1Id|component1Id|vulnerability1Id");
	formData.setItemList(itemList);

	ModelMap model = new ExtendedModelMap();

	// Test

	String returnValue = controller
		.saveNaiAuditDetails(formData, "", model);

	// Verify

	assertEquals("editNaiAuditDetailsForm", returnValue);

	String message = (String) model.get("message");
	assertEquals(null, message);

	NaiAuditViewData naiAuditViewData = (NaiAuditViewData) model
		.get("selectedVulnerabilities");

	assertEquals("testAppId", naiAuditViewData.getApplicationId());
	assertEquals("testAppName", naiAuditViewData.getApplicationName());
	assertEquals("Unspecified", naiAuditViewData.getApplicationVersion());
	assertEquals(null, naiAuditViewData.getComment());
	assertEquals(null, naiAuditViewData.getVulnerabilityNaiAuditStatus());
	assertEquals(null, naiAuditViewData.getItemList());

	List<AppCompVulnComposite> vulnNaiAuditDetailsList = (List<AppCompVulnComposite>) model
		.get("vulnNaiAuditDetailsList");

	assertEquals("component1Name", vulnNaiAuditDetailsList.get(0)
		.getCcPart().getComponentName());
	assertEquals("vulnerability1Name", vulnNaiAuditDetailsList.get(0)
		.getCcPart().getVulnerabilityName());

	assertEquals(COMMENT_512_BYTES, vulnNaiAuditDetailsList.get(0)
		.getAuditPart().getVulnerabilityNaiAuditComment());
	assertEquals("testStatus", vulnNaiAuditDetailsList.get(0)
		.getAuditPart().getVulnerabilityNaiAuditStatus());
    }

    @Test
    public void testPostNoRowSelected() throws Exception {

	// Setup

	EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	controller.setConfig(config);

	VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
	controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

	Authentication auth = new TestingAuthenticationToken("testUserName",
		null);
	SecurityContextHolder.getContext().setAuthentication(auth);

	NaiAuditViewData formData = new NaiAuditViewData();
	formData.setApplicationId("testAppId");
	formData.setApplicationName("testAppName");
	formData.setApplicationVersion("Unspecified");
	formData.setComment("testComment");
	formData.setVulnerabilityNaiAuditStatus("testStatus");

	List<String> itemList = null;
	formData.setItemList(itemList);

	ModelMap model = new ExtendedModelMap();

	// Test

	String returnValue = controller
		.saveNaiAuditDetails(formData, "", model);

	// Verify

	String message = (String) model.get("message");
	assertEquals("No rows selected.", message);

	assertEquals("editNaiAuditDetailsForm", returnValue);

	NaiAuditViewData naiAuditViewData = (NaiAuditViewData) model
		.get("selectedVulnerabilities");

	assertEquals("testAppId", naiAuditViewData.getApplicationId());
	assertEquals("testAppName", naiAuditViewData.getApplicationName());
	assertEquals("Unspecified", naiAuditViewData.getApplicationVersion());
	assertEquals(null, naiAuditViewData.getComment());
	assertEquals(null, naiAuditViewData.getVulnerabilityNaiAuditStatus());
	assertEquals(null, naiAuditViewData.getItemList());

	List<AppCompVulnComposite> vulnNaiAuditDetailsList = (List<AppCompVulnComposite>) model
		.get("vulnNaiAuditDetailsList");

	assertEquals("component1Name", vulnNaiAuditDetailsList.get(0)
		.getCcPart().getComponentName());
	assertEquals("vulnerability1Name", vulnNaiAuditDetailsList.get(0)
		.getCcPart().getVulnerabilityName());

	assertEquals("vulnerability1NaiAuditComment", vulnNaiAuditDetailsList
		.get(0).getAuditPart().getVulnerabilityNaiAuditComment());
	assertEquals("vulnerability1NaiAuditStatus", vulnNaiAuditDetailsList
		.get(0).getAuditPart().getVulnerabilityNaiAuditStatus());
    }

    @Test
    public void testPostCommentTooLong() throws Exception {

	// Setup

	String longComment = COMMENT_512_BYTES + "a";

	EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	controller.setConfig(config);

	VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
	controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

	Authentication auth = new TestingAuthenticationToken("testUserName",
		null);
	SecurityContextHolder.getContext().setAuthentication(auth);

	NaiAuditViewData formData = new NaiAuditViewData();
	formData.setApplicationId("testAppId");
	formData.setApplicationName("testAppName");
	formData.setApplicationVersion("Unspecified");
	formData.setComment(longComment);
	formData.setVulnerabilityNaiAuditStatus("testStatus");

	List<String> itemList = new ArrayList<>();
	itemList.add("app1Id|request1Id|component1Id|vulnerability1Id");
	formData.setItemList(itemList);

	ModelMap model = new ExtendedModelMap();

	// Test

	String returnValue = controller
		.saveNaiAuditDetails(formData, "", model);

	// Verify

	String message = (String) model.get("message");
	assertEquals(
		"The comment entered is too long. Maximum length is 512 characters",
		message);

	assertEquals("editNaiAuditDetailsForm", returnValue);

	NaiAuditViewData naiAuditViewData = (NaiAuditViewData) model
		.get("selectedVulnerabilities");

	assertEquals("testAppId", naiAuditViewData.getApplicationId());
	assertEquals("testAppName", naiAuditViewData.getApplicationName());
	assertEquals("Unspecified", naiAuditViewData.getApplicationVersion());
	assertEquals(null, naiAuditViewData.getComment());
	assertEquals(null, naiAuditViewData.getVulnerabilityNaiAuditStatus());
	assertEquals(null, naiAuditViewData.getItemList());

	List<AppCompVulnComposite> vulnNaiAuditDetailsList = (List<AppCompVulnComposite>) model
		.get("vulnNaiAuditDetailsList");

	assertEquals("component1Name", vulnNaiAuditDetailsList.get(0)
		.getCcPart().getComponentName());
	assertEquals("vulnerability1Name", vulnNaiAuditDetailsList.get(0)
		.getCcPart().getVulnerabilityName());

	assertEquals("vulnerability1NaiAuditComment", vulnNaiAuditDetailsList
		.get(0).getAuditPart().getVulnerabilityNaiAuditComment());
	assertEquals("vulnerability1NaiAuditStatus", vulnNaiAuditDetailsList
		.get(0).getAuditPart().getVulnerabilityNaiAuditStatus());
    }

    @Test
    public void testPostCommentViolatesPattern() throws Exception {

	// Setup

	String numericComment = COMMENT_512_BYTES;

	EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
	Properties props;
	props = getProperties();
	props.setProperty("field.input.validation.regex.naiauditcomment",
		"[a-zA-Z]*");

	AppEditConfigManager config = new AppEditConfigManager(props);

	controller.setConfig(config);

	VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
	controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

	Authentication auth = new TestingAuthenticationToken("testUserName",
		null);
	SecurityContextHolder.getContext().setAuthentication(auth);

	NaiAuditViewData formData = new NaiAuditViewData();
	formData.setApplicationId("testAppId");
	formData.setApplicationName("testAppName");
	formData.setApplicationVersion("Unspecified");
	formData.setComment(numericComment);
	formData.setVulnerabilityNaiAuditStatus("testStatus");

	List<String> itemList = new ArrayList<>();
	itemList.add("app1Id|request1Id|component1Id|vulnerability1Id");
	formData.setItemList(itemList);

	ModelMap model = new ExtendedModelMap();

	// Test

	String returnValue = controller
		.saveNaiAuditDetails(formData, "", model);

	// Verify

	String message = (String) model.get("message");
	assertEquals("The comment entered is invalid.", message);

	assertEquals("editNaiAuditDetailsForm", returnValue);

	NaiAuditViewData naiAuditViewData = (NaiAuditViewData) model
		.get("selectedVulnerabilities");

	assertEquals("testAppId", naiAuditViewData.getApplicationId());
	assertEquals("testAppName", naiAuditViewData.getApplicationName());
	assertEquals("Unspecified", naiAuditViewData.getApplicationVersion());
	assertEquals(null, naiAuditViewData.getComment());
	assertEquals(null, naiAuditViewData.getVulnerabilityNaiAuditStatus());
	assertEquals(null, naiAuditViewData.getItemList());

	List<AppCompVulnComposite> vulnNaiAuditDetailsList = (List<AppCompVulnComposite>) model
		.get("vulnNaiAuditDetailsList");

	assertEquals("component1Name", vulnNaiAuditDetailsList.get(0)
		.getCcPart().getComponentName());
	assertEquals("vulnerability1Name", vulnNaiAuditDetailsList.get(0)
		.getCcPart().getVulnerabilityName());

	assertEquals("vulnerability1NaiAuditComment", vulnNaiAuditDetailsList
		.get(0).getAuditPart().getVulnerabilityNaiAuditComment());
	assertEquals("vulnerability1NaiAuditStatus", vulnNaiAuditDetailsList
		.get(0).getAuditPart().getVulnerabilityNaiAuditStatus());
    }

    @Test
    public void testPostInvalidSelectedKeyFormat() throws Exception {

	// Setup

	String validComment = COMMENT_512_BYTES;

	EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	controller.setConfig(config);

	VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
	controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

	Authentication auth = new TestingAuthenticationToken("testUserName",
		null);
	SecurityContextHolder.getContext().setAuthentication(auth);

	NaiAuditViewData formData = new NaiAuditViewData();
	formData.setApplicationId("testAppId");
	formData.setApplicationName("testAppName");
	formData.setApplicationVersion("Unspecified");
	formData.setComment(validComment);
	formData.setVulnerabilityNaiAuditStatus("testStatus");

	List<String> itemList = new ArrayList<>();
	itemList.add("badkey|");
	formData.setItemList(itemList);

	ModelMap model = new ExtendedModelMap();

	// Test

	String returnValue = controller
		.saveNaiAuditDetails(formData, "", model);

	// Verify

	String message = (String) model.get("message");
	assertEquals(
		"The selected row key (badkey|) is invalid; failed extracting IDs.",
		message);

	assertEquals("error/programError", returnValue);
    }

    @Test
    public void testPostSelectedRowKeyNotInList() throws Exception {

	// Setup

	String validComment = COMMENT_512_BYTES;

	EditNaiAuditDetailsController controller = new EditNaiAuditDetailsController();
	Properties props;
	props = getProperties();

	AppEditConfigManager config = new AppEditConfigManager(props);

	controller.setConfig(config);

	VulnNaiAuditDetailsService vulnNaiAuditDetailsService = new MockVulnNaiAuditDetailsService();
	controller.setVulnNaiAuditDetailsService(vulnNaiAuditDetailsService);

	Authentication auth = new TestingAuthenticationToken("testUserName",
		null);
	SecurityContextHolder.getContext().setAuthentication(auth);

	NaiAuditViewData formData = new NaiAuditViewData();
	formData.setApplicationId("testAppId");
	formData.setApplicationName("testAppName");
	formData.setApplicationVersion("Unspecified");
	formData.setComment(validComment);
	formData.setVulnerabilityNaiAuditStatus("testStatus");

	List<String> itemList = new ArrayList<>();
	itemList.add("bogus|request1Id|component1Id|vulnerability1Id");
	formData.setItemList(itemList);

	ModelMap model = new ExtendedModelMap();

	// Test

	String returnValue = controller
		.saveNaiAuditDetails(formData, "", model);

	// Verify

	String message = (String) model.get("message");
	assertEquals(
		"The selected row key (bogus|request1Id|component1Id|vulnerability1Id) not found in full vulnerabilities list.",
		message);

	assertEquals("error/programError", returnValue);
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
