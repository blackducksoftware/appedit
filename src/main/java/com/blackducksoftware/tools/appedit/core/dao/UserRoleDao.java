package com.blackducksoftware.tools.appedit.core.dao;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;

public interface UserRoleDao {
	boolean userHasRole(long userId, String RoleId) throws AppEditException;
}
