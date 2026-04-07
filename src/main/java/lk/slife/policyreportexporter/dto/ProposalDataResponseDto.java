package lk.slife.policyreportexporter.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDataResponseDto {
    private Integer id;
    private String proposalNo;
    private Instant sysDate;
    private boolean mergedPdfAvailable;
    private List<PinDataResponseDto> pins;
}
