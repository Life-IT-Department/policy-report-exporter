package lk.slife.policyreportexporter.service;

import lk.slife.policyreportexporter.entity.postgres.ProposalDataEntity;

import java.util.List;

public interface PolicyPinMappingService {

    List<ProposalDataEntity> getLatestProposalNos();
}
