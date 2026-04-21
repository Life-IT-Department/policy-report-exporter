package lk.slife.policyreportexporter.service.impl;

import lk.slife.policyreportexporter.dto.excel.ExcelDataResponseDTO;
import lk.slife.policyreportexporter.dto.excel.ExcelExtractorRequestDTO;
import lk.slife.policyreportexporter.entity.postgres.CityEntity;
import lk.slife.policyreportexporter.entity.postgres.ElectoratePolicyEntity;
import lk.slife.policyreportexporter.repository.postgres.CityRepository;
import lk.slife.policyreportexporter.repository.postgres.ElectoratePolicyRepository;
import lk.slife.policyreportexporter.service.ElectorateMappingService;
import lk.slife.policyreportexporter.service.ExcelDataExtractorEnhancedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElectorateMappingServiceImpl implements ElectorateMappingService {

    @Value("${policy.data.file.path}")
    private String policyDataFilePath;

    private final ExcelDataExtractorEnhancedService excelDataExtractorService;
    private final ElectoratePolicyRepository policyRepository;
    private final CityRepository cityRepository;

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

    private static final double JARO_WINKLER_THRESHOLD = 0.88;
    private static final JaroWinklerSimilarity JARO_WINKLER = new JaroWinklerSimilarity();

    @Override
    public void mapElectorate(){
        List<ElectoratePolicyEntity> unmappedEntities = policyRepository.findAllByElectorateIsNull();
        log.info("Found {} records with no electorate assigned", unmappedEntities.size());

        if (unmappedEntities.isEmpty()) {
            return;
        }

        Map<String, CityEntity> cityLookup = cityRepository.findAllWithElectorate()
                .stream()
                .collect(Collectors.toMap(
                        c -> normalize(c.getName()),
                        c -> c,
                        (existing, duplicate) -> existing
                ));

        List<ElectoratePolicyEntity> toUpdate = new ArrayList<>();
        int unmatchedCount = 0;

        for (ElectoratePolicyEntity entity : unmappedEntities) {
            CityEntity matched = resolveCity(entity.getCity(), cityLookup);

            if (matched == null) {
                unmatchedCount++;
                continue;
            }

            entity.setElectorate(matched.getElectorate().getName());
            toUpdate.add(entity);
        }

        policyRepository.saveAll(toUpdate);

        log.info("Electorate mapping complete — updated: {}, unmatched: {}", toUpdate.size(), unmatchedCount);
    }

    private CityEntity resolveCity(String rawCity, Map<String, CityEntity> cityLookup) {
        if (rawCity == null || rawCity.isBlank()) {
            return null;
        }

        // Layer 1: if comma-separated, take the last segment
        String city = rawCity.contains(",")
                ? rawCity.substring(rawCity.lastIndexOf(',') + 1)
                : rawCity;

        // Layer 2: normalize (lowercase, trim, collapse spaces, strip special chars)
        String normalized = normalize(city);

        // Layer 3: exact match on normalized value
        CityEntity match = cityLookup.get(normalized);
        if (match != null) return match;

        // Layer 4: contains match — city lookup key contains the input or vice versa
        for (Map.Entry<String, CityEntity> entry : cityLookup.entrySet()) {
            String key = entry.getKey();
            if (key.contains(normalized) || normalized.contains(key)) {
                return entry.getValue();
            }
        }

        // Layer 5: Jaro-Winkler fuzzy match
        CityEntity bestMatch = null;
        double bestScore = 0;
        for (Map.Entry<String, CityEntity> entry : cityLookup.entrySet()) {
            double score = JARO_WINKLER.apply(normalized, entry.getKey());
            if (score > bestScore) {
                bestScore = score;
                bestMatch = entry.getValue();
            }
        }

        return bestScore >= JARO_WINKLER_THRESHOLD ? bestMatch : null;
    }

    private String normalize(String value) {
        return value.trim()
                .toLowerCase()
                .replaceAll("[^a-z\\s]", "")  // strip special chars and digits
                .replaceAll("\\s+", " ")       // collapse multiple spaces
                .trim();
    }
}
