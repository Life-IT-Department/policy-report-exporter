package lk.slife.policyreportexporter.service.impl;

import lk.slife.policyreportexporter.entity.mariadb.MasterPersonDataEntity;
import lk.slife.policyreportexporter.entity.postgres.PinDataEntity;
import lk.slife.policyreportexporter.entity.postgres.ProposalDataEntity;
import lk.slife.policyreportexporter.repository.mariadb.MasterPersonDataRepository;
import lk.slife.policyreportexporter.repository.mariadb.MasterProposalRepository;
import lk.slife.policyreportexporter.repository.postgres.ProposalDataRepository;
import lk.slife.policyreportexporter.service.PolicyPinMappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyPinMappingServiceImpl implements PolicyPinMappingService {

    private final MasterProposalRepository masterProposalRepository;
    private final MasterPersonDataRepository masterPersonDataRepository;
    private final ProposalDataRepository proposalDataRepository;

    @Override
    public List<ProposalDataEntity> getLatestProposalNos() {

        List<String> orderedProposals = masterProposalRepository.findDistinctProposalNosOrderedByLatest();
        List<MasterPersonDataEntity> allPersonData = masterPersonDataRepository.findAll();

        List<ProposalDataEntity> result = new ArrayList<>();
        log.info("Proposal Pin mapping starting....");

        for (String proposalNo : orderedProposals) {

            List<MasterPersonDataEntity> matched = allPersonData.stream()
                    .filter(p -> isValidProposalNo(p.getProposalNo()) && p.getProposalNo().equalsIgnoreCase(proposalNo) && isPinValid(p.getPin()))
                    .toList();

            if (!matched.isEmpty()) {
                ProposalDataEntity proposal = new ProposalDataEntity();
                proposal.setProposalNo(proposalNo);

                List<PinDataEntity> pins = matched.stream()
                        .map(p -> {
                            PinDataEntity pin = new PinDataEntity();
                            pin.setPin(p.getPin());
                            pin.setProposal(proposal);
                            return pin;
                        })
                        .toList();

                proposal.setPins(pins);
                result.add(proposal);
                log.info("Proposal No: {} mapped to {} pins", proposalNo, pins.size());
            }
        }
        proposalDataRepository.saveAll(result);
        log.info("Saved {} proposals", result.size());
        return result;
    }

    private boolean isValidProposalNo(String proposalNo) {
        if (proposalNo == null || proposalNo.isBlank()) {
            return false;
        }
        return true;
    }

    private boolean isPinValid(String pin) {
        if (pin == null || pin.isBlank()) {
            return false;
        }
        try {
            return Integer.parseInt(pin.trim()) > 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
