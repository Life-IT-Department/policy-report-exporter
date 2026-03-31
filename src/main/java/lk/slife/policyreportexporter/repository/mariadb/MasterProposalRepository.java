package lk.slife.policyreportexporter.repository.mariadb;

import lk.slife.policyreportexporter.entity.mariadb.MasterProposalEntity;
import lk.slife.policyreportexporter.repository.mariadb.projection.ProposalView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MasterProposalRepository extends JpaRepository<MasterProposalEntity, Integer> {

    @Query("""
            SELECT new lk.slife.policyreportexporter.repository.mariadb.projection.ProposalView(
                m.proposalNo, m.sysDate
            )
            FROM MasterProposalEntity m
            WHERE m.id IN (
                SELECT MAX(sub.id)
                FROM MasterProposalEntity sub
                GROUP BY sub.proposalNo
            )
            ORDER BY m.id DESC
            """)
    List<ProposalView> findDistinctProposalNosOrderedByLatest();
}
