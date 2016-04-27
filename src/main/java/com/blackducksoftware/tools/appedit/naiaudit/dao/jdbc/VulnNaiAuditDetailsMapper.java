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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;

/**
 * Builds a VulnNaiAuditDetails object from result sets fetched from the NAI
 * Audit Details table.
 * 
 * @author sbillings
 *
 */
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
