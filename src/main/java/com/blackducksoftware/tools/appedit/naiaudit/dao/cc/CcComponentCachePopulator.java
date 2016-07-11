package com.blackducksoftware.tools.appedit.naiaudit.dao.cc;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.core.AppEditConfigManager;
import com.blackducksoftware.tools.commonframework.core.exception.CommonFrameworkException;
import com.blackducksoftware.tools.connector.codecenter.ICodeCenterServerWrapper;

public class CcComponentCachePopulator {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	private final int COMP_CACHE_SIZE = 10000;
	private final int COMP_CACHE_EXPIRATION_DAYS = 2;

	private boolean firstRunDone = false;

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
		try {
			logger.info("Pre-loading component cache from catalog");

			if (!firstRunDone) {
				logger.info("First run: resetting cache: size: " + COMP_CACHE_SIZE + "; expiration (days): "
						+ COMP_CACHE_EXPIRATION_DAYS);
				// TODO configurable
				ccsw.getComponentManager().resetComponentCache(COMP_CACHE_SIZE, COMP_CACHE_EXPIRATION_DAYS,
						TimeUnit.DAYS);
				firstRunDone = true;
			}

			ccsw.getComponentManager().populateComponentCacheFromCatalog(config.getNaiAuditPreloadBatchSize());
			logger.info("Done pre-loading component cache from catalog");
		} catch (final CommonFrameworkException e) {
			logger.error("Error pre-populating component cache from catalog; the NAI Audit screen will take longer to load");
		}
	}
}
