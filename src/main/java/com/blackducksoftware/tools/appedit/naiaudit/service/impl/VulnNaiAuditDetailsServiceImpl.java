package com.blackducksoftware.tools.appedit.naiaudit.service.impl;

import java.util.List;

import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnNaiAuditDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;

public class VulnNaiAuditDetailsServiceImpl implements VulnNaiAuditDetailsService {

    // DAO objects (wired)
    private VulnNaiAuditDetailsDao vulnNaiAuditDetailsDao;

    public void setVulnNaiAuditDetailsDao(VulnNaiAuditDetailsDao vulnNaiAuditDetailsDao) {
        this.vulnNaiAuditDetailsDao = vulnNaiAuditDetailsDao;
    }

    @Override
    public List<VulnNaiAuditDetails> getVulnNaiAuditDetailsList(String applicationId) {

        return vulnNaiAuditDetailsDao.getVulnNaiAuditDetailsList(applicationId);
    }

    @Override
    public VulnNaiAuditDetails updateVulnNaiAuditDetails(VulnNaiAuditDetails vulnNaiAuditDetails) {
        return vulnNaiAuditDetailsDao.updateVulnNaiAuditDetails(vulnNaiAuditDetails);
    }

}
