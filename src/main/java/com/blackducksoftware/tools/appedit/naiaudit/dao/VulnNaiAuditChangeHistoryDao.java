/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.blackducksoftware.tools.appedit.naiaudit.dao;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditChange;

/**
 * Data Access Object for the change history (audit trail) that records changes
 * to NAI Audit data.
 * 
 * @author sbillings
 *
 */
public interface VulnNaiAuditChangeHistoryDao {
    void insertVulnNaiAuditChange(VulnNaiAuditChange vunlNaiAuditChange)
	    throws AppEditException;

}
