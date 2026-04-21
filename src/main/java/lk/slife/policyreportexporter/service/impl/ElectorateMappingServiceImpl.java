package lk.slife.policyreportexporter.service.impl;

import lk.slife.policyreportexporter.dto.excel.ExcelDataResponseDTO;
import lk.slife.policyreportexporter.dto.excel.ExcelExtractorRequestDTO;
import lk.slife.policyreportexporter.entity.postgres.ElectoratePolicyEntity;
import lk.slife.policyreportexporter.repository.postgres.ElectoratePolicyRepository;
import lk.slife.policyreportexporter.service.ElectorateMappingService;
import lk.slife.policyreportexporter.service.ExcelDataExtractorEnhancedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElectorateMappingServiceImpl implements ElectorateMappingService {

    @Value("${policy.data.file.path}")
    private String policyDataFilePath;

    private final ExcelDataExtractorEnhancedService excelDataExtractorService;
    private final ElectoratePolicyRepository policyRepository;

    /**
     * Uploads policy data from an Excel file to the ElectoratePolicyEntity.
     * Only the policy number, address, and city fields are imported.
     */
    @Override
    public void uploadPolicyData(){

        ExcelExtractorRequestDTO requestDTO = ExcelExtractorRequestDTO.builder()
                .filePath(policyDataFilePath)
                .sheetIndex(0)
                .headerRow(2)
                .dataRow(3)
                .build();

        ExcelDataResponseDTO responseDTO = excelDataExtractorService.extractExcelFileFromPath(requestDTO);

        if (responseDTO == null || responseDTO.getExtractedData() == null) {
            log.warn("No data extracted from file: {}", policyDataFilePath);
            return;
        }

        List<ElectoratePolicyEntity> entities = new ArrayList<>();

        for (Map<String, Object> row : responseDTO.getExtractedData()) {
            Object policyNumberVal = row.get("PN");
            Object addressVal = row.get("Address");

            if (policyNumberVal == null || addressVal == null) {
                continue;
            }

            ElectoratePolicyEntity entity = new ElectoratePolicyEntity();
            entity.setPolicyNumber(policyNumberVal.toString());
            entity.setAddress(addressVal.toString());

            Object cityVal = row.get("City");
            if (cityVal != null) {
                entity.setCity(cityVal.toString());
            }

            entities.add(entity);
        }

        log.info("Mapped {} records from Excel file", entities.size());

        policyRepository.saveAll(entities);
        log.info("Saved {} records to database", entities.size());
    }
}
