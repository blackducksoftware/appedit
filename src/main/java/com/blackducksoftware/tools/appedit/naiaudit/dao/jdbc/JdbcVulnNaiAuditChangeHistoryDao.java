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
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnNaiAuditChangeHistoryDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditChange;

/**
 * JDBC Data Access Object for the change history (audit trail) that records
 * changes to NAI Audit data.
 *
 * @author sbillings
 *
 */
public class JdbcVulnNaiAuditChangeHistoryDao implements
VulnNaiAuditChangeHistoryDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getName());

	private NamedParameterJdbcTemplate jdbcTemplateVulnNaiAudit;

	@Inject
	public void setJdbcTemplate(final NamedParameterJdbcTemplate jdbcTemplateVulnNaiAudit) {
		this.jdbcTemplateVulnNaiAudit = jdbcTemplateVulnNaiAudit;
	}

	/**
	 * Add a new NAI Audit Change history record.
	 *
	 * @param vunlNaiAuditChange
	 * @throws AppEditException
	 */
	@Override
	public void insertVulnNaiAuditChange(final VulnNaiAuditChange vunlNaiAuditChange)
			throws AppEditException {
		final String SQL = "INSERT INTO vuln_nai_audit_change_history (change_time, application_id, componentuse_id, component_id, vulnerability_id, "
				+ "cc_user_name, old_nai_audit_status, old_nai_audit_comment, new_nai_audit_status, new_nai_audit_comment) "
				+ "VALUES (:changeTime, :appId, :compUseId, :compId, :vulnId, :ccUserName, :oldNaiAuditStatus, :oldNaiAuditComment, :newNaiAuditStatus, :newNaiAuditComment)";
		final Map<String, Object> namedParameters = new HashMap<>();

		namedParameters.put("changeTime", vunlNaiAuditChange.getChangeTime());
		namedParameters.put("appId", vunlNaiAuditChange.getAppCompVulnKey()
				.getApplicationId());
		namedParameters.put("compUseId", vunlNaiAuditChange.getAppCompVulnKey().getRequestId());
		namedParameters.put("compId", vunlNaiAuditChange.getAppCompVulnKey()
				.getComponentId());
		namedParameters.put("vulnId", vunlNaiAuditChange.getAppCompVulnKey()
				.getVulnerabilityId());

		namedParameters.put("ccUserName", vunlNaiAuditChange.getCcUserName());

		namedParameters.put("oldNaiAuditStatus",
				vunlNaiAuditChange.getOldNaiAuditStatus());
		namedParameters.put("oldNaiAuditComment",
				vunlNaiAuditChange.getOldNaiAuditComment());
		namedParameters.put("newNaiAuditStatus",
				vunlNaiAuditChange.getNewNaiAuditStatus());
		namedParameters.put("newNaiAuditComment",
				vunlNaiAuditChange.getNewNaiAuditComment());

		jdbcTemplateVulnNaiAudit.update(SQL, namedParameters);
		logger.debug("Inserted Vuln NAI Audit Change Record for: "
				+ vunlNaiAuditChange);

	}

}
