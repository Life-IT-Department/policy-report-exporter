package lk.slife.policyreportexporter.repository.mariadb;

import lk.slife.policyreportexporter.entity.mariadb.MasterProposalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MasterProposalRepository extends JpaRepository<MasterProposalEntity, Integer> {

    @Query("SELECT m.proposalNo FROM MasterProposalEntity m GROUP BY m.proposalNo ORDER BY MAX(m.id) DESC")
    List<String> findDistinctProposalNosOrderedByLatest();
}
