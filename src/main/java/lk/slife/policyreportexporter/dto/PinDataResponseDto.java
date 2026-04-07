package lk.slife.policyreportexporter.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PinDataResponseDto {
    private Integer id;
    private String pin;
}
