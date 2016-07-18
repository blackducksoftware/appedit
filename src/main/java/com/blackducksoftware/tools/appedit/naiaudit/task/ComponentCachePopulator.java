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
package com.blackducksoftware.tools.appedit.naiaudit.task;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.dao.ComponentNameVersionDao;

public class ComponentCachePopulator {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private AppEditConfigManager config;

	@Inject
	public void setConfig(final AppEditConfigManager config) {
		this.config = config;
	}

	private ComponentNameVersionDao componentNameVersionDao;

	@Inject
	public void setComponentNameVersionDao(final ComponentNameVersionDao componentNameVersionDao) {
		this.componentNameVersionDao = componentNameVersionDao;
	}

	public ComponentCachePopulator() {
	}

	public void run() {
		if (!config.isNaiAuditPreloadComponents()) {
			logger.debug("Periodically loading/updating of components into cache is disabled.");
			return;
		}
		try {
			logger.info("Pre-loading component name version cache with possibly-NAI components");

			componentNameVersionDao.cachePossiblyNaiComponents();
			logger.info("Done Pre-loading component name version cache with possibly-NAI components");
		} catch (final AppEditException e) {
			logger.error("Error pre-populating component cache from catalog; the NAI Audit screen will take longer to load");
		}
	}
}
