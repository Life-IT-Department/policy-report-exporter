package lk.slife.policyreportexporter.repository.mariadb;

import lk.slife.policyreportexporter.entity.mariadb.MasterPersonDataEntity;
import lk.slife.policyreportexporter.repository.mariadb.projection.PersonDataView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MasterPersonDataRepository extends JpaRepository<MasterPersonDataEntity, Integer> {

    @Query(value = """
            SELECT mp.id, mp.proposal_no AS proposal_no, mp.pin
            FROM master_persondata mp
            INNER JOIN (
                SELECT MAX(id) AS max_id
                FROM master_persondata
                WHERE pin REGEXP '^[0-9]+$'
                  AND CAST(pin AS UNSIGNED) > 10
                GROUP BY proposal_no, pin
            ) latest ON mp.id = latest.max_id
            ORDER BY mp.id DESC
            """, nativeQuery = true)
    List<PersonDataView> findDistinctValidPinsWithMaxId();
}
