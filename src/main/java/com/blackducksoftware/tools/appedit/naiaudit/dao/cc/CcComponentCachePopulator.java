package com.blackducksoftware.tools.appedit.naiaudit.dao.cc;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.dao.ComponentNameVersionDao;

public class CcComponentCachePopulator {
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

	public CcComponentCachePopulator() {
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
