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
package com.blackducksoftware.tools.appedit.core.dao.jdbc;

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

import com.blackducksoftware.tools.appedit.core.dao.UserRoleDao;
import com.blackducksoftware.tools.appedit.core.exception.AppEditException;

/**
 * JDBC/SQL Data Access Object for user role details.
 *
 * @author sbillings
 *
 */
public class JdbcUserRoleDao implements UserRoleDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	private final String SQL_FETCH_USER_ROLE_ASSIGNMENT = "SELECT id FROM role_assignment WHERE enduser = :userId AND role = :roleId AND application is null";
	private NamedParameterJdbcTemplate jdbcTemplateBdsCatalog;

	@Inject
	public void setJdbcTemplateBdsCatalog(final NamedParameterJdbcTemplate jdbcTemplateBdsCatalog) {
		this.jdbcTemplateBdsCatalog = jdbcTemplateBdsCatalog;
	}

	public JdbcUserRoleDao() {
	}

	/**
	 * Returns true if the given user has the given role.
	 *
	 * @param userId
	 * @param RoleId
	 * @return
	 * @throws AppEditException
	 */
	@Override
	public boolean userHasRole(final long userId, final String roleId) throws AppEditException {

		final Map<String, Object> values = new HashMap<>(2);
		values.put("userId", userId);
		values.put("roleId", roleId);
		final SqlParameterSource namedParameters = new MapSqlParameterSource(values);
		final String queryString = SQL_FETCH_USER_ROLE_ASSIGNMENT;
		List<Integer> roleAssignmentIds;
		try {
			logger.debug("JDBC: Executing query: " + queryString);
			roleAssignmentIds = jdbcTemplateBdsCatalog.queryForList(queryString, namedParameters, Integer.class);
			logger.debug("JDBC: Done executing query; item count: " + roleAssignmentIds.size());
		} catch (final BadSqlGrammarException e) {
			logger.debug("JDBC: Error executing query");
			final String msg = "Error getting user role with userId " + userId + " and roleId " + roleId + ": "
					+ e.getMessage();
			logger.error(msg);
			throw new AppEditException(msg);
		}
		if (roleAssignmentIds.size() == 0) {
			return false;
		} else if (roleAssignmentIds.size() == 1) {
			return true;
		}
		throw new AppEditException("Query for role assignment with userId " + userId + " and roleId " + roleId
				+ " returned an invalid number of records: " + roleAssignmentIds.size());

	}

}
