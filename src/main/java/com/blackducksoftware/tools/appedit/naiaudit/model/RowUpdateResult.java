package com.blackducksoftware.tools.appedit.naiaudit.model;

public class RowUpdateResult {
	private final NaiAuditUpdateStatus status;
	private final String message;
	private final AppCompVulnComposite newRowData;

	public RowUpdateResult(final NaiAuditUpdateStatus status, final String message,
			final AppCompVulnComposite newRowData) {
		super();
		this.status = status;
		this.message = message;
		this.newRowData = newRowData;
	}

	public NaiAuditUpdateStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public AppCompVulnComposite getNewRowData() {
		return newRowData;
	}

	@Override
	public String toString() {
		return "RowUpdateResult [status=" + status + ", message=" + message + "]";
	}

}
