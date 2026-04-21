package lk.slife.policyreportexporter.repository.postgres;

import lk.slife.policyreportexporter.entity.postgres.ElectoratePolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectoratePolicyRepository extends JpaRepository<ElectoratePolicyEntity, Long> {

    List<ElectoratePolicyEntity> findAllByElectorateIsNull();
}
