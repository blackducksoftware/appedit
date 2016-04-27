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

import java.util.Map;

import com.blackducksoftware.tools.appedit.core.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;

/**
 * Interface for DAOs that add/get/update NAI Audit details.
 * 
 * @author sbillings
 *
 */
public interface VulnNaiAuditDetailsDao {

    VulnNaiAuditDetails insertVulnNaiAuditDetails(
	    VulnNaiAuditDetails vunlNaiAuditDetails) throws AppEditException;

    VulnNaiAuditDetails updateVulnNaiAuditDetails(
	    VulnNaiAuditDetails vunlNaiAuditDetails) throws AppEditException;

    Map<AppCompVulnKey, VulnNaiAuditDetails> getVulnNaiAuditDetailsMap(
	    String applicationId) throws AppEditException;
}
