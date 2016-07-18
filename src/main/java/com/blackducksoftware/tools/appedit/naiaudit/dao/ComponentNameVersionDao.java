package com.blackducksoftware.tools.appedit.naiaudit.dao;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.IdNameVersion;

/**
 * A Data Access Object for component name/version data.
 *
 * @author sbillings
 *
 */
public interface ComponentNameVersionDao {

	IdNameVersion getComponentNameVersionById(String componentId) throws AppEditException;

	void cachePossiblyNaiComponents() throws AppEditException;
}
