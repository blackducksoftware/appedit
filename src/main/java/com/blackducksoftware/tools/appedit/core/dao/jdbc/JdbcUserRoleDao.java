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
