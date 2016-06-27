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
	public void setJdbcTemplate(final NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
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

		jdbcTemplate.update(SQL, namedParameters);
		logger.debug("Inserted Vuln NAI Audit Change Record for: "
				+ vunlNaiAuditChange);

	}

}
