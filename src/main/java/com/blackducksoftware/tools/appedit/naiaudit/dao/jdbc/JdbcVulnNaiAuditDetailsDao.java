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

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnNaiAuditDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;

/**
 * JDBC DAO that adds/gets/updates NAI Audit details.
 * 
 * @author sbillings
 *
 */
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

    /**
     * Update a set of NAI Audit details.
     * 
     * @param vunlNaiAuditDetails
     * @return
     * @throws AppEditException
     */
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

    /**
     * Update a set of NAI Audit details.
     * 
     * @param vunlNaiAuditDetails
     * @return
     * @throws AppEditException
     */
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

    /**
     * Get a map containing all NAI Audit Details for the given application.
     * 
     * @param applicationId
     * @return
     * @throws AppEditException
     */
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

    @Override
    public VulnNaiAuditDetails getVulnNaiAuditDetails(AppCompVulnKey key)
	    throws AppEditException {

	String SQL = "SELECT application_id, request_id, component_id, vulnerability_id, nai_audit_status, nai_audit_comment FROM vuln_nai_audit "
		+ "WHERE application_id = :appId AND request_id = :requestId AND component_id = :compId AND vulnerability_id = :vulnId";
	Map<String, String> paramMap = new HashMap<>();
	paramMap.put("appId", key.getApplicationId());
	paramMap.put("requestId", key.getRequestId());
	paramMap.put("compId", key.getComponentId());
	paramMap.put("vulnId", key.getVulnerabilityId());
	SqlParameterSource namedParameters = new MapSqlParameterSource(paramMap);
	logger.debug("Getting vulnNaiAuditDetails for key " + key + "; SQL: "
		+ SQL);

	List<VulnNaiAuditDetails> vulnNaiAuditDetailsList = null;
	try {
	    vulnNaiAuditDetailsList = (List<VulnNaiAuditDetails>) jdbcTemplate
		    .query(SQL, namedParameters,
			    new VulnNaiAuditDetailsMapper());
	} catch (BadSqlGrammarException e) {
	    String msg = "Error getting NAI Audit details for vulnerability. Make sure the NAI Audit database tables have been created. Details: "
		    + e.getMessage();
	    logger.error(msg);
	    throw new IllegalStateException(msg);
	}

	logger.debug("Read " + vulnNaiAuditDetailsList.size()
		+ " vulnNaiAuditDetail records.");

	if (vulnNaiAuditDetailsList.size() == 0) {
	    logger.warn("No NAI Audit details record found for key: " + key);
	    return null;
	} else if (vulnNaiAuditDetailsList.size() > 1) {
	    String msg = "The NAI Audit details contains "
		    + vulnNaiAuditDetailsList.size() + "records with key "
		    + key + "; it should contains at most one";
	    logger.error(msg);
	    throw new IllegalStateException(msg);
	}

	return vulnNaiAuditDetailsList.get(0);
    }

}
