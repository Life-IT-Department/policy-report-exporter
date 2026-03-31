package lk.slife.policyreportexporter.service.impl;

import lk.slife.policyreportexporter.entity.postgres.PinDataEntity;
import lk.slife.policyreportexporter.entity.postgres.ProposalDataEntity;
import lk.slife.policyreportexporter.repository.mariadb.MasterPersonDataRepository;
import lk.slife.policyreportexporter.repository.mariadb.MasterProposalRepository;
import lk.slife.policyreportexporter.repository.mariadb.projection.PersonDataView;
import lk.slife.policyreportexporter.repository.mariadb.projection.ProposalView;
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

        // Collect distinct proposal numbers with sysDate
        List<ProposalView> orderedProposals = masterProposalRepository.findDistinctProposalNosOrderedByLatest();
        // Collect distinct pin numbers that are > 10
        List<PersonDataView> allPersonData = masterPersonDataRepository.findDistinctValidPinsWithMaxId();

        List<ProposalDataEntity> result = new ArrayList<>();
        log.info("Proposal Pin mapping starting....");

        for (ProposalView proposalView : orderedProposals) {
            String proposalNo = proposalView.proposalNo();
            // Collect matching pin numbers
            List<PersonDataView> matched = allPersonData.stream()
                    .filter(p -> proposalNo.equalsIgnoreCase(p.proposalNo()))
                    .toList();

            if (!matched.isEmpty()) {
                ProposalDataEntity proposal = new ProposalDataEntity();
                proposal.setProposalNo(proposalNo);
                proposal.setSysDate(proposalView.sysDate());

                List<PinDataEntity> pins = matched.stream()
                        .map(p -> {
                            PinDataEntity pin = new PinDataEntity();
                            pin.setPin(p.pin());
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
}
