package com.blackducksoftware.tools.appedit.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.Items;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;

/**
 * Controller for requests for and form submissions from the Edit NAI Audit Details
 * screen.
 *
 * @author sbillings
 *
 */
@Controller
@SessionAttributes({ "app", "dataSource" })
public class EditNaiAuditDetailsController {
    private VulnNaiAuditDetailsService vulnNaiAuditDetailsService;

    @Inject
    public void setVulnNaiAuditDetailsService(VulnNaiAuditDetailsService vulnNaiAuditDetailsService) {
        this.vulnNaiAuditDetailsService = vulnNaiAuditDetailsService;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass()
            .getName());

    /**
     * Handles Edit NAI Audit Details form submissions. Updates app in Code Center.
     */
    @RequestMapping(value = "/editnaiauditdetails", method = RequestMethod.POST)
    public String saveNaiAuditDetails(@ModelAttribute("selectedVulnerabilities") Items formData,
            @RequestParam String action, ModelMap model) {

        logger.info("EditNaiAuditDetailsController.saveNaiAuditDetails(): selectedVulnerabilities: " + formData);
        /*
         * TODO: Validate input
         * // Validate input
         * int i = 0;
         * for (String attrLabel : app.getAttrNames()) {
         * InputValidatorEditAppDetails inputValidator = new InputValidatorEditAppDetails(
         * config);
         * if (!inputValidator.validateAttributeValue(attrLabel, app
         * .getAttrValues().get(i++).getValue())) {
         * String msg = "The value of " + attrLabel + " is invalid.";
         * logger.error(msg);
         * model.addAttribute("message", msg);
         * return "error/programError";
         * }
         * }
         */
        // TODO: Check: None selected / none in list
        List<String> selectedRows = formData.getItemList();
        List<AppCompVulnComposite> fullVulnNaiAuditDetailsList = vulnNaiAuditDetailsService.getAppCompVulnCompositeList(formData
                .getApplicationId());
        List<VulnNaiAuditDetails> selectedVulnNaiAuditDetailsList;
        if (selectedRows == null) {
            String msg = "No rows selected.";
            logger.warn(msg);
        } else {
            // User selected one or more rows; update each one
            for (String selectedRowKey : selectedRows) {
                logger.info("Selected vulnerability key: " + selectedRowKey);
                String[] selectedKeyParts = selectedRowKey.split("\\|");
                if (selectedKeyParts.length != 3) {
                    String msg = "selected row key (" + selectedRowKey + ") is invalid; failed extracting IDs.";
                    logger.error(msg);
                    model.addAttribute("message", msg);
                    return "error/programError";
                }
                String applicationId = selectedKeyParts[0];
                String componentId = selectedKeyParts[1];
                String vulnerabilityId = selectedKeyParts[2];
                AppCompVulnKey key = new AppCompVulnKey(applicationId, componentId, vulnerabilityId);

                AppCompVulnComposite selectedVuln = findVuln(fullVulnNaiAuditDetailsList, key);
                if (selectedVuln == null) {
                    String msg = "selected row key (" + selectedRowKey + ") not found in full vulnerabilities list.";
                    logger.error(msg);
                    model.addAttribute("message", msg);
                    return "error/programError";
                }

                selectedVuln.getAuditPart().setVulnerabilityNaiAuditStatus(formData.getVulnerabilityNaiAuditStatus());
                selectedVuln.getAuditPart().setVulnerabilityNaiAuditComment(formData.getComment());
                logger.info("Updating vulnerability with: " + selectedVuln);
                vulnNaiAuditDetailsService.updateVulnNaiAuditDetails(selectedVuln);
            }
        }
        Items newValues = new Items();
        newValues.setApplicationId(formData.getApplicationId()); // pass appId through to view
        model.addAttribute("selectedVulnerabilities", newValues);

        model.addAttribute("vulnNaiAuditDetailsList", fullVulnNaiAuditDetailsList);
        return "editNaiAuditDetailsForm";
    }

    private AppCompVulnComposite findVuln(List<AppCompVulnComposite> vulnList, AppCompVulnKey key) {
        for (AppCompVulnComposite vuln : vulnList) {
            if (vuln.getKey().equals(key)) {
                return vuln;
            }
        }
        return null;
    }
}
