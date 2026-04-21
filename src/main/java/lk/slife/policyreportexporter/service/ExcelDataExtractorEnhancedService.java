package lk.slife.policyreportexporter.service;


import lk.slife.policyreportexporter.dto.excel.ExcelDataResponseDTO;
import lk.slife.policyreportexporter.dto.excel.ExcelExtractorRequestDTO;

public interface ExcelDataExtractorEnhancedService {
    ExcelDataResponseDTO extractExcelFileFromPath(ExcelExtractorRequestDTO requestDTO);

    ExcelDataResponseDTO extractExcelFile(ExcelExtractorRequestDTO requestDTO);
}
