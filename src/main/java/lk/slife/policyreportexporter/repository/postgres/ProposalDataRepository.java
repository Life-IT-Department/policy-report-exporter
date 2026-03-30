package lk.slife.policyreportexporter.repository.postgres;

import lk.slife.policyreportexporter.entity.postgres.ProposalDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalDataRepository extends JpaRepository<ProposalDataEntity, Integer> {
}
