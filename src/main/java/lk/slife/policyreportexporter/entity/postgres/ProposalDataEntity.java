package lk.slife.policyreportexporter.entity.postgres;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "proposal_data")
public class ProposalDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "proposal_no")
    private String proposalNo;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PinDataEntity> pins;
}
