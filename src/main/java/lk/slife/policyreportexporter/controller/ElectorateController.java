package lk.slife.policyreportexporter.controller;

import lk.slife.policyreportexporter.service.ElectorateMappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/electorate")
public class ElectorateController {

    private final ElectorateMappingService electorateMappingService;

    @PostMapping("/upload-policy-data")
    public ResponseEntity<Void> uploadPolicyData() {
        electorateMappingService.uploadPolicyData();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/map-electorate")
    public ResponseEntity<Void> mapElectorate() {
        electorateMappingService.mapElectorate();
        return ResponseEntity.ok().build();
    }
}
