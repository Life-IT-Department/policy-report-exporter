package lk.slife.policyreportexporter.service.impl;

import lk.slife.policyreportexporter.clients.AusysWebClient;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyPinMappingServiceImpl implements PolicyPinMappingService {

    private final MasterProposalRepository masterProposalRepository;
    private final MasterPersonDataRepository masterPersonDataRepository;
    private final ProposalDataRepository proposalDataRepository;
    private final AusysWebClient ausysWebClient;

    @Value("${storage.proposal.pdf.path}")
    private String fileStoragePath;

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
        log.info("Saving proposal data....");
        proposalDataRepository.saveAll(result);
        log.info("Saved {} proposals", result.size());
        return result;
    }

    @Override
    public void transferMergedPdfs(){
        List<ProposalDataEntity> proposalEntities = proposalDataRepository.findAll();
        for (ProposalDataEntity entity : proposalEntities){
            String proposalNo = entity.getProposalNo();
            byte[] bytes = ausysWebClient.getProposalPdf(proposalNo);

            if (bytes != null) {

                int sizeInKB = bytes.length / 1024;
                log.info("Received PDF for proposalNo={} with size={} KB", proposalNo, sizeInKB);

                if (bytes.length > (30 * 1024)) { // 30KB check

                    try {
                        savePdfFile(proposalNo, bytes);
                        entity.setMergedPdfAvailable(Boolean.TRUE);
                    } catch (IOException e) {
                        log.error("Error saving PDF for proposalNo={}", proposalNo, e);
                    }

                } else {
                    log.warn("Skipped saving PDF for proposalNo={}, size < 30KB", proposalNo);
                }

            } else {
                log.warn("No PDF received for proposalNo={}", proposalNo);
            }

        }
        log.info("Updating entities.....");
        proposalDataRepository.saveAll(proposalEntities);
        log.info("Done.....!!!");
    }

    private void savePdfFile(String proposalNo, byte[] bytes) throws IOException {
        // Base directory from property
        Path baseDirectory = Paths.get(fileStoragePath);
        // Create proposal-specific folder
        Path proposalDirectory = baseDirectory.resolve(proposalNo);

        // Ensure directories exist
        if (!Files.exists(proposalDirectory)) {
            Files.createDirectories(proposalDirectory);
            log.info("Created directory: {}", proposalDirectory);
        }

        // File path inside proposal folder
        String fileName = proposalNo + ".pdf";
        Path filePath = proposalDirectory.resolve(fileName);
        // Write file
        Files.write(filePath, bytes);

        log.info("PDF saved successfully at: {}", filePath);
    }

    @Transactional
    @Override
    public void updateMergePdfStatus() {

        List<ProposalDataEntity> proposalEntities = proposalDataRepository.findAll();

        Set<String> folderNames = new HashSet<>(getFolderNames());

        log.info("UPDATE_MERGED_PDF_STATUS: Started. Total proposals={}, total folders={}",
                proposalEntities.size(), folderNames.size());

        int updatedCount = 0;

        for (ProposalDataEntity entity : proposalEntities) {

            String proposalNo = entity.getProposalNo();

            if (folderNames.contains(proposalNo) && !entity.isMergedPdfAvailable()) {

                entity.setMergedPdfAvailable(true);
                updatedCount++;

                log.debug("UPDATE_MERGED_PDF_STATUS: Marked TRUE for proposalNo={}", proposalNo);
            }
        }

        proposalDataRepository.saveAll(proposalEntities);

        log.info("UPDATE_MERGED_PDF_STATUS: Completed. Updated records={}", updatedCount);
    }

    private List<String> getFolderNames() {

        List<String> folderNames = new ArrayList<>();

        Path basePath = Paths.get(fileStoragePath); // your network path

        try (Stream<Path> paths = Files.list(basePath)) {

            folderNames = paths
                    .filter(Files::isDirectory) // only folders
                    .map(path -> path.getFileName().toString()) // get folder name
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("Error reading folders from path: {}", fileStoragePath, e);
        }

        return folderNames;
    }
}
