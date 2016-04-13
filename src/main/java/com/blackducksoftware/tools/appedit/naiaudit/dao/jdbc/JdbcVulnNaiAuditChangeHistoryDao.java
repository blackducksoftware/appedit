package com.blackducksoftware.tools.appedit.naiaudit.dao.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.blackducksoftware.tools.appedit.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnNaiAuditChangeHistoryDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditChange;

public class JdbcVulnNaiAuditChangeHistoryDao implements
	VulnNaiAuditChangeHistoryDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());

    private NamedParameterJdbcTemplate jdbcTemplate;

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
