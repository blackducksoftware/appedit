package com.blackducksoftware.tools.appedit.naiaudit.dao;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.IdNameVersion;

// TODO comments
public interface ComponentNameVersionDao {
	void init();

	IdNameVersion getComponentNameVersionById(String componentId) throws AppEditException;

	void cachePossiblyNaiComponents() throws AppEditException;
}
