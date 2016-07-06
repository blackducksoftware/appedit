package com.blackducksoftware.tools.appedit.naiaudit.dao.jdbc;

public class RemediationStatusName {
	private final long id;
	private final String name;

	public RemediationStatusName(final long id, final String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "RemediationStatusName [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final RemediationStatusName other = (RemediationStatusName) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}



}
