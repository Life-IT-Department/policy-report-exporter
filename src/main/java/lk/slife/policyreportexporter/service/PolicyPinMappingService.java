package lk.slife.policyreportexporter.service;

import lk.slife.policyreportexporter.entity.postgres.ProposalDataEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PolicyPinMappingService {

    List<ProposalDataEntity> getLatestProposalNos();

    void transferMergedPdfs();

    @Transactional
    void updateMergePdfStatus();
}
