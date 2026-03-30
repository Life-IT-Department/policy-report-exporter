package lk.slife.policyreportexporter.entity.postgres;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pin_data")
public class PinDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pin")
    private String pin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id", nullable = false)
    private ProposalDataEntity proposal;
}
