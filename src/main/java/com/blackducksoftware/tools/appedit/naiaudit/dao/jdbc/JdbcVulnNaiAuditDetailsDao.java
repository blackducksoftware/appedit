package com.blackducksoftware.tools.appedit.naiaudit.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnNaiAuditDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;

public class JdbcVulnNaiAuditDetailsDao implements VulnNaiAuditDetailsDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Inject
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
	this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcVulnNaiAuditDetailsDao() {

    }

    @Override
    public VulnNaiAuditDetails insertVulnNaiAuditDetails(
	    VulnNaiAuditDetails vulnNaiAuditDetails) {

	String SQL = "INSERT INTO vuln_nai_audit (application_id, request_id, component_id, vulnerability_id, nai_audit_status, nai_audit_comment) "
		+ "VALUES (:appId, :requestId, :compId, :vulnId, :naiAuditStatus, :naiAuditComment)";
	Map<String, String> namedParameters = new HashMap<>();
	namedParameters.put("appId", vulnNaiAuditDetails.getAppCompVulnKey()
		.getApplicationId());
	namedParameters.put("requestId", vulnNaiAuditDetails
		.getAppCompVulnKey().getRequestId());
	namedParameters.put("compId", vulnNaiAuditDetails.getAppCompVulnKey()
		.getComponentId());
	namedParameters.put("vulnId", vulnNaiAuditDetails.getAppCompVulnKey()
		.getVulnerabilityId());
	namedParameters.put("naiAuditStatus",
		vulnNaiAuditDetails.getVulnerabilityNaiAuditStatus());
	namedParameters.put("naiAuditComment",
		vulnNaiAuditDetails.getVulnerabilityNaiAuditComment());

	jdbcTemplate.update(SQL, namedParameters);
	logger.debug("Inserted Vuln NAI Audit Details Record for: "
		+ vulnNaiAuditDetails);

	return vulnNaiAuditDetails;
    }

    @Override
    public VulnNaiAuditDetails updateVulnNaiAuditDetails(
	    VulnNaiAuditDetails vulnNaiAuditDetails) {

	String SQL = "UPDATE vuln_nai_audit SET nai_audit_status=:naiAuditStatus, nai_audit_comment=:naiAuditComment "
		+ "WHERE application_id = :appId AND request_id = :requestId AND component_id = :compId AND vulnerability_id = :vulnId";
	Map<String, String> namedParameters = new HashMap<>();
	namedParameters.put("appId", vulnNaiAuditDetails.getAppCompVulnKey()
		.getApplicationId());
	namedParameters.put("requestId", vulnNaiAuditDetails
		.getAppCompVulnKey().getRequestId());
	namedParameters.put("compId", vulnNaiAuditDetails.getAppCompVulnKey()
		.getComponentId());
	namedParameters.put("vulnId", vulnNaiAuditDetails.getAppCompVulnKey()
		.getVulnerabilityId());
	namedParameters.put("naiAuditStatus",
		vulnNaiAuditDetails.getVulnerabilityNaiAuditStatus());
	namedParameters.put("naiAuditComment",
		vulnNaiAuditDetails.getVulnerabilityNaiAuditComment());

	jdbcTemplate.update(SQL, namedParameters);
	logger.debug("Updated Vuln NAI Audit Details Record for: "
		+ vulnNaiAuditDetails);

	return vulnNaiAuditDetails;
    }

    @Override
    public Map<AppCompVulnKey, VulnNaiAuditDetails> getVulnNaiAuditDetailsMap(
	    String applicationId) {

	String SQL = "SELECT application_id, request_id, component_id, vulnerability_id, nai_audit_status, nai_audit_comment FROM vuln_nai_audit "
		+ "WHERE application_id = :appId";
	SqlParameterSource namedParameters = new MapSqlParameterSource("appId",
		applicationId);
	logger.debug("Getting vulnNaiAuditDetails for appID " + applicationId
		+ "; SQL: " + SQL);

	List<VulnNaiAuditDetails> vulnNaiAuditDetailsList = null;
	try {
	    vulnNaiAuditDetailsList = (List<VulnNaiAuditDetails>) jdbcTemplate
		    .query(SQL, namedParameters,
			    new VulnNaiAuditDetailsMapper());
	} catch (BadSqlGrammarException e) {
	    String msg = "Error getting NAI Audit details. Make sure the NAI Audit database tables have been created. Details: "
		    + e.getMessage();
	    logger.error(msg);
	    throw new IllegalStateException(msg);
	}

	logger.debug("Read " + vulnNaiAuditDetailsList.size()
		+ " vulnNaiAuditDetail records.");
	Map<AppCompVulnKey, VulnNaiAuditDetails> vulnNaiAuditDetailsMap = new HashMap<>();
	for (VulnNaiAuditDetails vulnNaiAuditDetails : vulnNaiAuditDetailsList) {
	    logger.info("VulnNaiAuditDetails: " + vulnNaiAuditDetails);
	    vulnNaiAuditDetailsMap.put(vulnNaiAuditDetails.getAppCompVulnKey(),
		    vulnNaiAuditDetails);
	}
	return vulnNaiAuditDetailsMap;
    }

}
