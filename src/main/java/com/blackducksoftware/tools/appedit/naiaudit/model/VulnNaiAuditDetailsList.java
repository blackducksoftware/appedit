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
package com.blackducksoftware.tools.appedit.naiaudit.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A list of vulnerability-specific NAI Audit details.
 * 
 * @author sbillings
 *
 */
@XmlRootElement(name = "vulnNaiAuditDetailsList")
public class VulnNaiAuditDetailsList {
    private List<VulnNaiAuditDetails> vulnNaiAuditDetailsList;

    public VulnNaiAuditDetailsList() {
	vulnNaiAuditDetailsList = new ArrayList<>();
    }

    public VulnNaiAuditDetailsList(
	    List<VulnNaiAuditDetails> vulnNaiAuditDetailsList) {
	this.vulnNaiAuditDetailsList = vulnNaiAuditDetailsList;
    }

    public List<VulnNaiAuditDetails> getVulnNaiAuditDetailsList() {
	return vulnNaiAuditDetailsList;
    }

    public void setVulnNaiAuditDetailsList(
	    List<VulnNaiAuditDetails> vulnNaiAuditDetailsList) {
	this.vulnNaiAuditDetailsList = vulnNaiAuditDetailsList;
    }

}
