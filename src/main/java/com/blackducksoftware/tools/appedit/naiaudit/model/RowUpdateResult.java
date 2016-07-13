package com.blackducksoftware.tools.appedit.naiaudit.model;

public class RowUpdateResult {
	private final boolean success;
	private final String message;
	private final AppCompVulnComposite newRowData;

	public RowUpdateResult(final boolean success, final String message, final AppCompVulnComposite newRowData) {
		super();
		this.success = success;
		this.message = message;
		this.newRowData = newRowData;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public AppCompVulnComposite getNewRowData() {
		return newRowData;
	}
}
