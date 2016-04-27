/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
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

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Inject
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
	this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertVulnNaiAuditChange(VulnNaiAuditChange vunlNaiAuditChange)
	    throws AppEditException {
	String SQL = "INSERT INTO vuln_nai_audit_change_history (change_time, application_id, request_id, component_id, vulnerability_id, "
		+ "cc_user_name, old_nai_audit_status, old_nai_audit_comment, new_nai_audit_status, new_nai_audit_comment) "
		+ "VALUES (:changeTime, :appId, :requestId, :compId, :vulnId, :ccUserName, :oldNaiAuditStatus, :oldNaiAuditComment, :newNaiAuditStatus, :newNaiAuditComment)";
	Map<String, Object> namedParameters = new HashMap<>();

	namedParameters.put("changeTime", vunlNaiAuditChange.getChangeTime());
	namedParameters.put("appId", vunlNaiAuditChange.getAppCompVulnKey()
		.getApplicationId());
	namedParameters.put("requestId", vunlNaiAuditChange.getAppCompVulnKey()
		.getRequestId());
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

	jdbcTemplate.update(SQL, namedParameters);
	logger.debug("Inserted Vuln NAI Audit Change Record for: "
		+ vunlNaiAuditChange);

    }

}
