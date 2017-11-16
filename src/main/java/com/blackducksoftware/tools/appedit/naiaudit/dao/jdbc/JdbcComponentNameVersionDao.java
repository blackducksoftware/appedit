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

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.dao.ComponentNameVersionDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.IdNameVersion;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class JdbcComponentNameVersionDao implements ComponentNameVersionDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	private static final String SQL_FETCH_POSSIBLY_NAI_COMPONENTS = "SELECT DISTINCT component.id, component.name, component.version"
			+ " FROM component"
			+ " INNER JOIN componentuse ON component.id = componentuse.component"
			+ " INNER JOIN componentuse_vulnerability on componentuse_vulnerability.componentuse_id = componentuse.id";
	private static final String SQL_FETCH_COMPONENT_BY_ID = "SELECT id, name, version FROM component"
			+ " WHERE id = :componentId";
	private boolean initialized = false;
	private LoadingCache<String, IdNameVersion> compNameVersionByIdCache;

	private NamedParameterJdbcTemplate jdbcTemplateBdsCatalog;

	@Inject
	public void setJdbcTemplateBdsCatalog(final NamedParameterJdbcTemplate jdbcTemplateBdsCatalog) {
		this.jdbcTemplateBdsCatalog = jdbcTemplateBdsCatalog;
	}

	private AppEditConfigManager config;

	@Inject
	public void setConfig(final AppEditConfigManager config) {
		this.config = config;
	}

	public JdbcComponentNameVersionDao() {
	}

	private IdNameVersion fetchComponentById(final String componentId) throws AppEditException {
		final SqlParameterSource componentNamedParameters = new MapSqlParameterSource("componentId", componentId);
		final String queryString = SQL_FETCH_COMPONENT_BY_ID;
		List<IdNameVersion> componentIdNameVersions;
		try {
			logger.debug("JDBC: Executing query: " + queryString);
			componentIdNameVersions = jdbcTemplateBdsCatalog.query(queryString, componentNamedParameters,
					new ComponentIdNameVersionMapper());
			logger.debug("JDBC: Done executing query; item count: " + componentIdNameVersions.size());
		} catch (final BadSqlGrammarException e) {
			logger.debug("JDBC: Error executing query");
			final String msg = "Error getting component id/name/version with component ID " + componentId + ": "
					+ e.getMessage();
			logger.error(msg);
			throw new AppEditException(msg);
		}
		logger.debug("Read " + componentIdNameVersions.size() + " component records for component ID " + componentId);

		if (componentIdNameVersions.size() == 0) {
			final String msg = "Error getting component id/name/version with component ID " + componentId
					+ ": Zero records returned";
			logger.error(msg);
			throw new AppEditException(msg);
		} else if (componentIdNameVersions.size() > 1) {
			final String msg = "Error getting component id/name/version with component ID " + componentId + ": "
					+ componentIdNameVersions.size() + " records returned (expected 1)";
			logger.error(msg);
			throw new AppEditException(msg);
		}

		return componentIdNameVersions.get(0);
	}

	@Override
	public IdNameVersion getComponentNameVersionById(final String componentId) throws AppEditException {
		if (!initialized) {
			init();
		}
		logger.debug("getComponentNameVersionById(): getting from cache component with ID: " + componentId);
		IdNameVersion comp;
		try {
			comp = compNameVersionByIdCache.get(componentId);
		} catch (final ExecutionException e) {
			throw new AppEditException("Error getting component with ID " + componentId + " from cache");
		}
		logger.debug("Got component: " + comp.getName() + " / " + comp.getVersion());
		return comp;
	}

	@Override
	public void cachePossiblyNaiComponents() throws AppEditException {
		if (!initialized) {
			init();
		}
		final String queryString = SQL_FETCH_POSSIBLY_NAI_COMPONENTS;
		List<IdNameVersion> componentIdNameVersions;
		try {
			logger.debug("JDBC: Executing query: " + queryString);
			componentIdNameVersions = jdbcTemplateBdsCatalog.query(queryString, new ComponentIdNameVersionMapper());
			logger.debug("JDBC: Done executing query; item count: " + componentIdNameVersions.size());
		} catch (final BadSqlGrammarException e) {
			logger.debug("JDBC: Error executing query");
			final String msg = "Error getting possibly-NAI component id/name/versions: " + e.getMessage();
			logger.error(msg);
			throw new AppEditException(msg);
		}
		logger.debug("Read " + componentIdNameVersions.size() + " possibly-NAI component records");

		for (final IdNameVersion comp : componentIdNameVersions) {
			logger.debug("Caching component: " + comp);
			compNameVersionByIdCache.put(comp.getId(), comp);
		}
	}

	long getCacheSize() {
		if (!initialized) {
			init();
		}
		return compNameVersionByIdCache.size();
	}

	private void init() {
		if (initialized) {
			return;
		}
		final int cacheSize = config.getNaiAuditPreloadComponentsCacheSize();
		final int timeoutValue = config.getNaiAuditPreloadComponentsTimeoutValue();
		final TimeUnit timeoutUnits = config.getNaiAuditPreloadComponentsTimeoutUnits();
		compNameVersionByIdCache = CacheBuilder.newBuilder().maximumSize(cacheSize)
				.expireAfterWrite(timeoutValue, timeoutUnits).build(new CacheLoader<String, IdNameVersion>() {
					@Override
					public IdNameVersion load(final String componentId) throws AppEditException {
						return fetchComponentById(componentId);
					}
				});
		initialized = true;
	}

}
