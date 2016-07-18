/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
	private static final String SQL_INSERT_VULNERABILITY = "INSERT INTO vuln_nai_audit (application_id, componentuse_id, component_id, vulnerability_id, nai_audit_status, nai_audit_comment) VALUES (:appId, :compUseId, :compId, :vulnId, :naiAuditStatus, :naiAuditComment)";

	private static final String SQL_UPDATE_VULNERABILITIES = "UPDATE vuln_nai_audit SET nai_audit_status=:naiAuditStatus, nai_audit_comment=:naiAuditComment WHERE application_id = :appId AND componentuse_id = :compUseId AND component_id = :compId AND vulnerability_id = :vulnId";

	private static final String SQL_SELECT_ALL_VULNERABILITIES_FOR_APP = "SELECT application_id, componentuse_id, component_id, vulnerability_id, nai_audit_status, nai_audit_comment FROM vuln_nai_audit WHERE application_id = :appId";

	private static final String SQL_FETCH_ONE_VULNERABILITY_BY_KEY = "SELECT application_id, componentuse_id, component_id, vulnerability_id, nai_audit_status, nai_audit_comment FROM vuln_nai_audit WHERE application_id = :appId AND componentuse_id = :compUseId AND component_id = :compId AND vulnerability_id = :vulnId";

	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getName());

	private NamedParameterJdbcTemplate jdbcTemplateVulnNaiAudit;

	@Inject
	public void setJdbcTemplate(final NamedParameterJdbcTemplate jdbcTemplateVulnNaiAudit) {
		this.jdbcTemplateVulnNaiAudit = jdbcTemplateVulnNaiAudit;
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
			final VulnNaiAuditDetails vulnNaiAuditDetails) {

		final String SQL = SQL_INSERT_VULNERABILITY;
		final Map<String, String> namedParameters = new HashMap<>();
		namedParameters.put("appId", vulnNaiAuditDetails.getAppCompVulnKey()
				.getApplicationId());
		namedParameters.put("compUseId", vulnNaiAuditDetails.getAppCompVulnKey().getRequestId());
		namedParameters.put("compId", vulnNaiAuditDetails.getAppCompVulnKey()
				.getComponentId());
		namedParameters.put("vulnId", vulnNaiAuditDetails.getAppCompVulnKey()
				.getVulnerabilityId());
		namedParameters.put("naiAuditStatus",
				vulnNaiAuditDetails.getVulnerabilityNaiAuditStatus());
		namedParameters.put("naiAuditComment",
				vulnNaiAuditDetails.getVulnerabilityNaiAuditComment());

		jdbcTemplateVulnNaiAudit.update(SQL, namedParameters);
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
			final VulnNaiAuditDetails vulnNaiAuditDetails) {

		final String SQL = SQL_UPDATE_VULNERABILITIES;
		final Map<String, String> namedParameters = new HashMap<>();
		namedParameters.put("appId", vulnNaiAuditDetails.getAppCompVulnKey()
				.getApplicationId());
		namedParameters.put("compUseId", vulnNaiAuditDetails.getAppCompVulnKey().getRequestId());
		namedParameters.put("compId", vulnNaiAuditDetails.getAppCompVulnKey()
				.getComponentId());
		namedParameters.put("vulnId", vulnNaiAuditDetails.getAppCompVulnKey()
				.getVulnerabilityId());
		namedParameters.put("naiAuditStatus",
				vulnNaiAuditDetails.getVulnerabilityNaiAuditStatus());
		namedParameters.put("naiAuditComment",
				vulnNaiAuditDetails.getVulnerabilityNaiAuditComment());

		jdbcTemplateVulnNaiAudit.update(SQL, namedParameters);
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
			final String applicationId) {

		final String SQL = SQL_SELECT_ALL_VULNERABILITIES_FOR_APP;
		final SqlParameterSource namedParameters = new MapSqlParameterSource("appId",
				applicationId);
		logger.debug("Getting vulnNaiAuditDetails for appID " + applicationId
				+ "; SQL: " + SQL);

		List<VulnNaiAuditDetails> vulnNaiAuditDetailsList = null;
		try {
			vulnNaiAuditDetailsList = jdbcTemplateVulnNaiAudit
					.query(SQL, namedParameters,
							new VulnNaiAuditDetailsMapper());
		} catch (final BadSqlGrammarException e) {
			final String msg = "Error getting NAI Audit details. Make sure the NAI Audit database tables have been created. Details: "
					+ e.getMessage();
			logger.error(msg);
			throw new IllegalStateException(msg);
		}

		logger.debug("Read " + vulnNaiAuditDetailsList.size()
				+ " vulnNaiAuditDetail records.");
		final Map<AppCompVulnKey, VulnNaiAuditDetails> vulnNaiAuditDetailsMap = new HashMap<>();
		for (final VulnNaiAuditDetails vulnNaiAuditDetails : vulnNaiAuditDetailsList) {
			logger.debug("VulnNaiAuditDetails: " + vulnNaiAuditDetails);
			vulnNaiAuditDetailsMap.put(vulnNaiAuditDetails.getAppCompVulnKey(),
					vulnNaiAuditDetails);
		}
		return vulnNaiAuditDetailsMap;
	}

	@Override
	public VulnNaiAuditDetails getVulnNaiAuditDetails(final AppCompVulnKey key)
			throws AppEditException {

		final String SQL = SQL_FETCH_ONE_VULNERABILITY_BY_KEY;
		final Map<String, String> paramMap = new HashMap<>();
		paramMap.put("appId", key.getApplicationId());
		paramMap.put("compUseId", key.getRequestId());
		paramMap.put("compId", key.getComponentId());
		paramMap.put("vulnId", key.getVulnerabilityId());
		final SqlParameterSource namedParameters = new MapSqlParameterSource(paramMap);
		logger.debug("Getting vulnNaiAuditDetails for key " + key + "; SQL: "
				+ SQL);

		List<VulnNaiAuditDetails> vulnNaiAuditDetailsList = null;
		try {
			vulnNaiAuditDetailsList = jdbcTemplateVulnNaiAudit
					.query(SQL, namedParameters,
							new VulnNaiAuditDetailsMapper());
		} catch (final BadSqlGrammarException e) {
			final String msg = "Error getting NAI Audit details for vulnerability. Make sure the NAI Audit database tables have been created. Details: "
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
			final String msg = "The NAI Audit details contains "
					+ vulnNaiAuditDetailsList.size() + "records with key "
					+ key + "; it should contains at most one";
			logger.error(msg);
			throw new IllegalStateException(msg);
		}

		return vulnNaiAuditDetailsList.get(0);
	}

}
