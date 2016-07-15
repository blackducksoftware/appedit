package com.blackducksoftware.tools.appedit.naiaudit;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;

public class NaiAuditNoChangeException extends AppEditException {

	public NaiAuditNoChangeException(final String message) {
		super(message);
	}

	public NaiAuditNoChangeException(final Throwable cause) {
		super(cause);
	}

	public NaiAuditNoChangeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public NaiAuditNoChangeException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
