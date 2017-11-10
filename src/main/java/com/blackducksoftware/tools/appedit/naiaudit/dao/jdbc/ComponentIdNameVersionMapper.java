/**
 * Application Details Edit Webapp
 *
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.blackducksoftware.tools.appedit.naiaudit.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.blackducksoftware.tools.appedit.naiaudit.model.IdNameVersion;


public class ComponentIdNameVersionMapper implements
 RowMapper<IdNameVersion> {

	@Override
	public IdNameVersion mapRow(final ResultSet rs, final int rowNum)
			throws SQLException {

		final IdNameVersion result = new IdNameVersion(rs.getString("id"), rs.getString("name"),
				rs.getString("version"));
		return result;
	}

}
