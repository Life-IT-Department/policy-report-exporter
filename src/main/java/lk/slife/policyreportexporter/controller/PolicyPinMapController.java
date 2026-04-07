package lk.slife.policyreportexporter.controller;

import lk.slife.policyreportexporter.service.PolicyPinMappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/policy-pin-map")
public class PolicyPinMapController {

    private final PolicyPinMappingService policyPinMappingService;

    @GetMapping("/test")
    public String getPolicyPinMapTest() {
        policyPinMappingService.getLatestProposalNos();
        return "Policy Pin Map Testing";
    }

    @PostMapping("/transfer-merged-pdfs")
    public void tranferMergedPdfs() {
        policyPinMappingService.transferMergedPdfs();
    }

    @PostMapping("/update-merge-pdf-status")
    public void updateMergePdfStatus() {
        policyPinMappingService.updateMergePdfStatus();
    }

}
