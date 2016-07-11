package com.blackducksoftware.tools.appedit.naiaudit.dao.cc;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.commonframework.core.exception.CommonFrameworkException;
import com.blackducksoftware.tools.connector.codecenter.ICodeCenterServerWrapper;

public class CcComponentCachePopulator {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private ICodeCenterServerWrapper ccsw;

	@Inject
	public void setCcsw(final ICodeCenterServerWrapper ccsw) {
		this.ccsw = ccsw;
	}

	private AppEditConfigManager config;

	@Inject
	public void setConfig(final AppEditConfigManager config) {
		this.config = config;
	}

	public CcComponentCachePopulator() {
	}

	public void run() {
		if (!config.isNaiAuditPreloadComponents()) {
			logger.debug("Periodically loading/updating of components into cache is disabled.");
			return;
		}
		try {
			logger.info("Pre-loading component cache from catalog");

			ccsw.getComponentManager()
			.populateComponentCacheFromCatalog(config.getNaiAuditPreloadComponentsBatchSize());
			logger.info("Done pre-loading component cache from catalog");
		} catch (final CommonFrameworkException e) {
			logger.error("Error pre-populating component cache from catalog; the NAI Audit screen will take longer to load");
		}
	}
}
