package com.blackducksoftware.tools.appedit.naiaudit.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;

public class VulnNaiAuditDetailsMapper implements
	RowMapper<VulnNaiAuditDetails> {

    @Override
    public VulnNaiAuditDetails mapRow(ResultSet rs, int rowNum)
	    throws SQLException {

	AppCompVulnKey key = new AppCompVulnKey(rs.getString("application_id"),
		rs.getString("request_id"), rs.getString("component_id"),
		rs.getString("vulnerability_id"));
	VulnNaiAuditDetails result = new VulnNaiAuditDetails(key,
		rs.getString("nai_audit_status"),
		rs.getString("nai_audit_comment"));
	return result;
    }

}
